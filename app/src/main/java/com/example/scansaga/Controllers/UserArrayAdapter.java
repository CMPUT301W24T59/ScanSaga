package com.example.scansaga.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.scansaga.Model.User;
import com.example.scansaga.R;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter to display a list of User objects in a ListView.
 */
public class UserArrayAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> Users;

    /**
     * Constructor for UserArrayAdapter.
     * @param context The current context.
     * @param Users The list of User objects to display.
     */
    public UserArrayAdapter(Context context, ArrayList<User> Users) {
        super(context, 0, Users);
        this.Users = Users;
        this.context = context;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        View view = convertView;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_content, parent, false);
        }

        User user = Users.get(position);

        // Lookup view for data population
        TextView firstNameTextView = view.findViewById(R.id.textview_first_name);
        TextView lastNameTextView = view.findViewById(R.id.textview_last_name);
        TextView emailTextView = view.findViewById(R.id.textview_email);
        TextView phoneTextView = view.findViewById(R.id.textview_phone);

        // Populate the data into the template view using the data object
        firstNameTextView.setText(user.getFirstname());
        lastNameTextView.setText(user.getLastname());
        emailTextView.setText(user.getEmail());
        phoneTextView.setText(user.getPhone());

        // Return the completed view to render on screen
        return view;
    }
}
