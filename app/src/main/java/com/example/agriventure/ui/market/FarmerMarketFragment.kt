package com.example.agriventure.ui.market

import android.content.Context
import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.data.models.Produce
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.button.MaterialButton
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.example.agriventure.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agriventure.data.adapter.ProductsAdapter
import com.example.agriventure.data.adapter.ProductsAdapter.ProduceClickListener
import com.example.agriventure.databinding.FragmentFarmerMarketBinding
import com.example.agriventure.util.Constants
import java.util.ArrayList

class FarmerMarketFragment : BaseFragment() {
    private lateinit var myProductList: MutableList<Produce>
    private lateinit var allProduceList: MutableList<Produce>

    lateinit var binding : FragmentFarmerMarketBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFarmerMarketBinding.inflate(inflater, container, false)
        myProductList = ArrayList()
        allProduceList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val userName = activity?.getPreferences(Context.MODE_PRIVATE)?.getString(Constants.userName,"")
        val businessName = activity?.getPreferences(Context.MODE_PRIVATE)?.getString(Constants.farmerBusinessName,"")
        binding.welcomeTitle.text = "Welcome, "+userName

        val mDatabase = FirebaseDatabase.getInstance().reference.child("produce")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myProductList.clear()
                for (dataSnapshot in snapshot.children) {
                    val produce = dataSnapshot.getValue(Produce::class.java)
                    produce?.let {
                        if (produce.seller_name == businessName){
                            myProductList.add(it)
                        }else{
                            allProduceList.add(it)
                        }
                    }
                }
                updateView()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        binding.btnAddProduce.setOnClickListener { v: View? ->
            Navigation.findNavController(
                requireView()
            ).navigate(R.id.action_navigation_farmer_market_to_navigation_add_produce)
        }
    }

    private fun updateView() {
        binding.btnAddProduce.visibility = View.VISIBLE
        var validate = allProduceList.isEmpty() || myProductList.isEmpty()
        if (allProduceList.isEmpty() and myProductList.isEmpty()){
            binding.btnAddProduce.visibility = View.VISIBLE
            binding.marketEmpty.visibility = View.VISIBLE
            binding.imgMarketEmpty.visibility = View.VISIBLE
            binding.dataLoading.visibility = View.GONE
        }else{
            setUpMyProducts(myProductList)
            setUpAllProducts(allProduceList)
        }
    }

    private fun setUpMyProducts(products: List<Produce?>?) {
        if (products!=null && products.isNotEmpty()){
            val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val myProductsAdapter = ProductsAdapter(activity, products) { produce: Produce? ->
                val bundle = Bundle()
                bundle.putParcelable("produce", produce)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_farmer_market_to_navigation_produce_detail, bundle)
            }
            binding.userMarketProducts.adapter = myProductsAdapter
            binding.userMarketProducts.layoutManager = linearLayoutManager
            binding.userMarketProducts.visibility = View.VISIBLE
            binding.userMarketTitle.visibility = View.VISIBLE
            binding.dataLoading.visibility = View.GONE
        }
    }

    private fun setUpAllProducts(products: List<Produce?>?) {
        if (products!=null && products.isNotEmpty()) {
            val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val allProductsAdapter = ProductsAdapter(activity, products) { produce: Produce? -> }
            binding.allMarketProducts.adapter = allProductsAdapter
            binding.allMarketProducts.layoutManager = linearLayoutManager
            binding.allMarketProducts.visibility = View.VISIBLE
            binding.allMarketTitle.visibility = View.VISIBLE
            binding.dataLoading.visibility = View.GONE
        }
    }
}