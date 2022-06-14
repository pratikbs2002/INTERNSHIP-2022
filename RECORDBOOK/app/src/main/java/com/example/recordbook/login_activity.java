package com.example.recordbook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity implements View.OnClickListener {

    Button login_button;
    Intent intent;
    TextView register, forgot_password;
    Button okay_btn_wrong, okay_btn_right;
    TextView popUp_wrong, popUp_right;
    Dialog dialog_rightSign, dialog_wrongSign;
    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress_login);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword_login);
        progressBar = findViewById(R.id.progressbar);


        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);


        //dialog ::::..........................................................................
        dialog_rightSign = new Dialog(login_activity.this);
        dialog_rightSign.setContentView(R.layout.dialog_rightsign);
        dialog_rightSign.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_rightSign.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_rightSign.setCancelable(false);
        dialog_rightSign.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        popUp_right = dialog_rightSign.findViewById(R.id.popUp_message_right);
        okay_btn_right = dialog_rightSign.findViewById(R.id.btn_okay_right);
        okay_btn_right.setOnClickListener(this);

        dialog_wrongSign = new Dialog(login_activity.this);
        dialog_wrongSign.setContentView(R.layout.dialog_wrongsign);
        dialog_wrongSign.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_wrongSign.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_wrongSign.setCancelable(false);
        dialog_wrongSign.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        popUp_wrong = dialog_wrongSign.findViewById(R.id.popUp_message_wrong);
        okay_btn_wrong = dialog_wrongSign.findViewById(R.id.btn_okay_wrong);
        okay_btn_wrong.setOnClickListener(this);
        //........................................................................:::: dialog
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                intent = new Intent(login_activity.this, register_activity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                userLogin();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(login_activity.this, ResetPassword.class));
                break;

        }
    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required !");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required !");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(login_activity.this, HomePage.class));
                } else {

                    dialog_wrongSign.show();
                    String message = "Failed to Login check Your Credentials";
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

//                    Toast.makeText(login_activity.this,"Failed to Login check Your Credentials",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}