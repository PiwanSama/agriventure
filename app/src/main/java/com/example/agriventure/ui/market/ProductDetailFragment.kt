package com.example.agriventure.ui.market

import com.example.agriventure.ui.BaseFragment
import com.google.android.material.textview.MaterialTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agriventure.data.models.Offer
import com.google.firebase.database.DatabaseReference
import android.widget.ProgressBar
import com.example.agriventure.data.models.Produce
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.agriventure.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agriventure.data.adapter.OffersAdapter
import com.example.agriventure.data.adapter.OffersAdapter.OfferClickListener
import android.app.ProgressDialog
import android.view.View
import android.widget.Toast
import com.example.agriventure.data.models.Transaction
import com.example.agriventure.databinding.FragmentMarketItemDetailBinding
import com.example.agriventure.util.Constants
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailFragment : BaseFragment() {
    private lateinit var myOfferList : MutableList<Offer>
    private lateinit var binding : FragmentMarketItemDetailBinding
    private lateinit var produce : Produce
    private val databaseRef : DatabaseReference by lazy{
        Firebase.database.reference.child("offers")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketItemDetailBinding.inflate(inflater, container, false)
        myOfferList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        produce = bundle?.getParcelable("produce")!!
        produce.let {
            binding.itemName.text = produce.product_name
            binding.itemWeight.text = produce.product_quantity
            binding.itemPrice.text = produce.product_quantity
        }
        val productKey = produce.getProduct_id().replace("-", "")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myOfferList.clear()
                for (dataSnapshot in snapshot.children) {
                    val offer = dataSnapshot.getValue(Offer::class.java)!!
                    offer.let {
                        if (offer.getProduct_id() == productKey) {
                            myOfferList.add(it)
                        }
                    }
                }
                updateView()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateView() {
        if (myOfferList.size > 0) {
            setUpMyOffers(myOfferList)
        } else {
            binding.noOffersText.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setUpMyOffers(offers: List<Offer?>?) {
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val offersAdapter = OffersAdapter(activity, offers, object : OfferClickListener {
            override fun acceptOffer(offer: Offer) {
                databaseRef.child(offer.getOffer_id()).child("offer_status")
                    .setValue("Accepted")
                val productProgressDialog = ProgressDialog(activity)
                productProgressDialog.setTitle("Initiating Transaction")
                productProgressDialog.show()
                val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                val formatter = DecimalFormat("#,###,###")
                val tran_amount =
                    offer.getOffer_amount().toInt() * produce!!.getProduct_quantity().toInt()
                val formattedAmount = formatter.format(tran_amount.toLong()) + " UGX"
                val transaction = Transaction(
                    formattedAmount,
                    Constants.sellerName,
                    Constants.buyerName,
                    "Payment of " + tran_amount + " to " + Constants.sellerName + " for " + produce!!.getProduct_quantity() + " of " + produce!!.getProduct_name(),
                    today,
                    "Pending",
                    false,
                    false
                )
                val newKey = databaseRef.child("payments").push().key!!
                transaction.setTran_id(newKey)
                databaseRef.child("payments").child(newKey).setValue(transaction)
                Toast.makeText(
                    activity,
                    "Transaction has been submitted for authorization",
                    Toast.LENGTH_SHORT
                ).show()
                productProgressDialog.hide()
            }

            override fun declineOffer(offer: Offer) {
                databaseRef.child("offers").child(offer.getOffer_id()).child("offer_status")
                    .setValue("Declined").addOnSuccessListener { aVoid: Void? ->
                    Toast.makeText(
                        activity,
                        "You have declined this offer",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        binding.itemOffersRv.adapter = offersAdapter
        binding.itemOffersRv.layoutManager = linearLayoutManager
        binding.itemOffersRv.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }
}