//package com.example.scansaga;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import androidx.test.core.app.ActivityScenario;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//
//@RunWith(MockitoJUnitRunner.class)
//public class EventHomePageTest {
//
//    @Mock
//    Bundle mockBundle;
//
//    @Mock
//    Bitmap mockBitmap;
//
//    EventHomePage activity;
//
//    @Before
//    public void setUp() {
//        activity = Mockito.spy(new EventHomePage());
//        // Stub Intent with necessary extras
//        Intent intent = new Intent();
//        intent.putExtra("eventName", "Test Event");
//        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//        intent.putExtra("qrCodeBitmap", bitmap);
//
//        // Launch the Activity with the Intent
//        ActivityScenario.launch(EventHomePage.class, intent.getExtras());
//    }
//
//    @Test
//    public void testOnCreate_WithValidExtras() {
//        // Arrange
//        when(mockBundle.getString("eventName")).thenReturn("Test Event");
//        when(mockBundle.getParcelable("qrCodeBitmap")).thenReturn(mockBitmap);
//
//        // Act
//        activity.onCreate(mockBundle);
//
//        // Assert
//        assertEquals("Welcome to the homepage of Test Event", activity.eventNameTextView.getText());
//        verify(activity).setContentView(R.layout.activity_event_homepage);
//        verify(activity).finish();
//    }
//
//    @Test
//    public void testOnCreate_WithMissingExtras() {
//        // Arrange
//        when(mockBundle.getString("eventName")).thenReturn(null);
//        when(mockBundle.getParcelable("qrCodeBitmap")).thenReturn(null);
//
//        // Act
//        activity.onCreate(mockBundle);
//
//        // Assert
//        verify(activity).finish();
//    }
//
//    @Test
//    public void eventNameTextView_DisplayedWithCorrectText() {
//        onView(withId(R.id.event_name_text_view))
//                .check(matches(withText("Welcome to the homepage of Test Event")));
//    }
//
//    @Test
//    public void qrCodeImageView_Displayed() {
//        onView(withId(R.id.qr_code_image_view)).check(matches(isDisplayed()));
//    }
//}
//
