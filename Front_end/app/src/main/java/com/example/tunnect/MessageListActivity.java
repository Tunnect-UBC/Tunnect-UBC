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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/*
 * The class for the activity that displays all of the chats that the user has.
 */
public class MessageListActivity extends AppCompatActivity {

    private boolean isInFront;
    private static String USER_ID;
    private static final String BASE_URL = "http://52.188.167.58:5000/chatservice/";
    private static String LOAD_URL;
    private RequestQueue queue;
    private RecyclerView chatOptions;
    private ChatListAdaptor chatListAdaptor;
    private List<Chat> chatsList = new ArrayList<>();
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");
        LOAD_URL = BASE_URL + USER_ID;
        date = new Date();
        isInFront = true;

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            String title = "Messages";
            actionBar.setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Fill recycler view with existing chat entries
        queue = Volley.newRequestQueue(this);
        chatOptions = findViewById(R.id.chatOptions);
        chatOptions.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        chatOptions.setLayoutManager(layoutManager);

        // Must first check if device can send and receive messages
        if (!MessageListActivity.this.checkGooglePlayServices()) {
            Toast.makeText(getApplicationContext(), "This device cannot receive notifications! Must have google play services.", Toast.LENGTH_LONG).show();
        }

        populateChatList();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("new_last_message"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("LAST_MESSAGE");
            int index = intent.getIntExtra("INDEX", 0);
            long time = intent.getLongExtra("TIME", 0);

            chatsList.get(index).updateLastMessage(message);
            chatsList.get(index).updateTimestamp(time);
            chatListAdaptor.notifyItemChanged(index);
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
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

    // Using a Volley connection, this method adds entries in the chatsList to display onto the recycler view
    private void populateChatList() {
        chatsList.add(new Chat("tunnect", "Tunnect", "Welcome to tunnect messaging! \nWhen making matches with other users, a chat will appear here.", date.getTime(), 0xFFD2691E));
        chatListAdaptor = new ChatListAdaptor(this, chatsList, chatOptions);
        chatOptions.setAdapter(chatListAdaptor);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, LOAD_URL, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject chat = response.getJSONObject(i);
                            if (chat.has("usrID1")) {
                                chatsList.add(new Chat(chat.getString("usrID1"), chat.getString("usrName1"), chat.getString("lastMessage"), chat.getLong("lastTime"), Integer.parseInt((String) chat.get("usrColour1"))));
                            } else {
                                chatsList.add(new Chat(chat.getString("usrID2"), chat.getString("usrName2"), chat.getString("lastMessage"), chat.getLong("lastTime"), Integer.parseInt((String) chat.get("usrColour2"))));
                            }
                        }

                        chatListAdaptor = new ChatListAdaptor(this, chatsList, chatOptions);
                        chatOptions.setAdapter(chatListAdaptor);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to retrieve data from server!", Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show());
        queue.add(request);
    }

    // Using a Volley connection, this method adds entries in the chatsList to update the entries on the recycler view
    private void updateChatList() {
        chatsList.add(new Chat("tunnect", "Tunnect", "Welcome to tunnect messaging! \nWhen making matches with other users, a chat will appear here.", date.getTime(), 0xFFD2691E));
        chatListAdaptor = new ChatListAdaptor(this, chatsList, chatOptions);
        chatOptions.setAdapter(chatListAdaptor);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, LOAD_URL, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject chat = response.getJSONObject(i);
                            if (chat.has("usrID1")) {
                                chatsList.add(new Chat(chat.getString("usrID1"), chat.getString("usrName1"), chat.getString("lastMessage"), chat.getLong("lastTime"), Integer.parseInt((String) chat.get("usrColour1"))));
                            } else {
                                chatsList.add(new Chat(chat.getString("usrID2"), chat.getString("usrName2"), chat.getString("lastMessage"), chat.getLong("lastTime"), Integer.parseInt((String) chat.get("usrColour2"))));
                            }
                        }
                        chatOptions.notifyAll();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to retrieve data from server!", Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show());
        queue.add(request);
    }

    // With the retrieved user id, opens a chat with that user
    public void openNewChat(String other_user_id, String other_user_name, int other_user_colour, int index) {
        Intent messageIntent = new Intent(MessageListActivity.this, MessagesActivity.class);
        messageIntent.putExtra("OTHER_USER_ID", other_user_id);
        messageIntent.putExtra("OTHER_USER_NAME", other_user_name);
        messageIntent.putExtra("OTHER_USER_COLOUR", other_user_colour);
        messageIntent.putExtra("USER_ID", USER_ID);
        messageIntent.putExtra("INDEX", index);
        startActivity(messageIntent);
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

    // Adds message to screen from received broadcast
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        public void onReceive(@Nullable Context context, @NonNull Intent intent) {
            if (isInFront) {
                chatsList.clear();
                try {
                    // There may be a delay for server to update so must wait for adequate time
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateChatList();
            }
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

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }
}