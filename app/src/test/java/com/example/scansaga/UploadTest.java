package com.example.scansaga;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link Upload}.
 * Verifies proper handling of the imageUrl property in the Upload class.
 */
public class UploadTest {

    private Upload upload;

    /**
     * Sets up the test environment before each test method.
     * Initializes a new Upload instance.
     */
    @Before
    public void setUp() {
        upload = new Upload();
    }

    /**
     * Tests that the default constructor initializes the image URL to null.
     */
    @Test
    public void testDefaultConstructor() {
        assertNull("ImageUrl should be null", upload.getImageUrl());
    }

    /**
     * Tests the parameterized constructor for correct assignment of the image URL.
     */
    @Test
    public void testParameterizedConstructor() {
        String imageUrl = "https://example.com/image.png";
        Upload uploadWithImage = new Upload(imageUrl);
        assertEquals("ImageUrl should match the constructor argument", imageUrl, uploadWithImage.getImageUrl());
    }

    /**
     * Tests both the setter and getter methods for the image URL property.
     * Verifies that the image URL is set and retrieved correctly.
     */
    @Test
    public void testSetAndGetImageUrl() {
        String imageUrl = "https://example.com/image.png";
        upload.setImageUrl(imageUrl);
        assertEquals("ImageUrl should be set and retrieved correctly", imageUrl, upload.getImageUrl());
    }
}