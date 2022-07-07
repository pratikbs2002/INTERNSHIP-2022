package com.example.recordbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class currentUserProfile extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();  //Network
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_profile);


        TextView logOut = (TextView) findViewById(R.id.LogOut_MyProfile);
        TextView Reset_password = (TextView) findViewById(R.id.ResetPassword_MyProfile);
        ImageView Back_button = (ImageView) findViewById(R.id.BackButton_MyProfile);
//        ImageView Edit_button = (ImageView) findViewById(R.id.EditButton_MyProfile);


        progressBar = findViewById(R.id.progressbar_myProfile);
        progressBar.setVisibility(View.VISIBLE);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUser();
            }
        });
        Reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentUserProfile.this, ResetPassword.class));
            }
        });
        Back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(currentUserProfile.this, Dashboard_activity.class));
                finish();
            }
        });
//        Edit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(currentUserProfile.this, UpdateProfile.class));
//            }
//        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = user.getUid();
        final TextView nameTextView = (TextView) findViewById(R.id.Username_MyProfile);
        final TextView emailTextView = (TextView) findViewById(R.id.EmailAddress_MyProfile);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                if (userprofile != null) {

                    String name = userprofile.name.substring(0, 1).toUpperCase() + userprofile.name.substring(1).toLowerCase();
                    String email = userprofile.email;
                    nameTextView.setText(name);
                    emailTextView.setText(email);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(currentUserProfile.this, "something wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    //log out ..........
    private void LogoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentUserProfile.this);
        builder.setTitle("LOG OUT")
                .setMessage("Are you sure you want to Log out ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(currentUserProfile.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    //Network : ...........................
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(
                networkChangeListener, filter
        );
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // ........................ : Network

}