package com.example.agriventure.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.agriventure.R;
import com.example.agriventure.data.models.Offer;
import com.example.agriventure.ui.BaseFragment;

import java.util.Objects;

public class LoginFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        PinLockView mPinLockView = root.findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(pinLockListener);

        IndicatorDots mIndicatorDots = root.findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        return root;
    }

    private final PinLockListener pinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d("Login", "Complete");
            if (pin.equals("1234")){
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_login_to_navigation_market);
            }else{
                Toast.makeText(activity, "Incorrect PIN entered", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onEmpty() {
            Log.d("Login", "Empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d("Login", "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };
}
