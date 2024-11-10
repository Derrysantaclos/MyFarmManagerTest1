package com.derrysantaclos.myFarmManager.models;

public class User {

    private String fullName;
    private String phone;

    public User(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }
}

