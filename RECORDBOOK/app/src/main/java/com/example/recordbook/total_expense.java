package com.example.recordbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.recordbook.databinding.ActivityTotalExpenseBinding;
import com.example.recordbook.databinding.ActivityTotalIncomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class total_expense extends AppCompatActivity {
    ActivityTotalExpenseBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;
//    long sumIncome = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageView Back_button = (ImageView) findViewById(R.id.BackButton_totalExpense);
        Back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(total_expense.this, Dashboard_activity.class));
                finish();
            }
        });
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        transactionModelArrayList = new ArrayList<>();

        binding.historyCycleTotalExpense.setLayoutManager(new LinearLayoutManager(this));
        binding.historyCycleTotalExpense.setHasFixedSize(true);

        loaddata();
        binding.progressbarTotalExpense.setVisibility(View.VISIBLE);
//        onBackPressed();

    }

    private void loaddata() {
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Transaction").orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        transactionModelArrayList.clear();

                        for (DocumentSnapshot ds : task.getResult()) {

                            TransactionModel model = new TransactionModel(
                                    ds.getString("id"),
                                    ds.getString("note"),
                                    ds.getString("amount"),
                                    ds.getString("type"),
                                    ds.getString("date"));
//                            long amount = Integer.parseInt(ds.getString("amount"));
                            if (ds.getString("type").equals("Expense")) {
                                transactionModelArrayList.add(model);
                            }

                        }
                        transactionAdapter = new TransactionAdapter(total_expense.this, transactionModelArrayList);
                        binding.historyCycleTotalExpense.setAdapter(transactionAdapter);
                        binding.progressbarTotalExpense.setVisibility(View.GONE);

                    }
                });
    }
    public void onBackPressed(){
        startActivity(new Intent(total_expense.this,Dashboard_activity.class));
        finish();
        return;
    }
}