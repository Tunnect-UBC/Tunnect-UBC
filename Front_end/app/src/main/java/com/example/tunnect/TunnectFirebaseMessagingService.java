package com.example.tunnect;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.android.gms.tasks.OnCompleteListener;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class TunnectFirebaseMessagingService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;

    protected void onCreate(Bundle savedInstanceState) {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    // TODO: override onNewToken

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
        handler.post(new Runnable() {
            public final void run() {
                Toast.makeText(getBaseContext(), getString(R.string.handle_notification_now), Toast.LENGTH_LONG).show();
                if (remoteMessage.getNotification() != null) {
                    Intent intent = new Intent("MyData");
                    intent.putExtra("message", remoteMessage.getData().get("Text"));
                    if (broadcaster != null) {
                        broadcaster.sendBroadcast(intent);
                    }
                }
            }
        });
    }

    // TODO: Create a message receiver constant

    // companion object { //USE STATIC INSTEAD OF THIS
    //    private const val TAG = "MyFirebaseMessagingS"
    //}
}