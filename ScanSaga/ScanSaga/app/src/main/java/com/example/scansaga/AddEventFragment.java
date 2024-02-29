package com.example.scansaga;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.scansaga.Event;
import com.example.scansaga.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddEventFragment extends DialogFragment {

    // Interface for communication with the Main activity
    interface AddEventDialogListener {
        void addEvent(Event event);
        void deleteEvent(Event event);
    }

    private AddEventDialogListener listener; // Listener for communicating with the main activity
    private EditText editEventName; // EditText for event name
    private EditText editDate; // EditText for date


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_event, null);

        // Get references to EditText fields
        editEventName = view.findViewById(R.id.edit_text_event_text);
        editDate = view.findViewById(R.id.edit_date_text);

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

                    // Validate input fields
                    if (eventName.isEmpty() || date.isEmpty()) {
                        // Display error message if any field is empty
                        Toast.makeText(requireContext(), "Please fill in all details of the event", Toast.LENGTH_SHORT).show();
                    } else {
                        // Add the event using the listener
                        listener.addEvent(new Event(eventName, date, null)); // Assuming QR code is not generated here
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
}
