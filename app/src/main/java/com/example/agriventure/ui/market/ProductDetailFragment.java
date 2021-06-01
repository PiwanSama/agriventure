package com.example.agriventure.ui.market;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriventure.R;
import com.example.agriventure.data.adapter.OffersAdapter;
import com.example.agriventure.data.adapter.ProductsAdapter;
import com.example.agriventure.data.models.Offer;
import com.example.agriventure.data.models.Produce;
import com.example.agriventure.data.models.Transaction;
import com.example.agriventure.ui.BaseFragment;
import com.example.agriventure.util.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductDetailFragment extends BaseFragment {

    private MaterialTextView productName;
    private MaterialTextView productAmount;
    private MaterialTextView productPrice;
    private MaterialTextView noOffersText;

    private RecyclerView myOffersRv;

    private List<Offer> myOfferList;

    private DatabaseReference mDatabase;

    private ProgressBar mProgressBar;

    private Produce produce;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_market_item_detail, container, false);

        productName = view.findViewById(R.id.item_name);
        productAmount = view.findViewById(R.id.item_weight);
        productPrice = view.findViewById(R.id.item_price);
        noOffersText = view.findViewById(R.id.no_offers_text);

        myOffersRv = view.findViewById(R.id.item_offers_rv);

        mProgressBar = view.findViewById(R.id.progress_bar);

        myOfferList = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        produce = bundle.getParcelable("produce");

        productName.setText(produce.getProduct_name());
        productAmount.setText(produce.getProduct_price());
        productPrice.setText(produce.getProduct_quantity());

        String productKey = produce.getProduct_id().replace("-","");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("offers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOfferList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Offer offer = dataSnapshot.getValue(Offer.class);
                    assert offer != null;
                    if (offer.getProduct_id().equals(productKey)){
                        myOfferList.add(offer);
                    }
                }
                updateView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateView() {
        if (myOfferList.size()>0){
            setUpMyOffers(myOfferList);
        }else{
            noOffersText.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setUpMyOffers(List<Offer> offers) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        OffersAdapter offersAdapter = new OffersAdapter(activity, offers, new OffersAdapter.OfferClickListener() {
            @Override
            public void acceptOffer(Offer offer) {
                String status =  offer.getOffer_status();
                if (status.equals("Accepted")){
                    writeNewTransaction(offer);
                }
            }

            @Override
            public void declineOffer(Offer offer) {
                mDatabase.child("offers").child(offer.getOffer_id()).child("offer_status").setValue("Declined").addOnSuccessListener(aVoid -> Toast.makeText(activity, "You have declined this offer", Toast.LENGTH_SHORT).show());
            }
        });
        myOffersRv.setAdapter(offersAdapter);
        myOffersRv.setLayoutManager(linearLayoutManager);
        myOffersRv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private void writeNewTransaction(Offer offer){
        final ProgressDialog productProgressDialog = new ProgressDialog(activity);
        productProgressDialog.setTitle("Initiating Transaction");
        productProgressDialog.show();

        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        DecimalFormat formatter = new DecimalFormat("#,###,###");

        int tran_amount = Integer.parseInt(offer.getOffer_amount())*Integer.parseInt(produce.getProduct_quantity());
        String formattedAmount = formatter.format(tran_amount)+" UGX";
        Log.i("TOTAL", formattedAmount);

        Transaction transaction = new Transaction(formattedAmount, Constants.sellerName,Constants.buyerName, "Payment of "+tran_amount+" to "+Constants.sellerName+" for "+produce.getProduct_quantity()+" of "+produce.getProduct_name(), today, "Pending", false,false);

        String newKey = mDatabase.child("payments").push().getKey();
        assert newKey != null;
        transaction.setTran_id(newKey);
        mDatabase.child("payments").child(newKey).setValue(transaction);
        Toast.makeText(activity, "Transaction has been submitted for authorization",Toast.LENGTH_SHORT).show();

        productProgressDialog.hide();
    }

}