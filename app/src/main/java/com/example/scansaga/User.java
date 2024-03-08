package com.example.scansaga;

import java.io.Serializable;

/**
 * User class representing a user entity.
 */

public class User implements Serializable {
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private String pictureUrl;

    /**
     * Constructor for creating a User object.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     * @param email The email of the user.
     * @param phone The phone number of the user.
     */

    public User(String firstname, String lastname,  String email, String phone, String pictureUrl) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
        this.pictureUrl = pictureUrl;
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

    public void setPhone(String phone) {this.phone = phone;}

    // Getter for the image URL of the event's poster
    public String getProfileImageUrl() {
        return pictureUrl;
    }

    // Setter for the image URL of the event's poster
    public void setProfileImageUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
