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

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/*
 * The class for the activity that display and handles the chat with another user.
 */
public class MessagesActivity extends AppCompatActivity {
    private static final int SENT_MESSAGE = 0;
    private static final int RECEIVED_MESSAGE = 1;

    private static String USER_ID;
    private static String SEND_URL;
    private static String BASE_URL;
    private String receiverID;
    private String otherUserName;
    private int otherUserColour;
    private int index;
    private MessageListAdaptor messageAdapter;
    private RecyclerView messageHistory;
    private RequestQueue queue;
    private List<Message> messagesList = new ArrayList<>();
    private EditText editText;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        editText = findViewById(R.id.edit_text_chatbox);
        receiverID = Objects.requireNonNull(getIntent().getExtras()).getString("OTHER_USER_ID");
        otherUserName = Objects.requireNonNull(getIntent().getExtras()).getString("OTHER_USER_NAME");
        otherUserColour = Objects.requireNonNull(getIntent().getExtras()).getInt("OTHER_USER_COLOUR");
        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");
        index = Objects.requireNonNull(getIntent().getExtras()).getInt("INDEX");
        BASE_URL = "http://52.188.167.58:5000/chatservice/"+USER_ID+"/"+receiverID;
        SEND_URL = "http://52.188.167.58:5000/chatservice/"+receiverID;
        date = new Date();

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(otherUserName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Fill the recycler view with the history of chat messages
        queue = Volley.newRequestQueue(this);
        messageHistory = findViewById(R.id.messageHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messageHistory.setLayoutManager(layoutManager);

        populateMessageHistory();

        // Set up button to send a message
        Button sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(view -> {
            if (!editText.getText().toString().equals("")) {
                sendMessage();
            }
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

    // Using a Volley connection, send a message to the server
    private void sendMessage() {
        JSONObject message = new JSONObject();
        try {
            message.put("senderid", USER_ID);
            message.put("message", editText.getText().toString());
            message.put("timeStamp", date.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to send your message into the server.", Toast.LENGTH_LONG).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SEND_URL, message, response -> {
            updateRecyclerView(editText.getText().toString(), SENT_MESSAGE);
            editText.setText("");
        }, error -> {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to send, please check your internet connection.", Toast.LENGTH_LONG).show();
        });
        queue.add(jsonObjectRequest);

        updateLastMessage(editText.getText().toString());
    }

    // Using a Volley connection, this method adds entries in the messagesList from the provided server data
    private void populateMessageHistory() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL, null,
                response -> {
                    try {
                        JSONObject content = response.getJSONObject(0);
                        JSONArray messages = content.getJSONArray("messages");

                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject message = messages.getJSONObject(i);

                            int colour;
                            String name;
                            if(message.getString("senderid").equals("tunnect")) {
                                colour = 0xFFD2691E;
                                name = "Tunnect";
                            } else {
                                colour = otherUserColour;
                                name = otherUserName;
                            }
                            messagesList.add(new Message(message.getString("senderid"), name,
                                    message.getString("message"), message.getLong("timeStamp"), colour));
                        }

                        messageAdapter = new MessageListAdaptor(this, messagesList, USER_ID, receiverID);
                        messageHistory.setAdapter(messageAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to retrieve data from server!", Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show());
        queue.add(request);

        if (receiverID.equals("tunnect")) {
            messagesList.add(new Message("tunnect", "Tunnect",
                    "Welcome to tunnect messaging! \nWhen making matches with other users, a chat will appear here.", date.getTime(), 0xFFD2691E));
        }
        messageAdapter = new MessageListAdaptor(this, messagesList, USER_ID, receiverID);
        messageHistory.setAdapter(messageAdapter);
    }

    // A method that updates the recycler view, adding new message entries to the screen
    private void updateRecyclerView(String message, int sender) {
        int newMessageIndex = messagesList.size();
        Message newMessage;

        if (sender == SENT_MESSAGE) {
            newMessage = new Message(USER_ID, "Test_user", message, date.getTime(), 0xff777777);
        } else {
            newMessage = new Message(receiverID, otherUserName, message, date.getTime(), otherUserColour);
        }

        messagesList.add(newMessage);
        messageAdapter.notifyItemInserted(newMessageIndex);
        messageHistory.scrollToPosition(messagesList.size() - 1);
    }

    // Sends a broadcast to update last message on chat list activity
    private void updateLastMessage(String message) {
        Intent intent = new Intent("new_last_message");
        intent.putExtra("LAST_MESSAGE", message);
        intent.putExtra("INDEX", index);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // Adds message to screen from received broadcast
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        public void onReceive(@Nullable Context context, @NonNull Intent intent) {
            String message = Objects.requireNonNull(intent.getExtras()).getString("BROADCAST_MESSAGE");
            updateRecyclerView(message, RECEIVED_MESSAGE);
            Toast.makeText(getBaseContext(), "got broadcast", Toast.LENGTH_LONG).show();
            updateLastMessage(message);
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