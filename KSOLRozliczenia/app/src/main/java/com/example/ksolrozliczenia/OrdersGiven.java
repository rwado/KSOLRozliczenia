package com.example.ksolrozliczenia;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.OrdersGivenFragments.OrdersGivenFragment0;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class OrdersGiven extends AppCompatActivity {

    private DatabaseReference mArchivalOrdersReference = FirebaseDatabase.getInstance().getReference("ArchivalOrdersCoach");
    private DatabaseReference mUnacceptedReference = FirebaseDatabase.getInstance().getReference("UnacceptedOrders");
    private DatabaseReference mAwaitingPaymentReference = FirebaseDatabase.getInstance().getReference("AwaitingPayment");
    private DatabaseReference mOrderReference = FirebaseDatabase.getInstance().getReference("Orders");

    private DataToPass dataToPass;

    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_given);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Towar wydany");


        dataToPass = (DataToPass) getIntent().getSerializableExtra("DATA");

        dataToPass.setFromWhichActivity(DataToPass.WhichActivity.ACTIVITY_ORDERS_GIVEN);

        FragmentManager fm = getSupportFragmentManager();
        OrdersGivenFragment0 fragment0 = new OrdersGivenFragment0();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dataToPass);
        fragment0.setArguments(bundle);
        fm.beginTransaction().replace(R.id.switch_fragment, fragment0).commit();

        mUnacceptedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Order> ordersToAcceptList = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    if (order.getRecipientName().equals(dataToPass.getMyName())) {
                        ordersToAcceptList.add(order);
                    }
                }
                if (ordersToAcceptList.size() > 0) {
                        showAcceptDialog(ordersToAcceptList.get(ordersToAcceptList.size() - 1));
                }
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


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateAwaitingPaymentListAndShowDialog(@NonNull DataSnapshot dataSnapshot) {

        List<Order> awaitingList = new ArrayList<>();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Order order = child.getValue(Order.class);
            if (order.getSenderName().equals(dataToPass.getMyName())) {
                awaitingList.add(order);
            }
        }
        if(awaitingList.size() > 0) {
            showAcceptPaymentDialog(awaitingList.get(awaitingList.size()-1));
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


    private void showAcceptDialog(final Order order) {

        if(dialog == null) {
            dialog = new Dialog(OrdersGiven.this);
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


            if (!((Activity) OrdersGiven.this).isFinishing()) {
                dialog.show();
            }
        }

    }

    private void showAcceptPaymentDialog(final Order order) {
        if(dialog == null) {
            dialog = new Dialog(OrdersGiven.this);
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


            if (!((Activity) OrdersGiven.this).isFinishing()) {
                dialog.show();
            }
        }
    }

}