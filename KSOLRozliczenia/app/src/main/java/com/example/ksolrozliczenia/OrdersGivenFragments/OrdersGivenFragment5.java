package com.example.ksolrozliczenia.OrdersGivenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ksolrozliczenia.MakeOrderFragments.YourOrdersFragment;
import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.R;
import com.example.ksolrozliczenia.TrainingGroupsOrdersFragments.YourGroups;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class OrdersGivenFragment5 extends Fragment {

    private Button mButtonBack, mButtonFinish;
    private TextView textSummary;

    private DatabaseReference mUnacceptedOrdersReference = FirebaseDatabase.getInstance().getReference("UnacceptedOrders");
    private DatabaseReference mGroupsOrdersReference = FirebaseDatabase.getInstance().getReference("GroupsOrders");
    private DatabaseReference mPlacedOrdersReference = FirebaseDatabase.getInstance().getReference("PlacedOrders");
    private DataToPass dataToPass = new DataToPass();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_order_5, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        }

        mButtonBack = view.findViewById(R.id.buttonBackNewOrderFragment5);
        mButtonFinish = view.findViewById(R.id.buttonFinishNewOrder);
        textSummary = view.findViewById(R.id.textSummaryNewOrder);

        textSummary.setText(dataToPass.getOrder().toString());

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                FragmentManager fm = getFragmentManager();
                OrdersGivenFragment4 fragment4 = new OrdersGivenFragment4();
                fragment4.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment4).commit();

            }
        });

        mButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataToPass.getFromWhichActivity().equals(DataToPass.WhichActivity.ACTIVITY_ORDERS_GIVEN)){
                    addOrderToUnaccepted();
                } else if (dataToPass.getFromWhichActivity().equals(DataToPass.WhichActivity.ACTIVITY_TRAINING_GROUPS_ORDERS)) {
                    addNewGroupsOrder();
                } else if (dataToPass.getFromWhichActivity().equals(DataToPass.WhichActivity.ACTIVITY_MAKE_ORDER)) {
                    addPlacedOrder();
                }

            }
        });

        return view;
    }

    private void addPlacedOrder() {

        String key = mPlacedOrdersReference.push().getKey();
        dataToPass.getOrder().setSenderName(dataToPass.getMyName());
        dataToPass.getOrder().setOrderId(key);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        dataToPass.getOrder().setDate(currentDate);
        mPlacedOrdersReference.child(key).setValue(dataToPass.getOrder());
        FragmentManager fm = getFragmentManager();
        YourOrdersFragment fragment = new YourOrdersFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dataToPass);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
    }

    private void addNewGroupsOrder() {

        mGroupsOrdersReference = mGroupsOrdersReference.child(dataToPass.getMyName());

        String key = mGroupsOrdersReference.push().getKey();
        dataToPass.getOrder().setSenderName(dataToPass.getMyName());
        dataToPass.getOrder().setOrderId(key);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        dataToPass.getOrder().setDate(currentDate);
        mGroupsOrdersReference.child(key).setValue( dataToPass.getOrder());
        FragmentManager fm = getFragmentManager();
        YourGroups fragment = new YourGroups();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dataToPass);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();

    }

    private void addOrderToUnaccepted() {
        String key = mUnacceptedOrdersReference.push().getKey();
        dataToPass.getOrder().setOrderId(key);
        dataToPass.getOrder().setSenderName(dataToPass.getMyName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        dataToPass.getOrder().setDate(currentDate);
        mUnacceptedOrdersReference.child(key).setValue(dataToPass.getOrder());
        FragmentManager fm = getFragmentManager();
        OrdersGivenFragment0 fragment0 = new OrdersGivenFragment0();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dataToPass);
        fragment0.setArguments(bundle);
        fm.beginTransaction().replace(R.id.switch_fragment, fragment0).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
