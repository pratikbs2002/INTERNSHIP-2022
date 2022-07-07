package com.example.recordbook;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recordbook.databinding.ActivityAddNewTransactionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddNewTransaction_activity extends AppCompatActivity {
    ActivityAddNewTransactionBinding binding;
    String type = "";
    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();  //Network


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView Back_button = (ImageView) findViewById(R.id.BackButton_newTransaction);

        Back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewTransaction_activity.this, Dashboard_activity.class));
                finish();
            }
        });


        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        binding.expenseNewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.expenseNewTransaction.setTextColor(Color.WHITE);
                binding.incomeNewTransaction.setTextColor(Color.BLACK);
                binding.incomeNewTransaction.setBackgroundResource(R.drawable.checkbox_background_red);
                binding.expenseNewTransaction.setBackgroundResource(R.drawable.selected_checkbox_background_red);


                type = "Expense";
                binding.expenseNewTransaction.setChecked(true);
                binding.incomeNewTransaction.setChecked(false);
            }
        });
        binding.incomeNewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.expenseNewTransaction.setBackgroundResource(R.drawable.checkbox_background_green);
                binding.incomeNewTransaction.setBackgroundResource(R.drawable.selected_checkbox_background_green);
                binding.incomeNewTransaction.setTextColor(Color.WHITE);
                binding.expenseNewTransaction.setTextColor(Color.BLACK);

                type = "Income";
                binding.expenseNewTransaction.setChecked(false);
                binding.incomeNewTransaction.setChecked(true);
            }
        });
        binding.saveTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressbar.setVisibility(View.VISIBLE);
                String amount = binding.amountNewTransaction.getText().toString().trim();
                String note = binding.noteNewTransaction.getText().toString().trim();
                if (amount.length() <= 0) {
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                }
                if (note.length() <= 4) {
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(AddNewTransaction_activity.this, "Add more character in note", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type.length() <= 0) {
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(AddNewTransaction_activity.this, "select transaction type", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy | h:mm a", Locale.getDefault());
                String currentDate = simpleDateFormat.format(new Date());

                String id = UUID.randomUUID().toString();

                Map<String, Object> transaction = new HashMap<>();

                transaction.put("id", id);
                transaction.put("amount", amount);
                transaction.put("note", note);
                transaction.put("type", type);
                transaction.put("date", currentDate);
                fStore.collection("Expenses").document(firebaseAuth.getCurrentUser().getUid()).collection("Transaction").document(id)
                        .set(transaction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddNewTransaction_activity.this, "Transaction added Successfully !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddNewTransaction_activity.this, Dashboard_activity.class));
                                binding.noteNewTransaction.setText("");
                                binding.amountNewTransaction.setText("");
                                binding.progressbar.setVisibility(View.GONE);
                                onBackPressed();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                binding.progressbar.setVisibility(View.GONE);
                                Toast.makeText(AddNewTransaction_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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
    public void onBackPressed(){
        startActivity(new Intent(AddNewTransaction_activity.this,Dashboard_activity.class));
        finish();
        return;
    }
}