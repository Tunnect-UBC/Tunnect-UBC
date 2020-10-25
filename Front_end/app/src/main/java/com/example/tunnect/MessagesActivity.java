package com.example.tunnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessagesActivity extends AppCompatActivity {
    MessageAdapter messageAdapter;
    RecyclerView messageHistory;
    RecyclerView.LayoutManager layoutManager;
    List<Message> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // Must first check if device can send and receive messages
        if (MessagesActivity.this.checkGooglePlayServices()) {
            FirebaseInstanceId id = FirebaseInstanceId.getInstance();
            id.getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w("user", "getInstanceId failed", task.getException());
                        return;
                    }

                    // To know if connection works, we want to see the resulting token
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                    Log.d("user", token);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "This device cannot receive notifications! Must have google play services.", Toast.LENGTH_LONG).show();
        }
    }


    /*
     *   This function checks Google Play services to see if the device can receive notifications.
     *  @return: returns true if it can receive notifications, false otherwise.
     */
    private boolean checkGooglePlayServices() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable((Context)this);
        if (status != ConnectionResult.SUCCESS) {
            Log.e("MainActivity", "Error");
            return false;
        } else {
            Log.i("MainActivity", "Google play services updated");
            return true;
        }
    }
/*
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("MyData"));
    }

    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }*/
}