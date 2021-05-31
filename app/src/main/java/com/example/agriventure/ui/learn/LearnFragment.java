package com.example.agriventure.ui.learn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.adapter.PostsAdapter;
import com.example.agriventure.data.models.Post;
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

public class LearnFragment extends BaseFragment {

    private List<Post> myPostList;
    private RecyclerView postsRv;
    private MaterialTextView postsEmptyText;
    private ProgressBar mProgressBar;
    private AppCompatImageView emptyImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        postsRv = view.findViewById(R.id.all_posts);

        postsEmptyText = view.findViewById(R.id.posts_empty);

        mProgressBar = view.findViewById(R.id.data_loading);

        emptyImage = view.findViewById(R.id.img_posts_empty);

        myPostList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myPostList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    myPostList.add(post);
                }
                updateView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateView() {
        if (myPostList.size()>0){
            setUpMyPosts(myPostList);
        }else{
            postsEmptyText.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setUpMyPosts(List<Post> posts) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        PostsAdapter productsAdapter = new PostsAdapter(activity, posts, post -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("post", post);
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_learn_to_article_details, bundle);
        });
        postsRv.setAdapter(productsAdapter);
        postsRv.setLayoutManager(linearLayoutManager);
        postsRv.setVisibility(View.VISIBLE);
        postsRv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

}