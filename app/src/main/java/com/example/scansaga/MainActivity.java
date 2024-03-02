package com.example.scansaga;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

                // Add the user to Firestore
        usernamesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    userDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String firstName = doc.getId();
                        String lastName = doc.getString("Lastname");
                        String email = doc.getString("Email");
                        String phoneNumber = doc.getString("Phone");
                        Log.d("Firestore", String.format("User(%s, %s) fetched", firstName, lastName, email, phoneNumber));
                        userDataList.add(new User(firstName, lastName, email, phoneNumber));
                    }
                    userArrayAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void addNewUser(User user) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Lastname", user.getLastname());
        data.put("Firstname", user.getFirstname());
        data.put("Email", user.getEmail());
        data.put("PhoneNumber", user.getPhone());

        usernamesRef.document(user.getLastname()).set(data);
        usernamesRef
                .document(user.getFirstname() + user.getPhone())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                        // After adding the user, start the EventActivity
                        Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
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

