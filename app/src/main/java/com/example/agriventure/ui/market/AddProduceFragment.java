package com.example.agriventure.ui.market;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.agriventure.R;
import com.example.agriventure.data.models.Produce;
import com.example.agriventure.databinding.FragmentAddProduceBinding;
import com.example.agriventure.ui.BaseFragment;
import com.example.agriventure.util.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddProduceFragment extends BaseFragment {

    private Calendar c;
    private FragmentAddProduceBinding binding;
    private DatabaseReference mDatabase;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String mDownloadUrl;
    private boolean isImageUploaded;
    private final int PICK_IMAGE_REQUEST = 71;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_produce, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddProduce.setOnClickListener(v -> {
            createNewProduct();
        });

        c = Calendar.getInstance();

        isImageUploaded = false;

        binding.btnUploadImage.setOnClickListener(v -> selectProduceImage());

        String[]  category_array= getResources().getStringArray(R.array.product_categories);
        ArrayList<String> categories= new ArrayList<>(Arrays.asList(category_array));

        ArrayAdapter category_adapter = new ArrayAdapter(activity, R.layout.list_item, categories);
        binding.produceCategory.setAdapter(category_adapter);

        String[]  availability_array= getResources().getStringArray(R.array.product_availablity);
        ArrayList<String> states= new ArrayList<>(Arrays.asList(availability_array));

        ArrayAdapter availability_adapter = new ArrayAdapter(activity, R.layout.list_item, states);
        binding.produceState.setAdapter(availability_adapter);
        DecimalFormat formatter = new DecimalFormat("#,###,###");

    }

    private void selectProduceImage() {
        Intent imgUploadIntent = new Intent();
        imgUploadIntent.setType("image/*");
        imgUploadIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imgUploadIntent, "Select Produce Image"),PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filePath);
                binding.itemImage.setVisibility(View.VISIBLE);
                binding.itemImage.setImageBitmap(bitmap);
                isImageUploaded = true;
                mDownloadUrl = uploadProduceImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String uploadProduceImage() {
        if(filePath!=null){
            final ProgressDialog imageProgressDialog = new ProgressDialog(activity);
            imageProgressDialog.setTitle("Uploading your image");
            imageProgressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(uri -> {
                            mDownloadUrl = uri.toString();
                            Toast.makeText(activity, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            binding.btnUploadImage.setEnabled(false);
                            imageProgressDialog.dismiss();
                        });
                    })
                    .addOnFailureListener(e -> {
                        imageProgressDialog.dismiss();
                        Toast.makeText(activity, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        imageProgressDialog.setMessage("Uploaded "+(int)progress+"%");
                    });
        }
        return mDownloadUrl;
    }

    private void createNewProduct(){
        Produce produce = new Produce();

        String category = Objects.requireNonNull(binding.produceCategory.getText().toString());
        String name = Objects.requireNonNull(binding.produceName.getText()).toString();
        String amount = Objects.requireNonNull(binding.produceQuantity.getText()).toString();
        String price = Objects.requireNonNull(binding.producePrice.getText()).toString();
        String state = Objects.requireNonNull(binding.produceState.getText()).toString();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_WEEK);

        if (category.equals("")||category.equals("Produce Category")||state.equals("")||state.equals("Produce Availability")||name.equals("")||amount.equals("")||price.equals("")|| !isImageUploaded){
            Toast.makeText(activity, "Please enter all fields",Toast.LENGTH_SHORT).show();
        }else{
            produce.setProduct_name(name);
            produce.setProduct_category(category);
            produce.setProduct_quantity(amount);
            produce.setProduct_price(price);
            //Set default variables
            produce.setIs_sold(false);
            produce.setSeller_name(Constants.sellerName);
            produce.setUser_id(Constants.sellerID);
            //set image url
            produce.setProduct_image(mDownloadUrl);
            //show date picker if product is available soon
            if (state.equals("Available Soon")){
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        (view, year1, monthOfYear, dayOfMonth) -> {

                            String dateAvailable = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                            if (dateValid((dateAvailable))){
                                produce.setProduct_maturity_date(dateAvailable);
                                writeNewProduct(produce);
                            }else{
                                Toast.makeText(activity, "Availability date cannot be before than today",Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);

                     datePickerDialog.setTitle("Product Availability Date");
                     datePickerDialog.show();
                 }else{
                writeNewProduct(produce);
            }
            }
        }

        private boolean dateValid(String selectedDate) {
            String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date currentDate =  dateFormat.parse(today);
                Date availableDate = dateFormat.parse(selectedDate);
                return availableDate.after(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
    }

        private void writeNewProduct(Produce produce){
            final ProgressDialog productProgressDialog = new ProgressDialog(activity);
            productProgressDialog.setTitle("Uploading your produce");
            productProgressDialog.show();

            String newKey = mDatabase.child("produce").push().getKey();
            assert newKey != null;
            produce.setProduct_id(newKey);
            //produce.setProduct_image(uploadProduceImage());
            mDatabase.child("produce").child(newKey).setValue(produce);
            Toast.makeText(activity, produce.getProduct_name()+" has been added to your products",Toast.LENGTH_SHORT).show();
            binding.btnAddProduce.setEnabled(false);

            productProgressDialog.hide();
        }
}
