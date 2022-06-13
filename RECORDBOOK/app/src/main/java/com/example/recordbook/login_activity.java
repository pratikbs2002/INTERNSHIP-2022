package com.example.recordbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity implements View.OnClickListener{

    Button login_button;
    Intent intent;
    TextView register,forgot_password;
    private EditText editTextEmail,editTextPassword;

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

//        login_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent = new Intent(login_activity.this, register_activity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.register:
                intent = new Intent(login_activity.this, register_activity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                userLogin();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(login_activity.this,ResetPassword.class));
                break;

        }
    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Email is required !");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is required !");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(login_activity.this,HomePage.class));
                }else{
                    Toast.makeText(login_activity.this,"Failed to Login check Your Credentials",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}