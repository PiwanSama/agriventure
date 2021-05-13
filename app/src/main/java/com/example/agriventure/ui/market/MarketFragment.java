package com.example.agriventure.ui.market;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class MarketFragment extends BaseFragment {

    private List<Produce> myProductList;
    private RecyclerView myProductsRv, allProductsRv;
    private MaterialTextView marketEmptyText, myProduceTitle, allProduceTitle;
    private MaterialButton buttonAddProduce;
    private ProgressBar mProgressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        myProductsRv = view.findViewById(R.id.user_market_products);
        allProductsRv = view.findViewById(R.id.all_market_products);

        marketEmptyText = view.findViewById(R.id.market_empty);
        myProduceTitle = view.findViewById(R.id.user_market_title);
        allProduceTitle = view.findViewById(R.id.other_market_title);

        buttonAddProduce = view.findViewById(R.id.btn_add_produce);

        mProgressBar = view.findViewById(R.id.data_loading);

        myProductList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("produce");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Produce produce = dataSnapshot.getValue(Produce.class);
                    myProductList.add(produce);
                    Log.i("METHOD", String.valueOf(myProductList.size()));
                }
                updateView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        buttonAddProduce.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_market_to_navigation_add_produce);
        });
    }

    private void updateView() {
        Log.i("MARKET", String.valueOf(myProductList.size()));
        if (myProductList.size()>0){
            myProduceTitle.setVisibility(View.VISIBLE);
            myProductsRv.setVisibility(View.VISIBLE);
            allProductsRv.setVisibility(View.VISIBLE);
            setUpMyProducts(myProductList);
        }else{
            buttonAddProduce.setVisibility(View.VISIBLE);
            marketEmptyText.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setUpMyProducts(List<Produce> produceList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        ProductsAdapter productsAdapter = new ProductsAdapter(activity, produceList, new ProductsAdapter.ProduceClickListener() {
            @Override
            public void getProductId(Produce produce) {
                Toast.makeText(activity, produce.getProduct_name(), Toast.LENGTH_SHORT).show();
            }
        });
        myProductsRv.setAdapter(productsAdapter);
        myProductsRv.setLayoutManager(linearLayoutManager);
        mProgressBar.setVisibility(View.GONE);
    }
}