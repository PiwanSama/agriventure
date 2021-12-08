package com.example.agriventure.ui.market

import android.content.Context
import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.data.models.Offer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.card.MaterialCardView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.example.agriventure.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agriventure.data.adapter.BuyerOffersAdapter
import com.example.agriventure.databinding.FragmentOffersBinding
import com.example.agriventure.util.Constants
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class BuyerOffersFragment : BaseFragment() {
    private lateinit var allOfferList: MutableList<Offer>
    private lateinit var binding : FragmentOffersBinding
    private lateinit var businessName : String
    private val databaseRef : DatabaseReference by lazy{
        Firebase.database.reference.child("offers")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOffersBinding.inflate(inflater, container, false)
        allOfferList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        businessName =
            activity.getPreferences(Context.MODE_PRIVATE).getString(Constants.buyerBusinessName, "").toString()
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allOfferList.clear()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Offer::class.java)?.let {
                        if (it.buyer_name == businessName) {
                            allOfferList.add(it)
                        }
                    }
                }
                updateView()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateView() {
        if (allOfferList.size > 0) {
            binding.statsHolder.visibility = View.VISIBLE
            setUpMyOffers(allOfferList)
        } else {
            binding.offersEmpty.visibility = View.VISIBLE
            binding.imgOffersEmpty.visibility = View.VISIBLE
            binding.dataLoading.visibility = View.GONE
            binding.totalAccepted.text = "0"
            binding.totalPending.text = "0"
            binding.totalDeclined.text = "0"
        }
    }

    private fun setUpMyOffers(offers: List<Offer?>?) {
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val offersAdapter = BuyerOffersAdapter(activity, offers)
        setUpStats(offers)
        binding.allOffers.adapter = offersAdapter
        binding.allOffers.layoutManager = linearLayoutManager
        binding.allOffers.visibility = View.VISIBLE
        binding.allOffers.visibility = View.VISIBLE
        binding.itemCard.visibility = View.VISIBLE
        binding.dataLoading.visibility = View.GONE
    }

    private fun setUpStats(offers: List<Offer?>?) {
        var accepted = 0
        var pending = 0
        var declined = 0
        for (i in offers!!.indices) {
            val o = offers[i]
            if (o!!.getOffer_status() == "Accepted") {
                accepted += 1
            } else if (o.getOffer_status() == "Pending") {
                pending += 1
            } else {
                declined += 1
            }
        }
        binding.totalAccepted.text = accepted.toString()
        binding.totalPending.text = pending.toString()
        binding.totalDeclined.text = declined.toString()
    }
}