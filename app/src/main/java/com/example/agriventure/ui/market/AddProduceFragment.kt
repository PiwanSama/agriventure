package com.example.agriventure.ui.market

import com.example.agriventure.ui.BaseFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.agriventure.R
import android.widget.ArrayAdapter
import android.content.Intent
import android.app.Activity
import android.provider.MediaStore
import android.app.ProgressDialog
import com.google.android.gms.tasks.OnSuccessListener
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.OnProgressListener
import com.example.agriventure.data.models.Produce
import android.text.Editable
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.DatePicker
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import com.example.agriventure.databinding.FragmentAddProduceBinding
import com.example.agriventure.util.Constants
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.lang.Exception
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddProduceFragment() : BaseFragment() {
    private lateinit var c: Calendar
    private lateinit var binding: FragmentAddProduceBinding
    private lateinit var filePath: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var mDownloadUrl: String
    private lateinit var businessName : String
    private var isImageUploaded = false
    private val PICK_IMAGE_REQUEST = 71

    private val databaseRef by lazy{
        Firebase.database.reference
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddProduceBinding.inflate(inflater, container, false)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        c = Calendar.getInstance()

        businessName =
            activity.getPreferences(Context.MODE_PRIVATE).getString(Constants.farmerBusinessName, "").toString()

        binding.companyName.setText(businessName)
        binding.btnAddProduce.setOnClickListener { v: View? -> createNewProduct() }
        binding.btnUploadImage.setOnClickListener { v: View? -> selectProduceImage() }

        val category_array = resources.getStringArray(R.array.product_categories)
        val categories = ArrayList(Arrays.asList(*category_array))
        val category_adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(activity, R.layout.list_item, categories as List<Any?>)
        binding.produceCategory.setAdapter(category_adapter)
        val availability_array = resources.getStringArray(R.array.product_availablity)
        val states = ArrayList(Arrays.asList(*availability_array))
        val availability_adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(activity, R.layout.list_item, states as List<Any?>)
        binding.produceState.setAdapter(availability_adapter)
        val formatter = DecimalFormat("#,###,###")
    }

    private fun selectProduceImage() {
        val imgUploadIntent = Intent()
        imgUploadIntent.type = "image/*"
        imgUploadIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(imgUploadIntent, "Select Produce Image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            Log.i("UPLOAD", "Got path")
            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, filePath)
                binding.itemImage.visibility = View.VISIBLE
                binding.itemImage.setImageBitmap(bitmap)
                isImageUploaded = true
                mDownloadUrl = uploadProduceImage()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("UPLOAD", e.toString())
            }
        }
    }

    private fun uploadProduceImage(): String {
        val imageProgressDialog = ProgressDialog(activity)
        imageProgressDialog.setTitle("Uploading your image")
        imageProgressDialog.show()
        val ref = storageReference.child("images/" + UUID.randomUUID())
        ref.putFile(filePath)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    val firebaseUri = taskSnapshot.storage.downloadUrl
                    firebaseUri.addOnSuccessListener { uri: Uri ->
                        mDownloadUrl = uri.toString()
                        Toast.makeText(activity, "Image Uploaded", Toast.LENGTH_SHORT).show()
                        binding.btnUploadImage.isEnabled = false
                        imageProgressDialog.dismiss()
                    }
                }
                .addOnFailureListener { e: Exception ->
                    imageProgressDialog.dismiss()
                    Toast.makeText(activity, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                    imageProgressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        return mDownloadUrl
    }

    private fun createNewProduct() {
        val produce = Produce()
        val category = binding.produceCategory.text.toString()
        val name = binding.produceName.text.toString()
        val amount = binding.produceQuantity.text.toString()
        val price = binding.producePrice.text.toString()
        val state = binding.produceState.text.toString()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_WEEK]
        if (category.isEmpty() || category == "Produce Category" || state.isEmpty() || state == "Produce Availability" || name.isEmpty() || amount.isEmpty() || price.isEmpty() || !isImageUploaded) {
            Toast.makeText(activity, "Please enter all fields", Toast.LENGTH_SHORT).show()
        } else {
            produce.product_name = name
            produce.product_category = category
            produce.product_quantity = amount
            produce.product_price = price
            //Set default variables
            produce.isIs_sold = false
            produce.setSeller_name(businessName)
            //set image url
            produce.setProduct_image(mDownloadUrl)
            //show date picker if product is available soon
            if (state.equals("Available Soon")) {
                val datePickerDialog = DatePickerDialog(activity,
                        { view: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                            val dateAvailable = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year1
                            if (dateValid(dateAvailable)) {
                                produce.setProduct_maturity_date(dateAvailable)
                                writeNewProduct(produce)
                            } else {
                                Toast.makeText(activity, "Availability date cannot be before than today", Toast.LENGTH_SHORT).show()
                            }
                        }, year, month, day)
                datePickerDialog.setTitle("Product Availability Date")
                datePickerDialog.show()
            } else {
                writeNewProduct(produce)
            }
        }
    }

    private fun dateValid(selectedDate: String): Boolean {
        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        @SuppressLint("SimpleDateFormat") val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        try {
            val currentDate = dateFormat.parse(today)
            val availableDate = dateFormat.parse(selectedDate)
            return availableDate.after(currentDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    private fun writeNewProduct(produce: Produce) {
        val productProgressDialog = ProgressDialog(activity)
        productProgressDialog.setTitle("Uploading your produce")
        productProgressDialog.show()
        val newKey = databaseRef.child("produce").push().key!!
        produce.setProduct_id(newKey)
        //produce.setProduct_image(uploadProduceImage());
        databaseRef.child("produce").child(newKey).setValue(produce)
        Toast.makeText(activity, produce.product_name + " has been added to your products", Toast.LENGTH_SHORT).show()
        binding.btnAddProduce.isEnabled = false
        productProgressDialog.hide()
    }
}