package com.example.agriventure.ui.credit

import android.content.Context
import com.example.agriventure.ui.BaseFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import com.google.firebase.database.DatabaseReference
import com.google.android.material.card.MaterialCardView
import android.widget.RelativeLayout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.example.agriventure.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agriventure.data.adapter.TransactionsAdapter
import com.example.agriventure.data.adapter.TransactionsAdapter.PaymentClickListener
import com.example.agriventure.data.models.Transaction
import com.example.agriventure.databinding.FragmentPaymentsBinding
import com.example.agriventure.util.Constants
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.util.ArrayList

class PaymentsFragment : BaseFragment() {
    private var allTransactionList: MutableList<Transaction?>? = null
    private lateinit var binding : FragmentPaymentsBinding
    private lateinit var businessName:String
    private val databaseRef by lazy{
        Firebase.database.reference.child("payments")
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        allTransactionList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        businessName =
            activity.getPreferences(Context.MODE_PRIVATE).getString(Constants.buyerBusinessName, "").toString()
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allTransactionList!!.clear()
                for (dataSnapshot in snapshot.children) {
                    val transaction = dataSnapshot.getValue(
                        Transaction::class.java
                    )
                    if (transaction!!.getSender_name() == businessName) {
                        allTransactionList!!.add(transaction)
                    }
                }
                updateView()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateView() {
        if (allTransactionList!!.size > 0) {
            setUpMyTransactions(allTransactionList)
        } else {
            binding.imgPaymentsEmpty.visibility = View.VISIBLE
            binding.paymentsEmpty.visibility = View.VISIBLE
            binding.dataLoading.visibility = View.GONE
        }
    }

    private fun setUpMyTransactions(transactions: List<Transaction?>?) {
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val transactionsAdapter = TransactionsAdapter(
            activity,
            transactions
        ) { transaction: Transaction ->
            databaseRef.child(transaction.getTran_id()).child("status").setValue("Authorized")
        }
        binding.allPayments.adapter = transactionsAdapter
        binding.allPayments.layoutManager = linearLayoutManager
        setUpStats(transactions)
        binding.allPayments.visibility = View.VISIBLE
        binding.statsHolder.visibility = View.VISIBLE
        binding.itemCard.visibility = View.VISIBLE
        binding.dataLoading.visibility = View.GONE
    }

    private fun setUpStats(transactions: List<Transaction?>?) {
        var accepted = 0.0
        for (i in transactions!!.indices) {
            val t = transactions[i]
            if (t!!.getStatus() == "Authorized") {
                val amt = t.getTran_amount().replace(" UGX", "")
                val amtFormat = amt.replace(",", "")
                val price = amtFormat.toDouble()
                accepted = accepted + price
            }
        }
        val formatter = DecimalFormat("#,###,###")
        binding.total.text =  formatter.format(accepted) + " UGX"
    }
}