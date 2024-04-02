//package com.example.scansaga.Views;
//
//import static com.example.scansaga.Model.MainActivity.CHANNEL_ID;
//import static com.example.scansaga.Model.MainActivity.NOTIFICATION_PERMISSION_REQUEST_CODE;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.app.Notification;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.annotation.RequiresApi;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.fragment.app.DialogFragment;
//
//import com.example.scansaga.Model.Event;
//import com.example.scansaga.Model.User;
//import com.example.scansaga.R;
//
//public class NotificationFragment extends DialogFragment {
//
//    int notificationId;
//    private EditText notificationTitle, notificationMessage;
//    private Button notificationSendButton;
//    private Event event;
//
//
////    public static NotificationFragment newInstance(Event event) {
////        Bundle args = new Bundle();
////        args.putSerializable("event", event);
////        NotificationFragment fragment = new NotificationFragment();
////        fragment.setArguments(args);
////        return fragment;
////    }
//
//    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.send_notification, null);
//        builder.setView(view);
//
//        notificationTitle = view.findViewById(R.id.notification_title);
//        notificationMessage = view.findViewById(R.id.notification_msg);
//        notificationSendButton = view.findViewById(R.id.notification_send_button);
//
//
//        Bundle args = getArguments();
//        if (args != null && args.containsKey("event")) {
//            event = (Event) args.getSerializable("event");
//            if (event != null)
//                if (notificationTitle == null) notificationTitle.setText(event.getName());
//
//        }
//    }
//}
