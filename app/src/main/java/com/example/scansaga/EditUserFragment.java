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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A DialogFragment for editing user information.
 */
public class EditUserFragment extends DialogFragment {

    private User userToEdit;

    /**
     * Creates a new instance of EditUserFragment with user information.
     *
     * @param user The user to edit.
     * @return An instance of EditUserFragment.
     */
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

        Bundle args = getArguments();
        if (args != null) {
            userToEdit = (User) args.getSerializable("user");
            if (userToEdit != null) {
                editFirstName.setText(userToEdit.getFirstname());
                editLastName.setText(userToEdit.getLastname());
                editEmail.setText(userToEdit.getEmail());
                editPhone.setText(userToEdit.getPhone());
            }
        }

        deleteButton.setOnClickListener(v -> dismiss());

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

                    updateUserInFirestore(firstName, lastName, email, phone);

                    dismiss();
                })
                .create();
    }

    /**
     * Updates user information in Firestore.
     *
     * @param firstName The updated first name.
     * @param lastName  The updated last name.
     * @param email     The updated email.
     * @param phone     The updated phone number.
     */
    private void updateUserInFirestore(String firstName, String lastName, String email, String phone) {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        Query query = usersRef.whereEqualTo("phone", userToEdit.getPhone());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    document.getReference().update(
                                    "firstname", firstName,
                                    "lastname", lastName,
                                    "phone", phone,
                                    "email", email
                            ).addOnSuccessListener(aVoid ->
                                    Toast.makeText(requireContext(), "User Profile Updated Successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(requireContext(), "Failed to update user information: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(requireContext(), "Failed to fetch user information: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
