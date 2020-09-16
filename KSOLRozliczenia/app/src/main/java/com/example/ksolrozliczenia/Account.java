package com.example.ksolrozliczenia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity {


    private List<Order> list = new ArrayList<>();
    private Button mButtonLogout, mButtonNewPassword, mButtonAddItem, mButtonAddImage;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private DataToPass dataToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        dataToPass = (DataToPass) getIntent().getSerializableExtra("DATA");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ustawienia");
        mButtonLogout = findViewById(R.id.buttonLogout);
        mButtonNewPassword = findViewById(R.id.buttonNewPassword);
        mButtonAddItem = findViewById(R.id.buttonNewItem);
        mButtonAddImage = findViewById(R.id.buttonNewImageResource);

        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        mButtonNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForNewPassword();
            }
        });

        mButtonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Account.this, AddItemActivity.class));
            }
        });

        mButtonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Account.this, AddImageResource.class));

            }
        });

        if(dataToPass.getMyName().equals("Ryszard Wadowski")) {
            mButtonAddItem.setVisibility(View.VISIBLE);
            mButtonAddImage.setVisibility(View.VISIBLE);
        } else {
            mButtonAddItem.setVisibility(View.GONE);
            mButtonAddImage.setVisibility(View.GONE);
        }
    }

    private void askForNewPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);

        builder.setCancelable(true);
        builder.setMessage("Czy chcesz, aby na twój adres email została wysłana instrukcja zmiany hasła?");

        builder.setPositiveButton(
                "Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mFirebaseAuth.sendPasswordResetEmail(mFirebaseAuth.getCurrentUser().getEmail())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Account.this, "Na twój email zostałą wysłana instrukcja zmiany hasła", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Account.this, getResources().getString(R.string.password_reset_send), Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton(
                "Nie",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
        builder.setMessage("Czy chcesz się wylogować?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mFirebaseAuth.signOut();
                        Toast.makeText(Account.this, "Udało się wylogować", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Account.this, LoginActivity.class));
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

}