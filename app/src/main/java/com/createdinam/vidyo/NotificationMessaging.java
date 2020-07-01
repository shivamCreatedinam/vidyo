package com.createdinam.vidyo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class NotificationMessaging extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a notification payload.
        JSONObject json = null,data = null;
        try {
            json = new JSONObject(remoteMessage.getData().toString());
            data = json.getJSONObject("data");
            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("","",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("");
            NotificationManager mManager = getSystemService(NotificationManager.class);
            mManager.createNotificationChannel(channel);
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getTitle()+ " | " +remoteMessage.getNotification().getBody());
        }
    }
}
