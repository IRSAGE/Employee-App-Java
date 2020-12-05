package com.irsage.employeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private Button logout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userId;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logout = (Button)findViewById(R.id.signOut);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent( ProfileActivity.this,MainActivity.class));
                Toast.makeText(ProfileActivity.this,
                        "You Have successfully LoggedOut",
                        Toast.LENGTH_LONG).show();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId =  user.getUid();
        final TextView departmentT = (TextView)findViewById(R.id.employeeDepartment);
        final TextView fullNameT = (TextView)findViewById(R.id.employeeName);
        final TextView emailT = (TextView)findViewById(R.id.employeeEmail);
        final TextView passwordT = (TextView)findViewById(R.id.employeePassword);
        final TextView districtT = (TextView)findViewById(R.id.employeeDistrict);
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String department = userProfile.department;
                    String password = userProfile.password;
                    String district = userProfile.district;
                    departmentT.setText("Your Department: "+ department);
                    fullNameT.setText("Full Names: "+ fullName);
                    emailT.setText("Your Email: "+ email);
                    passwordT.setText("Your Password: "+ password);
                    districtT.setText("your location: "+ district);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Something Went Wrong! While Retrieving Your Information",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createUser(View view) {
        startActivity(new Intent( ProfileActivity.this,CreateUserActivity.class));
    }
}