package com.example.scansaga.Controllers;
import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for sending notifications using Firebase Cloud Messaging (FCM).
 */
public class NotificationSender  {

    /** The FCM token of the user to whom the notification will be sent. */
    String userToken;
    String title;
    String body;
    Context mContext;
    Activity act;

    /** The Volley request queue for sending the notification. */
    public RequestQueue requestQueue;

    /** The URL for sending POST requests to FCM server. */
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";

    /** The server key required for authorization to send notifications via FCM. */
    private final String ServerKey ="AAAAsko8abA:APA91bGX6aboEQs28ccSp1dY9mkV15t00psLjkchR_0so33hYjsjUpQAsKvll54iTn3CE2gC13Xlqx0OnW-2VMZoORVGsesfOQ5sqBlqvZwsOzoPkOy2XaEm5tnt_cuIU9--uz40lPSa";

    /**
     * Constructs a new NotificationSender object with the provided parameters.
     *
     * @param userToken The FCM token of the user to whom the notification will be sent.
     * @param title The title of the notification.
     * @param body The body/content of the notification.
     * @param mContext The context of the application.
     * @param act The activity context.
     */
    public NotificationSender(String userToken, String title, String body, Context mContext, Activity act) {
        this.userToken = userToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.act = act;
    }

    /**
     * Sends the notification to the user with the specified FCM token.
     */
    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(act);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", "icon"); // enter icon that exists in drawable only
            mainObj.put("notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // code run is got response
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + ServerKey);
                    return header;
                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
