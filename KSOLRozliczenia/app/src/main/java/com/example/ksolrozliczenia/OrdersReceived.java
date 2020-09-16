package com.example.ksolrozliczenia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ksolrozliczenia.Adapters.ListOrdersReceived;
import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrdersReceived extends AppCompatActivity {

    private DatabaseReference mArchivalOrdersReference = FirebaseDatabase.getInstance().getReference("ArchivalOrdersCoach");
    private DatabaseReference mUnacceptedReference = FirebaseDatabase.getInstance().getReference("UnacceptedOrders");
    private DatabaseReference mAwaitingPaymentReference = FirebaseDatabase.getInstance().getReference("AwaitingPayment");
    private DatabaseReference mOrderReference = FirebaseDatabase.getInstance().getReference("Orders");
    private DatabaseReference mUserReference = FirebaseDatabase.getInstance().getReference("MyUser");
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private String myName = "";

    private List<Order> allOrdersReceivedList = new ArrayList<>();
    private List<Order> ordersOfChosenUserList = new ArrayList<>();
    private List<Order> ordersToAcceptList = new ArrayList<>();
    private List<Order> awaitingPayment = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<String> userNames = new ArrayList<>();

    private ListOrdersReceived ordersReceivedAdapter;
    private ListOrdersReceived ordersToAcceptAdapter;
    private ArrayAdapter<String> userListAdapter;
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_received);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Towar otrzymany");

        Spinner mSpinnerSortByUser = findViewById(R.id.spinnerSortByUserOrdersReceived);
        ListView mListOrdersReceived = findViewById(R.id.listViewOrdersReceived);
        ListView mListOrdersReceivedToAccept = findViewById(R.id.listViewOrdersReceivedWaiting);

        ordersReceivedAdapter = new ListOrdersReceived(ordersOfChosenUserList, OrdersReceived.this);
        ordersToAcceptAdapter = new ListOrdersReceived(ordersToAcceptList, OrdersReceived.this);
        mListOrdersReceived.setAdapter(ordersReceivedAdapter);
        mListOrdersReceivedToAccept.setAdapter(ordersToAcceptAdapter);

        userNames.add("Wybierz użytkownika");

        userListAdapter = new ArrayAdapter<String>(OrdersReceived.this, android.R.layout.simple_spinner_dropdown_item, userNames);
        userListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSortByUser.setAdapter(userListAdapter);

        mSpinnerSortByUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ordersOfChosenUserList.clear();
                if (i > 0) {
                    for (Order order : allOrdersReceivedList) {
                        if (order.getSenderName().equals(userNames.get(i))) {
                            ordersOfChosenUserList.add(order);
                        }
                    }
                } else {
                    ordersOfChosenUserList.addAll(allOrdersReceivedList);
                }
                ordersReceivedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mListOrdersReceived.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!ordersOfChosenUserList.isEmpty()) {
                    showAwaitingPaymentDialog(ordersOfChosenUserList.get(position));
                }

            }
        });

        mListOrdersReceivedToAccept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!ordersToAcceptList.isEmpty()) {

                    showAcceptDialog(ordersToAcceptList.get(position));
                }

            }
        });

    }

    private void showAwaitingPaymentDialog(final Order order) {
        if(dialog == null) {
            dialog = new Dialog(OrdersReceived.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_pay_for_order);

            TextView text = (TextView) dialog.findViewById(R.id.textDescriptionAlertPayment);
            int price = Integer.parseInt(order.getItem().getPrice());
            price *= order.getQuantity();
            String size = "\nRozmiar: ";
            String color = "\nKolor: ";
            if (!order.getItem().getSize().equals("")) {
                size += order.getItem().getSize();
            }
            if (!order.getItem().getColor().equals("")) {
                color += order.getItem().getColor();
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

            final String currentOrderId = order.getOrderId();

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAwaitingPaymentReference.child(currentOrderId).setValue(order);
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

            if (!((Activity) OrdersReceived.this).isFinishing()) {
                dialog.show();
            }
        }
    }

    private void showAcceptPaymentDialog(final Order order) {
        if (dialog == null) {
            dialog = new Dialog(OrdersReceived.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_accept_payment);

            TextView text = (TextView) dialog.findViewById(R.id.textDescriptionAcceptPaymentDialog);
            int price = Integer.parseInt(order.getItem().getPrice());
            price *= order.getQuantity();
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
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    String currentDate = dateFormat.format(Calendar.getInstance().getTime());
                    month = currentDate.substring(3, 5);
                    year = currentDate.substring(6);

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


            if (!((Activity) OrdersReceived.this).isFinishing()) {
                dialog.show();
            }
        }

    }

    private void showAcceptDialog(final Order order) {
        if (dialog == null) {

            dialog = new Dialog(OrdersReceived.this);


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.alert_dialog_accept_order);

                TextView text = (TextView) dialog.findViewById(R.id.textDescriptionAlertAccept);
                int price = Integer.parseInt(order.getItem().getPrice());
                price *= order.getQuantity();
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

                if (!((Activity) OrdersReceived.this).isFinishing()) {
                    dialog.show();

            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

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

                updateAwaitingPaymentListAndShowDialog(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void updateAwaitingPaymentListAndShowDialog(@NonNull DataSnapshot dataSnapshot) {
        awaitingPayment.clear();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Order order = child.getValue(Order.class);
            if (order.getSenderName().equals(myName)) {
                awaitingPayment.add(order);
            }
        }
        if (awaitingPayment.size() > 0) {
            showAcceptPaymentDialog(awaitingPayment.get(awaitingPayment.size() - 1));
        }
    }

    private void updateOrderList(@NonNull DataSnapshot dataSnapshot) {
        allOrdersReceivedList.clear();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Order order = child.getValue(Order.class);
            if (order.getRecipientName().equals(myName)) {
                allOrdersReceivedList.add(order);
            }

        }
        ordersOfChosenUserList.clear();
        ordersOfChosenUserList.addAll(allOrdersReceivedList);
        ordersReceivedAdapter.notifyDataSetChanged();
    }

    private void updateOrdersToAcceptList(@NonNull DataSnapshot dataSnapshot) {
        ordersToAcceptList.clear();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Order order = child.getValue(Order.class);
            if (order.getRecipientName().equals(myName)) {
                ordersToAcceptList.add(order);

            }
        }
        if (ordersToAcceptList.size() > 0) {

            showAcceptDialog(ordersToAcceptList.get(ordersToAcceptList.size() - 1));
        }
        ordersToAcceptAdapter.notifyDataSetChanged();
    }

    private void updateMyNameAndUserList(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            User user = child.getValue(User.class);
            userList.add(user);
            if (mFirebaseAuth.getCurrentUser().getEmail().equals(user.getEmail())) {
                myName = user.getName();
            }
            if (!user.getName().equals(myName)) {
                userNames.add(user.getName());
            }
        }
        userListAdapter.notifyDataSetChanged();
    }
}