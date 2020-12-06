package com.irsage.employeeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ContextMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    Context context;
    ArrayList<User> profiles = null;
    String employeeEmail;
    String empFullName,empPassword,empDepartment,empDistrict;
    String profileUrlToDelete;
    FirebaseDatabase mFirebaseDatabase ;
    DatabaseReference mRef;
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
        User employeeData = profiles.get(position);
         employeeEmail = employeeData.getEmail();
         profileUrlToDelete = employeeData.getProfileUrl();
         empFullName = employeeData.getFullName();
         empDepartment= employeeData.getDepartment();
         empDistrict = employeeData.getDistrict();
         empPassword= employeeData.getPassword();
         mFirebaseDatabase = FirebaseDatabase.getInstance();
         mRef = mFirebaseDatabase.getReference("Users");
        holder.fName.setText(profiles.get(position).getFullName());
        holder.empDepartment.setText(profiles.get(position).getDepartment());
        holder.empEmail.setText(profiles.get(position).getEmail());
        Picasso.get().load(profiles.get(position).getProfileUrl()).into(holder.profilePic);
    }



    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener
    {
         TextView fName,empEmail,empDepartment;
        ImageView profilePic;
        CardView cardView;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
             fName = (TextView)itemView.findViewById(R.id.employeeName);
             empEmail= (TextView)itemView.findViewById(R.id.employeeEmail);

             empDepartment= (TextView)itemView.findViewById(R.id.employeeDepartment);
             profilePic = (ImageView) itemView.findViewById(R.id.profileImage);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Choose Operation");
            contextMenu.add(getAdapterPosition(),101,0,"Delete Employee");
            contextMenu.add(getAdapterPosition(),102,1,"Update Employee");
        }
    }
    public  void deleteEmployee() {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Delete Employee");
        builder.setMessage("Do You Want To Delete This Employee?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.delete_icon);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference("Users").child(employeeEmail);
                 Query mQuery = mRef.orderByChild("email").equalTo(employeeEmail);
                 mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for (DataSnapshot ds: snapshot.getChildren()){
                             ds.getRef().removeValue();
                             Toast.makeText(context, "Employee Deleted SuccessFull"  ,
                                     Toast.LENGTH_LONG).show();
                             //AllUsersActivity.showStudentEmployee();
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {
                         Toast.makeText(context, error.getDetails()  , Toast.LENGTH_LONG).show();
                     }
                 });
                 //Deleting Related Image
                //StorageReference mPictureRefe = getI
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }



    public  void  updateEmployee(){
                Intent intent = new Intent(context,UpdateEmployeeActivity.class);
                intent.putExtra("FullName", empFullName);
                intent.putExtra("Email", employeeEmail);
                intent.putExtra("Password",empPassword);
                intent.putExtra("Department",empDepartment);
                intent.putExtra("District",empDistrict);
                intent.putExtra("Profile", profileUrlToDelete);
                intent.putExtra("editMode", true);
                context.startActivity(intent);
    }

}
