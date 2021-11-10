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
import com.example.agriventure.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddOfferFragment extends BaseFragment {

    private MaterialButton btn_add_offer;
    private DatabaseReference mDatabase;

    private Produce produce;

    private MaterialTextView productName;
    private MaterialTextView productAmount;
    private MaterialTextView productPrice;
    private MaterialTextView sellerName;

    private TextInputEditText amountText;

    private List<Offer> allOfferList;

    private boolean offerPlaced;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_offer, container, false);
        btn_add_offer = view.findViewById(R.id.btn_add_offer);

        productName = view.findViewById(R.id.item_name);
        productAmount = view.findViewById(R.id.item_weight);
        productPrice = view.findViewById(R.id.item_price);
        sellerName = view.findViewById(R.id.seller_name);

        amountText = view.findViewById(R.id.offer_amount);

        allOfferList = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        produce = bundle.getParcelable("produce");

        productName.setText(produce.getProduct_name());
        productPrice.setText(produce.getProduct_price()+" UGX");
        productAmount.setText(produce.getProduct_quantity());
        sellerName.setText(produce.getSeller_name());

        String productKey = produce.getProduct_id().replace("-","");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_add_offer.setOnClickListener(v -> {
            String offerAmount = amountText.getText().toString();
            if (offerAmount.equals("")){
                Toast.makeText(activity, "Please enter offer amount",Toast.LENGTH_SHORT).show();
            }
            writeNewOffer(productKey, offerAmount, produce.getProduct_name());
        });

    }

    private void writeNewOffer(String productKey, String offerAmount, String productName){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("offers");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allOfferList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Offer offer = dataSnapshot.getValue(Offer.class);
                    if (offer.getBuyer_name().equals(Constants.buyerName)||offer.getProduct_name().equals(produce.getProduct_name())){
                        offerPlaced=true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (offerPlaced){
            Toast.makeText(activity, "You have already placed an offer for this item",Toast.LENGTH_SHORT).show();
        }else{
            String now = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            Offer offer = new Offer(productKey,productName,Constants.buyerName, "Pending",offerAmount, now, Constants.sellerName);
            String newKey = mDatabase.push().getKey();
            assert newKey != null;
            offer.setOffer_id(newKey);
            mDatabase.child(newKey).setValue(offer);
            Toast.makeText(activity, "Your offer has been placed!",Toast.LENGTH_SHORT).show();

            btn_add_offer.setEnabled(false);
        }
    }
}