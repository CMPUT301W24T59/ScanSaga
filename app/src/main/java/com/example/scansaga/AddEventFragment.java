package com.example.scansaga;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEventFragment extends DialogFragment {

    // Interface for communication with the Main activity
    interface AddEventDialogListener {
        void addEvent(Event event);

        void deleteEvent(Event event);
    }

    private AddEventDialogListener listener; // Listener for communicating with the main activity
    private EditText editEventName; // EditText for event name
    private EditText editDate; // EditText for date
    private EditText editVenue; // EditText for venue
    private TextView txtQrCode; // TextView to display the generated QR code

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_event, null);

        // Get references to EditText fields
        editEventName = view.findViewById(R.id.edit_text_event_text);
        editDate = view.findViewById(R.id.edit_date_text);
        editVenue = view.findViewById(R.id.edit_venue_text);


        // Set click listener for date EditText to show date picker dialog
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit an Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String eventName = editEventName.getText().toString();
                    String date = editDate.getText().toString();
                    String venue = editVenue.getText().toString();

                    // Validate input fields
                    if (eventName.isEmpty() || date.isEmpty() || venue.isEmpty()) {
                        // Display error message if any field is empty
                        Toast.makeText(requireContext(), "Please fill in all details of the event", Toast.LENGTH_SHORT).show();
                    } else {
                        // Generate QR code for the event
                        String qrContent = generateQRContent(eventName, date, venue);
                        Bitmap qr = generateQRCodeBitmap(qrContent); // Display QR code
                        // Add the event using the listener
                        listener.addEvent(new Event(eventName, date, venue, qr));
                    }
                })
                .create();
    }

    // Method to set the listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddEventDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddEventDialogListener");
        }
    }

    // Method to show the date picker dialog
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the date EditText with the selected date
                        editDate.setText(String.format("%d/%d/%d", monthOfYear + 1, dayOfMonth, year));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    //FOR CITATION LATER: https://www.geeksforgeeks.org/how-to-build-a-qr-code-android-app-using-firebase/

    // Method to generate QR content for the event
    private String generateQRContent(String eventName, String date, String venue) {
        return "Event: " + eventName + "\nDate: " + date + "\nVenue: " + venue;
    }

    // Method to generate QR code from content
    private Bitmap generateQRCodeBitmap(String data) {
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

