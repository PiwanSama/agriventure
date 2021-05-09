package com.example.agriventure.data.adapter;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.rv_market_item, parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Offer offer = offerList.get(position);

        MaterialTextView buyer_name = holder.buyer_name;
        MaterialTextView offer_amount = holder.offer_amount;
        MaterialTextView offer_date = holder.offer_date;

        buyer_name.setText(offer.getBuyer_name());
        offer_amount.setText(offer.getOffer_amount());
        offer_date.setText(offer.getOffer_date());
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

            buyer_name = itemView.findViewById(R.id.item_image);
            offer_amount = itemView.findViewById(R.id.item_name);
            offer_date = itemView.findViewById(R.id.item_weight);
        }
    }
}
