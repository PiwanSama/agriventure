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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class BuyerOffersAdapter extends RecyclerView.Adapter<BuyerOffersAdapter.ViewHolder> {

    private List<Offer> offerList;
    private Context context;

    public BuyerOffersAdapter(@NonNull Context context, List<Offer> offers){
        this.context = context;
        this.offerList = offers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_buyer_offer_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerOffersAdapter.ViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        String status = offer.getOffer_status();
        switch (status){
            case "Accepted":
                holder.btnAcceptOffer.setVisibility(View.VISIBLE);
                break;

            case "Pending":
                holder.btnPendingOffer.setVisibility(View.VISIBLE);
                break;

            case "Declined":
                holder.btnDeclineOffer.setVisibility(View.VISIBLE);
                break;
        }
        holder.bind(offer);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public MaterialTextView product_name;
        public MaterialTextView seller_name;
        public MaterialTextView offer_amount;
        public MaterialTextView offer_date;

        public MaterialButton btnAcceptOffer;
        public MaterialButton btnDeclineOffer;
        public MaterialButton btnPendingOffer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            seller_name = itemView.findViewById(R.id.seller_name);
            offer_amount = itemView.findViewById(R.id.offer_amount);
            offer_date = itemView.findViewById(R.id.offer_date);

            btnAcceptOffer = itemView.findViewById(R.id.accept_offer);
            btnDeclineOffer = itemView.findViewById(R.id.decline_offer);
            btnPendingOffer = itemView.findViewById(R.id.pending_offer);
        }

        public void bind(Offer offer){
            product_name.setText(offer.getProductName()+" -");
            seller_name.setText(offer.getSeller_name());
            offer_amount.setText(offer.getOffer_amount());
            offer_date.setText(offer.getOffer_date());
        }
    }
}
