package com.example.scansaga.Views;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.Model.User;
import com.example.scansaga.R;
import com.example.scansaga.Controllers.UserArrayAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * The ShowAllUsers class displays all users stored in Firestore.
 */
public class ShowAllUsers extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference usersRef;

    private ListView listView;
    private Button delete;
    private UserArrayAdapter userAdapter;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_users);

        // Initialize variables
        listView = findViewById(R.id.listView);
        delete = findViewById(R.id.button_delete);
        userList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        // Initialize adapter for ListView
        userAdapter = new UserArrayAdapter(this, userList);
        listView.setAdapter(userAdapter);

        // Fetch users from Firestore
        fetchUsersFromFirestore();

        // Set item click listener for ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected user based on the position clicked
            User selectedUser = userList.get(position);
            if (selectedUser != null) {
                delete.setOnClickListener(v -> {
                    deleteUserFromFirestore(selectedUser);
                    userAdapter.notifyDataSetChanged();
                });
            }
        });
    }

    /**
     * Method to fetch users from Firestore.
     */
    private void fetchUsersFromFirestore() {
        usersRef.addSnapshotListener((querySnapshots, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (querySnapshots != null) {
                userList.clear();
                for (QueryDocumentSnapshot doc : querySnapshots) {
                    String email = doc.getString("Email");
                    String firstname = doc.getString("Firstname");
                    String lastname = doc.getString("Lastname");
                    String phone = doc.getString("PhoneNumber");
                    Log.d("Firestore", String.format("User(%s, %s , %s, %s) fetched", firstname, lastname, email, phone));
                    userList.add(new User(firstname, lastname, email, phone,null));
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Method to delete a user from Firestore.
     * @param user The user to be deleted.
     */
    private void deleteUserFromFirestore(User user) {
        if (user != null) {
            // Query for documents where the user's information matches
            usersRef.whereEqualTo("Firstname", user.getFirstname())
                    .whereEqualTo("Lastname", user.getLastname())
                    .whereEqualTo("Email", user.getEmail())
                    .whereEqualTo("PhoneNumber", user.getPhone())
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // Delete the document
                            usersRef.document(document.getId()).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Remove the user from the list and update the UI
                                        userList.remove(user);
                                        userAdapter.notifyDataSetChanged();
                                        Log.d("Firestore", "User deleted successfully!");
                                        Toast.makeText(ShowAllUsers.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error deleting user", e);
                                        Toast.makeText(ShowAllUsers.this, "Error deleting user", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error querying user", e);
                        Toast.makeText(ShowAllUsers.this, "Error querying user", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
