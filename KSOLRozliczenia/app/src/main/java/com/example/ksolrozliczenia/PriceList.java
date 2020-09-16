package com.example.ksolrozliczenia;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.ksolrozliczenia.Adapters.ListPriceListAdapter;
import com.example.ksolrozliczenia.Model.Item;
import com.example.ksolrozliczenia.Model.PriceListItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PriceList extends AppCompatActivity {

    private DatabaseReference mPriceListReference = FirebaseDatabase.getInstance().getReference("PriceList");

    private Spinner mSpinnerCategory;
    private ListView mListItems;

    private List<Item> itemList = new ArrayList<>();
    private List<PriceListItem> priceItemList = new ArrayList<>();
    private ListPriceListAdapter priceListAdapter;
    private List<String> categoryList = new ArrayList<>();
    private List<String> subcategoryList = new ArrayList<>();

    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cennik");


        mSpinnerCategory = findViewById(R.id.spinnerPriceListCategory);
        mListItems = findViewById(R.id.listViewPriceList);

        categoryAdapter = new ArrayAdapter<String>(PriceList.this, android.R.layout.simple_spinner_dropdown_item, subcategoryList);
        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(categoryAdapter);
        priceListAdapter = new ListPriceListAdapter(priceItemList, PriceList.this);
        mListItems.setAdapter(priceListAdapter);
        priceListAdapter.notifyDataSetChanged();

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<PriceListItem> priceList = new ArrayList<>();

                for (Item item : itemList) {
                    if (item.getDescription().equals(subcategoryList.get(i))) {
                        priceList.add(new PriceListItem(item.getDescription(), item.getSize(), item.getPrice()));
                    }
                }
                priceList = eliminateDuplicates(priceList);

                priceListAdapter = new ListPriceListAdapter(priceList, PriceList.this);
                mListItems.setAdapter(priceListAdapter);
                priceListAdapter.notifyDataSetChanged();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mPriceListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categoryList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String category = child.getKey();
                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();

                for (final String string : categoryList) {
                    mPriceListReference.child(string).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String sub = child.getKey();
                                subcategoryList.add(sub);
                            }
                            categoryAdapter.notifyDataSetChanged();

                            for (String subcategory : subcategoryList) {
                                mPriceListReference.child(string).child(subcategory).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            Item item = child.getValue(Item.class);
                                            itemList.add(item);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                priceListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private List<PriceListItem> eliminateDuplicates(List<PriceListItem> list) {
        for (int index = 0; index < list.size() -1 ; index++) {
            for (int j = 1 + index; j < list.size(); j++) {
                PriceListItem itemOne = list.get(index);
                PriceListItem itemTwo = list.get(j);
                if (itemOne.getDescription().equals(itemTwo.getDescription())
                        && itemOne.getSize().equals(itemTwo.getSize())
                        && itemOne.getPrice().equals(itemTwo.getPrice())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }


    @Override
    protected void onStart() {
        super.onStart();


    }
}