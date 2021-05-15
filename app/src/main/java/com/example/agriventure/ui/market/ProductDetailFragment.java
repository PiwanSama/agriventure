package com.example.agriventure.ui.market;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Produce;
import com.example.agriventure.ui.BaseFragment;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class ProductDetailFragment extends BaseFragment {

    private MaterialTextView productName;
    private MaterialTextView productAmount;
    private MaterialTextView productPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_market_item_detail, container, false);

        productName = view.findViewById(R.id.item_name);
        productAmount = view.findViewById(R.id.item_weight);
        productPrice = view.findViewById(R.id.item_price);

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
    }
}
