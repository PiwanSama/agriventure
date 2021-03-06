package com.example.agriventure.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.agriventure.R
import com.example.agriventure.data.models.FarmerProfile
import com.example.agriventure.databinding.FragmentFarmerRegisterBinding
import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FarmerRegisterFragment : BaseFragment() {

    private lateinit var binding : FragmentFarmerRegisterBinding
    private lateinit var controller : NavController
    private val databaseRef : DatabaseReference by lazy{
        Firebase.database.reference.child("farmer_profiles")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
       binding = FragmentFarmerRegisterBinding.inflate(inflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controller = Navigation.findNavController(view)

        binding.btnCreateFarmerProfile.setOnClickListener(View.OnClickListener {
            val farmerName = binding.fullName.text.toString()
            val farmerContact = binding.phoneNumber.text.toString()
            val businessName = binding.businessName.text.toString()
            val farmerPin = binding.pin.text.toString()
            val profile = FarmerProfile(farmerName, farmerContact, businessName, farmerPin)
            if (farmerName.isEmpty()||farmerContact.isEmpty()||businessName.isEmpty()||farmerPin.isEmpty()){
                Toast.makeText(activity, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }else{
                if(isPinDuplicate(farmerPin)){
                    Toast.makeText(activity, "Please select a different PIN", Toast.LENGTH_SHORT).show()
                }else{
                    createFarmerProfile(profile);
                }
            }
        })
    }

    private fun isPinDuplicate(selectedPin: String) : Boolean {
        var isPinDuplicate = false
        databaseRef.addListenerForSingleValueEvent(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap : DataSnapshot in snapshot.children){
                        val loopPin = snap.child("pin").value as String
                        if (loopPin.equals(selectedPin)){
                            isPinDuplicate = true
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
        return isPinDuplicate
    }

    private fun createFarmerProfile(profile:FarmerProfile){
        val registerDialog = ProgressDialog(activity)
        registerDialog.setTitle("Creating your profile...")
        registerDialog.show()
        val key = databaseRef.push().key!!
        databaseRef.child(key).setValue(profile)
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)?:return
        with(sharedPrefs.edit()){
            putString(Constants.farmerName, profile.name)
            putString(Constants.farmerBusinessName, profile.businessName)
            putString(Constants.farmerContact, profile.contact)
            putString(Constants.farmerFirebaseKey, key)
            apply()
        }

        registerDialog.hide()
        Toast.makeText(activity, "Your profile has been created!",LENGTH_SHORT).show()

        controller.navigate(R.id.action_navigation_farmer_register_to_navigation_farmer_market)


    }
}