package com.example.scansaga;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView; // Add import statement

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class ShowAllUsers extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference usersRef;

    private ListView listView;
    private Button delete;
    private UserArrayAdapter userAdapter;
    ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_users);

        listView = findViewById(R.id.listView);
        delete = findViewById(R.id.button_delete);

        userList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

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


    // Method to fetch users from Firestore
    void fetchUsersFromFirestore() {
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
                    userList.add(new User(firstname, lastname, email, phone));
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    // Method to delete a user from Firestore
    void deleteUserFromFirestore(User user) {
        usersRef.document(user.getLastname())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User deleted successfully!");
                    // Remove the deleted user from the userList
                    userList.remove(user);
                    // Notify the adapter of the dataset change
                    userAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error deleting user", e));
    }
}
