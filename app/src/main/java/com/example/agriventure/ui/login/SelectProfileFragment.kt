package com.example.agriventure.ui.login

import com.example.agriventure.ui.BaseFragment
import androidx.constraintlayout.widget.ConstraintLayout
import com.jjlf.jjkit_ripplewrapper.JJRippleWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.agriventure.R
import androidx.annotation.RequiresApi
import android.os.Build
import android.view.View
import androidx.navigation.Navigation
import com.example.agriventure.databinding.FragmentSelectProfileBinding

class SelectProfileFragment : BaseFragment() {
    private lateinit var binding : FragmentSelectProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isNetworkConnected) {
            binding.farmerProfile.setOnClickListener { v: View? ->
                Navigation.findNavController(
                    v!!
                ).navigate(R.id.action_navigation_select_profile_to_farmer_login)
            }
            binding.buyerProfile.setOnClickListener { v: View? ->
                Navigation.findNavController(
                    v!!
                ).navigate(R.id.action_navigation_select_profile_to_navigation_buyer_login)
            }
            binding.topRipple.setOnClickListener { v: View? ->
                Navigation.findNavController(
                    v!!
                ).navigate(R.id.action_navigation_select_profile_to_farmer_login)
            }
            binding.bottomRipple.setOnClickListener { v: View? ->
                Navigation.findNavController(
                    v!!
                ).navigate(R.id.action_navigation_select_profile_to_navigation_buyer_login)
            }
        } else {
            binding.farmerProfile!!.setOnClickListener { v: View? -> activity.showSnack("No internet connection!") }
            binding.buyerProfile.setOnClickListener { v: View? -> activity.showSnack("No internet connection!") }
            binding.topRipple.setOnClickListener { v: View? -> activity.showSnack("No internet connection!") }
            binding.bottomRipple.setOnClickListener { v: View? -> activity.showSnack("No internet connection!") }
        }
    }
}