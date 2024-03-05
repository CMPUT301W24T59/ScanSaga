package com.example.scansaga;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ShowAllEvents extends AppCompatActivity {

    private ListView listView;
    private EventArrayAdapter eventAdapter;
    private ArrayList<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_events);

        listView = findViewById(R.id.event_list);
        eventList = new ArrayList<>(); // Populate this list with events

        // Initialize the custom adapter and set it to the ListView
        eventAdapter = new EventArrayAdapter(this, eventList);
        listView.setAdapter(eventAdapter);
    }
}
