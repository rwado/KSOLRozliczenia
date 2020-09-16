package com.example.ksolrozliczenia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ksolrozliczenia.Model.ImageHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddImageResource extends AppCompatActivity {

    private DatabaseReference mImageResourcesReference = FirebaseDatabase.getInstance().getReference("ImageResources");
    private EditText mEditName, mEditResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_resource);

        mEditName = findViewById(R.id.editItemName);
        mEditResource = findViewById(R.id.editItemResource);
        Button mButtonAdd = findViewById(R.id.buttonAddImageResource);

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewImageResource();
            }
        });

    }

    private void addNewImageResource() {
        ImageHandler imageHandler = new ImageHandler(mEditName.getText().toString(), mEditResource.getText().toString());

        String dbKey = mImageResourcesReference.push().getKey();

        mEditResource.setText("");
        mEditName.setText("");

        mImageResourcesReference.child(dbKey).setValue(imageHandler);
    }
}