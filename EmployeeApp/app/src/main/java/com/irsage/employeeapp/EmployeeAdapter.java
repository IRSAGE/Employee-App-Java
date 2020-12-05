package com.irsage.employeeapp;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    Context context;
    ArrayList<User> profiles = null;
    public EmployeeAdapter (Context c , ArrayList<User>p){
        context = c;
        profiles = p;
    }
    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeeViewHolder(LayoutInflater.from(context).inflate(R.layout.employee_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        holder.fName.setText(profiles.get(position).getFullName());
        holder.empDepartment.setText(profiles.get(position).getDepartment());
        holder.empEmail.setText(profiles.get(position).getEmail());
        //Picasso.get().load(profiles.get(position).getProfilePic()).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder
    {
         TextView fName,empEmail,empDepartment;
        ImageView profilePic;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
             fName = (TextView)itemView.findViewById(R.id.employeeName);
             empEmail= (TextView)itemView.findViewById(R.id.employeeEmail);
             empDepartment= (TextView)itemView.findViewById(R.id.employeeDepartment);
            profilePic = (ImageView) itemView.findViewById(R.id.profileImage);
        }
    }

}
