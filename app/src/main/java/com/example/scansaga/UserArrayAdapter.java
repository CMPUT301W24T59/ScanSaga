package com.example.scansaga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private ArrayList<User> mUserList;

    public UserArrayAdapter(Context context, ArrayList<User> userList) {
        super(context, 0, userList);
        mContext = context;
        mUserList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_content, parent, false);
        }

        // Lookup view for data population
        TextView firstNameTextView = convertView.findViewById(R.id.textview_first_name);
        TextView lastNameTextView = convertView.findViewById(R.id.textview_last_name);
        TextView emailTextView = convertView.findViewById(R.id.textview_email);
        TextView phoneTextView = convertView.findViewById(R.id.textview_phone);

        // Populate the data into the template view using the data object
        if (user != null) {
            firstNameTextView.setText(user.getFirstname());
            lastNameTextView.setText(user.getLastname());
            emailTextView.setText(user.getEmail());
            phoneTextView.setText(user.getPhone());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
