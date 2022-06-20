package com.example.recordbook;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    Button reset_button;
    FirebaseAuth auth;
    Button okay_btn_wrong, okay_btn_right;
    TextView popUp_wrong, popUp_right;
    Dialog dialog_rightSign, dialog_wrongSign;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();  //Network

    private EditText emailId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailId = findViewById(R.id.editTextTextEmailAddress_reset);
        reset_button = (Button) findViewById(R.id.reset_button);
        progressBar = findViewById(R.id.progressbar);

        //dialog ::::..........................................................................
        dialog_rightSign = new Dialog(ResetPassword.this);
        dialog_rightSign.setContentView(R.layout.dialog_rightsign);
        dialog_rightSign.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_rightSign.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_rightSign.setCancelable(false);
        dialog_rightSign.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        popUp_right = dialog_rightSign.findViewById(R.id.popUp_message_right);
        okay_btn_right = dialog_rightSign.findViewById(R.id.btn_okay_right);
        okay_btn_right.setOnClickListener(this);

        dialog_wrongSign = new Dialog(ResetPassword.this);
        dialog_wrongSign.setContentView(R.layout.dialog_wrongsign);
        dialog_wrongSign.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_wrongSign.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_wrongSign.setCancelable(false);
        dialog_wrongSign.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        popUp_wrong = dialog_wrongSign.findViewById(R.id.popUp_message_wrong);
        okay_btn_wrong = dialog_wrongSign.findViewById(R.id.btn_okay_wrong);
        okay_btn_wrong.setOnClickListener(this);
        //........................................................................:::: dialog

        auth = FirebaseAuth.getInstance();
        reset_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_button:
                resetPassword();
        }

    }

    private void resetPassword() {
        String email = emailId.getText().toString().trim();

        if (email.isEmpty()) {
            emailId.setError("Email is required !");
            emailId.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError("Please provide valid email");
            emailId.requestFocus();
            return;

        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(ResetPassword.this, "Check Your Mail-Box to reset Your Password", Toast.LENGTH_SHORT).show();

                    dialog_rightSign.show();
                    String message = "Check Your Mail-Box to reset Your Password";
                    popUp_right.setText(message);
                    //.....................
                    okay_btn_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ResetPassword.this, login_activity.class));
                        }
                    });
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog_rightSign.cancel();
                            startActivity(new Intent(ResetPassword.this, login_activity.class));

                        }
                    }, 4000);

                } else {
                    dialog_wrongSign.show();
                    String message = "Email-ID not Registered ! \nPlease Enter Valid Email-ID";
                    popUp_wrong.setText(message);

                    okay_btn_wrong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_wrongSign.cancel();
                        }
                    });

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog_wrongSign.cancel();
                        }
                    }, 5000);
//                    Toast.makeText(ResetPassword.this, "Email-ID not Registered !", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //Network
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

}