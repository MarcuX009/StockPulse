package com.example.stockpulse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    TextView registerTextView;
    Button loginButton;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String PREFS_NAME = "StockPulse_Prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameLayout);
        passwordEditText = findViewById(R.id.passwordLayout);
        registerTextView = findViewById(R.id.registerLayout);
        loginButton = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            loginUser();
        });
        registerTextView.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
        Log.d("DEBUG_LOG", "LoginActivity onCreated...");
    }

    private void loginUser() {
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Log.d("DEBUG_LOG", "LoginActivity loginUser: " + email + " " + password);
        if (TextUtils.isEmpty(email)) {
            usernameEditText.setError("Email cannot be empty");
            usernameEditText.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password cannot be empty");
            passwordEditText.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG_LOG", "User logged in successfully!");
                        checkUserDocument();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG_LOG", "Log in Error: " + task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void checkUserDocument() {
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DEBUG_LOG", "User has a document in the database");
                        updateLocalFavouritesList(document);
                    } else {
                        Log.d("DEBUG_LOG", "User does not have a document in the database, creating one");
                        createUserDocument();
                    }
                } else {
                    Log.d("DEBUG_LOG", "Failed to check if user document exists: ", task.getException());
                }
            }
        });
    }

    private void createUserDocument() {
        String email = mAuth.getCurrentUser().getEmail();
        Map<String, Object> user = new HashMap<>();
        user.put("username", email);
        // It's generally not a good practice to store passwords in Firestore.
        // Consider storing other relevant user information instead.
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DEBUG_LOG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DEBUG_LOG", "Error writing document", e);
                    }
                });
    }

    private void updateLocalFavouritesList(DocumentSnapshot document) {
        List<String> favouritesList = (List<String>) document.get("favourites");
        if (favouritesList != null) {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Set<String> favouritesSet = new HashSet<>(favouritesList);
            editor.putStringSet("FavouritesList", favouritesSet);
            editor.apply();
            Log.d("DEBUG_LOG", "Updated local FavouritesList from Firestore");
        } else {
            Log.d("DEBUG_LOG", "No FavouritesList found in Firestore for this user");
        }
    }
}