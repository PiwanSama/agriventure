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
import com.example.agriventure.data.models.BuyerProfile
import com.example.agriventure.databinding.FragmentBuyerRegisterBinding
import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BuyerRegisterFragment : BaseFragment() {

    private lateinit var binding : FragmentBuyerRegisterBinding
    private lateinit var controller : NavController
    private val databaseRef : DatabaseReference by lazy{
        Firebase.database.reference.child("buyer_profiles")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
       binding = FragmentBuyerRegisterBinding.inflate(inflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controller = Navigation.findNavController(view)

        binding.btnBuyerRegister.setOnClickListener(View.OnClickListener {
            val buyerName = binding.fullName.text.toString()
            val buyerContact = binding.phoneNumber.text.toString()
            val businessName = binding.businessName.text.toString()
            val buyerPin = binding.pin.text.toString()
            val profile = BuyerProfile(buyerName, buyerContact, businessName, buyerPin)
            if (buyerName.isEmpty()||buyerContact.isEmpty()||businessName.isEmpty()||buyerPin.isEmpty()){
                Toast.makeText(activity, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }else{
                if(isPinDuplicate(buyerPin)){
                    Toast.makeText(activity, "Please select a different PIN", Toast.LENGTH_SHORT).show()
                }else{
                    createBuyerProfile(profile);
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

    private fun createBuyerProfile(profile:BuyerProfile){
        val registerDialog = ProgressDialog(activity)
        registerDialog.setTitle("Creating your profile...")
        registerDialog.show()
        val key = databaseRef.push().key!!
        databaseRef.child(key).setValue(profile)
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)?:return
        with(sharedPrefs.edit()){
            putString(Constants.buyerName, profile.name)
            putString(Constants.buyerBusinessName, profile.businessName)
            putString(Constants.buyerContact, profile.contact)
            putString(Constants.buyerFirebaseKey, key)
            apply()
        }

        registerDialog.hide()
        Toast.makeText(activity, "Your profile has been created!",LENGTH_SHORT).show()

        controller.navigate(R.id.action_navigation_buyer_register_to_navigation_buyer_market)


    }
}