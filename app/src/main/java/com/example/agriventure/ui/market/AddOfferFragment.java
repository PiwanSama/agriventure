package com.example.agriventure.ui.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Offer;
import com.example.agriventure.data.models.Produce;
import com.example.agriventure.ui.BaseFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class AddOfferFragment extends BaseFragment {

    private MaterialButton btn_add_offer;
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_offer, container, false);
        btn_add_offer = root.findViewById(R.id.btn_add_offer);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_add_offer.setOnClickListener(v -> {
            writeNewOffer();
        });

    }

    private void writeNewOffer(){
        Offer offer = new Offer("M_j_c8Er_n62WloRyuX","Mama's NGO", "Pending","550 UGX", "14th August 2020");
        String newKey = mDatabase.child("offers").push().getKey();
        assert newKey != null;
        offer.setOffer_id(newKey);
        mDatabase.child("offers").child(newKey).setValue(offer);
    }
}