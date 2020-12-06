package com.irsage.employeeapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UpdateEmployeeActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private ImageView employeeImg;
    private ProgressBar progressBar;
    private EditText editTextFullName,editTextEmail,editTextPassword,editTextDepartment,editTextDistrict;
    Button saveEmployeeBtn;
    ActionBar actionBar;

    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=101;
    private static final int IMAGE_PICK_CAMERA_CODE=102;
    private static final int IMAGE_PICK_GALLERY_CODE=103;

    private String[] cameraPermissions;
    private String[] storagePermissions;
    private Uri imageUri;

    String employeeEmail;
    String empFullName,empPassword,empDepartment,empDistrict;
    String profileUrlToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Create Firebase storage Reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        employeeImg =findViewById(R.id.employeeImage);
        editTextFullName= findViewById(R.id.employeeName);
        editTextEmail=findViewById(R.id.employeeEmail);
        editTextPassword=findViewById(R.id.employeePassword);
        editTextDepartment=findViewById(R.id.employeeDepartment);
        editTextDistrict = findViewById(R.id.employeeDistrict);
        saveEmployeeBtn = findViewById(R.id.addButton);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        Intent intent=getIntent();
        cameraPermissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions =new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        employeeEmail= intent.getStringExtra("Email");
        empDepartment= intent.getStringExtra("Department");
        empFullName= intent.getStringExtra("empFullName");
        empDistrict= intent.getStringExtra("District");
        empPassword= intent.getStringExtra("Password");
        profileUrlToDelete= intent.getStringExtra("Profile");

        if (profileUrlToDelete.equals(null)){
            employeeImg.setImageResource(R.drawable.add_photo);
        }
        else {
            Picasso.get().load(profileUrlToDelete).into(employeeImg);
        }
        editTextFullName.setText(empFullName);
        editTextEmail.setText(employeeEmail);
        editTextPassword.setText(empPassword);
        editTextDepartment.setText(empDepartment);
        editTextDistrict.setText(empDistrict);


    }
}