package com.example.scansaga.Controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.scansaga.Model.User;
import com.example.scansaga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter to display a list of User objects in a ListView.
 */
public class SignedUpAttendeesAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> Users;

    private String eventName;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private CollectionReference usersRef;

    private String userID;


    /**
     * Constructor for UserArrayAdapter.
     * @param context The current context.
     * @param Users The list of User objects to display.
     */
    public SignedUpAttendeesAdapter(Context context, ArrayList<User> Users , String eventName) {
        super(context, 0, Users);
        this.Users = Users;
        this.context = context;
        this.eventName = eventName;
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
            view = LayoutInflater.from(context).inflate(R.layout.name_content, parent, false);
        }
        db = FirebaseFirestore.getInstance();

        User user = Users.get(position);
        String ev;
        eventsRef = db.collection("events");
        usersRef = db.collection("users");

        // Lookup view for data population
        TextView firstNameTextView = view.findViewById(R.id.textview_first_name);
        TextView lastNameTextView = view.findViewById(R.id.textview_last_name);
        TextView event_count = view.findViewById(R.id.singin_count);

        firstNameTextView.setText(user.getFirstname());
        lastNameTextView.setText(user.getLastname());

        usersRef.whereEqualTo("Firstname",user.getFirstname())
                .whereEqualTo("Lastname",user.getLastname())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                userID = document.getString("DeviceID");
                            }
                        }
                        else
                        {
                            Log.d("error",userID);
                        }
                    }
                });

        eventsRef.whereArrayContains("signedUpAttendees",userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int ev_count = 0;
                    ev_count++;
                    String ev = String.valueOf(ev_count);
                    event_count.setText(ev);

                }
            }
        });

        // Populate the data into the template view using the data object



        // Return the completed view to render on screen
        return view;
    }
}

