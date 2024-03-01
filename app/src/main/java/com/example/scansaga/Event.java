package com.example.scansaga;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Event class represents an event with its name, date, venue, and QR code bitmap.
 */
public class Event implements Serializable {
    private String name;
    private String date;
    private String venue;
    private Bitmap qrCodeBitmap;

    /**
     * Constructor for creating an Event object.
     * @param name The name of the event.
     * @param date The date of the event.
     * @param venue The venue of the event.
     * @param qrCodeBitmap The QR code bitmap associated with the event.
     */
    public Event(String name, String date, String venue, Bitmap qrCodeBitmap) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.qrCodeBitmap = qrCodeBitmap;
    }

    /**
     * Getter for retrieving the name of the event.
     * @return The name of the event.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for retrieving the date of the event.
     * @return The date of the event.
     */
    public String getDate() {
        return date;
    }

    /**
     * Getter for retrieving the venue of the event.
     * @return The venue of the event.
     */
    public String getVenue() {
        return venue;
    }

    /**
     * Getter for retrieving the QR code bitmap associated with the event.
     * @return The QR code bitmap.
     */
    public Bitmap getQrCodeBitmap() {
        return qrCodeBitmap;
    }

    /**
     * Setter for updating the name of the event.
     * @param name The new name of the event.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for updating the date of the event.
     * @param date The new date of the event.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Setter for updating the venue of the event.
     * @param venue The new venue of the event.
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     * Setter for updating the QR code bitmap associated with the event.
     * @param qrCodeBitmap The new QR code bitmap.
     */
    public void setQrCodeBitmap(Bitmap qrCodeBitmap) {
        this.qrCodeBitmap = qrCodeBitmap;
    }
}
