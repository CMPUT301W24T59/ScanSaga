package com.example.scansaga;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

/**
 * A DialogFragment for editing user information.
 */

public class EditUserFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST =1 ;
    private User userToEdit;
    private String deviceId;
    private Uri profilePicUri;
    private ImageView imageView;
    private static OnUserUpdatedListener listener;
    private String profilePictureName;

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
        imageView = view.findViewById(R.id.profile_image_view);
        Button uploadProfilButton = view.findViewById(R.id.profile_button);

        uploadProfilButton.setOnClickListener(v -> openFileChooser());

        if (getArguments() != null) {
            deviceId = getArguments().getString("deviceId");
            User currentUser = (User) getArguments().getSerializable("currentUser");

            // Use deviceId and currentUser as needed
            if (currentUser != null) {
                editFirstName.setText(currentUser.getFirstname());
                editLastName.setText(currentUser.getLastname());
                editEmail.setText(currentUser.getEmail());
                editPhone.setText(currentUser.getPhone());

                // Set profile picture if available
                if (currentUser.getProfileImageUrl() != null && !currentUser.getProfileImageUrl().isEmpty()) {
                    Glide.with(this).load(currentUser.getProfileImageUrl()).into(imageView);
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        return builder
                .setView(view)
                .setTitle("Edit User")
                .setNeutralButton("Delete Profile Picture", (dialog, which) -> {
                    // Delete profile picture here
                    deleteProfilePicture(editFirstName.getText().toString(), editLastName.getText().toString(), editEmail.getText().toString(), editPhone.getText().toString());
                })
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
        // Check if a profile picture is selected
        if (profilePicUri != null) {
            // Upload the profile picture to Firebase Storage
            uploadProfilePicture(firstName, lastName, email, phone);
        } else {
            // If no profile picture is selected, update user information without the profile picture
            updateUserInfoWithoutProfilePicture(firstName, lastName, email, phone, null);
        }
    }

    private void uploadProfilePicture(String firstName, String lastName, String email, String phone) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference profilePicRef = storageRef.child("profile_pictures/" + deviceId);

        profilePicRef.putFile(profilePicUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Profile picture uploaded successfully
                    // Get the download URL of the uploaded profile picture
                    profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update user information in Firestore with the profile picture URL
                        updateUserInfoWithoutProfilePicture(firstName, lastName, email, phone, uri.toString());
                    }).addOnFailureListener(e -> {
                        // Handle failure to get profile picture download URL
                        Toast.makeText(requireContext(), "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failure to upload profile picture
                    Toast.makeText(requireContext(), "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserInfoWithoutProfilePicture(String firstName, String lastName, String email, String phone, String profilePictureUrl) {
        // Update user information in Firestore
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        usersRef.document(deviceId).update(
                "Firstname", firstName,
                "Lastname", lastName,
                "PhoneNumber", phone,
                "Email", email,
                "ProfilePicture", profilePictureUrl
        ).addOnSuccessListener(aVoid -> {
            // User information updated successfully
            // Create a new User object with updated information
            User updatedUser = new User(firstName, lastName, email, phone, profilePictureUrl);
            // Notify the listener that user information is updated
            if (listener != null) {
                listener.onUserUpdated(updatedUser);
            }
        }).addOnFailureListener(e -> {
            // Handle failure to update user information
            Toast.makeText(requireContext(), "Failed to update user information", Toast.LENGTH_SHORT).show();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            profilePicUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), profilePicUri);
                // Now you have the bitmap, you can use it to set the image in an ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void deleteProfilePicture(String firstName, String lastName, String email, String phone) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference profilePicRef = storage.getReference().child("profile_pictures/" + deviceId);

        profilePicRef.delete().addOnSuccessListener(aVoid -> {
            updateUserInfoWithoutProfilePicture(firstName, lastName, email, phone, null);
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Toast.makeText(requireContext(), "Failed to delete profile picture", Toast.LENGTH_SHORT).show();
        });
    }
}

