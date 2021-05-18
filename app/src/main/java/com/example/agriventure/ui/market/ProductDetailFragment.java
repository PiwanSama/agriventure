package com.example.agriventure.ui.market;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.adapter.OffersAdapter;
import com.example.agriventure.data.adapter.ProductsAdapter;
import com.example.agriventure.data.models.Offer;
import com.example.agriventure.data.models.Produce;
import com.example.agriventure.ui.BaseFragment;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends BaseFragment {

    private MaterialTextView productName;
    private MaterialTextView productAmount;
    private MaterialTextView productPrice;
    private MaterialTextView noOffersText;

    private RecyclerView myOffersRv;

    private List<Offer> myOfferList;

    private DatabaseReference mDatabase;

    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_market_item_detail, container, false);

        productName = view.findViewById(R.id.item_name);
        productAmount = view.findViewById(R.id.item_weight);
        productPrice = view.findViewById(R.id.item_price);
        noOffersText = view.findViewById(R.id.no_offers_text);

        myOffersRv = view.findViewById(R.id.item_offers_rv);

        mProgressBar = view.findViewById(R.id.progress_bar);

        myOfferList = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        Produce produce = bundle.getParcelable("produce");

        productName.setText(produce.getProduct_name());
        productAmount.setText(produce.getProduct_price());
        productPrice.setText(produce.getProduct_quantity());

        String productKey = produce.getProduct_id().replace("-","");
        Log.i("DETAILS", productKey);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("offers");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOfferList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Offer offer = dataSnapshot.getValue(Offer.class);
                    Log.i("DETAILS", offer.getProduct_id());
                    assert offer != null;
                    if (offer.getProduct_id().equals(productKey)){
                        myOfferList.add(offer);
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
        if (myOfferList.size()>0){
            setUpMyOffers(myOfferList);
        }else{
            noOffersText.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setUpMyOffers(List<Offer> offers) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        OffersAdapter offersAdapter = new OffersAdapter(activity, offers, offer -> {
            Toast.makeText(activity, offer.getBuyer_name(), Toast.LENGTH_SHORT).show();
        });
        myOffersRv.setAdapter(offersAdapter);
        myOffersRv.setLayoutManager(linearLayoutManager);
        myOffersRv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
}
