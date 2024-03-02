package com.example.scansaga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> Users;

    public UserArrayAdapter(Context context, ArrayList<User> Users) {
        super(context, 0, Users);
        this.Users = Users;
        this.context = context;

    }

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
