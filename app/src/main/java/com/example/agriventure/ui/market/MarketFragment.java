package com.example.agriventure.ui.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.agriventure.R;

public class MarketFragment extends Fragment {

    private MarketViewModel marketViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        marketViewModel =
                new ViewModelProvider(this).get(MarketViewModel.class);
        return inflater.inflate(R.layout.fragment_market, container, false);
    }
}