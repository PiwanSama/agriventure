package com.example.agriventure.ui.learn

import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.data.models.Post
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.example.agriventure.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agriventure.data.adapter.PostsAdapter
import com.example.agriventure.data.adapter.PostsAdapter.PostClickListener
import com.example.agriventure.databinding.FragmentLearnBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class LearnFragment : BaseFragment() {
    
    private lateinit var binding : FragmentLearnBinding
    private lateinit var myPostList : ArrayList<Post>
    private val databaseRef by lazy{
        Firebase.database.reference.child("posts")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLearnBinding.inflate(inflater, container, false)
        myPostList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myPostList.clear()
                for (dataSnapshot in snapshot.children) {
                    val post = dataSnapshot.getValue(Post::class.java)
                    post?.let { myPostList.add(it) }
                }
                updateView()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateView() {
        if (myPostList.size > 0) {
            setUpMyPosts(myPostList)
        }
    }

    private fun setUpMyPosts(posts: List<Post?>?) {
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val productsAdapter = PostsAdapter(activity, posts) { post: Post? ->
            val bundle = Bundle()
            bundle.putParcelable("post", post)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_learn_to_article_details, bundle)
        }
        binding.allPosts.adapter = productsAdapter
        binding.allPosts.layoutManager = linearLayoutManager
        binding.allPosts.visibility = View.VISIBLE
        binding.allPosts.visibility = View.VISIBLE
        binding.dataLoading.visibility = View.GONE
    }
}