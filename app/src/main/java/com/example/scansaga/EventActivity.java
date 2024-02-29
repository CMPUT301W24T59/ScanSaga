package com.example.scansaga;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class EventActivity extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {

    private ArrayList<Event> dataList;
    private ArrayAdapter<Event> eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_main);

        dataList = new ArrayList<>();

        // Example data - you may want to replace this with actual data retrieval logic
        String[] names = {};
        String[] years = {};
        Bitmap[] Qrs = {};

        // Populate dataList with events
        for (int i = 0; i < names.length; i++) {
            dataList.add(new Event(names[i], years[i], Qrs[i]));
        }

        // Initialize and set up the ArrayAdapter
        eventAdapter = new EventArrayAdapter(this, dataList);

        // Set up FloatingActionButton to show AddEventFragment
        FloatingActionButton fab = findViewById(R.id.add_event_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the AddEventFragment when the FloatingActionButton is clicked
                new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
            }
        });

    }

    // Method to show the date picker dialog
    public void showDatePickerDialog(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

    }


    public void addEvent(Event event) {
        // Add the new event to the dataList
        dataList.add(event);
        // Notify the adapter that the data set has changed
        eventAdapter.notifyDataSetChanged();
    }


    public void deleteEvent(Event event) {
        // Remove the event from the dataList
        dataList.remove(event);
        // Notify the adapter that the data set has changed
        eventAdapter.notifyDataSetChanged();
    }
}
