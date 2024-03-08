//package com.example.scansaga;
//
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.MockitoSession;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@RunWith(AndroidJUnit4.class)
//public class ShowAllUsersTest {
//
//    private ShowAllUsers activity;
//
//    @Mock
//    private FirebaseFirestore mockFirestore;
//    @Mock
//    private CollectionReference mockCollectionRef;
//    @Mock
//    private QuerySnapshot mockQuerySnapshot;
//    @Mock
//    private DocumentReference mockDocumentRef;
//    @Mock
//    private Task<Void> mockTask;
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this);
//        activity = new ShowAllUsers();
//        activity.db = mockFirestore;
//        activity.usersRef = mockCollectionRef;
//    }
//
//    @Test
//    public void testFetchUsersFromFirestore_Success() {
//        // Mock Firestore response
//        ArrayList<User> usersList = new ArrayList<>();
//        usersList.add(new User("John", "Doe", "john@example.com", "123456789"));
//        usersList.add(new User("Alice", "Smith", "alice@example.com", "987654321"));
//
//        when(mockQuerySnapshot.isEmpty()).thenReturn(false);
//        when(mockQuerySnapshot.toObjects(User.class)).thenReturn(usersList);
//        when(mockCollectionRef.addSnapshotListener(Mockito.any())).thenReturn(null);
//
//        activity.fetchUsersFromFirestore();
//
//        assertEquals(usersList.size(), activity.userList.size());
//    }
//
//    @Test
//    public void testDeleteUserFromFirestore_Success() {
//        // Mock Firestore response
//        User userToDelete = new User("John", "Doe", "john@example.com", "123456789");
//
//        when(mockCollectionRef.document(Mockito.anyString())).thenReturn(mockDocumentRef);
//        when(mockDocumentRef.delete()).thenReturn(mockTask);
//        when(mockTask.isSuccessful()).thenReturn(true);
//
//        activity.deleteUserFromFirestore(userToDelete);
//
//        verify(mockDocumentRef).delete();
//        assertEquals(0, activity.userList.size());
//    }
//}
//
