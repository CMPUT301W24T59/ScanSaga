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
    private Bitmap qrCodeBitmap;
    private String imageUrl; // URL of the event's poster image

    /**
     * Constructor for creating an Event object with QR code bitmap and image URL.
     *
     * @param name         The name of the event.
     * @param date         The date of the event.
     * @param venue        The venue of the event.
//     * @param qrCodeBitmap The QR code bitmap associated with the event.
     * @param imageUrl     The URL of the image associated with the event.
     */
    public Event(String name, String date, String venue,  String imageUrl) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.imageUrl = imageUrl;
        this.qrCodeBitmap = null;
        try {
            this.qrCodeBitmap = generateQrCodeBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
            // Handle the exception
            this.qrCodeBitmap = null;
        }
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
    public Bitmap getQrCodeBitmap() {
        return qrCodeBitmap;
    }
    public Bitmap generateQrCodeBitmap() throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode((this.name+this.venue).toString(), BarcodeFormat.QR_CODE, 512, 512);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bmp;
    }

    // Setter for the QR code bitmap of the event
    public void setQrCodeBitmap(Bitmap qrCodeBitmap) {
        this.qrCodeBitmap = qrCodeBitmap;
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
