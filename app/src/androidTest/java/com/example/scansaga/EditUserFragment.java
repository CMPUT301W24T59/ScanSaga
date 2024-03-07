//package com.example.scansaga;
//
//import android.widget.EditText;
//
//import androidx.fragment.app.FragmentActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.WriteBatch;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(AndroidJUnit4.class)
//public class EditUserFragmentTest {
//
//    private ActivityScenario<FragmentActivity> activityScenario;
//
//    @Before
//    public void setUp() {
//        activityScenario = ActivityScenario.launch(FragmentActivity.class);
//    }
//
//    @Test
//    public void testUpdateUserInFirestore() {
//        activityScenario.onActivity(activity -> {
//            // Mock user data
//            User user = new User("John", "Doe", "john.doe@example.com", "1234567890");
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("user", user);
//
//            // Create EditUserFragment
//            FragmentManager fragmentManager = activity.getSupportFragmentManager();
//            EditUserFragment editUserFragment = new EditUserFragment();
//            editUserFragment.setArguments(bundle);
//            editUserFragment.show(fragmentManager, "EditUserFragment");
//
//            // Perform update
//            EditText editFirstName = editUserFragment.getView().findViewById(R.id.edit_text_firstname);
//            EditText editLastName = editUserFragment.getView().findViewById(R.id.edit_text_lastname);
//            EditText editEmail = editUserFragment.getView().findViewById(R.id.edit_text_email);
//            EditText editPhone = editUserFragment.getView().findViewById(R.id.edit_text_phone);
//
//            editFirstName.setText("Jane");
//            editLastName.setText("Smith");
//            editEmail.setText("jane.smith@example.com");
//            editPhone.setText("0987654321");
//
//            // Get Firestore instance and collection reference
//            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//            CollectionReference usersCollection = firestore.collection("users");
//
//            // Add a test document to the collection
//            Task<Void> addTask = usersCollection.document("testUser").set(user);
//            Tasks.await(addTask); // Wait for the task to complete
//
//            // Call the update method
//            editUserFragment.updateUserInFirestore(
//                    editFirstName.getText().toString(),
//                    editLastName.getText().toString(),
//                    editEmail.getText().toString(),
//                    editPhone.getText().toString()
//            );
//
//            // Verify Firestore update
//            Task<QuerySnapshot> queryTask = usersCollection.whereEqualTo("phone", user.getPhone()).get();
//            queryTask.addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        assertEquals("Jane", document.getString("firstname"));
//                        assertEquals("Smith", document.getString("lastname"));
//                        assertEquals("jane.smith@example.com", document.getString("email"));
//                        assertEquals("0987654321", document.getString("phone"));
//                    }
//                }
//            });
//        });
//    }
//}
//
//
