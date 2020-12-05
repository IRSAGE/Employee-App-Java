package com.irsage.employeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail , editTextPassword;
    private Button sighIn;
    private FirebaseUser user;
    private DatabaseReference reference;
    private  String userId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        sighIn = (Button)findViewById(R.id.signIn);
        sighIn.setOnClickListener(this);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                //startActivity(new Intent(this, Register.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
        }
    }

    public void userLogin() {
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
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
            editTextPassword.setError(" PassWord Should Be At least 6 Characters");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            reference = FirebaseDatabase.getInstance().getReference("Users");
                            userId =  user.getUid();
                            //Getting logged user info
                            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User userProfile = snapshot.getValue(User.class);
                                    if (userProfile != null){
                                        boolean isAdmin = userProfile.isAdmin;
                                        if (!isAdmin){
                                            startActivity(new Intent(MainActivity
                                                    .this, ProfileActivity.class));
                                        }else{
                                            startActivity(new Intent(MainActivity
                                                    .this, CreateUserActivity.class));
                                        }
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this,
                                                "User successfully LoggedIn",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(MainActivity.this,
                                            "Something Went Wrong!",Toast.LENGTH_LONG).show();
                                }
                            });

                        }else{
                            Toast.makeText(MainActivity.this,
                                    "Invalid Employee Email Or Password",
                                    Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}