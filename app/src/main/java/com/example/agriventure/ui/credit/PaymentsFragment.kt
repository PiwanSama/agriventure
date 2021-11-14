package com.example.agriventure.ui.credit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.adapter.TransactionsAdapter;
import com.example.agriventure.data.models.Offer;
import com.example.agriventure.data.models.Transaction;
import com.example.agriventure.ui.BaseFragment;
import com.example.agriventure.util.Constants;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentsFragment extends BaseFragment {

    private List<Transaction> allTransactionList;
    private RecyclerView allTransactionsRv;
    private MaterialTextView transactionsEmptyText, totalText;
    private ProgressBar mProgressBar;
    private AppCompatImageView emptyImage;
    private DatabaseReference mDatabase;
    private MaterialCardView cardView;
    private RelativeLayout statsHolder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payments, container, false);

        allTransactionsRv = view.findViewById(R.id.all_payments);

        transactionsEmptyText = view.findViewById(R.id.payments_empty);
        totalText = view.findViewById(R.id.total);

        mProgressBar = view.findViewById(R.id.data_loading);

        emptyImage = view.findViewById(R.id.img_payments_empty);

        cardView = view.findViewById(R.id.item_card);

        statsHolder = view.findViewById(R.id.stats_holder);

        allTransactionList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("payments");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allTransactionList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Transaction transaction = dataSnapshot.getValue(Transaction.class);
                    if (transaction.getSender_name().equals(Constants.businessName)){
                        allTransactionList.add(transaction);
                    }
                }
                updateView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateView() {
        if (allTransactionList.size()>0){
            setUpMyTransactions(allTransactionList);
        }else{
            transactionsEmptyText.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setUpMyTransactions(List<Transaction> transactions) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(activity, transactions, transaction -> mDatabase.child(transaction.getTran_id()).child("status").setValue("Authorized"));
        allTransactionsRv.setAdapter(transactionsAdapter);
        allTransactionsRv.setLayoutManager(linearLayoutManager);
        setUpStats(transactions);
        allTransactionsRv.setVisibility(View.VISIBLE);
        allTransactionsRv.setVisibility(View.VISIBLE);
        statsHolder.setVerticalGravity(View.VISIBLE);
        statsHolder.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private void setUpStats(List<Transaction> transactions) {
        double accepted = 0;
        for (int i=0;i<transactions.size();i++){
            Transaction t = transactions.get(i);
            if (t.getStatus().equals("Authorized")){
                String amt = t.getTran_amount().replace(" UGX","");
                String amtFormat = amt.replace(",", "");
                double price =  Double.parseDouble(amtFormat);
                accepted = accepted+price;
            }
        }
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        totalText.setText(formatter.format(accepted) +" UGX");
    }
}