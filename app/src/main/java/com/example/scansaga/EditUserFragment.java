package com.example.scansaga;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
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
    private String deviceId;
    private static OnUserUpdatedListener listener;

    /**
     * Interface definition for a callback to be invoked when a user is updated.
     */
    public interface OnUserUpdatedListener {
        void onUserUpdated(User updatedUser);
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method to validate phone number format
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    /**
     * Creates a new instance of EditUserFragment with user information.
     *
     * @param currentUser The user to edit.
     * @param deviceId The see which user is on.
     * @return An instance of EditUserFragment.
     */
    public static EditUserFragment newInstance(String deviceId, User currentUser) {
        Bundle args = new Bundle();
        args.putString("deviceId", deviceId);
        args.putSerializable("currentUser", currentUser);
        EditUserFragment fragment = new EditUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setListener(OnUserUpdatedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_user, null);

        EditText editFirstName = view.findViewById(R.id.edit_text_firstname);
        EditText editLastName = view.findViewById(R.id.edit_text_lastname);
        EditText editEmail = view.findViewById(R.id.edit_text_email);
        EditText editPhone = view.findViewById(R.id.edit_text_phone);

        if (getArguments() != null) {
            deviceId = getArguments().getString("deviceId");
            User currentUser = (User) getArguments().getSerializable("currentUser");

            // Use deviceId and currentUser as needed
            if (currentUser != null) {
                editFirstName.setText(currentUser.getFirstname());
                editLastName.setText(currentUser.getLastname());
                editEmail.setText(currentUser.getEmail());
                editPhone.setText(currentUser.getPhone());
            }
        }

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

                    if (!isValidEmail(email)) {
                        // Show error message for invalid email
                        Toast.makeText(requireContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isValidPhoneNumber(phone)) {
                        // Show error message for invalid phone number
                        Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dismiss();
                    updateUserInFirestore(firstName, lastName, email, phone);
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
        usersRef.document(deviceId).update(
                "Firstname", firstName,
                "Lastname", lastName,
                "PhoneNumber", phone,
                "Email", email
        );
    }
}

