package com.example.agriventure.ui.login

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
import com.andrognito.pinlockview.PinLockListener
import com.example.agriventure.R
import com.example.agriventure.databinding.FragmentFarmerLoginBinding
import com.example.agriventure.interfaces.OnDataReceived
import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FarmerLoginFragment : BaseFragment() {

    private lateinit var binding:FragmentFarmerLoginBinding
    private lateinit var controller: NavController
    private val databaseRef :DatabaseReference by lazy{
        Firebase.database.reference.child("farmer_profiles")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentFarmerLoginBinding.inflate(inflater,container,false)
        binding.pinLockView.setPinLockListener(pinLockListener)
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controller = Navigation.findNavController(requireView())
        binding.btnCreateProfile.setOnClickListener { view1: View? -> controller.navigate(R.id.action_navigation_farmer_login_to_navigation_farmer_register) }
    }

    private val pinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            validateUserPIN(pin, object : OnDataReceived{
                override fun userFound(pin: String, key:String) {
                    loginUser(pin, key)
                }

                override fun userNotFound() {
                    showRegisterUI()
                }
            })
        }

        override fun onEmpty() {

        }
        override fun onPinChange(pinLength: Int, intermediatePin: String) {}
    }

    fun validateUserPIN(inputPin : String, callback : OnDataReceived){
        val queryKey = activity.getPreferences(Context.MODE_PRIVATE).getString(Constants.firebaseKey,null)
        var run = true
        if (queryKey!=null){
            Log.i("LOGIN", "not null")
            loginUser(inputPin, queryKey)
        }else{
            Log.i("LOGIN", "null")
            databaseRef.addListenerForSingleValueEvent(
                object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(run){
                            for (snap : DataSnapshot in snapshot.children){
                                var currentKey = snap.key!!
                                var currentPin = snap.child("pin").value as String
                                if (currentPin.equals(inputPin)){
                                    run = false
                                    callback.userFound(currentPin, currentKey)
                                }else{
                                    callback.userNotFound()
                                }
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
        }
    }

    private fun loginUser(inputPin: String, queryKey: String) {
       databaseRef.child(queryKey).get().addOnSuccessListener{
           Log.i("LOGIN", it.toString())
            it?.let {
                /*Get key-value pairs from child node
                val remotePinKey = it.child("pin").key
                val remotePinValue = it.child("pin").value.toString()
                if (remotePinKey != null) {
                    Log.i("LOGIN PINKEY", remotePinKey)
                }
                Log.i("LOGIN PINVAL", remotePinValue)*/
                val remotePin = it.child("pin").value.toString()
                if (inputPin == remotePin){
                    val sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE)
                        with(sharedPrefs.edit()){
                            putString(Constants.userName, it.child("name").value.toString())
                            putString(Constants.businessName, it.child("businessName").value.toString())
                            putString(Constants.contact, it.child("contact").value.toString())
                            putString(Constants.firebaseKey, queryKey)
                            apply()
                        }
                    activity.setUpBottomNavigation("Farmer", R.menu.farmer_bottom_nav_menu, R.id.navigation_farmer_market)
                    controller.navigate(R.id.action_navigation_farmer_login_to_navigation_farmer_market)
                }else{
                    Toast.makeText(activity, "Incorrect PIN",LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener{
            Toast.makeText(activity, "Something went wrong", LENGTH_SHORT).show()
        }
    }

    private fun showRegisterUI() {
        Toast.makeText(activity, "Profile does not exist. Please register", LENGTH_SHORT).show()
        binding.pinLockView.visibility = View.GONE
        binding.appSubtitle.visibility = View.GONE
        binding.indicatorDots.visibility = View.GONE
        //binding.orTxt.visibility = View.VISIBLE
        binding.btnCreateProfile.visibility = View.VISIBLE
    }

}