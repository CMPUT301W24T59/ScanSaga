package com.example.scansaga.Model;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.scansaga.Controllers.ViewPagerAdapter;
import com.example.scansaga.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ShowCheckedInAttendeesActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_NAME_DATE = "extra_event_name_date"; // use a consistent key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_checked_in_attendees);

        String eventNameDate = getIntent().getStringExtra(EXTRA_EVENT_NAME_DATE); // Get the extra from the intent

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(this, eventNameDate)); // Pass the extra to the adapter

        TabLayout tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Map" : "Attendees")
        ).attach();
    }
}
