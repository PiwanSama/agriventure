package com.example.agriventure.ui.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.adapter.ProductsAdapter;
import com.example.agriventure.data.models.Produce;
import com.example.agriventure.ui.BaseFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerMarketFragment extends BaseFragment {

    private List<Produce> allProduceList;
    private RecyclerView allProductsRv;
    private MaterialTextView marketEmptyText, buyerMarketTitle, buyerMarketSubtitle;
    private ProgressBar mProgressBar;
    private AppCompatImageView emptyImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_market, container, false);
        
        allProductsRv = view.findViewById(R.id.all_market_products);

        marketEmptyText = view.findViewById(R.id.market_empty);
        buyerMarketTitle = view.findViewById(R.id.buyer_market_title);
        buyerMarketSubtitle = view.findViewById(R.id.buyer_market_subtitle);

        mProgressBar = view.findViewById(R.id.data_loading);

        emptyImage = view.findViewById(R.id.img_market_empty);

        allProduceList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("produce");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProduceList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Produce produce = dataSnapshot.getValue(Produce.class);
                    allProduceList.add(produce);
                }
                updateView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateView() {
        if (allProduceList.size()>0){
            buyerMarketTitle.setVisibility(View.VISIBLE);
            buyerMarketSubtitle.setVisibility(View.VISIBLE);
            setUpMyProducts(allProduceList);
        }else{
            marketEmptyText.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setUpMyProducts(List<Produce> products) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        ProductsAdapter productsAdapter = new ProductsAdapter(activity, products, produce -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("produce", produce);
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_buyer_market_to_navigation_add_offer, bundle);
        });
        allProductsRv.setAdapter(productsAdapter);
        allProductsRv.setLayoutManager(linearLayoutManager);
        allProductsRv.setVisibility(View.VISIBLE);
        allProductsRv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
}