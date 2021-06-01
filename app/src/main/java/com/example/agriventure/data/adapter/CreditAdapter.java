package com.example.agriventure.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Transaction;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private Context context;

    public CreditAdapter(@NonNull Context context, List<Transaction> transactions){
        this.transactionList = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_credit_item,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditAdapter.ViewHolder holder, int position) {
        holder.bind(transactionList.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public MaterialTextView payment_amount;
        public MaterialTextView paid_to_text;
        public MaterialTextView payment_narration;
        public MaterialTextView payment_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            payment_amount = itemView.findViewById(R.id.payment_amount);
            paid_to_text = itemView.findViewById(R.id.paid_to_text);
            payment_narration = itemView.findViewById(R.id.payment_narration);
            payment_date = itemView.findViewById(R.id.payment_date);
        }

        public void bind(Transaction transaction) {
            payment_amount.setText(transaction.getTran_amount()+" - ");
            paid_to_text.setText(transaction.getRecepient_name());
            payment_narration.setText(transaction.getPayment_reason());
            payment_date.setText(transaction.getPayment_date());
        }
    }

}
