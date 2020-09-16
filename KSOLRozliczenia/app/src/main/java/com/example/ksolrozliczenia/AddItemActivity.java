package com.example.ksolrozliczenia;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ksolrozliczenia.Model.Category;
import com.example.ksolrozliczenia.Model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    Button mButtonAddItemToDb, mButtonAddNewCategory;
    EditText mEditNewCategory, mEditDescription, mEditSize, mEditColor, mEditPrice;
    Spinner mSpinnerSelectCategory;

    ArrayAdapter<String> adapter;

    List<String> categories = new ArrayList<String>();

    DatabaseReference mPriceListReference = FirebaseDatabase.getInstance().getReference("PriceList");
    DatabaseReference mCategoriesReference = FirebaseDatabase.getInstance().getReference("Categories");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mButtonAddItemToDb = (Button) findViewById(R.id.buttonAddItemToDb);
        mButtonAddNewCategory = (Button) findViewById(R.id.buttonAddNewCategory);
        mEditNewCategory = (EditText) findViewById(R.id.editNewCategory);
        mEditDescription = (EditText) findViewById(R.id.editDescription);
        mEditPrice = (EditText) findViewById(R.id.editItemPrice);
        mEditSize = (EditText) findViewById(R.id.editSize);
        mEditColor = (EditText) findViewById(R.id.editColor);
        mSpinnerSelectCategory = (Spinner) findViewById(R.id.spinnerSelectSize);

        adapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSelectCategory.setAdapter(adapter);

        mButtonAddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCategory();
            }
        });

        mButtonAddItemToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });

    }

    private void addNewCategory() {

        String categoryName = mEditNewCategory.getText().toString();
        String id = mCategoriesReference.push().getKey();
        mCategoriesReference.child(id).setValue(new Category(id, categoryName));
    }

    private void addNewItem() {

        String category = mSpinnerSelectCategory.getSelectedItem().toString();
        String description = mEditDescription.getText().toString();
        String size = mEditSize.getText().toString();
        String color = mEditColor.getText().toString();
        String price = mEditPrice.getText().toString();
        String id = mPriceListReference.push().getKey();
        mPriceListReference.child(category).child(description).child(id).setValue(new Item(id, category, description, description, size, color, price));
    }

    @Override
    protected void onStart() {
        super.onStart();

        mCategoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    categories.add(child.getValue(Category.class).getName());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}