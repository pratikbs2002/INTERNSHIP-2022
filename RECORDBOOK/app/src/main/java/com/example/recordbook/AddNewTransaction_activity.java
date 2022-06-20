package com.example.recordbook;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

                Map<String,Object> transaction = new HashMap<>();

                transaction.put("id",id);
                transaction.put("amount",amount);
                transaction.put("note",note);
                transaction.put("type",type);
                transaction.put("date",currentDate);
                fStore.collection("Expenses").document(firebaseAuth.getCurrentUser().getEmail()).collection("Transaction").document(id)
                        .set(transaction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddNewTransaction_activity.this, "added", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddNewTransaction_activity.this,Dashboard_activity.class));
                                binding.noteNewTransaction.setText("");
                                binding.amountNewTransaction.setText("");
                                binding.progressbar.setVisibility(View.GONE);

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
}