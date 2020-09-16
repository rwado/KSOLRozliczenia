package com.example.ksolrozliczenia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ksolrozliczenia.ArchivalOrdersFragments.ArchivalPupilsFragment;
import com.example.ksolrozliczenia.ArchivalOrdersFragments.ArchivalTrainersFragment;
import com.example.ksolrozliczenia.Model.DataToPass;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class ArchivalOrders extends AppCompatActivity {

    private Button mButtonTrainers, mButtonPupils;
    private DataToPass dataToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archival_orders);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Archiwalne zamówienia");

        dataToPass = (DataToPass) getIntent().getSerializableExtra("DATA");

        mButtonTrainers = findViewById(R.id.buttonTrainersArchival);
        mButtonPupils = findViewById(R.id.buttonPupilsArchival);

        final FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dataToPass);
        ArchivalTrainersFragment fragment = new ArchivalTrainersFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();

        mButtonTrainers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                ArchivalTrainersFragment fragment = new ArchivalTrainersFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
            }
        });


        mButtonPupils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                ArchivalPupilsFragment fragment = new ArchivalPupilsFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
            }
        });
    }
}