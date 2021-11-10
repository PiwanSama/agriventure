package com.example.agriventure.ui.market

import com.example.agriventure.ui.BaseFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.example.agriventure.data.models.Produce
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.textfield.TextInputEditText
import com.example.agriventure.data.models.Offer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.example.agriventure.R
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.example.agriventure.databinding.FragmentAddOfferBinding
import com.example.agriventure.databinding.FragmentFarmerMarketBinding
import com.example.agriventure.util.Constants
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AddOfferFragment : BaseFragment() {
    private lateinit var allOfferList: MutableList<Offer>
    private lateinit var binding : FragmentAddOfferBinding
    private lateinit var productKey : String
    private lateinit var product : Produce
    private var offerPlaced = false
    private val databaseRef : DatabaseReference by lazy{
        Firebase.database.reference.child("offers")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOfferBinding.inflate(inflater, container, false)
        allOfferList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        product = bundle?.getParcelable<Produce>("produce")!!
        product.let {
            binding.itemName.text = it.product_name
            binding.itemPrice.text = it.product_price
            binding.itemWeight.text = it.product_quantity
            binding.sellerName.text = it.seller_name
            productKey = it.product_id.replace("-", "")
        }
        binding.btnAddOffer.setOnClickListener { v: View? ->
            val offerAmount = binding.offerAmount.text.toString()
            if (offerAmount == "") {
                Toast.makeText(activity, "Please enter offer amount", Toast.LENGTH_SHORT).show()
            }
        product.let { writeNewOffer(productKey, offerAmount, it.product_name) }
        }
    }

    private fun writeNewOffer(productKey: String, offerAmount: String, productName: String) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allOfferList.clear()
                for (dataSnapshot in snapshot.children) {
                    val offer = dataSnapshot.getValue(Offer::class.java)!!
                    if (offer.getBuyer_name() == Constants.businessName || offer.getProduct_name() == product.getProduct_name()) {
                        offerPlaced = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        if (offerPlaced) {
            Toast.makeText(
                activity,
                "You have already placed an offer for this item",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val now = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val offer = Offer(
                productKey,
                productName,
                Constants.businessName,
                "Pending",
                offerAmount,
                now,
                product.seller_name
            )
            val newKey = databaseRef.push().key!!
            offer.setOffer_id(newKey)
            databaseRef.child(newKey).setValue(offer)
            Toast.makeText(activity, "Your offer has been placed!", Toast.LENGTH_SHORT).show()
            binding.btnAddOffer.isEnabled = false
        }
    }
}