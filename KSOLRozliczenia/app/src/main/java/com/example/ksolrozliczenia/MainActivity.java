package com.example.ksolrozliczenia;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.ImageHandler;
import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

public class MainActivity extends AppCompatActivity {


    private DatabaseReference mArchivalOrdersReference = FirebaseDatabase.getInstance().getReference("ArchivalOrdersCoach");
    private DatabaseReference mUnacceptedReference = FirebaseDatabase.getInstance().getReference("UnacceptedOrders");
    private DatabaseReference mAwaitingPaymentReference = FirebaseDatabase.getInstance().getReference("AwaitingPayment");
    private DatabaseReference mOrderReference = FirebaseDatabase.getInstance().getReference("Orders");
    private DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference("MyUser");
    private DatabaseReference mImageResourcesReference = FirebaseDatabase.getInstance().getReference("ImageResources");
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    private DataToPass dataToPass = new DataToPass();

    private TextView mTextMyName, mTextMyEmail;
    private ImageView mImageMyPicture;

    private Boolean canShowDialog = true;

    private Dialog dialog = null;

    public static HashMap<String, String> imageMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (mFirebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        Button mButtonOrdersGiven = findViewById(R.id.buttonOrdersGiven);
        Button mButtonOrdersReceived = findViewById(R.id.buttonOrdersReceived);
        Button mButtonPriceList = findViewById(R.id.buttonPriceList);
        Button mButtonMakeOrder = findViewById(R.id.butonMakeOrder);
        Button mButtonTrainingGroupsOrders = findViewById(R.id.buttonTrainingGroupsOrders);
        Button mButtonArchivalOrders = findViewById(R.id.buttonArchivalOrders);
        Button mButtonAccount = findViewById(R.id.buttonAccount);

        mTextMyName = findViewById(R.id.textUserName);
        mTextMyEmail = findViewById(R.id.textUserEmail);
        mImageMyPicture = findViewById(R.id.imageMyPicture);

        dataToPass.setMyName("");

        mButtonOrdersGiven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataToPass.getMyName().equals("")) {
                    Intent intent = new Intent(MainActivity.this, OrdersGiven.class);
                    intent.putExtra("DATA", dataToPass);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Poczekaj, aż załaduje zalogowanego użytkownika", Toast.LENGTH_LONG).show();
                }
            }
        });

        mButtonOrdersReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataToPass.getMyName().equals("")) {
                    Intent intent = new Intent(MainActivity.this, OrdersReceived.class);
                    intent.putExtra("DATA", dataToPass);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Poczekaj, aż załaduje zalogowanego użytkownika", Toast.LENGTH_LONG).show();
                }
            }
        });

        mButtonPriceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PriceList.class));
            }
        });

        mButtonMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataToPass.getMyName().equals("")) {
                    Intent intent = new Intent(MainActivity.this, MakeOrder.class);
                    intent.putExtra("DATA", dataToPass);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Poczekaj, aż załaduje zalogowanego użytkownika", Toast.LENGTH_LONG).show();
                }
            }
        });

        mButtonTrainingGroupsOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataToPass.getMyName().equals("")) {
                    Intent intent = new Intent(MainActivity.this, TrainingGroupsOrders.class);
                    intent.putExtra("DATA", dataToPass);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Poczekaj, aż załaduje zalogowanego użytkownika", Toast.LENGTH_LONG).show();
                }
            }
        });

        mButtonArchivalOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataToPass.getMyName().equals("")) {
                    Intent intent = new Intent(MainActivity.this, ArchivalOrders.class);
                    intent.putExtra("DATA", dataToPass);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Poczekaj, aż załaduje zalogowanego użytkownika", Toast.LENGTH_LONG).show();
                }
            }
        });

        mButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataToPass.getMyName().equals("")) {
                    Intent intent = new Intent(MainActivity.this, Account.class);
                    intent.putExtra("DATA", dataToPass);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Poczekaj, aż załaduje zalogowanego użytkownika", Toast.LENGTH_LONG).show();
                }
            }
        });

        mImageResourcesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageMap.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    ImageHandler imageHandler = child.getValue(ImageHandler.class);
                    imageMap.put(imageHandler.getKey(), imageHandler.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                updateMyNameAndUserList(dataSnapshot);

                mUnacceptedReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        updateOrdersToAcceptList(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mOrderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateOrderList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAwaitingPaymentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    updateAwaitingPaymentListAndShowDialog(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateAwaitingPaymentListAndShowDialog(@NonNull DataSnapshot dataSnapshot) {
        List<Order> awaitingList = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Order order = child.getValue(Order.class);
            if (order.getSenderName().equals(dataToPass.getMyName())) {
                awaitingList.add(order);
            }
        }
        if (awaitingList.size() > 0) {
            showAcceptPaymentDialog(awaitingList.get(awaitingList.size() - 1));
        }

    }

    private void updateOrderList(@NonNull DataSnapshot dataSnapshot) {
        List<Order> ordersReceivedList = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Order order = child.getValue(Order.class);
            if (order.getRecipientName().equals(dataToPass.getMyName())) {
                ordersReceivedList.add(order);
            }

        }
    }

    private void updateMyNameAndUserList(@NonNull DataSnapshot dataSnapshot) {
        List<User> userList = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            User user = child.getValue(User.class);
            if (mFirebaseAuth.getCurrentUser().getEmail().equals(user.getEmail())) {
                String myName = user.getName();
                dataToPass.setMyName(myName);
                mTextMyName.setText(myName);
                mTextMyEmail.setText(user.getEmail());
            } else {
                userList.add(user);
            }
        }
        dataToPass.setUserList(userList);

        int resourceId;
        switch (dataToPass.getMyName()) {
            case "Ryszard Wadowski":
                resourceId = R.drawable.ryszard_wadowski_small;
                break;
            case "Marcin Wójcik":
                resourceId = R.drawable.marcin_wojcik_small;
                break;
            case "Paweł Lecki":
                resourceId = R.drawable.pawel_lecki_small;
                break;
            case "Mateusz Pichur":
                resourceId = R.drawable.mateusz_pichur_small;
                break;
            default:
                resourceId = R.drawable.person;
        }
        mImageMyPicture.setImageResource(resourceId);
    }

    private void updateOrdersToAcceptList(@NonNull DataSnapshot dataSnapshot) {

        List<Order> ordersToAcceptList = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Order order = child.getValue(Order.class);
            if (order.getRecipientName().equals(dataToPass.getMyName())) {
                ordersToAcceptList.add(order);
            }
        }
        if (ordersToAcceptList.size() > 0) {
            if (canShowDialog)
                showAcceptDialog(ordersToAcceptList.get(ordersToAcceptList.size() - 1));
        }
    }

    @Override
    protected void onResume() {
        canShowDialog = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        canShowDialog = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showAcceptDialog(final Order order) {
        if (dialog == null && canShowDialog) {
            dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_accept_order);

            TextView text = (TextView) dialog.findViewById(R.id.textDescriptionAlertAccept);
            int price = Integer.parseInt(order.getItem().getPrice()) * order.getQuantity();
            String size = "";
            String color = "";
            if (!order.getItem().getSize().equals("")) {
                size += "\nRozmiar: " + order.getItem().getSize();
            }
            if (!order.getItem().getColor().equals("")) {
                color += "\nKolor: " + order.getItem().getColor();
            }

            String string = "Otrzymałem towar od:\n" +
                    order.getSenderName() +
                    "\n" + order.getItem().getSubcategoryName() +
                    "\nIlość: " + order.getQuantity() +
                    size + color +
                    "\nNależność: " + price +
                    " PLN";
            text.setText(string);

            Button buttonAccept = (Button) dialog.findViewById(R.id.buttonAcceptDialog);
            Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancelDialog);
            Button buttonNotReceived = (Button) dialog.findViewById(R.id.buttonNotReceived);

            final String currentOrderId = order.getOrderId();

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = mOrderReference.push().getKey();
                    order.setOrderId(key);
                    mOrderReference.child(key).setValue(order);
                    mUnacceptedReference.child(currentOrderId).removeValue();
                    dialog.dismiss();
                    dialog = null;
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    dialog = null;
                }
            });

            buttonNotReceived.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUnacceptedReference.child(currentOrderId).removeValue();
                    dialog.dismiss();
                    dialog = null;
                }
            });

            if (!((Activity) MainActivity.this).isFinishing()) {
                dialog.show();
            }
        }
    }

    private void showAcceptPaymentDialog(final Order order) {

        if (dialog == null && canShowDialog) {
            dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_accept_payment);
            TextView text = (TextView) dialog.findViewById(R.id.textDescriptionAcceptPaymentDialog);
            int price = Integer.parseInt(order.getItem().getPrice()) * order.getQuantity();
            String size = "\nRozmiar: ";
            String color = "\nKolor: ";
            if (!order.getItem().getSize().equals("")) {
                size += order.getItem().getSize();
            }
            if (!order.getItem().getColor().equals("")) {
                color += order.getItem().getColor();
            }

            String string = order.getRecipientName()
                    + "\nOpłaca towar:"
                    + order.getItem().getSubcategoryName() +
                    "\n" +
                    "\nIlość: " + order.getQuantity() +
                    size + color +
                    "\nNależność: " + price +
                    " PLN";
            text.setText(string);

            Button buttonAccept = (Button) dialog.findViewById(R.id.buttonAcceptPaymentDialog);
            Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancelPaymentDialog);

            final String currentOrderId = order.getOrderId();

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String month, year;
                    String date = order.getDate();
                    month = date.substring(3, 5);
                    year = date.substring(6);

                    mAwaitingPaymentReference.child(currentOrderId).removeValue();
                    mOrderReference.child(currentOrderId).removeValue();
                    mArchivalOrdersReference.child(year).child(month).child(currentOrderId).setValue(order);

                    dialog.dismiss();
                    dialog = null;
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAwaitingPaymentReference.child(currentOrderId).removeValue();
                    dialog.dismiss();
                    dialog = null;
                }
            });


            if (!((Activity) MainActivity.this).isFinishing()) {
                dialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }


}