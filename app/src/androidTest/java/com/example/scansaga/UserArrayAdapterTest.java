package com.example.scansaga;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class UserArrayAdapterTest {

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testGetView() {
        // Prepare test data
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("Will", "Smith", "will@gmail.com", "1234567890"));

        // Create adapter instance
        UserArrayAdapter adapter = new UserArrayAdapter(context, userList);

        // Test getView method
        View view = adapter.getView(0, null, null);

        // Check if view is not null
        assertNotNull(view);

        // Check if views are populated correctly
        TextView firstNameTextView = view.findViewById(R.id.textview_first_name);
        TextView lastNameTextView = view.findViewById(R.id.textview_last_name);
        TextView emailTextView = view.findViewById(R.id.textview_email);
        TextView phoneTextView = view.findViewById(R.id.textview_phone);

        assertEquals("Will", firstNameTextView.getText().toString());
        assertEquals("Smith", lastNameTextView.getText().toString());
        assertEquals("will@gmail.com", emailTextView.getText().toString());
        assertEquals("1234567890", phoneTextView.getText().toString());
    }

    @Test
    public void testGetViewWithNullData() {
        // Prepare test data with null values
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User(null, null, null, null));

        // Create adapter instance
        UserArrayAdapter adapter = new UserArrayAdapter(context, userList);

        // Test getView method
        View view = adapter.getView(0, null, null);

        // Check if view is not null
        assertNotNull(view);

        // Check if views are populated correctly
        TextView firstNameTextView = view.findViewById(R.id.textview_first_name);
        TextView lastNameTextView = view.findViewById(R.id.textview_last_name);
        TextView emailTextView = view.findViewById(R.id.textview_email);
        TextView phoneTextView = view.findViewById(R.id.textview_phone);

        assertEquals("", firstNameTextView.getText().toString());
        assertEquals("", lastNameTextView.getText().toString());
        assertEquals("", emailTextView.getText().toString());
        assertEquals("", phoneTextView.getText().toString());
    }
}

