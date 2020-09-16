package com.example.ksolrozliczenia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ksolrozliczenia.MakeOrderFragments.MakeOrderFragment;
import com.example.ksolrozliczenia.MakeOrderFragments.OthersOrderedFragment;
import com.example.ksolrozliczenia.MakeOrderFragments.YourOrdersFragment;
import com.example.ksolrozliczenia.Model.DataToPass;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MakeOrder extends AppCompatActivity {

    private Button mButtonOthersOrdered, mButtonMakeOrder, mButtonYourOrders;
    private DataToPass dataToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Twoje zamówienia");

        mButtonOthersOrdered = findViewById(R.id.buttonOthersOrdered);
        mButtonMakeOrder = findViewById(R.id.buttonMakeOrder);
        mButtonYourOrders = findViewById(R.id.buttonYourOrders);

        dataToPass = (DataToPass) getIntent().getSerializableExtra("DATA");

        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dataToPass);
        OthersOrderedFragment fragment = new OthersOrderedFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();

        mButtonOthersOrdered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                OthersOrderedFragment fragment = new OthersOrderedFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();

            }
        });


        mButtonMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                MakeOrderFragment fragment = new MakeOrderFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();

            }
        });

        mButtonYourOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                YourOrdersFragment fragment = new YourOrdersFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
            }
        });
    }
}