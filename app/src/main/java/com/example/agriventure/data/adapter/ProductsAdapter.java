package com.example.agriventure.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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
    private Context context;
    private ProduceClickListener produceClickListener;

    public ProductsAdapter(@NonNull Context context, List<Produce> products, ProduceClickListener listener){
        this.produceList = products;
        this.context = context;
        this.produceClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_market_item,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        Produce produce = produceList.get(position);
        AppCompatImageView product_image = holder.product_image;
        MaterialTextView product_name = holder.product_name;
        MaterialTextView product_quantity = holder.product_quantity;
        MaterialTextView product_price = holder.product_price;
        MaterialTextView seller_name = holder.seller_name;

        //product_image.setImageResource(produce.getProduct_image());
        product_name.setText(produce.getProduct_name());
        product_quantity.setText(produce.getProduct_quantity());
        product_price.setText(produce.getProduct_price());
        seller_name.setText(produce.getSeller_name());
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
        public MaterialTextView seller_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.item_image);
            product_name = itemView.findViewById(R.id.item_name);
            product_quantity = itemView.findViewById(R.id.item_weight);
            product_price = itemView.findViewById(R.id.item_price);
            seller_name = itemView.findViewById(R.id.seller_name);
        }
    }

    public interface ProduceClickListener {
        void getProductId(Produce produce);
    }
}
