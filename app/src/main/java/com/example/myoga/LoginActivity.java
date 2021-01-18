package com.example.myoga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myoga.models.BooksSourceData;
import com.example.myoga.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import static com.example.myoga.models.DatabaseConnection.getMyogaUsers;
import static com.example.myoga.models.Utils.encryptPassword;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    private static int PASSWORD_LENGTH = 6;
    EditText etEmail;
    EditText etPassword;
    OnSuccessListener<AuthResult> successListener = authResult -> {
        //onSuccess
        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        startActivity(intent);
    };

    OnFailureListener failureListener = e -> {
        //use a dialog instead:
        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    };

    private void writeNewUser(String userKey, String email, String password) {
        getMyogaUsers().child(userKey).child("id").setValue(userKey);
        getMyogaUsers().child(userKey).child("email").setValue(email);
        getMyogaUsers().child(userKey).child("password").setValue(password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        //click listeners:
        btnRegister.setOnClickListener((v) -> {
            if (getEmail().length() < 1 || getPassword().length() < 1) {
                Toast.makeText(LoginActivity.this, "Please fill credentials to register", Toast.LENGTH_LONG).show();
            } else if (getPassword().length() < PASSWORD_LENGTH) {
                Toast.makeText(LoginActivity.this, "Password should contain atleast 6 figures", Toast.LENGTH_LONG).show();
            } else {
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(getEmail(), getPassword())
                        .addOnSuccessListener(this, successListener)
                        .addOnFailureListener(this, failureListener);
                // Add a new user to database -->
                String key = getMyogaUsers().push().getKey();
                writeNewUser(key, getEmail(), encryptPassword(getPassword())); // No one is allowed to view PASSWORD!
            }
        });

        btnLogin.setOnClickListener(v -> {
            if (getEmail().length() < 1 || getPassword().length() < 1) {
                Toast.makeText(LoginActivity.this, "Please fill credentials to login", Toast.LENGTH_LONG).show();
            } else {
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(getEmail(), getPassword())
                        .addOnSuccessListener(this, successListener)
                        .addOnFailureListener(this, failureListener);

            }

        });
    }

    private String getEmail() {
        return etEmail.getText().toString();
    }

    private String getPassword() {
        return etPassword.getText().toString();
    }
}
