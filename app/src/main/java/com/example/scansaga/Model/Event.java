package com.example.scansaga.Model;

import android.graphics.Bitmap;
import java.io.Serializable;
import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class Event implements Serializable {
    private String name;
    private String date;
    private String venue;
    private String imageUrl; // URL of the event's poster image
    private String qrUrl;

    /**
     * Constructor for creating an Event object with QR code bitmap and image URL.
     *
     * @param name         The name of the event.
     * @param date         The date of the event.
     * @param venue        The venue of the event.
     * @param imageUrl     The URL of the image associated with the event.
     */
    public Event(String name, String date, String venue,  String imageUrl, String qrUrl) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.imageUrl = imageUrl;
        this.qrUrl = qrUrl;

    }

    // Getter for the name of the event
    public String getName() {
        return name;
    }

    // Setter for the name of the event
    public void setName(String name) {
        this.name = name;
    }

    // Getter for the date of the event
    public String getDate() {
        return date;
    }

    // Setter for the date of the event
    public void setDate(String date) {
        this.date = date;
    }

    // Getter for the venue of the event
    public String getVenue() {
        return venue;
    }

    // Setter for the venue of the event
    public void setVenue(String venue) {
        this.venue = venue;
    }

    // Getter for the QR code bitmap of the event
    public String getQrUrl() {
        return qrUrl;
    }

    // Setter for the QR code bitmap of the event
    public void setQrCodeUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    // Getter for the image URL of the event's poster
    public String getImageUrl() {
        return imageUrl;
    }

    // Setter for the image URL of the event's poster
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
