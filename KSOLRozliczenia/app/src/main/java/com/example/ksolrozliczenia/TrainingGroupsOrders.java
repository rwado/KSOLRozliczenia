package com.example.ksolrozliczenia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.TrainingGroupsOrdersFragments.GroupOrders;
import com.example.ksolrozliczenia.TrainingGroupsOrdersFragments.OrdersGivenToGroups;
import com.example.ksolrozliczenia.TrainingGroupsOrdersFragments.YourGroups;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class TrainingGroupsOrders extends AppCompatActivity {

    private Button mButtonYourGroups, mButtonGroupOrders, mButtonOrdersGiven;
    private DataToPass dataToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_groups_orders);

        dataToPass = (DataToPass) getIntent().getSerializableExtra("DATA");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Zamówienia - Twoje grupy");

        mButtonYourGroups = findViewById(R.id.buttonYourGroups);
        mButtonGroupOrders = findViewById(R.id.buttonGroupOrders);
        mButtonOrdersGiven = findViewById(R.id.buttonOrdersGiven);

        final String myName = getIntent().getStringExtra("MY_NAME");

        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dataToPass);
        YourGroups yourGroupsFragment = new YourGroups();
        yourGroupsFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.switch_fragment, yourGroupsFragment).commit();

        mButtonYourGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                YourGroups yourGroupsFragment = new YourGroups();
                yourGroupsFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, yourGroupsFragment).commit();

            }
        });


        mButtonGroupOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                GroupOrders groupOrdersFragment = new GroupOrders();
                groupOrdersFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, groupOrdersFragment).commit();

            }
        });

        mButtonOrdersGiven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                OrdersGivenToGroups fragment = new OrdersGivenToGroups();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}