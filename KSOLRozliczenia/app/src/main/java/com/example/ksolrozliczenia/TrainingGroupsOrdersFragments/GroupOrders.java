package com.example.ksolrozliczenia.TrainingGroupsOrdersFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ksolrozliczenia.Account;
import com.example.ksolrozliczenia.Adapters.ListOrdersGivenAdapter;
import com.example.ksolrozliczenia.LoginActivity;
import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.OrdersGiven;
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

public class GroupOrders extends Fragment {

    private DatabaseReference mGroupsReference = FirebaseDatabase.getInstance().getReference("Groups");
    private DatabaseReference mGroupsOrdersReference = FirebaseDatabase.getInstance().getReference("GroupsOrders");
    private DatabaseReference mGroupsOrdersGivenReference = FirebaseDatabase.getInstance().getReference("GroupsOrdersGiven");

    private ListView mListOrders;

    private List<Order> orderList = new ArrayList<>();
    private List<String> groupList = new ArrayList<>();
    private ListOrdersGivenAdapter groupOrdersAdapter;
    private DataToPass dataToPass = new DataToPass();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_orders, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        } else {
            getActivity().getFragmentManager().popBackStack();
        }

        mListOrders = view.findViewById(R.id.listViewGroupOrders);

        groupOrdersAdapter = new ListOrdersGivenAdapter(orderList, getContext());
        mListOrders.setAdapter(groupOrdersAdapter);


        mListOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showGiveItemDialog(orderList.get(i));
            }
        });


        mGroupsReference.child(dataToPass.getMyName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupList.clear();
                groupList.add("Wybierz grupę");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    groupList.add(child.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                groupOrdersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void showGiveItemDialog(final Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String message = "Czy chcesz wydać towar następujący towar:\n\n";
        message += order.toString();
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String key = mGroupsOrdersGivenReference.child(dataToPass.getMyName()).push().getKey();
                        mGroupsOrdersReference.child(dataToPass.getMyName()).child(order.getOrderId()).removeValue();
                        order.setOrderId(key);
                        mGroupsOrdersGivenReference.child(dataToPass.getMyName()).child(key).setValue(order);

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
}
