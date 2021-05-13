package com.example.agriventure.ui.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Produce;
import com.example.agriventure.ui.BaseFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MarketFragment extends BaseFragment {

    private DatabaseReference mDatabase;
    private List<Produce> myProducts;
    private RecyclerView myProductsRv, allProductsRv;
    private MaterialTextView marketEmptyText, myProduceTitle, allProduceTitle;
    private MaterialButton buttonAddProduce;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        myProductsRv = view.findViewById(R.id.user_market_products);
        allProductsRv = view.findViewById(R.id.all_market_products);

        marketEmptyText = view.findViewById(R.id.market_empty);
        myProduceTitle = view.findViewById(R.id.user_market_title);
        allProduceTitle = view.findViewById(R.id.other_market_title);

        buttonAddProduce = view.findViewById(R.id.btn_add_produce);

        myProducts = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener queryValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iterable = snapshot.getChildren();

                for (DataSnapshot next : iterable) {
                    myProducts.add((Produce) next.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };

        Query query = mDatabase.orderByKey();
        query.addListenerForSingleValueEvent(queryValueListener);
        
        updateView();

        buttonAddProduce.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_market_to_navigation_add_produce);
        });
    }

    private void updateView() {
        if (myProducts.size()>0){
            myProduceTitle.setVisibility(View.VISIBLE);
            myProductsRv.setVisibility(View.VISIBLE);
            allProductsRv.setVisibility(View.VISIBLE);
        }else{
            buttonAddProduce.setVisibility(View.VISIBLE);
            marketEmptyText.setVisibility(View.VISIBLE);
        }
    }
}