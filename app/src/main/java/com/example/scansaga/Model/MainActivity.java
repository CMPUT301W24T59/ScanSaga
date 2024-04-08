package com.example.scansaga.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.scansaga.R;
import com.example.scansaga.Controllers.UserArrayAdapter;
import com.example.scansaga.Views.AttendeeHomePage;
import com.example.scansaga.Views.HomepageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The MainActivity class is responsible for handling user registration and login activities
 * within the application. It allows new users to register by entering their personal information,
 * checks for existing users based on device ID, and navigates to different homepages based on
 * the user's role (admin or attendee).
 */
public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ArrayList<User> userDataList;
    UserArrayAdapter userArrayAdapter;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumberEditText;
    private Button addUserButton;
    private FirebaseFirestore db;
    private CollectionReference usernamesRef;
    private String deviceId;
    private Button uploadProfilePicture;
    public static Boolean isUserAdmin;

    private Uri profileUri;
    private String profilePictureString;
    private ImageView imageView;


    // These isRunningTest is only used when trying to bypass the automatic sign in for andriodTest

    /**
     * Checks if the application is running in test mode.
     *
     * @return True if running in test mode, false otherwise.
     */
    private static boolean isRunningTest = false;

    public static boolean isRunningTest() {
        return isRunningTest;
    }
    public static void setRunningTest(boolean runningTest) {
        isRunningTest = runningTest;
    }
        // Method to validate email format
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method to validate phone number format
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize EditText fields and Button
        firstNameEditText = findViewById(R.id.Firstname_editText);
        lastNameEditText = findViewById(R.id.lastname_editText);
        emailEditText = findViewById(R.id.email_editText);
        phoneNumberEditText = findViewById(R.id.phonenumber_editText);
        addUserButton = findViewById(R.id.confirm_button);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        usernamesRef = db.collection("users");
        userDataList = new ArrayList<>();
        userArrayAdapter = new UserArrayAdapter(this, userDataList);
        isUserAdmin = false;


        // Get unique device ID
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if((!MainActivity.isRunningTest())) {
            // Check if the user already exists based on device ID
            // Assuming you're in a context where this is possible (e.g., an Activity)
            usernamesRef.whereEqualTo("DeviceId", deviceId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            String firstName = snapshot.getString("Firstname");
                            String lastName = snapshot.getString("Lastname");
                            String email = snapshot.getString("Email");
                            String phoneNumber = snapshot.getString("PhoneNumber");
                            String imageUri = snapshot.getString("ImageUri");

                            checkIfUserAdmin(deviceId, firstName, lastName, phoneNumber, email, isAdmin -> {
                                if (isAdmin) {
                                    isUserAdmin = true;
                                    User user = new User(firstName, lastName, email, phoneNumber, imageUri);
                                    Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    finish(); // Finish MainActivity so that it's not kept in the back stack
                                } else {
                                    User user = new User(firstName, lastName, email, phoneNumber, imageUri);
                                    Intent intent = new Intent(MainActivity.this, AttendeeHomePage.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    finish(); // Finish MainActivity so that it's not kept in the back stack
                                }
                            });
                            break;
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error checking for device ID", e);
                    });
        } else {
            deviceId=null;
        }

        // Add click listener for the addUserButton
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input from EditText fields
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();

                // Check if any field is empty
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email and phone number are valid
                if (isValidEmail(email) && isValidPhoneNumber(phoneNumber)) {
                    addNewUser(new User(firstName, lastName, email, phoneNumber, null));

                } else if (!isValidEmail(email)){
                    // Display error message if email is invalid
                    Toast.makeText(MainActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                } else {
                    // Display error message if phone number is invalid
                    Toast.makeText(MainActivity.this, "Invalid Phone Number. Please enter a 10-digit phone number", Toast.LENGTH_SHORT).show();
                }

                // Clear EditText fields after adding a user
                firstNameEditText.setText("");
                lastNameEditText.setText("");
                emailEditText.setText("");
                phoneNumberEditText.setText("");
            }
        });
    }

    /**
     * Callback interface for checking if a user is an admin.
     */
    public interface AdminCheckCallback {
        void onAdminCheckCompleted(boolean isAdmin);
    }

    /**
     * Checks if the current user, identified by device ID and personal information,
     * is an admin and performs actions based on the admin status.
     *
     * @param deviceId The unique device identifier.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param phoneNumber The user's phone number.
     * @param email The user's email address.
     * @param callback The callback to execute after admin check completion.
     */
    public void checkIfUserAdmin(String deviceId, String firstName, String lastName, String phoneNumber, String email, AdminCheckCallback callback){
        CollectionReference adminRef = FirebaseFirestore.getInstance().collection("admin");
        adminRef.document(firstName + phoneNumber)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    callback.onAdminCheckCompleted(documentSnapshot.exists());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error checking for admin document", e);
                    callback.onAdminCheckCompleted(false);
                });
    }


    /**
     * Adds a new user to the Firestore database with the provided user details.
     *
     * @param user The user object containing user details to add to the database.
     */

    private void addNewUser(User user) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Lastname", user.getLastname());
        data.put("Firstname", user.getFirstname());
        data.put("Email", user.getEmail());
        data.put("PhoneNumber", user.getPhone());
        data.put("DeviceId", deviceId);

        if(MainActivity.isRunningTest()){
            deviceId = Long.toString((long) (Math.random() * 1_000_000_0000L) + 1_000_000_000L);
        }

        usernamesRef
                .document(deviceId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CollectionReference adminRef = db.collection("admin");
                        adminRef.document(user.getFirstname() + user.getPhone())
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        finish(); // Finish MainActivity so that it's not kept in the back stack
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, AttendeeHomePage.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        finish(); // Finish MainActivity so that it's not kept in the back stack
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error checking for admin document", e);
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firebase", e.getMessage());
                    }
                });

    }

}
