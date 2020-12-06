package com.irsage.employeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    DatabaseReference reference;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ArrayList<User> list;
    EmployeeAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<User>();


        fab =findViewById(R.id.addUserBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AllUsersActivity.this,CreateUserActivity.class);
                intent.putExtra("editMode",false);
                startActivity(intent);
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                   User employee = dataSnapshot1.getValue(User.class);
                   list.add(employee);
               }
                showStudentEmployee();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllUsersActivity.this, "Something Is Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        showStudentEmployee();
    }

    public  void showStudentEmployee() {
        adapter = new EmployeeAdapter(AllUsersActivity.this,list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()){
            case 101:
                adapter.deleteEmployee();
                return true;
            case 102:
                adapter.updateEmployee();
                return true;

        }
        return true;
    }


}