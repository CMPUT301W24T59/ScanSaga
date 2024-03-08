//package com.example.scansaga;
//
//import static com.google.common.io.Files.getFileExtension;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.webkit.MimeTypeMap;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.FirebaseDatabaseKtxRegistrar;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.squareup.picasso.Picasso;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.Calendar;
//
//public class AddEventFragment extends DialogFragment {
//
//    // Interface for communication with the Main activity
//    interface AddEventDialogListener {
//        void addNewEvent(Event event);
//
//        void editEvent(Event event);
//
//        void deleteEvent(Event event);
//    }
//
//    // Correcting the newInstance method
//    static AddEventFragment newInstance(Event event) {
//        Bundle args = new Bundle();
//        args.putSerializable("event", event); // Change "book" to "event"
//        AddEventFragment fragment = new AddEventFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    private AddEventDialogListener listener; // Listener for communicating with the main activity
//    private EditText editEventName; // EditText for event name
//    private EditText editDate; // EditText for date
//    private EditText editVenue; // EditText for venue
//    private Button deleteButton;
//    private TextView txtQrCode; // TextView to display the generated QR code
//    private Event eventToEdit;
//    private ActivityResultLauncher<Intent> mGetContent;
//    private StorageReference StorageRef;
//    private DatabaseReference DatabaseRef;
//
//    // Method to set the listener
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        mGetContent = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        if (data != null && data.getData() != null) {
//                            Uri imageUri = data.getData();
//
//                            // Asynchronously load the image as a Bitmap and display it
////                            loadBitmapFromUriAndDisplay(imageUri);
//
//                            // If you just want to display the image in an ImageView with Picasso
//                            Picasso.get().load(imageUri).into(imageView);
//                        }
//                    }
//                });
//
//        try {
//            listener = (AddEventDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement AddEventDialogListener");
//        }
//    }
//
//    private void openFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        if (mGetContent != null) {
//            mGetContent.launch(intent); // This should no longer cause NullPointerException
//        } else {
//            Log.e("AddEventFragment", "mGetContent is null. Cannot launch file chooser.");
//        }
//    }
//
//
//    private static final int PICK_IMAGE_REQUEST = 1; // Unique request code
//    private ImageView imageView;
//    private Uri imageUri;
//
//
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_event_fragment, null);
//        imageView = view.findViewById(R.id.image_view_poster);
//
//        Button buttonChooseImage = view.findViewById(R.id.upload_poster);
//        buttonChooseImage.setOnClickListener(v -> openFileChooser());
//        StorageRef = FirebaseStorage.getInstance().getReference("uploads");
//        DatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
//
//
//        mGetContent = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
//                        imageUri = result.getData().getData(); // Set the image URI
//                        Picasso.get().load(imageUri).into(imageView); // Load the image into the ImageView
//                    }
//                });
//
//
//        // Get references to EditText fields
//        editEventName = view.findViewById(R.id.edit_text_event_text);
//        editDate = view.findViewById(R.id.edit_date_text);
//        editVenue = view.findViewById(R.id.edit_venue_text);
//        deleteButton = view.findViewById(R.id.delete_event_button);
//
//        // Set click listener for date EditText to show date picker dialog
//        editDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog();
//            }
//        });
//
//        Bundle args = getArguments();
//        if (args != null) {
//            eventToEdit = (Event) args.getSerializable("event");
//
//            //if the event object is not null then edit the contents
//            if (eventToEdit != null) {
//                // Populate the EditText fields with existing event details when editing
//                editEventName.setText(eventToEdit.getName());
//                editDate.setText(eventToEdit.getDate());
//                editVenue.setText(eventToEdit.getVenue());
//            }
//        }
//
//        //listen if the delete button is clicked
//        deleteButton.setOnClickListener(v -> {
//            if (eventToEdit != null) {
//                listener.deleteEvent(eventToEdit); // Call the deleteBook method of the listener
//                dismiss(); // Close the dialog after deletion
//            } else {
//                // Display an error message if any field is empty
//                new AlertDialog.Builder(requireContext())
//                        .setTitle("Empty Event List. ")
//                        .setMessage("Sorry, can't delete any events.\nThe event list is empty.")
//                        .setPositiveButton("OK", null)
//                        .show();
//            }
//        });
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//        return builder
//                .setView(view)
//                .setTitle("Add/Edit an Event")
//                .setNegativeButton("Cancel", null)
//                .setPositiveButton("Add", (dialog, which) -> {
//                    String eventName = editEventName.getText().toString();
//                    String date = editDate.getText().toString();
//                    String venue = editVenue.getText().toString();
//                    String qrContent = generateQRContent(eventName, date, venue);
//                    Bitmap qr = generateQRCodeBitmap(qrContent);
//
//                    // Validate input fields
//                    if (eventName.isEmpty() || date.isEmpty() || venue.isEmpty()) {
//                        // Display error message if any field is empty
//                        Toast.makeText(requireContext(), "Please fill in all details of the event", Toast.LENGTH_SHORT).show();
//                    } else {/*
//                        if (eventToEdit != null) {
//                            eventToEdit.setName(eventName);
//                            eventToEdit.setDate(date);
//                            eventToEdit.setVenue(venue);
//                            eventToEdit.setQrCodeBitmap(qr);
//                            listener.editEvent(eventToEdit);
//
//                        } else {*/
//                        // Add the event using the listener
//                        listener.addNewEvent(new Event(eventName, date, venue, qr));
//
//
//                    }
//                })
//                .create();
//    }
//
//
//    // Method to show the date picker dialog
//    private void showDatePickerDialog() {
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        // Update the date EditText with the selected date
//                        editDate.setText(String.format("%d/%d/%d", monthOfYear + 1, dayOfMonth, year));
//                    }
//                }, year, month, day);
//        datePickerDialog.show();
//    }
//
//    //FOR CITATION LATER: https://www.geeksforgeeks.org/how-to-build-a-qr-code-android-app-using-firebase/
//    // Method to generate QR content for the event
//    private String generateQRContent(String eventName, String date, String venue) {
//        return "Event: " + eventName + "\nDate: " + date + "\nVenue: " + venue;
//    }
//
//    // Method to generate QR code from content
//    private Bitmap generateQRCodeBitmap(String data) {
//        Bitmap bitmap = null;
//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try {
//            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
//                }
//            }
//            // After successfully generating the QR Code bitmap, upload it to Firebase Storage
//            String filename = generateUniqueFilename(); // Implement this method based on your requirements
//            uploadImageToFirebaseStorage(bitmap, filename);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    private String generateUniqueFilename() {
//        // Example to generate a unique filename using the current timestamp
//        return "QR_" + System.currentTimeMillis();
//    }
//
//    public static void uploadImageToFirebaseStorage(Bitmap bitmap, String filename) {
//        // Your existing method to upload the bitmap to Firebase Storage
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference imageRef = storageRef.child("images/" + filename + ".jpg");
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = imageRef.putBytes(data);
//        uploadTask.addOnFailureListener(exception -> {
//            // Handle unsuccessful uploads
//        }).addOnSuccessListener(taskSnapshot -> {
//            // Handle successful uploads
//        });
//    }
//
//    private String getFileExtension(Uri uri) {
//        ContentResolver cR = getActivity().getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }
//
//
//    private void uploadFile() {
//        if (imageUri != null) {
//            StorageReference fileReference = StorageRef.child(System.currentTimeMillis()
//                    + "." + getFileExtension(imageUri));
//            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
//                // After uploading the file successfully, get the download URL
//                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                    // Here you get the image download URL
//                    String uploadId = DatabaseRef.push().getKey(); // Create a unique ID for the database entry
//                    DatabaseRef.child(uploadId).setValue(uri.toString()); // Save the download URL in the database
//                    Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
//                });
//            }).addOnFailureListener(e -> {
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            });
//        } else {
//            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
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

