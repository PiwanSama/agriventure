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

public class FarmerLoginFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_farmer_login, container, false);

        PinLockView mPinLockView = root.findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(pinLockListener);

        IndicatorDots mIndicatorDots = root.findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        return root;
    }

    private final PinLockListener pinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if(pin.equals("2222")){
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_login_to_navigation_market);
            }else{
                }
                Toast.makeText(activity, "Incorrect PIN entered", Toast.LENGTH_SHORT).show();
            }

        @Override
        public void onEmpty() {
            Log.d("Login", "Empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };
}
