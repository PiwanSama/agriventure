package com.example.agriventure.ui.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.adapter.BuyerOffersAdapter;
import com.example.agriventure.data.models.Offer;
import com.example.agriventure.ui.BaseFragment;
import com.example.agriventure.util.Constants;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerOffersFragment extends BaseFragment {

    private List<Offer> allOfferList;
    private RecyclerView allOffersRv;
    private MaterialTextView marketEmptyText, acceptedText, pendingText, declinedText;
    private LinearLayout statsHolder;
    private ProgressBar mProgressBar;
    private AppCompatImageView emptyImage;
    private MaterialCardView cardView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        
        allOffersRv = view.findViewById(R.id.all_market_products);

        marketEmptyText = view.findViewById(R.id.offers_empty);
        statsHolder = view.findViewById(R.id.stats_holder);
        acceptedText = view.findViewById(R.id.total_accepted);
        pendingText = view.findViewById(R.id.total_pending);
        declinedText = view.findViewById(R.id.total_declined);

        mProgressBar = view.findViewById(R.id.data_loading);

        emptyImage = view.findViewById(R.id.img_offers_empty);

        cardView = view.findViewById(R.id.item_card);

        allOfferList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("offers");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allOfferList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Offer offer = dataSnapshot.getValue(Offer.class);
                    if (offer.getBuyer_name().equals(Constants.buyerName)){
                        allOfferList.add(offer);
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
        if (allOfferList.size()>0){
            statsHolder.setVisibility(View.VISIBLE);
            setUpMyOffers(allOfferList);
        }else{
            marketEmptyText.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setUpMyOffers(List<Offer> offers) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        BuyerOffersAdapter offersAdapter = new BuyerOffersAdapter(activity, offers);
        setUpStats(offers);
        allOffersRv.setAdapter(offersAdapter);
        allOffersRv.setLayoutManager(linearLayoutManager);
        allOffersRv.setVisibility(View.VISIBLE);
        allOffersRv.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private void setUpStats(List<Offer> offers) {
        int accepted = 0;
        int pending = 0;
        int declined = 0;
        for (int i=0;i<offers.size();i++){
            Offer o = offers.get(i);
            if (o.getOffer_status().equals("Accepted")){
                accepted+=1;
            }else if (o.getOffer_status().equals("Pending")){
                pending+=1;
            }else{
                declined+=1;
            }
        }
        acceptedText.setText(String.valueOf(accepted));
        pendingText.setText(String.valueOf(pending));
        declinedText.setText(String.valueOf(declined));
    }
}