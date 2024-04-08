
package com.example.scansaga.Views;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.scansaga.Controllers.QRImageAdapter;
import com.example.scansaga.Model.Event;
import com.example.scansaga.R;
import com.example.scansaga.Views.Utils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.provider.Settings;


/**
 * A fragment for adding, editing, or deleting an event.
 */
public class AddEventFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;
    private String deviceId;
    private boolean useExistingQRCodeClicked = false;
    private Uri qrUri;
    private ImageView qrImageView;

    /**
     * Interface for communicating with the activity.\
     */
    interface AddEventDialogListener {
        void addNewEvent(Event event);
        void editEvent(Event event);
        void deleteEvent(Event event);
    }

    private AddEventDialogListener listener;
    private EditText editEventName, editDate, editVenue, editLimit;
    private Button  uploadPosterButton,use_existing_qr_button;
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
            // Get the device ID here when the context is available
            deviceId = Utils.getDeviceId(context);
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
        editLimit = view.findViewById(R.id.select_sign_up_limit);
        imageView = view.findViewById(R.id.image_view_poster);
        use_existing_qr_button = view.findViewById(R.id.use_existing_qr_button);

        Bundle args = getArguments();
        if (args != null && args.containsKey("event")) {
            eventToEdit = (Event) args.getSerializable("event");
            if (eventToEdit != null) {
                // Assuming your Event class has the appropriate getters
                editEventName.setText(eventToEdit.getName());
                editDate.setText(eventToEdit.getDate());
                editVenue.setText(eventToEdit.getVenue());
                editVenue.setText(eventToEdit.getLimit());

                qrUri = Uri.parse((String) eventToEdit.getQrUrl());
                qrImageView.setImageURI(qrUri);

                if (eventToEdit.getImageUrl() != null) {
                    imageUri = Uri.parse((String) eventToEdit.getImageUrl());
                    imageView.setImageURI(imageUri);
                }
            }
        }

        editDate.setOnClickListener(v -> showDateTimePickerDialog());
        uploadPosterButton.setOnClickListener(v -> openFileChooser());
        use_existing_qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UseExistingQr.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());
        builder.setPositiveButton("Add", null); // We'll handle the positive button click separately

        AlertDialog alertDialog = builder.create();

        // Override the positive button click listener to handle custom behavior
        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                // Handle click on positive button
                uploadImageAndSaveEventData();

                Intent intent = new Intent(requireContext(), AttendeeHomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        });

        return alertDialog;
    }


    /**
     * Shows the date picker dialog.
     */
    private void showDateTimePickerDialog() {
        // Initialize Calendar instance
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Initialize start time values
        final int[] startHour = {calendar.get(Calendar.HOUR_OF_DAY)};
        final int[] startMinute = {calendar.get(Calendar.MINUTE)};

        // Initialize end time values
        final int[] endHour = {calendar.get(Calendar.HOUR_OF_DAY)};
        final int[] endMinute = {calendar.get(Calendar.MINUTE)};

        // Create DatePickerDialog to select date
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            // Set the selected date to the calendar
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Create a layout inflater
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            View timePickerView = inflater.inflate(R.layout.time_picker_dialog, null);

            // Initialize NumberPickers for start time
            NumberPicker startHourPicker = timePickerView.findViewById(R.id.start_hour_picker);
            NumberPicker startMinutePicker = timePickerView.findViewById(R.id.start_minute_picker);

            // Initialize NumberPickers for end time
            NumberPicker endHourPicker = timePickerView.findViewById(R.id.end_hour_picker);
            NumberPicker endMinutePicker = timePickerView.findViewById(R.id.end_minute_picker);

            // Configure NumberPickers for start time
            startHourPicker.setMinValue(0);
            startHourPicker.setMaxValue(23);
            startHourPicker.setValue(startHour[0]);

            startMinutePicker.setMinValue(0);
            startMinutePicker.setMaxValue(59);
            startMinutePicker.setValue(startMinute[0]);

            // Configure NumberPickers for end time
            endHourPicker.setMinValue(0);
            endHourPicker.setMaxValue(23);
            endHourPicker.setValue(endHour[0]);

            endMinutePicker.setMinValue(0);
            endMinutePicker.setMaxValue(59);
            endMinutePicker.setValue(endMinute[0]);

            // Create AlertDialog with custom layout
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Select Start and End Time");
            builder.setView(timePickerView);

            // Set positive button click listener
            builder.setPositiveButton("OK", (dialog, which) -> {
                // Get selected start time
                startHour[0] = startHourPicker.getValue();
                startMinute[0] = startMinutePicker.getValue();

                // Get selected end time
                endHour[0] = endHourPicker.getValue();
                endMinute[0] = endMinutePicker.getValue();

                // Format the selected start and end times
                String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour[0], startMinute[0]);
                String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour[0], endMinute[0]);

                // Display the selected start and end times
                editDate.setText(year + "-" + month + "-" + day + "-" + "," + startTime + " - " + endTime);
            });

            builder.setNegativeButton("Cancel", null);

            // Show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }, year, month, day);

        // Show DatePickerDialog
        datePickerDialog.show();
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
        String limit = editLimit.getText().toString();

        if (eventName.isEmpty() || date.isEmpty() || venue.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }

        String qrContent = eventName + "_" + date;
        Bitmap qrBitmap = generateQRCode(qrContent);

            // Upload QR code image to Firebase Storage
        uploadQRCodeAndSaveEventData(qrBitmap, imageUrl, eventName, date, venue, limit);

    }

    /**
     * Generates a QR code bitmap from the given content.
     */
    private Bitmap generateQRCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            return toBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a BitMatrix to a Bitmap.
     */
    private Bitmap toBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bmp;
    }

    /**
     * Uploads the QR code image to Firebase Storage and saves event data.
     */
    private void uploadQRCodeAndSaveEventData(Bitmap qrBitmap, String imageUrl, String eventName, String date, String venue, String limit) {
        // Check if the QR bitmap is null
        if (qrBitmap == null) {
            Toast.makeText(getContext(), "Failed to generate QR code", Toast.LENGTH_SHORT).show();
            return;
        }

        final StorageReference qrRef = FirebaseStorage.getInstance().getReference("qr_codes")
                .child(UUID.randomUUID().toString() + ".png");

        // Convert Bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] qrBytes = baos.toByteArray();

        qrRef.putBytes(qrBytes).addOnSuccessListener(taskSnapshot -> qrRef.getDownloadUrl().addOnSuccessListener(qrUri -> {
            // Save the event details along with QR code URL
            String qrUrl = qrUri.toString();
            Map<String, Object> event = new HashMap<>();
            event.put("Name", eventName);
            event.put("Date", date);
            event.put("Venue", venue);
            event.put("Limit", limit);
            event.put("OrganizerDeviceId", deviceId);
            event.put("imageUrl", imageUrl != null ? imageUrl : ""); // Include the image URL if available
            event.put("qrUrl", qrUrl);

            // Concatenate eventName and date to form the document name
            String documentName = eventName + "_" + date;

            // Save the event details to Firestore with the document name
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("events").document(documentName) // Use the concatenated string as the document name
                    .set(event)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Event added successfully.", Toast.LENGTH_SHORT).show();
                        dismiss(); // Dismiss the fragment after adding the event
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error adding event.", Toast.LENGTH_SHORT).show());
        })).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload QR code", Toast.LENGTH_SHORT).show());
    }


}