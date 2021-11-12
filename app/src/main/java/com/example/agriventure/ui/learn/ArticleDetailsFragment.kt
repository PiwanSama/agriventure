package com.example.agriventure.ui.learn

import com.example.agriventure.ui.BaseFragment
import com.google.android.material.textview.MaterialTextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.example.agriventure.R
import com.example.agriventure.data.models.Post
import com.example.agriventure.databinding.FragmentArticleBinding

class ArticleDetailsFragment : BaseFragment() {
    private lateinit var binding : FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = requireArguments()
        bundle.getParcelable<Post>("post")?.let {
            binding.articleTitle.text = it.post_title
            binding.articleDate.text = it.post_date
            binding.articleBody.text = it.post_body
        }
    }
}