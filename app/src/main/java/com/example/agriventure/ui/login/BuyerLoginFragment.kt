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
import com.andrognito.pinlockview.PinLockListener
import com.example.agriventure.R
import com.example.agriventure.databinding.FragmentBuyerLoginBinding
import com.example.agriventure.interfaces.OnDataReceived
import com.example.agriventure.ui.BaseFragment
import com.example.agriventure.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BuyerLoginFragment : BaseFragment() {

    private lateinit var binding: FragmentBuyerLoginBinding
    private lateinit var controller: NavController
    private lateinit var loginDialog : ProgressDialog
    private val databaseRef :DatabaseReference by lazy{
        Firebase.database.reference.child("buyer_profiles")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentBuyerLoginBinding.inflate(inflater,container,false)
        binding.pinLockView.setPinLockListener(pinLockListener)
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controller = Navigation.findNavController(requireView())
        loginDialog = ProgressDialog(activity)
        loginDialog.setTitle("Logging you in...")
        binding.btnCreateProfile.setOnClickListener { view1: View? -> controller.navigate(R.id.action_navigation_buyer_login_to_navigation_buyer_register) }
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
        loginDialog.show()
        val queryKey = activity.getPreferences(Context.MODE_PRIVATE).getString(Constants.buyerFirebaseKey,null)
        if (queryKey!=null){
            //User data exists on the device
            //Confirm if PIN entered matches remote PIN
            loginUser(inputPin, queryKey)
        }else{
            //User data does not exist on device
            //Confirm if PIN entered matches remote PIN
            var isUserFound = false
            var currentKey = ""
            var currentPin = ""
            databaseRef.addListenerForSingleValueEvent(
                object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap : DataSnapshot in snapshot.children){
                            val loopKey = snap.key!!
                            val loopPin = snap.child("pin").value as String
                            if (loopPin.equals(inputPin)){
                                isUserFound = true
                                currentKey = loopKey
                                currentPin = loopPin
                            }
                        }
                        if(isUserFound){
                            callback.userFound(currentPin, currentKey)
                        }else{
                            callback.userNotFound()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )
        }
    }

    private fun loginUser(inputPin: String, queryKey: String) {
        databaseRef.child(queryKey).get().addOnSuccessListener{
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
                        putString(Constants.buyerName, it.child("name").value.toString())
                        putString(Constants.buyerBusinessName, it.child("businessName").value.toString())
                        putString(Constants.buyerContact, it.child("contact").value.toString())
                        putString(Constants.buyerFirebaseKey, queryKey)
                        apply()
                    }
                    loginDialog.hide()
                    activity.setUpBottomNavigation("Buyer", R.menu.buyer_bottom_nav_menu, R.id.navigation_buyer_market)
                    controller.navigate(R.id.action_navigation_buyer_login_to_navigation_buyer_market)
                }else{
                    loginDialog.hide()
                    Toast.makeText(activity, "Incorrect PIN",LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener{
            loginDialog.hide()
            Toast.makeText(activity, "Something went wrong. Try again", LENGTH_SHORT).show()
        }
    }

    private fun showRegisterUI() {
        loginDialog.hide()
        Toast.makeText(activity, "Profile does not exist, please register", LENGTH_SHORT).show()
        binding.pinLockView.visibility = View.GONE
        binding.appSubtitle.visibility = View.GONE
        binding.indicatorDots.visibility = View.GONE
        binding.btnCreateProfile.visibility = View.VISIBLE
    }

}