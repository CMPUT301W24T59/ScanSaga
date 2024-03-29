package com.example.scansaga;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.scansaga.Controllers.UserArrayAdapter;
import com.example.scansaga.Model.User;

/**
 * Tests for {@link UserArrayAdapter} to ensure it properly populates views with user data.
 */
@RunWith(AndroidJUnit4.class)
public class UserArrayAdapterTest {

    private Context context;

    /**
     * Sets up the context for the test.
     */
    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }

    /**
     * Tests the {@link UserArrayAdapter#getView(int, View, ViewGroup)} method with valid user data.
     * Verifies that the returned view correctly displays user information.
     */
    @Test
    public void testGetView() {
        // Prepare test data
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("Will", "Smith", "will@gmail.com", "1234567890", null));

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

    /**
     * Tests the {@link UserArrayAdapter#getView(int, View, ViewGroup)} method with null user data.
     * Verifies that the returned view correctly handles null values.
     */
    @Test
    public void testGetViewWithNullData() {
        // Prepare test data with null values
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User(null, null, null, null, null));

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
