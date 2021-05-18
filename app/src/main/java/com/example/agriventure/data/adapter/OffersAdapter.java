package com.example.agriventure.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Offer;
import com.example.agriventure.data.models.Produce;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private List<Offer> offerList;
    private OfferClickListener listener;
    private Context context;

    public OffersAdapter(@NonNull Context context, List<Offer> offers, OfferClickListener listener){
        this.context = context;
        this.listener = listener;
        this.offerList = offers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_offer_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.bind(offer, listener);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public MaterialTextView buyer_name;
        public MaterialTextView offer_amount;
        public MaterialTextView offer_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            buyer_name = itemView.findViewById(R.id.buyer_name);
            offer_amount = itemView.findViewById(R.id.offer_amount);
            offer_date = itemView.findViewById(R.id.offer_date);
        }

        public void bind(Offer offer, OfferClickListener listener){
            buyer_name.setText(offer.getBuyer_name());
            offer_amount.setText(offer.getOffer_amount());
            offer_date.setText(offer.getOffer_date());
            itemView.setOnClickListener(v -> listener.actionOffer(offer));
        }
    }

    public interface OfferClickListener{
        void actionOffer(Offer offer);
    }
}