public class AddEventFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;

    interface AddEventDialogListener {
        void addNewEvent(Event event);
        void editEvent(Event event);
        void deleteEvent(Event event);
    }

    private AddEventDialogListener listener;
    private EditText editEventName, editDate, editVenue;
    private Button uploadPosterButton;
    private Event eventToEdit;

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
        builder.setPositiveButton("Add", (dialog, which) -> uploadImageAndSaveEventData());

        return builder.create();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) ->
                editDate.setText(String.format("%d-%d-%d", year1, monthOfYear + 1, dayOfMonth)), year, month, day).show();
    }

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

    //    private void saveEventData(String imageUrl) {
//        Map<String, Object> event = new HashMap<>();
//        event.put("name", editEventName.getText().toString());
//        event.put("date", editDate.getText().toString());
//        event.put("venue", editVenue.getText().toString());
//        event.put("imageUrl", imageUrl);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("events").add(event)
//                .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Event added successfully.", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error adding event.", Toast.LENGTH_SHORT).show());
//    }
    private void saveEventData(String imageUrl) {
        String eventName = editEventName.getText().toString();
        String date = editDate.getText().toString();
        String venue = editVenue.getText().toString();

        if (eventName.isEmpty() || date.isEmpty() || venue.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }

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

        // Save the event details to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").add(event)
                .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Event added successfully.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error adding event.", Toast.LENGTH_SHORT).show());

    }
}