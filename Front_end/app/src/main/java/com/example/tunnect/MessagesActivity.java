package com.example.tunnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * The class for the activity that display and handles the chat with another user.
 */
public class MessagesActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://35.236.81.110/time";
    MessageListAdaptor messageAdapter;
    RecyclerView messageHistory;
    private RequestQueue queue;
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

        // Fill the recycler view with the history of chat messages
        queue = Volley.newRequestQueue(this);
        populateMessageHistory();
        messageHistory = findViewById(R.id.messageHistory);
        messageHistory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        messageHistory.setLayoutManager(layoutManager);
        messageAdapter = new MessageListAdaptor(this, messagesList, 1010);
        messageHistory.setAdapter(messageAdapter);

        // Set up button to send a message
        //TODO: Implement send button
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

    // Using a Volley connection, this method adds entries in the messagesList from the provided server data
    private void populateMessageHistory() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray;
                        try {
                            jsonArray = response.getJSONArray("messages");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject chat = jsonArray.getJSONObject(i);

                                messagesList.add(new Message(chat.getLong("Id"), chat.getString("Name"),
                                        chat.getString("Message"), chat.getString("Timestamp"), chat.getInt("Colour")));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed to retrieve data from server!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);

        // these entries are added for testing purposes
        //TODO: Delete this when testing is done!!!!!!!!!!!!!!!!!
        messagesList.add(new Message(1010, "David Onak", "Hello", "10:33am", 0x44AA44));
        messagesList.add(new Message(2020, "Jeff", "My name is Jeff", "8:00am", 0x4444AA));
        messagesList.add(new Message(1010, "David Onak", "I just got the chat display working!", "Oct. 24", 0xAA4444));
        messagesList.add(new Message(2020, "Jeff", "What a beat David!!!", "Oct. 23", 0x222222));
        messagesList.add(new Message(3030, "Joe Smith", "Hello, I am Linsay Lohan!", "Oct. 23", 0x222222));
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