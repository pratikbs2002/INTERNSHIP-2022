package com.example.recordbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class splash_screen extends AppCompatActivity {

    Animation zoom;
    ImageView img;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();


        zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash_animation);
        img = findViewById(R.id.image);
        img.startAnimation(zoom);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(4000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {

                    mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            if (mAuth.getCurrentUser() != null) {
                                try {
                                    startActivity(new Intent(splash_screen.this, Dashboard_activity.class));
                                    finish();

                                } catch (Exception e) {

                                }
                            }
                            else {
                                Intent intent = new Intent(splash_screen.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                    finish();
                }
            }
        };thread.start();
    }
}