package com.example.agriventure.data.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Produce;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<Produce> produceList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.rv_market_item, parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        Produce produce = produceList.get(position);
        AppCompatImageView product_image = holder.product_image;
        MaterialTextView product_name = holder.product_name;
        MaterialTextView product_quantity = holder.product_quantity;
        MaterialTextView product_price = holder.product_price;

        //product_image.setImageResource(produce.getProduct_image());
        product_name.setText(produce.getProduct_name());
        product_quantity.setText(produce.getProduct_quantity());
        product_price.setText(produce.getProduct_price());
    }

    @Override
    public int getItemCount() {
        return produceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView product_image;
        public MaterialTextView product_name;
        public MaterialTextView product_quantity;
        public MaterialTextView product_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.item_image);
            product_name = itemView.findViewById(R.id.item_name);
            product_quantity = itemView.findViewById(R.id.item_weight);
            product_price = itemView.findViewById(R.id.item_price);
        }
    }
}
