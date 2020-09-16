package com.example.ksolrozliczenia.MakeOrderFragments;

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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class YourOrdersFragment extends Fragment {

    private DatabaseReference mPlacedOrdersReference = FirebaseDatabase.getInstance().getReference("PlacedOrders");

    private ListView mListOrders;

    private List<Order> orderList = new ArrayList<>();
    private ListOrdersGivenAdapter orderListAdapter;
    private DataToPass dataToPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_orders, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        } else {
            getActivity().getFragmentManager().popBackStack();
        }

        mListOrders = view.findViewById(R.id.listViewYourOrders);

        orderListAdapter = new ListOrdersGivenAdapter(orderList, getContext());
        mListOrders.setAdapter(orderListAdapter);


        mListOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cancelThisOrder(orderList.get(i));
            }
        });

        mPlacedOrdersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    if(order.getSenderName().equals(dataToPass.getMyName())){
                        orderList.add(order);
                    }
                }
                orderListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void cancelThisOrder(final Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Czy chcesz odwołać:");
        String message = "\nZamówienie do:\n";
        message += order.toString();
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Odwołaj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mPlacedOrdersReference.child(order.getOrderId()).removeValue();

                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                " Nie ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
