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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

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

    private static String USER_ID;
    private static final String BASE_URL = "http://52.188.167.58:5000/chatservice/";
    private static String LOAD_URL;
    private RequestQueue queue;
    private RecyclerView chatOptions;
    private ChatListAdaptor chatListAdaptor;
    private RecyclerView.LayoutManager layoutManager;
    private List<Chat> chatsList = new ArrayList<>();
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");
        LOAD_URL = BASE_URL + USER_ID;
        date = new Date();

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
        layoutManager = new LinearLayoutManager(this);
        chatOptions.setLayoutManager(layoutManager);

        populateChatList();
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

    // Using a Volley connection, this method adds entries in the chatsList from the provided server data
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
                                chatsList.add(new Chat(chat.getString("usrID1"), chat.getString("usrName1"), chat.getString("lastMessage"), chat.getLong("lastTime"), 0xFFFFFFFF));
                            } else {
                                chatsList.add(new Chat(chat.getString("usrID2"), chat.getString("usrName2"), chat.getString("lastMessage"), chat.getLong("lastTime"), 0xFFFFFFFF));
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

    // With the retrieved user id, opens a chat with that user
    public void openNewChat(String other_user_id, String other_user_name, int other_user_colour) {
        Intent messageIntent = new Intent(MessageListActivity.this, MessagesActivity.class);
        messageIntent.putExtra("OTHER_USER_ID", other_user_id);
        messageIntent.putExtra("OTHER_USER_NAME", other_user_name);
        messageIntent.putExtra("OTHER_USER_COLOUR", other_user_colour);
        messageIntent.putExtra("USER_ID", USER_ID);
        startActivity(messageIntent);
    }

    // Adds message to screen from received broadcast
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        public void onReceive(@Nullable Context context, @NonNull Intent intent) {
            //String message = Objects.requireNonNull(intent.getExtras()).getString("BROADCAST_MESSAGE");
            //updateRecyclerView(message, RECEIVED_MESSAGE);
            //TODO: Setup an update for chats with last message sent
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