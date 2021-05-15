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
import com.example.agriventure.data.models.Produce;
import com.example.agriventure.ui.BaseFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class AddProduceFragment extends BaseFragment {

    private MaterialButton btn_add_produce, btn_upload_image;
    private AutoCompleteTextView category_view;
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_produce, container, false);
        btn_add_produce = root.findViewById(R.id.btn_add_produce);
        btn_upload_image = root.findViewById(R.id.btn_upload_image);
        category_view = root.findViewById(R.id.produce_category);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_add_produce.setOnClickListener(v -> {
            writeNewProduct();
        });

        btn_upload_image.setOnClickListener(v -> Toast.makeText(activity, "Todo upload image", Toast.LENGTH_SHORT).show());

        String[]  category_array= getResources().getStringArray(R.array.product_categories);
        ArrayList<String> categories= new ArrayList<>(Arrays.asList(category_array));

        ArrayAdapter category_adapter = new ArrayAdapter(activity, R.layout.list_item, categories);
        category_view.setAdapter(category_adapter);

    }

    private void writeNewProduct(){
        Produce produce = new Produce("maize","Maize Ad Astra","Cereals","Available","12/06/2021", "600 UGX / KG", "2 Tonnes",false,1, "Hosanna Cereal Industries");
        String newKey = mDatabase.child("produce").push().getKey();
        assert newKey != null;
        produce.setProduct_id(newKey);
        mDatabase.child("produce").child(newKey).setValue(produce);
    }
}