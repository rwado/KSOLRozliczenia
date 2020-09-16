package com.example.ksolrozliczenia.OrdersGivenFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ksolrozliczenia.Adapters.ListOrdersGivenAdapter;
import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class OrdersGivenFragment0 extends Fragment {


    private Button mButtonCreateNewOrder;
    private ListView mListOrdersGiven, mListOrdersGivenWaiting;
    private List<Order> orderList = new ArrayList<>();
    private List<Order> unacceptedOrdersList = new ArrayList<>();
    private DataToPass dataToPass = new DataToPass();
    private DatabaseReference mOrdersGivenReference = FirebaseDatabase.getInstance().getReference("Orders");
    private DatabaseReference mUnacceptedReference = FirebaseDatabase.getInstance().getReference("UnacceptedOrders");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_order_0, container, false);

        Bundle bundle = getArguments();

        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        }

        mButtonCreateNewOrder = view.findViewById(R.id.buttonCreateNewOrder);

        mListOrdersGiven = view.findViewById(R.id.listViewOrdersGiven);
        mListOrdersGivenWaiting = view.findViewById(R.id.listViewOrdersGivenWaiting);


        final ListOrdersGivenAdapter ordersGivenAdapter = new ListOrdersGivenAdapter(orderList, getContext());
        final ListOrdersGivenAdapter unacceptedOrdersAdapter = new ListOrdersGivenAdapter(unacceptedOrdersList,  getContext());
        mListOrdersGiven.setAdapter(ordersGivenAdapter);
        mListOrdersGivenWaiting.setAdapter(unacceptedOrdersAdapter);
        ordersGivenAdapter.notifyDataSetChanged();

        mListOrdersGivenWaiting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cancelUnacceptedOrder(i);
            }
        });

        mUnacceptedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                unacceptedOrdersList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    if(order.getSenderName().equals(dataToPass.getMyName())) {
                        unacceptedOrdersList.add(order);
                    }
                }
                unacceptedOrdersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mOrdersGivenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);

                    if(order.getSenderName().equals(dataToPass.getMyName())) {
                        orderList.add(order);
                    }
                }
                ordersGivenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mListOrdersGiven.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showInfoDialog(orderList.get(position));

            }
        });



        mButtonCreateNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                OrdersGivenFragment1 fragment1 = new OrdersGivenFragment1();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                fragment1.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment1).commit();
            }
        });



        return view;
    }

    private void cancelUnacceptedOrder(int i) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final Order order = unacceptedOrdersList.get(i);
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

        String string = "Czy chcesz anulować:" +
                "\n" + order.getItem().getSubcategoryName() +
                "\nIlość: " + order.getQuantity() +
                size + color +
                "\nNależność: " + price + " PLN" +
                "\ndo: " + order.getRecipientName();

        builder.setMessage(string);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mUnacceptedReference.child(order.getOrderId()).removeValue();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "Nie",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void showInfoDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Informacje");
        builder.setMessage(getOrderInfoString(order));
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private String getOrderInfoString(Order order) {

        String size = order.getItem().getSize();
        if(!size.equals("")) {
            size = "\nRozmiar: " + size;
        }
        String color = order.getItem().getColor();
        if(!color.equals("")) {
            color = "\nKolor: " + color;
        }
        String price = String.valueOf(Integer.parseInt(order.getItem().getPrice()) * order.getQuantity());

        String string = "\nTowar wydał: " + order.getSenderName()
                + "\nTowar otrzymał: " + order.getRecipientName()
                + "\n\n" + order.getItem().getDescription()
                + size
                + color
                + "\nSztuk: " + order.getQuantity()
                + "\nCena łącznie: " + price + " PLN";

        return string;
    }
}
