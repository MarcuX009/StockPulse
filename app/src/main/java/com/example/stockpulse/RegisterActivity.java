package com.example.stockpulse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    TextView loginTextView;
    Button registerButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d("DEBUG_LOG", "RegisterActivity onCreated...");
        usernameEditText = findViewById(R.id.userNameRegisterLayout);
        passwordEditText = findViewById(R.id.passwordRegisterLayout);
        loginTextView = findViewById(R.id.logInHereLayout);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(view -> {
            createUser();
        });

        loginTextView.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser() {
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Log.d("DEBUG_LOG", "RegisterActivity createUser: " + email + " " + password);
        if (TextUtils.isEmpty(email)) {
            usernameEditText.setError("Email cannot be empty");
            usernameEditText.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password cannot be empty");
            passwordEditText.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG_LOG", "User registered successfully ");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG_LOG", "Registration Error: " + task.getException().getMessage());
                    }
                }
            });
        }
    }
}