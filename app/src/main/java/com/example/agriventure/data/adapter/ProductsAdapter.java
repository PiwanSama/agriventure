package com.example.agriventure.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Produce;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

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
        holder.bind(produceList.get(position), produceClickListener);
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

        public void bind(Produce produce, ProduceClickListener listener) {
            product_name.setText(produce.getProduct_name());
            product_quantity.setText(produce.getProduct_quantity());
            product_price.setText(produce.getProduct_price()+" UGX");
            seller_name.setText(produce.getSeller_name());
            itemView.setOnClickListener(v -> listener.getProductId(produce));

            Picasso.with(itemView.getContext()).load(produce.getProduct_image()).into(product_image);
        }
    }

    public interface ProduceClickListener {
        void getProductId(Produce produce);
    }
}
