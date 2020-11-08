package com.example.tunnect;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/*
 *  This class is the instance of the FirebaseMessagingService used for this app. It
 *  handles push notifications on the background and foreground, while also sending out
 *  a broadcast of the message for MessagesActivity and MessagesListActivity to use.
 */
public class TunnectFirebaseMessagingService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;


    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        this.getSharedPreferences("_", 0).edit().putString("fcm_token", token).apply();
    }

    /*
     * This function takes in a remote message and makes a foreground notification.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        handleNow(remoteMessage);
    }

    /*
     * This function gets a message and extracts its data.
     */
    private void handleNow(final RemoteMessage remoteMessage) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            Toast.makeText(getBaseContext(), getString(R.string.handle_notification_now), Toast.LENGTH_LONG).show();
            if (remoteMessage.getNotification() != null) {
                Toast.makeText(getBaseContext(), "sending broadcast", Toast.LENGTH_LONG).show();
                Intent intent = new Intent("ReceivedMessage");
                intent.putExtra("BROADCAST_MESSAGE", remoteMessage.getData().get("Text"));
                if (broadcaster != null) {
                    broadcaster.sendBroadcast(intent);
                }
            }
        });
    }
}