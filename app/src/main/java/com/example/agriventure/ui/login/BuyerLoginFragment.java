package com.example.agriventure.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.agriventure.R;
import com.example.agriventure.ui.BaseFragment;

public class BuyerLoginFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buyer_login, container, false);

        PinLockView mPinLockView = root.findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(pinLockListener);

        IndicatorDots mIndicatorDots = root.findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        return root;
    }

    private final PinLockListener pinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (pin.equals("5555")){
                activity.setUpBottomNavigation("Buyer",R.menu.buyer_bottom_nav_menu,R.id.navigation_buyer_market);
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_buyer_login_to_navigation_buyer_market);
            }else{
                Toast.makeText(activity, "Incorrect PIN entered", Toast.LENGTH_SHORT).show();
                }
            }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };
}
