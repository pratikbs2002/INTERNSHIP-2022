package com.example.recordbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import com.example.recordbook.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Dashboard_activity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();  //Network

    ActivityDashboardBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;

    int sumExpense = 0, sumIncome = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        transactionModelArrayList = new ArrayList<>();

        binding.historyCycle.setLayoutManager(new LinearLayoutManager(this));
        binding.historyCycle.setHasFixedSize(true);
        binding.addNewTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard_activity.this, AddNewTransaction_activity.class));
            }
        });
        binding.profileBtnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard_activity.this, currentUserProfile.class));
            }
        });
        loaddata();
        binding.progressbar.setVisibility(View.VISIBLE);
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        loaddata();
//    }

    private void loaddata() {
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getCurrentUser().getEmail()).collection("Transaction").orderBy("date", Query.Direction.DESCENDING)
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
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if (ds.getString("type").equals("Expense")) {
                                sumExpense = sumExpense + amount;
                            } else {
                                sumIncome = sumIncome + amount;
                            }
                            transactionModelArrayList.add(model);
                        }
                        binding.incomeDashboard.setText(getString(R.string.money) + " " + String.valueOf(sumIncome));
                        binding.expenseDashboard.setText(getString(R.string.money) + " " + String.valueOf(sumExpense));
                        binding.balanceDashboard.setText(getString(R.string.money) + " " + String.valueOf(sumIncome - sumExpense));

                        transactionAdapter = new TransactionAdapter(Dashboard_activity.this, transactionModelArrayList);
                        binding.historyCycle.setAdapter(transactionAdapter);
                        binding.progressbar.setVisibility(View.GONE);

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