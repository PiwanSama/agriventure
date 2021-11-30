package com.example.agriventure.ui.market

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agriventure.R
import com.example.agriventure.data.adapter.ProductsAdapter
import com.example.agriventure.data.models.Produce
import com.example.agriventure.databinding.FragmentBuyerMarketBinding
import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class BuyerMarketFragment : BaseFragment() {
    private lateinit var allProduceList: MutableList<Produce?>
    private lateinit var binding : FragmentBuyerMarketBinding
    private val databaseRef : DatabaseReference by lazy{
        Firebase.database.reference.child("produce")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuyerMarketBinding.inflate(inflater, container, false)
        allProduceList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val userName = activity?.getPreferences(Context.MODE_PRIVATE)?.getString(Constants.buyerName,"")
        val businessName = activity?.getPreferences(Context.MODE_PRIVATE)?.getString(Constants.buyerBusinessName,"")
        binding.welcomeTitle.text = "Welcome, "+userName

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allProduceList.clear()
                for (dataSnapshot in snapshot.children) {
                    val produce = dataSnapshot.getValue(Produce::class.java)
                    allProduceList.add(produce)
                }
                updateView()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateView() {
        if (allProduceList.size > 0) {
            binding.welcomeTitle.visibility = View.VISIBLE
            binding.buyerMarketSubtitle.visibility = View.VISIBLE
            setUpMyProducts(allProduceList)
        } else {
            binding.marketEmpty.visibility = View.VISIBLE
            binding.imgMarketEmpty.visibility = View.VISIBLE
            binding.dataLoading.visibility = View.GONE
        }
    }

    private fun setUpMyProducts(products: List<Produce?>?) {
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val productsAdapter = ProductsAdapter(activity, products) { produce: Produce? ->
            val bundle = Bundle()
            bundle.putParcelable("produce", produce)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_buyer_market_to_navigation_add_offer, bundle)
        }
        binding.allMarketProducts.adapter = productsAdapter
        binding.allMarketProducts.layoutManager = linearLayoutManager
        binding.allMarketProducts.visibility = View.VISIBLE
        binding.allMarketProducts.visibility = View.VISIBLE
        binding.dataLoading.visibility = View.GONE
    }
}