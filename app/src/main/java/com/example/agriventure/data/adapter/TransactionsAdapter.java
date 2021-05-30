package com.example.agriventure.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Offer;
import com.example.agriventure.data.models.Transaction;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private Context context;
    private PaymentClickListener listener;

    public TransactionsAdapter(@NonNull Context context, List<Transaction> transactions, PaymentClickListener listener){
        this.transactionList = transactions;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_payment_item,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {
        holder.bind(transactionList.get(position), listener);
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

        public void bind(Transaction transaction, PaymentClickListener listener) {
            payment_amount.setText(transaction.getTran_amount()+" -");
            paid_to_text.setText(transaction.getRecepient_name());
            payment_narration.setText(transaction.getPayment_reason());
            payment_date.setText(transaction.getPayment_date());

            listener.authorizePayment(transaction);
        }
    }

    public interface PaymentClickListener{
        void authorizePayment(Transaction transaction);
    }

}
