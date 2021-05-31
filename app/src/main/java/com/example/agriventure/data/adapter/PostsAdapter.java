package com.example.agriventure.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Post;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private List<Post> postList;
    private Context context;
    private PostClickListener postClickListener;

    public PostsAdapter(@NonNull Context context, List<Post> posts, PostClickListener listener){
        this.postList = posts;
        this.context = context;
        this.postClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_learn_item,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        holder.bind(postList.get(position), postClickListener);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView product_image;
        public MaterialTextView post_title;
        public MaterialTextView post_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.post_image);
            post_title = itemView.findViewById(R.id.article_title);
            post_date = itemView.findViewById(R.id.article_date);
        }

        public void bind(Post post, PostClickListener listener) {
            post_title.setText(post.getPost_title());
            post_date.setText(post.getPost_date());
            itemView.setOnClickListener(v -> listener.getPostId(post));

            //Picasso.with(itemView.getContext()).load(post.getProduct_image()).into(product_image);
        }
    }

    public interface PostClickListener {
        void getPostId(Post post);
    }
}
