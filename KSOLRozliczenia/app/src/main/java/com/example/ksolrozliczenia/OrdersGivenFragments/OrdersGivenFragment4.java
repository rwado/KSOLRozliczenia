package com.example.ksolrozliczenia.OrdersGivenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.Item;
import com.example.ksolrozliczenia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class OrdersGivenFragment4 extends Fragment {

    private Spinner mSpinnerSize, mSpinnerColor;
    private Button mButtonInc, mButtonDec, mButtonBack, mButtonNext;
    private TextView textQuantity, textDescription;
    private LinearLayout layoutSize, layoutColor;

    private DatabaseReference mSubcategoriesReference = FirebaseDatabase.getInstance().getReference("PriceList");

    private List<String> sizeList = new ArrayList<>();
    private List<String> colorList = new ArrayList<>();
    private  List<Item> items = new ArrayList<>();

    private DataToPass dataToPass = new DataToPass();

    private ArrayAdapter<String> sizeAdapter, colorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_order_4, container, false);


        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        }

        mButtonInc = view.findViewById(R.id.buttonIncNewOrder);
        mButtonDec = view.findViewById(R.id.buttonDecNewOrder);
        mButtonBack = view.findViewById(R.id.buttonBackNewOrderFragment4);
        mButtonNext = view.findViewById(R.id.buttonNextNewOrderFragment4);
        mSpinnerSize = view.findViewById(R.id.spinnerSizeNewOrder);
        mSpinnerColor = view.findViewById(R.id.spinnerColorNewOrder);
        textQuantity = view.findViewById(R.id.textQuantityNewOrder);
        textDescription = view.findViewById(R.id.textDescriptionNewOrderFragment4);
        layoutSize = view.findViewById(R.id.layoutSizeNewOrder);
        layoutColor = view.findViewById(R.id.layoutColorNewOrder);

        sizeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, sizeList);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSize.setAdapter(sizeAdapter);

        colorAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, colorList);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerColor.setAdapter(colorAdapter);


        textDescription.setText(dataToPass.getOrder().getItem().getSubcategoryName());

        final Set<String> sizeSet = new LinkedHashSet<>();
        final Set<String> colorSet = new LinkedHashSet<>();
        mSubcategoriesReference = mSubcategoriesReference.child(dataToPass.getOrder().getItem().getCategoryName()).child(dataToPass.getOrder().getItem().getSubcategoryName());
        mSubcategoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sizeList.clear();
                colorList.clear();
                items.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {

                    Item item = child.getValue(Item.class);
                    items.add(item);
                    sizeSet.add(item.getSize());
                    colorSet.add(item.getColor());
                }
                sizeList.addAll(sizeSet);
                colorList.addAll(colorSet);
                sizeAdapter.notifyDataSetChanged();
                colorAdapter.notifyDataSetChanged();

                if(sizeList.size() == 1 && sizeList.get(0).equals("")) {
                    layoutSize.setVisibility(View.GONE);
                } else {
                    layoutSize.setVisibility(View.VISIBLE);
                }

                if(colorList.size() == 1 && colorList.get(0).equals("")) {
                    layoutColor.setVisibility(View.GONE);
                } else {
                    layoutColor.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mButtonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(textQuantity.getText().toString());
                if(quantity > 1) {
                    textQuantity.setText(String.valueOf(--quantity));
                }
            }
        });

        mButtonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(textQuantity.getText().toString());
                if(quantity < Integer.MAX_VALUE) {
                    textQuantity.setText(String.valueOf(++quantity));
                }
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                FragmentManager fm = getFragmentManager();
                OrdersGivenFragment3 fragment3 = new OrdersGivenFragment3();
                fragment3.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment3).commit();

            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Item item = new Item();
                String color = "", size = "";
                if(layoutSize.getVisibility() != View.GONE) {
                    size = mSpinnerSize.getSelectedItem().toString();
                }
                if(layoutColor.getVisibility() != View.GONE) {
                    color = mSpinnerColor.getSelectedItem().toString();
                }
                for(Item i : items) {

                    if(i.getSize().equals(size) && i.getColor().equals(color)) {
                        item = i;
                    }
                }
                dataToPass.getOrder().setItem(item);
                dataToPass.getOrder().getItem().setSubcategoryName(textDescription.getText().toString());
                dataToPass.getOrder().setQuantity(Integer.parseInt(textQuantity.getText().toString()));
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                FragmentManager fm = getFragmentManager();
                OrdersGivenFragment5 fragment5 = new OrdersGivenFragment5();
                fragment5.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment5).commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
