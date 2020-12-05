package com.irsage.employeeapp;

public class User {
    public String fullName, email, password,department,district,profileUrl;
    public boolean isAdmin;

    public User(){

    }

    public User(String fullName, String email, String password,String department,String district,boolean isAdmin) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.district = district;
        this.isAdmin = isAdmin;
        //this.profileUrl = profile;
    }
}
