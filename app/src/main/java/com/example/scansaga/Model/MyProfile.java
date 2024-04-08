package com.example.scansaga.Model;

import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.scansaga.Views.EditUserFragment;
import com.example.scansaga.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * MyProfile class displays the user's profile information.
 */
public class MyProfile extends AppCompatActivity {

    private TextView firstNameTextView, lastNameTextView, emailTextView, phoneNumberTextView;
    private ImageView profileImageView;
    private String deviceId;
    private User currentUser;
    private ListenerRegistration userDataListener;

    /**
     * Called when the activity is first created. Initializes UI elements,
     * retrieves the device ID, and sets up listeners to fetch user data from Firestore.
     *
     * @param savedInstanceState  If the activity is being re-initialized after previously
     *                            being shut down then this Bundle contains the data it most
     *                            recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_display);

        // Initialize TextViews
        firstNameTextView = findViewById(R.id.first_name_text_view);
        lastNameTextView = findViewById(R.id.last_name_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        phoneNumberTextView = findViewById(R.id.phone_number_text_view);
        profileImageView = findViewById(R.id.profile_image_view);

        // Get the unique device ID
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Fetch user data from Firestore
        fetchUserDataFromFirestore(deviceId);

        // Set click listener for the edit profile button
        FloatingActionButton fab = findViewById(R.id.edit_profile_button);
        fab.setOnClickListener(v -> {
            // Pass the deviceId and currentUser to the EditUserFragment
            EditUserFragment editUserFragment = EditUserFragment.newInstance(deviceId, currentUser);
            editUserFragment.show(getSupportFragmentManager(), "Edit User");
        });

    }

    /**
     * Fetches user data from Firestore based on the device ID.
     *
     * @param deviceId The unique device ID
     */
    private void fetchUserDataFromFirestore(String deviceId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userDataListener = db.collection("users")
                .whereEqualTo("DeviceId", deviceId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle errors
                        return;
                    }

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Update UI with fetched data
                            String retrievedFirstName = documentSnapshot.getString("Firstname");
                            String lastName = documentSnapshot.getString("Lastname");
                            String email = documentSnapshot.getString("Email");
                            String retrievedPhone = documentSnapshot.getString("PhoneNumber");
                            String profilePictureUrl = documentSnapshot.getString("ProfilePicture");

                            firstNameTextView.setText(retrievedFirstName);
                            lastNameTextView.setText(lastName);
                            emailTextView.setText(email);
                            phoneNumberTextView.setText(retrievedPhone);

                            // Load profile picture using the profilePictureUrl
                            if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                Glide.with(this).load(profilePictureUrl).into(profileImageView);
                            } else {
                                // Load default profile picture
                                generateUniqueProfilePicture(retrievedFirstName,lastName);
                            }

                            // Initialize currentUser object with fetched data
                            currentUser = new User(retrievedFirstName, lastName, email, retrievedPhone, profilePictureUrl);
                        }
                    }
                });
    }

    /**
     * Generates a unique profile picture for a user based on their first and last name and sets it to an ImageView.
     * This method first attempts to find a drawable resource that matches the first letter of the user's first name.
     * If such a resource is found, it is used as the profile image. Otherwise, a default profile image is set.
     * Additionally, it generates a unique background color for the ImageView based on the hash code of the user's full name.
     *
     * @param firstName The first name of the user, used to determine the initial for the profile picture and part of the hash for the background color.
     * @param lastName The last name of the user, used in generating the hash for the background color.
     */
    private void generateUniqueProfilePicture(String firstName, String lastName){
        ImageView profileImageView = (ImageView) findViewById(R.id.profile_image_view);

        // Extract the first letter of the user's first name and convert it to lowercase
        char firstLetter = Character.toLowerCase(firstName.charAt(0));

        // drawable resource name
        String resourceName = firstLetter + "";

        // Get the Android resource ID by name, type, and package
        Resources resources = this.getResources(); // 'this' refers to a Context object
        int resourceId = resources.getIdentifier(resourceName, "drawable", this.getPackageName());

        if (resourceId != 0) {
            // If the resource was found, set it as the icon for the ImageView
            profileImageView.setImageResource(resourceId);
        } else {
            // Handle the case where the resource was not found
            // Use Glide to set a default image
            Glide.with(this).load(R.drawable.profile_icon_black).into(profileImageView); // Fixed syntax
            Log.e("ImageViewSetup", "Resource not found for letter: " + firstLetter);
        }

        // Combine user attributes into a single string and hash it
        String combinedAttributes = firstName + lastName;
        int hash = combinedAttributes.hashCode();

        // Generate a color from the hash code ensuring the alpha channel is set to max
        int color = 0xFF000000 | (hash & 0x00FFFFFF); // This ensures the alpha channel is set to max
        System.out.println(color);
        // Set the generated color as the background of the ImageView
        profileImageView.setBackgroundColor(color);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the listener when the activity is destroyed to avoid memory leaks
        if (userDataListener != null) {
            userDataListener.remove();
        }
    }
}
