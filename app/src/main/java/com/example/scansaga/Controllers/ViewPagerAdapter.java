package com.example.scansaga.Controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.scansaga.Model.ShowCheckedInAttendeesActivity;
import com.example.scansaga.R;
import com.example.scansaga.Views.AttendeesListFragment;
import com.example.scansaga.Views.MapFragment;

// This manges the ability to rotate between the two tabs for checked in users (Maps and List fragments)
public class ViewPagerAdapter extends FragmentStateAdapter {

    private final String eventNameDate;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String eventNameDate) {
        super(fragmentActivity);
        this.eventNameDate = eventNameDate;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create a bundle to hold the arguments
        Bundle args = new Bundle();
        args.putString(ShowCheckedInAttendeesActivity.EXTRA_EVENT_NAME_DATE, eventNameDate);

        // Create the appropriate fragment and set its arguments
        Fragment fragment = position == 0 ? new MapFragment() : new AttendeesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2; // Since we have two tabs
    }
}
