package com.example.scansaga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String DETAILS_ENTERED_KEY = "detailsEntered";

    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumberEditText;
    private Button addUserButton;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Check if user has already entered details
        boolean detailsEntered = sharedPreferences.getBoolean(DETAILS_ENTERED_KEY, false);
        if (detailsEntered) {
            // User has already entered details, proceed to next activity
            startActivity(new Intent(MainActivity.this, EventActivity.class));
            finish(); // Finish this activity to prevent going back
            return;
        }

        // Initialize EditText fields and Button
        firstNameEditText = findViewById(R.id.Firstname_textview);
        lastNameEditText = findViewById(R.id.lastname_textview);
        emailEditText = findViewById(R.id.email_textview);
        phoneNumberEditText = findViewById(R.id.phonenumber_textview);
        addUserButton = findViewById(R.id.confirm_button);

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

                // Save the flag indicating that user has entered details
                sharedPreferences.edit().putBoolean(DETAILS_ENTERED_KEY, true).apply();

                // Add user details to Firestore
                addUserToFirestore(firstName, lastName, email, phoneNumber);

                // Proceed to next activity
                startActivity(new Intent(MainActivity.this, EventActivity.class));
                finish(); // Finish this activity to prevent going back
            }
        });
    }

    private void addUserToFirestore(String firstName, String lastName, String email, String phoneNumber) {
        // Create a User object with the retrieved data
        User user = new User(firstName, lastName, email, phoneNumber);

        // Add the user to Firestore
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // User added to Firestore successfully
                        Toast.makeText(MainActivity.this, "User added to Firestore successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add user to Firestore
                        Toast.makeText(MainActivity.this, "Failed to add user to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
