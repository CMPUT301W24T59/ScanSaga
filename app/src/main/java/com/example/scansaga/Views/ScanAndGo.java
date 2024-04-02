package com.example.scansaga.Views;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scansaga.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Arrays;
import java.util.List;

public class ScanAndGo extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentIntegrator intentIntegrator = new IntentIntegrator(ScanAndGo.this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan a QR code");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data){

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null){
            String content = intentResult.getContents();
            if(content!=null){
                checkUrl(content);
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }

    }

    private void checkUrl(String url){
        db.collection("events").document(url).get().addOnCompleteListener(task ->{
            if(task.isSuccessful())
            {
                if (task.getResult().exists())
                {
                    String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    List<String> signedUpAttendees = Arrays.asList(deviceId);
                    if(signedUpAttendees.contains(deviceId)){
                        Toast.makeText(ScanAndGo.this , "You've already joined this event",Toast.LENGTH_LONG).show();
                    }
                    if (task.getResult().contains("signedUpAttendees"))
                    {
                        db.collection("events").document(url).update("signedUpAttendees", FieldValue.arrayUnion(deviceId))
                                .addOnSuccessListener(s -> Toast.makeText(ScanAndGo.this, "Thank you for joining this event", Toast.LENGTH_LONG).show())
                                .addOnFailureListener(f -> Toast.makeText(ScanAndGo.this, "Error in joining Event", Toast.LENGTH_LONG).show());
                    }
                    else
                    {
                         // Start the array with the attendee
                        db.collection("events").document(url).update("signedUpAttendees", signedUpAttendees) // Use update to create the field
                                    .addOnSuccessListener(s -> Toast.makeText(ScanAndGo.this, "Thank you for joining this event", Toast.LENGTH_LONG).show())
                                    .addOnFailureListener(f -> Toast.makeText(ScanAndGo.this, "Error in joining Event", Toast.LENGTH_LONG).show());
                    }
                }
                else
                {
                    Toast.makeText(ScanAndGo.this, "Error checking URL", Toast.LENGTH_SHORT).show();
                }
            };

        });
    };
};
