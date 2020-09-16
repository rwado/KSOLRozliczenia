package com.example.ksolrozliczenia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    EditText mEditEmail, mEditPassword;
    Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        mEditPassword = (EditText) findViewById(R.id.editTextTextPassword);
        mButtonLogin = (Button) findViewById(R.id.buttonLogin);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEditEmail.getText().toString();
                final String password = mEditPassword.getText().toString();

                if (!email.equals("") && !password.equals("")) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_succesful), Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        mEditEmail.setText("");
                                        mEditPassword.setText("");
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_unsuccesful), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
    }
}