package com.example.recordbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class UpdateProfile extends AppCompatActivity {
    DatabaseReference databaseReference;
    String nam;
    EditText editTextEmail;
    EditText editTextUsername;
    String userId;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        editTextEmail = findViewById(R.id.editTextTextEmailAddress_update);
        editTextUsername = findViewById(R.id.editTextTextPersonName_update);
        ImageView Back_button = findViewById(R.id.BackButton_update);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        userId = user.getUid();

        Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        Back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, currentUserProfile.class));
                finish();
            }
        });
    }

    public void update() {
        String name = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty()) {
            editTextUsername.setError("FULL NAME IS REQUIRED");
            editTextUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("EMAIL IS REQUIRED");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("please provide valid email");
            editTextEmail.requestFocus();
            return;
        }
        databaseReference.child(userId).child("email").setValue(editTextEmail.getText().toString());
        databaseReference.child(userId).child("name").setValue(editTextUsername.getText().toString());
        Toast.makeText(this, "data has been updated", Toast.LENGTH_SHORT).show();

    }
}