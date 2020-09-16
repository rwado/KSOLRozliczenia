package com.example.ksolrozliczenia.MakeOrderFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ksolrozliczenia.Adapters.ListOrdersToMeAdapter;
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

public class OthersOrderedFragment extends Fragment {

    private DatabaseReference mPlacedOrdersReference = FirebaseDatabase.getInstance().getReference("PlacedOrders");

    private ListView mListView;

    private List<Order> orderList = new ArrayList<>();
    private ListOrdersToMeAdapter listAdapter;
    private DataToPass dataToPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others_ordered, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        } else {
            getActivity().getFragmentManager().popBackStack();
        }

        mListView = view.findViewById(R.id.listViewOthersOrdered);

        listAdapter = new ListOrdersToMeAdapter(orderList, getContext());
        mListView.setAdapter(listAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showInfoDialog(orderList.get(i));
            }
        });

        mPlacedOrdersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    if(order.getRecipientName().equals(dataToPass.getMyName())){
                        orderList.add(order);
                    }
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
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
