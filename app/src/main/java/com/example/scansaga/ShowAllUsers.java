package com.example.scansaga;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This activity displays a list of users fetched from Firestore.
 * Users can be deleted from the list and Firestore database.
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowAllUsers.this);
                builder.setTitle("Delete User");
                builder.setMessage("Are you sure you want to delete this user?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    deleteUserFromFirestore(selectedUser);
                    dialog.dismiss();
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            }
        });
    }

    /**
     * Fetches users from Firestore and updates the user list.
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
                    userList.add(new User(firstname, lastname, email, phone));
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Deletes a user from Firestore and updates the user list.
     * @param user The user to be deleted.
     */
    private void deleteUserFromFirestore(User user) {
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
