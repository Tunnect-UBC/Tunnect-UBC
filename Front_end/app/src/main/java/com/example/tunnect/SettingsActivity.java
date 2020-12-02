package com.example.tunnect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private String USER_ID;
    private final String DELETE_URL = "http://52.188.167.58:3000/userstore/";
    private static final String SC_URL = "http://52.188.167.58:5000/chatservice/";

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");
        queue = Volley.newRequestQueue(this);

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Button to delete current user's account
        Button deleteBtn = findViewById(R.id.delete_account);
        deleteBtn.setOnClickListener(view -> {
            deleteChats();
        });
    }

    // Deletes the account information for a user
    public void deleteUserAccount() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, DELETE_URL+USER_ID, null, response -> {
            Toast.makeText(getApplicationContext(), "Your account has been deleted!", Toast.LENGTH_LONG).show();
            Intent profileIntent = new Intent(SettingsActivity.this, SplashActivity.class);
            startActivity(profileIntent);
        }, error -> {
            Toast.makeText(getApplicationContext(), "Failed to delete account!", Toast.LENGTH_LONG).show();
        });
        queue.add(jsonObjectRequest);
    }

    // Deletes all existing chats with this user, then calls deleteMatches
    private void deleteChats() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SC_URL+USER_ID, null,
                response -> {
                    try {
                        JsonObjectRequest jsonObjectRequest;
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject chat = response.getJSONObject(i);
                            if (chat.has("usrID1")) {
                                jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, SC_URL+chat.getString("usrID1")+"/"+USER_ID, null, result -> {
                                    Toast.makeText(getApplicationContext(), "Your chats have been deleted!", Toast.LENGTH_LONG).show();
                                }, error -> {
                                    Toast.makeText(getApplicationContext(), "Failed to delete chats!", Toast.LENGTH_LONG).show();
                                });
                            } else {
                                jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, SC_URL+USER_ID+"/"+chat.getString("usrID2"), null, result -> {
                                    Toast.makeText(getApplicationContext(), "Your chats have been deleted!", Toast.LENGTH_LONG).show();
                                }, error -> {
                                    Toast.makeText(getApplicationContext(), "Failed to delete chats!", Toast.LENGTH_LONG).show();
                                });
                            }
                            queue.add(jsonObjectRequest);
                        }
                        deleteMatches();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to retrieve data from server!", Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show());
        queue.add(request);
    }

    // Deletes all existing chats with this user, then calls deleteUserAccount
    private void deleteMatches() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, DELETE_URL+USER_ID, null,
                response -> {
                    try {
                        JSONArray jsonMatches = response.optJSONArray("matches");
                        JsonObjectRequest jsonObjectRequest;

                        for (int i = 0; i < jsonMatches.length(); i++) {
                            String userID = jsonMatches.getString(i);
                            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, DELETE_URL+userID, null, result -> {
                                try {
                                    // Patch existing match list for other user
                                    JSONArray jsonOtherMatches = result.optJSONArray("matches");
                                    ArrayList<String> newMatches = new ArrayList<>();
                                    for (int j = 0; j < jsonOtherMatches.length(); j++) {
                                        JSONArray patchArray = new JSONArray();

                                        if (!jsonOtherMatches.getString(j).equals(USER_ID)) {
                                            newMatches.add(jsonOtherMatches.getString(j));
                                        }

                                        JSONObject songObject = new JSONObject();
                                        songObject.put("propName", "matches");
                                        if (newMatches.size() == 0) {
                                            songObject.put("value", null);
                                        } 
                                        patchArray.put(songObject);
                                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PATCH, DELETE_URL+userID, patchArray, r -> {
                                        }, error -> {
                                            // TODO: This removes the match correctly but it returns error for some reason, check it with Nick
                                        });
                                        queue.add(jsonArrayRequest);
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Failed to delete match!", Toast.LENGTH_LONG).show();
                                }
                            }, error -> {
                                Toast.makeText(getApplicationContext(), "Failed to delete match!", Toast.LENGTH_LONG).show();
                            });
                            queue.add(jsonObjectRequest);
                        }

                        deleteUserAccount();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Failed to retrieve data from server!", Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show());
        queue.add(request);
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

}