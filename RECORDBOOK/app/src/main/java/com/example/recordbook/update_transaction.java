package com.example.recordbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recordbook.databinding.ActivityUpdateProfileBinding;
import com.example.recordbook.databinding.ActivityUpdateTransactionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class update_transaction extends AppCompatActivity {
    ActivityUpdateTransactionBinding binding;
    String newType;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView Back_button = (ImageView) findViewById(R.id.BackButton_updateTransaction);
        Back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(update_transaction.this, Dashboard_activity.class));
                finish();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String id = getIntent().getStringExtra("id");
        String amount = getIntent().getStringExtra("amount");
        String note = getIntent().getStringExtra("note");
        String type = getIntent().getStringExtra("type");

        binding.amountUpdateTransaction.setText(amount);
        binding.noteUpdateTransaction.setText(note);

        switch (type) {
            case "Income":
                binding.expenseUpdateTransaction.setBackgroundResource(R.drawable.checkbox_background_green);
                binding.incomeUpdateTransaction.setBackgroundResource(R.drawable.selected_checkbox_background_green);
                binding.incomeUpdateTransaction.setTextColor(Color.WHITE);
                binding.expenseUpdateTransaction.setTextColor(Color.BLACK);
                newType = "Income";
                binding.incomeUpdateTransaction.setChecked(true);
                break;
            case "Expense":
                binding.expenseUpdateTransaction.setTextColor(Color.WHITE);
                binding.incomeUpdateTransaction.setTextColor(Color.BLACK);
                binding.incomeUpdateTransaction.setBackgroundResource(R.drawable.checkbox_background_red);
                binding.expenseUpdateTransaction.setBackgroundResource(R.drawable.selected_checkbox_background_red);
                newType = "Expense";
                binding.expenseUpdateTransaction.setChecked(true);
                break;
        }

        binding.incomeUpdateTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.expenseUpdateTransaction.setBackgroundResource(R.drawable.checkbox_background_green);
                binding.incomeUpdateTransaction.setBackgroundResource(R.drawable.selected_checkbox_background_green);
                binding.incomeUpdateTransaction.setTextColor(Color.WHITE);
                binding.expenseUpdateTransaction.setTextColor(Color.BLACK);

                newType = "Income";
                binding.incomeUpdateTransaction.setChecked(true);
                binding.expenseUpdateTransaction.setChecked(false);

            }
        });
        binding.expenseUpdateTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.expenseUpdateTransaction.setTextColor(Color.WHITE);
                binding.incomeUpdateTransaction.setTextColor(Color.BLACK);
                binding.incomeUpdateTransaction.setBackgroundResource(R.drawable.checkbox_background_red);
                binding.expenseUpdateTransaction.setBackgroundResource(R.drawable.selected_checkbox_background_red);

                newType = "Expense";
                binding.incomeUpdateTransaction.setChecked(false);
                binding.expenseUpdateTransaction.setChecked(true);

            }
        });

        //Update Transaction ..............................................................
        binding.UpdateTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(update_transaction.this);
                builder.setTitle("")
                        .setMessage("Are you sure you want to update data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String amount = binding.amountUpdateTransaction.getText().toString();
                                String note = binding.noteUpdateTransaction.getText().toString();


                                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Transaction").document(id)
                                        .update("amount", amount, "note", note, "type", newType)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                onBackPressed();
                                                Toast.makeText(update_transaction.this, "Data Updated Successfully ! ", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(update_transaction.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

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
        });

        //Delete Transaction ..............................................................
        binding.DeleteTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(update_transaction.this);
                builder.setTitle("")
                        .setMessage("Are you sure you want to Delete Transaction ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                                        .collection("Transaction").document(id).delete().
                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                onBackPressed();
                                                Toast.makeText(update_transaction.this, "Transaction Deleted Successfully !", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(update_transaction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
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
        });
    }

    public void onBackPressed() {

        this.startActivity(new Intent(update_transaction.this, Dashboard_activity.class));
        finish();


    }


}