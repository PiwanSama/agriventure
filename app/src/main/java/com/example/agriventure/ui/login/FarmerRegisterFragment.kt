package com.example.agriventure.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.agriventure.data.models.FarmerProfile
import com.example.agriventure.databinding.FragmentFarmerRegisterBinding
import com.example.agriventure.ui.BaseFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FarmerRegisterFragment : BaseFragment() {

    private lateinit var binding : FragmentFarmerRegisterBinding
    private val databaseRef : DatabaseReference by lazy{
        Firebase.database.reference
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFarmerRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCreateFarmerProfile.setOnClickListener(View.OnClickListener {
            val farmerName = binding.fullName.text.toString()
            val farmerContact = binding.phoneNumber.text.toString()
            val businessName = binding.businessName.text.toString()
            val farmerPin = binding.pin.text.toString()
            val profile = FarmerProfile(farmerName, farmerContact, businessName, farmerPin)
            if (farmerName.isEmpty()||farmerContact.isEmpty()||businessName.isEmpty()||farmerPin.isEmpty()){
                Toast.makeText(activity, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }else{
                createFarmerProfile(profile);
            }
        })
    }

    private fun createFarmerProfile(profile:FarmerProfile){

    }
}