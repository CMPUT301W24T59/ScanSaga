package com.example.scansaga.Model;

/**
 * A model class representing an upload, specifically used for storing image upload information.
 * This class contains information about uploaded images, such as the URL to access the image.
 */
public class Upload {
    private String ImageUrl;

    /**
     * Default constructor for creating an Upload instance without setting an image URL.
     * This constructor is used when no initial image URL is available at the time of object creation.
     */

    public Upload(){

    }

    /**
     * Constructs an Upload instance with a specified image URL.
     *
     * @param imageUrl The URL of the uploaded image.
     */
    public Upload(String imageUrl){
        ImageUrl = imageUrl;
    }

    /**
     * Retrieves the URL of the uploaded image.
     *
     * @return A string representing the URL of the image.
     */
    public String getImageUrl(){
        return ImageUrl;
    }

    /**
     * Sets or updates the URL of the uploaded image.
     *
     * @param imageUrl The new URL of the uploaded image.
     */
    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
