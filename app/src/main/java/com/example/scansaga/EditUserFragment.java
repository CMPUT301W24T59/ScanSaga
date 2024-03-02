package com.example.scansaga;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditUserFragment extends DialogFragment {

    private User userToEdit;
    private String number;

    // Method to create a new instance of EditUserFragment with user information
    static EditUserFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        EditUserFragment fragment = new EditUserFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_user, null);

        EditText editFirstName = view.findViewById(R.id.edit_text_firstname);
        EditText editLastName = view.findViewById(R.id.edit_text_lastname);
        EditText editEmail = view.findViewById(R.id.edit_text_email);
        EditText editPhone = view.findViewById(R.id.edit_text_phone);
        Button deleteButton = view.findViewById(R.id.button_delete);

        // Retrieve user information from arguments
        Bundle args = getArguments();
        if (args != null) {
            userToEdit = (User) args.getSerializable("user");
            number = String.valueOf(editPhone.getText());

            if (userToEdit != null) {
                editFirstName.setText(userToEdit.getFirstname());
                editLastName.setText(userToEdit.getLastname());
                editEmail.setText(userToEdit.getEmail());
                editPhone.setText(userToEdit.getPhone());
            }
        }

        deleteButton.setOnClickListener(v -> {
            // Implement deletion logic here
            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        return builder
                .setView(view)
                .setTitle("Edit User")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    String firstName = editFirstName.getText().toString();
                    String lastName = editLastName.getText().toString();
                    String email = editEmail.getText().toString();
                    String phone = editPhone.getText().toString();

                    // Update user information in Firestore
                    updateUserInFirestore(firstName, lastName, email, phone);

                    dismiss();
                })
                .create();
    }

    private void updateUserInFirestore(String firstName, String lastName, String email, String phone) {
        // Assuming you have a reference to the Firestore collection where user data is stored
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");

        // Query the user document based on the email
        Query query = usersRef.whereEqualTo("phone", number);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Update the user document in Firestore
                    document.getReference().update(
                            "firstname", firstName,
                            "lastname", lastName,
                            "phone", phone,
                            "email",email
                    ).addOnSuccessListener(aVoid -> {
                        // Update successful
                        Toast.makeText(requireContext(), "User Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        // Update failed
                        Toast.makeText(requireContext(), "Failed to update user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                // Handle errors
                Toast.makeText(requireContext(), "Failed to fetch user information: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
