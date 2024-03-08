package com.example.scansaga.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.scansaga.R;
import com.example.scansaga.Controllers.UserArrayAdapter;
import com.example.scansaga.Views.AttendeeHomePage;
import com.example.scansaga.Views.HomepageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MainActivity class to handle user registration and login.
 */
public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ArrayList<User> userDataList;
    UserArrayAdapter userArrayAdapter;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumberEditText;
    private Button addUserButton;
    private FirebaseFirestore db;
    private CollectionReference usernamesRef;
    private String deviceId;
    private Button uploadProfilePicture;

    private Uri profileUri;
    private String profilePictureString;
    private ImageView imageView;

    /**
     * Helper method to validate the format of an email address.
     *
     * @param email The email address to validate.
     * @return true if the email is in a valid format, false otherwise.
     */
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Helper method to validate the format of a phone number.
     *
     * @param phone The phone number to validate.
     * @return true if the phone number is a valid 10-digit number, false otherwise.
     */
    // Method to validate phone number format
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    /**
     * Called when the activity is first created. Initializes UI elements, Firestore,
     * and handles initial user login logic based on device ID.
     *
     * @param savedInstanceState  If the activity is being re-initialized after previously
     *                            being shut down then this Bundle contains the data it most
     *                            recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize EditText fields and Button
        firstNameEditText = findViewById(R.id.Firstname_editText);
        lastNameEditText = findViewById(R.id.lastname_editText);
        emailEditText = findViewById(R.id.email_editText);
        phoneNumberEditText = findViewById(R.id.phonenumber_editText);
        addUserButton = findViewById(R.id.confirm_button);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        usernamesRef = db.collection("users");
        userDataList = new ArrayList<>();
        userArrayAdapter = new UserArrayAdapter(this, userDataList);


        // Get unique device ID
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Check if the user already exists based on device ID
        usernamesRef.whereEqualTo("DeviceId", deviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        String firstName = snapshot.getString("Firstname");
                        String lastName = snapshot.getString("Lastname");
                        String email = snapshot.getString("Email");
                        String phoneNumber = snapshot.getString("PhoneNumber");
                        String imageUri = snapshot.getString("ImageUri");

                        // Check if the user exists in the admin collection
                        CollectionReference adminRef = FirebaseFirestore.getInstance().collection("admin");
                        adminRef.document(firstName + phoneNumber)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        // User exists in the admin collection
                                        // Navigate to HomepageActivity
                                        User user = new User(firstName, lastName, email, phoneNumber, null);
                                        Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        finish(); // Finish MainActivity so that it's not kept in the back stack
                                    } else {
                                        // User exists in the regular user collection
                                        // Navigate to AttendeeHomePage
                                        User user = new User(firstName, lastName, email, phoneNumber, null);
                                        Intent intent = new Intent(MainActivity.this, AttendeeHomePage.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        finish(); // Finish MainActivity so that it's not kept in the back stack
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error checking for admin document", e);
                                });

                        // Break the loop as we only need to navigate once
                        break;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error checking for device ID", e);
                });

        /**
         * Adds a new user to the Firestore database. Checks if the user should be
         * placed in the 'admin' collection or the regular 'users' collection.
         * Navigates to either the HomepageActivity or AttendeeHomePage, passing the
         * user object to the next activity.
         *
         * @param user The User object representing the new user to be added.
         */
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input from EditText fields
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();

                // Check if any field is empty
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email and phone number are valid
                if (isValidEmail(email) && isValidPhoneNumber(phoneNumber)) {
                    addNewUser(new User(firstName, lastName, email, phoneNumber, null));

                } else if (!isValidEmail(email)){
                    // Display error message if email is invalid
                    Toast.makeText(MainActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                } else {
                    // Display error message if phone number is invalid
                    Toast.makeText(MainActivity.this, "Invalid Phone Number. Please enter a 10-digit phone number", Toast.LENGTH_SHORT).show();
                }

                // Clear EditText fields after adding a user
                firstNameEditText.setText("");
                lastNameEditText.setText("");
                emailEditText.setText("");
                phoneNumberEditText.setText("");
            }
        });

    }

    /**
     * Method to add a new user to Firestore database.
     *
     * @param user User object containing user details
     */

    private void addNewUser(User user) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Lastname", user.getLastname());
        data.put("Firstname", user.getFirstname());
        data.put("Email", user.getEmail());
        data.put("PhoneNumber", user.getPhone());
        data.put("DeviceId", deviceId);


        usernamesRef
                .document(deviceId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CollectionReference adminRef = db.collection("admin");
                        adminRef.document(user.getFirstname() + user.getPhone())
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        finish(); // Finish MainActivity so that it's not kept in the back stack
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, AttendeeHomePage.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        finish(); // Finish MainActivity so that it's not kept in the back stack
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error checking for admin document", e);
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firebase", e.getMessage());
                    }
                });

    }

}
