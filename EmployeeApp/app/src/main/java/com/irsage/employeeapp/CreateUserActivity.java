package com.irsage.employeeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CreateUserActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
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
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions =new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        employeeImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                pickImage();
            }
        });
        saveEmployeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //uploadImageToFirebase();
                uploadEmployee();
            }
        });
    }

//Uploading Employee
private void uploadEmployee() {
    String fullName = editTextFullName.getText().toString();
    String email = editTextEmail.getText().toString();
    String password = editTextPassword.getText().toString();
    String department = editTextDepartment.getText().toString();
    String district = editTextDistrict.getText().toString();
    //String profile = profileUrl.toString();
    if (fullName.isEmpty()) {
        editTextFullName.setError("Full Name Is Required!");
        editTextFullName.requestFocus();
        return;
    }
    if (department.isEmpty()) {
        editTextDepartment.setError("Department");
        editTextDepartment.requestFocus();
        return;
    }
    if (district.isEmpty()) {
        editTextDistrict.setError("District is required!");
        editTextDistrict.requestFocus();
        return;
    }
    if (email.isEmpty()) {
        editTextEmail.setError("Email Is Required!");
        editTextEmail.requestFocus();
        return;
    }
    if (password.isEmpty()) {
        editTextPassword.setError("Password Is Required!");
        editTextPassword.requestFocus();
        return;
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        editTextEmail.setError("Enter a valid email address");
        editTextEmail.requestFocus();
        return;
    }
    if (password.length() < 6){
        editTextPassword.setError(" Pass Word Should Be At least 6 Characters");
        editTextPassword.requestFocus();
        return;
    }
}




    private void pickImage() {
        //Pick Profile Picture
        String[] options={"Camera","Gallery"};
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Select image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0){
                    //if 0 then open camera and check camera permissions
                    if (!checkCameraPermission()){
                        //if permission is not granted , request fot it
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                else if(which==1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        PickFromStorage();
                    }
                }
            }
        });
        builder.create().show();
    }

    //check storage permission
    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return  result;
    }
    // request storage permissions
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }
    // check camera permission
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }
    // request camera permissions
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]== PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera permission denied!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        PickFromStorage();
                    }
                    else{
                        Toast.makeText(this, "Storage permission denied!",Toast.LENGTH_SHORT).show();
                    }}
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*
        add image cropping library
         */
        if(resultCode == RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1).start(this);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).
                        setAspectRatio(1,1).start(this);

            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri=resultUri;
                    employeeImg.setImageURI(resultUri);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error= result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void PickFromStorage() {
        Intent galleryIntent=new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"images title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"images description");

        imageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }
}