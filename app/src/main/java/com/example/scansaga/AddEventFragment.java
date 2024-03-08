package com.example.scansaga;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * A fragment for adding, editing, or deleting an event.
 */
public class AddEventFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;

    /**
     * Interface for communicating with the activity.
     */
    interface AddEventDialogListener {
        void addNewEvent(Event event);
        void editEvent(Event event);
        void deleteEvent(Event event);
    }

    private AddEventDialogListener listener;
    private EditText editEventName, editDate, editVenue;
    private Button  uploadPosterButton;
    private Event eventToEdit;

    /**
     * Static method to create a new instance of the fragment with arguments.
     */
    static AddEventFragment newInstance(Event event) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        AddEventFragment fragment = new AddEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddEventDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddEventDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_event_fragment, null);
        builder.setView(view);

        editEventName = view.findViewById(R.id.edit_text_event_text);
        editDate = view.findViewById(R.id.edit_date_text);
        editVenue = view.findViewById(R.id.edit_venue_text);
        uploadPosterButton = view.findViewById(R.id.upload_poster);
        imageView = view.findViewById(R.id.image_view_poster);

        Bundle args = getArguments();
        if (args != null && args.containsKey("event")) {
            eventToEdit = (Event) args.getSerializable("event");
            if (eventToEdit != null) {
                // Assuming your Event class has the appropriate getters
                editEventName.setText(eventToEdit.getName());
                editDate.setText(eventToEdit.getDate());
                editVenue.setText(eventToEdit.getVenue());
                if (eventToEdit.getImageUrl() != null) {
                    imageUri = Uri.parse((String) eventToEdit.getImageUrl());
                    imageView.setImageURI(imageUri);
                }
            }
        }

        editDate.setOnClickListener(v -> showDatePickerDialog());

        uploadPosterButton.setOnClickListener(v -> openFileChooser());

        builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());
        builder.setPositiveButton("Add", null); // We'll handle the positive button click separately

        AlertDialog alertDialog = builder.create();

        // Override the positive button click listener to handle custom behavior
        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                // Handle click on positive button
                uploadImageAndSaveEventData();
            });
        });

        return alertDialog;
    }

    /**
     * Shows the date picker dialog.
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) ->
                editDate.setText(String.format("%d-%d-%d", year1, monthOfYear + 1, dayOfMonth)), year, month, day).show();
    }

    /**
     * Opens the file chooser for image selection.
     */
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    /**
     * Uploads the image to Firebase Storage and saves event data.
     */
    private void uploadImageAndSaveEventData() {
        if (imageUri == null) {
            saveEventData(null); // Save event without an image
            return;
        }

        final StorageReference fileRef = FirebaseStorage.getInstance().getReference("events_images")
                .child(UUID.randomUUID().toString());

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            saveEventData(imageUrl);
        })).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    /**
     * Saves event data to Firestore.
     */
    private void saveEventData(String imageUrl) {
        String eventName = editEventName.getText().toString();
        String date = editDate.getText().toString();
        String venue = editVenue.getText().toString();

        if (eventName.isEmpty() || date.isEmpty() || venue.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Concatenate eventName and date to form the document name
        String documentName = eventName + "_" + date;

        // Create a map to store the event details
        Map<String, Object> event = new HashMap<>();
        event.put("Name", eventName);
        event.put("Date", date);
        event.put("Venue", venue);
        // Include the image URL if available
        if (imageUrl != null && !imageUrl.isEmpty()) {
            event.put("imageUrl", imageUrl);
        } else {
            event.put("imageUrl", ""); // Use an empty string or a default value if no image is provided
        }

        // Save the event details to Firestore with the document name
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(documentName) // Use the concatenated string as the document name
                .set(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Event added successfully.", Toast.LENGTH_SHORT).show();
                    dismiss(); // Dismiss the fragment after adding the event
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error adding event.", Toast.LENGTH_SHORT).show());
    }

}