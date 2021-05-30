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

import static com.example.agriventure.R.color.greyish;

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

    class ViewHolder extends RecyclerView.ViewHolder {

        public MaterialTextView buyer_name;
        public MaterialTextView offer_amount;
        public MaterialTextView offer_date;

        public MaterialButton btnAcceptOffer;
        public MaterialButton btnDeclineOffer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buyer_name = itemView.findViewById(R.id.buyer_name);
            offer_amount = itemView.findViewById(R.id.offer_amount);
            offer_date = itemView.findViewById(R.id.offer_date);

            btnAcceptOffer = itemView.findViewById(R.id.accept_offer);
            btnDeclineOffer = itemView.findViewById(R.id.decline_offer);
        }

        public void bind(Offer offer, OfferClickListener listener){
            buyer_name.setText(offer.getBuyer_name());
            offer_amount.setText(offer.getOffer_amount());
            offer_date.setText(offer.getOffer_date());

            if (offer.getOffer_status().equals("Accepted")||offer.getOffer_status().equals("Declined")){
                disableButtons();
            }else{
                btnAcceptOffer.setOnClickListener(v -> {
                    listener.acceptOffer(offer);
                    disableButtons();
                });

                btnDeclineOffer.setOnClickListener(v -> {
                    listener.declineOffer(offer);
                });
            }
        }

        private void disableButtons(){
            btnAcceptOffer.setEnabled(false);
            btnDeclineOffer.setEnabled(false);
            btnDeclineOffer.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
            btnAcceptOffer.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
            btnDeclineOffer.setTextColor(context.getResources().getColor(R.color.white));
            btnAcceptOffer.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    public interface OfferClickListener{
        void acceptOffer(Offer offer);
        void declineOffer(Offer offer);
    }

}
