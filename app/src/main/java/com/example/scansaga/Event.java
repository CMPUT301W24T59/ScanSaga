package com.example.scansaga;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.Serializable;

public class Event implements Serializable {
    private String name;
    private String date;
    private String venue;
    private Bitmap qrCodeBitmap;


    public Event(String name, String date,String venue, Bitmap qrCodeBitmap) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.qrCodeBitmap = qrCodeBitmap;

    }

    public String getName() {
        return name;
    } //getter for book name
    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }  //getter for Publication Year
    public Bitmap getQrCodeBitmap(){return qrCodeBitmap;}

    public void setName(String name) {
        this.name = name;
    } //Setter for book name

    public void setDate(String date) {
        this.date = date;
    }  //Setter for Publication year
    public void setVenue(String venue){this.venue = venue;}
    public void setQrCodeBitmap(Bitmap qrCodeBitmap){this.qrCodeBitmap = qrCodeBitmap;}
}