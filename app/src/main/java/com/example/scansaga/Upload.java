package com.example.scansaga;

/**
 * Represents an upload with an associated image URL.
 */
public class Upload {
    private String ImageUrl;

    /**
     * Default constructor which initializes a new Upload instance with no image URL.
     */
    public Upload() {
    }

    /**
     * Constructs an Upload instance with the specified image URL.
     *
     * @param imageUrl The image URL to be associated with this upload.
     */
    public Upload(String imageUrl) {
        ImageUrl = imageUrl;
    }

    /**
     * Returns the image URL associated with this upload.
     *
     * @return The image URL of this upload.
     */
    public String getImageUrl() {
        return ImageUrl;
    }

    /**
     * Sets the image URL of this upload.
     *
     * @param imageUrl The new image URL to be associated with this upload.
     */
    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
