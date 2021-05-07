package com.example.agriventure.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.agriventure.R;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);
        /*final TextView textView = root.findViewById(R.id.text_login);
        loginViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        PinLockView mPinLockView = root.findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(pinLockListener);

        IndicatorDots mIndicatorDots = root.findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        return root;
    }

    private PinLockListener pinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d("Login", "Complete");
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
