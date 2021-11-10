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
import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.util.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FarmerLoginFragment : BaseFragment() {

    private lateinit var binding:FragmentFarmerLoginBinding
    private val databaseRef :DatabaseReference by lazy{
        Firebase.database.reference.child("farmer_profiles")
    }
    private lateinit var controller: NavController

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentFarmerLoginBinding.inflate(inflater,container,false)
        binding.pinLockView.setPinLockListener(pinLockListener)
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controller = Navigation.findNavController(requireView())
        binding.createFarmerProfile.setOnClickListener { view1: View? -> controller.navigate(R.id.action_navigation_farmer_login_to_navigation_farmer_register) }
    }

    private val pinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            validateUserPIN(pin)
        }

        override fun onEmpty() {

        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {}
    }

    fun validateUserPIN(pin : String){
        Log.i("PIN Supplied", pin)
        val queryKey = activity?.getPreferences(Context.MODE_PRIVATE)?.getString(Constants.firebaseKey,"")
        Log.i("LOGIN", queryKey!!)
        databaseRef.child(queryKey).child("pin").get().addOnSuccessListener{
            it?.let {
                Log.i("LOGIN", it.value as String)
                if (pin == it.value as String){
                    activity.setUpBottomNavigation("Farmer", R.menu.farmer_bottom_nav_menu, R.id.navigation_farmer_market)
                    controller.navigate(R.id.action_navigation_farmer_login_to_navigation_farmer_market)
                }else{
                    Toast.makeText(activity, "Incorrect PIN",LENGTH_SHORT).show()
                }
            }?:Toast.makeText(activity, "Profile does not exist. Please register", LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(activity, "Something went wrong", LENGTH_SHORT).show()
        }
    }
}