package com.example.agriventure.ui.login

import com.example.agriventure.ui.BaseFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.agriventure.R
import com.andrognito.pinlockview.PinLockView
import com.andrognito.pinlockview.IndicatorDots
import com.google.android.material.button.MaterialButton
import com.andrognito.pinlockview.PinLockListener
import android.widget.Toast
import androidx.navigation.Navigation

class FarmerLoginFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_farmer_login, container, false)
        val mPinLockView: PinLockView = root.findViewById(R.id.pin_lock_view)
        mPinLockView.setPinLockListener(pinLockListener)
        val mIndicatorDots: IndicatorDots = root.findViewById(R.id.indicator_dots)
        mPinLockView.attachIndicatorDots(mIndicatorDots)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navigateToRegister: MaterialButton = view.findViewById(R.id.create_farmer_profile)
        navigateToRegister.setOnClickListener { view1: View? -> Navigation.findNavController(requireView()).navigate(R.id.action_navigation_farmer_login_to_navigation_farmer_register) }
    }

    private val pinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            if (pin == "2222") {
                activity.setUpBottomNavigation("Farmer", R.menu.farmer_bottom_nav_menu, R.id.navigation_farmer_market)
                Navigation.findNavController(view!!).navigate(R.id.action_navigation_farmer_login_to_navigation_farmer_market)
            } else {
                Toast.makeText(activity, "Incorrect PIN entered", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onEmpty() {
            Log.d("Login", "Empty")
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {}
    }
}