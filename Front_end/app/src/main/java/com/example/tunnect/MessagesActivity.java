package com.example.tunnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
    private static final int SENT_MESSAGE = 0;
    private static final int RECEIVED_MESSAGE = 1;

    private static final String userID = "35i4h34h5j69jk";
    private String SEND_URL;
    private static String BASE_URL;
    private String receiverID;
    private String otherUserName;
    private int otherUserColour;
    private MessageListAdaptor messageAdapter;
    private RecyclerView messageHistory;
    private RequestQueue queue;
    private List<Message> messagesList = new ArrayList<>();
    private boolean err;
    private JSONArray messages = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        receiverID = Objects.requireNonNull(getIntent().getExtras()).getString("OTHER_USER_ID");
        otherUserName = Objects.requireNonNull(getIntent().getExtras()).getString("OTHER_USER_NAME");
        otherUserColour = Objects.requireNonNull(getIntent().getExtras()).getInt("OTHER_USER_COLOUR");
        BASE_URL = "http://52.188.167.58:5000/chatservice/"+userID+"/"+receiverID;
        SEND_URL = "http://52.188.167.58:5000/chatservice/"+receiverID;

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(otherUserName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messageHistory.setLayoutManager(layoutManager);
        messageAdapter = new MessageListAdaptor(this, messagesList, userID, receiverID);
        messageHistory.setAdapter(messageAdapter);

        // Set up button to send a message
        Button sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(view -> {
            sendMessage();
        });
    }

    // Code to return to last page when the return button on the title bar is hit
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /*
     *   This function checks Google Play services to see if the device can receive notifications.
     *  @return: returns true if it can receive notifications, false otherwise.
     */
    private boolean checkGooglePlayServices() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            Log.e("MainActivity", "Error");
            return false;
        } else {
            Log.i("MainActivity", "Google play services updated");
            return true;
        }
    }

    // Using a Volley connection, send a message to the server
    private void sendMessage() {
        EditText editText = findViewById(R.id.edit_text_chatbox);
        err = false;
        JSONObject message = new JSONObject();
        try {
            message.put("senderid", userID);
            message.put("message", editText.getText());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to send your message into the server.", Toast.LENGTH_LONG).show();
            err = true;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SEND_URL, message, response -> {
        }, error -> {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to send, please check your internet connection.", Toast.LENGTH_LONG).show();
            err = true;
        });
        queue.add(jsonObjectRequest);

        if (!err) {
            updateRecyclerView(editText.getText().toString(), SENT_MESSAGE);
            editText.setText("");
        }
    }

    // Using a Volley connection, this method adds entries in the messagesList from the provided server data
    private void populateMessageHistory() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL, null,
                response -> {
                    try {
                        messages = response;

                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject message = messages.getJSONObject(i);

                            //messagesList.add(new Message(chat.getString("senderid"), chat.getString("sender_name"),
                                    //chat.getString("message"), chat.getString("Timestamp"), chat.getInt("sender_colour")));
                            //messagesList.add(new Message(chat.getString("senderid"), chat.getString("sender_name"),
                                    //chat.getString("message"), "12:69am", chat.getInt("sender_colour")));
                            messagesList.add(new Message(message.getString("senderid"), message.getString("sender_name"),
                                    message.getString("message"), "12:69am", 0xff705533));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to retrieve data from server!", Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show());

        queue.add(request);

        // these entries are added for testing purposes
        //TODO: Delete this when testing is done!!!!!!!!!!!!!!!!!
        messagesList.add(new Message(userID, "David Onak", "Hello", "Oct. 24", 0xFF44AA44));
        messagesList.add(new Message(receiverID, "Jeff", "My name is Jeff", "Oct. 23", 0xFF4444AA));
        messagesList.add(new Message(userID, "David Onak", "I just got the chat display working!", "Oct. 23", 0xFFAA4444));
        messagesList.add(new Message(receiverID, "Jeff", "What a beat David!!!", "8:00am", 0xFF222222));
        messagesList.add(new Message(receiverID, "Joe Smith", "Hello, I am Linsay Lohan!", "2:04pm", 0xFF222222));
        messagesList.add(new Message(userID, "David Onak", "HELLLOS!", "3:14pm", 0xFFAA4444));
        messagesList.add(new Message(receiverID, "Jeff", "I ment to say beast btw", "3:23pm", 0xFF222222));
        messagesList.add(new Message(receiverID, "Jeff", "Hello...", "3:25pm", 0xFF222222));
        messagesList.add(new Message(receiverID, "Jeff", "Helloooo!!!", "4:29pm", 0xFF222222));
    }

    // A method that updates the recycler view, adding new message entries to the screen
    private void updateRecyclerView(String message, int sender) {
        int newMessageIndex = messagesList.size();
        Message newMessage;

        if (sender == SENT_MESSAGE) {
            newMessage = new Message(userID, "Test_user", message, "12:69am", 0xff777777);
        } else {
            newMessage = new Message(receiverID, otherUserName, message, "12:69am", otherUserColour);
        }

        messagesList.add(newMessage);
        messageAdapter.notifyItemInserted(newMessageIndex);
        messageHistory.scrollToPosition(messagesList.size() - 1);
    }

    // Adds message to screen from received broadcast
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        public void onReceive(@Nullable Context context, @NonNull Intent intent) {
            String message = Objects.requireNonNull(intent.getExtras()).getString("BROADCAST_MESSAGE");
            updateRecyclerView(message, RECEIVED_MESSAGE);
            Toast.makeText(getBaseContext(), "got broadcast", Toast.LENGTH_LONG).show();
        }
    };

    // Handles an incoming broadcast
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("ReceivedMessage"));
    }

    // Handles a finished broadcast
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
}