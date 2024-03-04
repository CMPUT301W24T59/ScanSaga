package com.example.scansaga;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ArrayList<User> userDataList;
    UserArrayAdapter userArrayAdapter;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumberEditText;
    private Button addUserButton;
    private FirebaseFirestore db;
    private CollectionReference usernamesRef;
    private String deviceId;

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
        usernamesRef=db.collection("users");
        userDataList = new ArrayList<>();
        userArrayAdapter = new UserArrayAdapter(this, userDataList);


        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        usernamesRef.whereEqualTo("DeviceId", deviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        String firstName = snapshot.getString("Firstname");
                        String lastName = snapshot.getString("Lastname");
                        String email = snapshot.getString("Email");
                        String phoneNumber = snapshot.getString("PhoneNumber");

                        // Create a User object with the retrieved data
                        User user = new User(firstName, lastName, email, phoneNumber);
                        // Device ID exists, navigate to HomepageActivity
                        Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish(); // Finish MainActivity so that it's not kept in the back stack
                        // Break the loop as we only need to navigate once
                        break;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error checking for device ID", e);
                });


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

                addNewUser(new User(firstName, lastName, email, phoneNumber));

                if (userArrayAdapter != null) {
                    userArrayAdapter.notifyDataSetChanged();
                }
                userArrayAdapter.notifyDataSetChanged();
                firstNameEditText.setText("");
                lastNameEditText.setText("");
                emailEditText.setText("");
                phoneNumberEditText.setText("");
            }
        });


    }

    private void addNewUser(User user) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Lastname", user.getLastname());
        data.put("Firstname", user.getFirstname());
        data.put("Email", user.getEmail());
        data.put("PhoneNumber", user.getPhone());
        data.put("DeviceId",deviceId);


        usernamesRef
                .document(user.getFirstname() + user.getPhone())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                        // After adding the user, start the EventActivity
                        Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
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
