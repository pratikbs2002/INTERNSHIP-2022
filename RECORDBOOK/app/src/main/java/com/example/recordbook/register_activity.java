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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register_activity extends AppCompatActivity implements View.OnClickListener {

    Button register_button;
    Intent intent;
    TextView textView;
    Button okay_btn_wrong, okay_btn_right;
    TextView popUp_wrong, popUp_right;
    Dialog dialog_rightSign, dialog_wrongSign;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();  //Network

    private EditText editTextName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        //..........................................................................................
        register_button = findViewById(R.id.register_button);
        register_button.setOnClickListener(this);
        textView = findViewById(R.id.login);
        textView.setOnClickListener(this);
        //..........................................................................................


        //dialog ::::..........................................................................
        dialog_rightSign = new Dialog(register_activity.this);
        dialog_rightSign.setContentView(R.layout.dialog_rightsign);
        dialog_rightSign.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_rightSign.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_rightSign.setCancelable(false);
        dialog_rightSign.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        popUp_right = dialog_rightSign.findViewById(R.id.popUp_message_right);
        okay_btn_right = dialog_rightSign.findViewById(R.id.btn_okay_right);
        okay_btn_right.setOnClickListener(this);

        dialog_wrongSign = new Dialog(register_activity.this);
        dialog_wrongSign.setContentView(R.layout.dialog_wrongsign);
        dialog_wrongSign.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_wrongSign.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_wrongSign.setCancelable(false);
        dialog_wrongSign.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        popUp_wrong = dialog_wrongSign.findViewById(R.id.popUp_message_wrong);
        okay_btn_wrong = dialog_wrongSign.findViewById(R.id.btn_okay_wrong);
        okay_btn_wrong.setOnClickListener(this);
        //........................................................................:::: dialog


        editTextName = (EditText) findViewById(R.id.editTextTextPersonName);
        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword1);

        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                startActivity(new Intent(this, login_activity.class));
                break;

            case R.id.register_button:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("FULL NAME IS REQUIRED");
            editTextName.requestFocus();
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
        if (password.isEmpty()) {
            editTextPassword.setError("PASSWORD IS REQUIRED");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password is 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user1 = new User(name, email);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                dialog_rightSign.show();
                                                String message = "Registration successfully";
                                                popUp_right.setText(message);
                                                //.....................
                                                okay_btn_right.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        intent = new Intent(register_activity.this, login_activity.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog_rightSign.cancel();
                                                        startActivity(new Intent(register_activity.this, login_activity.class));

                                                    }
                                                }, 8000);


                                                //........................
                                               /* Toast.makeText(register_activity.this, "Registration successfully",
                                                        Toast.LENGTH_LONG).show();

                                                startActivity(new Intent(register_activity.this, login_activity.class));*/

                                            } else {
//                                                Toast.makeText(register_activity.this, "Registration failed", Toast.LENGTH_LONG).show();
                                                dialog_wrongSign.show();
                                                String message = "Registration failed";
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
                                            }
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        } else {

                            dialog_wrongSign.show();
                            String message = "Email-ID already Registered ! ";
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
//                            Toast.makeText(register_activity.this, "Email-ID Already Used !", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
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
