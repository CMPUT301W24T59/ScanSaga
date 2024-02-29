package com.example.scansaga;

import java.io.Serializable;

public class User implements Serializable {
    private String lastname;
    private String firstname;
    private String email;
    private String phone;

    public User(String lastname, String firstname, String email, String phone) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // Setters
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
