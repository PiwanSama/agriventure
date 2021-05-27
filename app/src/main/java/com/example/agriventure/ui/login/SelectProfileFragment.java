package com.example.agriventure.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import com.example.agriventure.R;
import com.example.agriventure.ui.BaseFragment;
import com.jjlf.jjkit_ripplewrapper.JJRippleWrapper;

public class SelectProfileFragment extends BaseFragment {

    private ConstraintLayout farmerLayout, buyerLayout;
    private JJRippleWrapper topRipple, bottomRipple;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_select_profile, container, false);

        farmerLayout = root.findViewById(R.id.farmer_profile);

        buyerLayout = root.findViewById(R.id.buyer_profile);

        topRipple = root.findViewById(R.id.top_ripple);

        bottomRipple = root.findViewById(R.id.bottom_ripple);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        farmerLayout.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_select_profile_to_farmer_navigation));

        buyerLayout.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_select_profile_to_buyer_navigation));

        topRipple.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_select_profile_to_farmer_navigation));

        bottomRipple.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_select_profile_to_buyer_navigation));
    }
}
