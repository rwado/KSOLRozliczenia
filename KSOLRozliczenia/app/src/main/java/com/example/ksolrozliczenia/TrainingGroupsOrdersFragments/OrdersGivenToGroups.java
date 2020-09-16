package com.example.ksolrozliczenia.TrainingGroupsOrdersFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class OrdersGivenToGroups extends Fragment {

    private DatabaseReference mGroupsOrdersReference = FirebaseDatabase.getInstance().getReference("GroupsOrdersGiven");
    private DatabaseReference mArchivalOrdersReference = FirebaseDatabase.getInstance().getReference("ArchivalOrdersPupils");

    private ListView mListView;

    private List<Order> orderList = new ArrayList<>();
    private ListOrdersGivenAdapter listAdapter;
    private DataToPass dataToPass = new DataToPass();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_given_to_groups, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        } else {
            getActivity().getFragmentManager().popBackStack();
        }

        mListView = view.findViewById(R.id.listViewGroupOrdersGiven);

        listAdapter = new ListOrdersGivenAdapter(orderList, getContext());
        mListView.setAdapter(listAdapter);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showPaymentDialog(orderList.get(i));

            }
        });

        mGroupsOrdersReference.child(dataToPass.getMyName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    orderList.add(order);
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void showPaymentDialog(final Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String message = order.getRecipientName() + "\nOpłaca towar:\n";
        message += order.toString();
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Potwierdź",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mGroupsOrdersReference.child(dataToPass.getMyName()).child(order.getOrderId()).removeValue();

                        String month, year;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
                        month = currentDate.substring(3, 5);
                        year = currentDate.substring(6);

                        mArchivalOrdersReference.child(dataToPass.getMyName()).child(year).child(month).child(order.getOrderId()).setValue(order);

                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "Anuluj",
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
}
