package com.irsage.employeeapp;

public class User {
    public String fullName, email, password, department, district;
    public boolean isAdmin;

    public User(String fullName, String email, String password, String department, String district, String profileUrl, boolean isAdmin) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.district = district;
        //this.profileUrl = profileUrl;
        this.isAdmin = isAdmin;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDepartment() {
        return department;
    }

    public String getDistrict() {
        return district;
    }

    //public String getProfileUrl() {
       // return profileUrl;
    //}

    public boolean isAdmin() {
        return isAdmin;
    }

    public User() {

    }
}
