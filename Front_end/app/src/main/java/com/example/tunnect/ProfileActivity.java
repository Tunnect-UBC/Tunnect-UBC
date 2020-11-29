package com.example.tunnect;
import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private RequestQueue queue;
    private RequestQueue spotifyQueue;

    private static String USER_ID;
    private static String RETRIEVE_URL;
    private static final String ADD_URL = "http://52.188.167.58:3000/userstore/";
    private int selectedColorRGB;
    private Drawable wrappedIconImage;
    private ImageView iconImage;
    private EditText username;
    private TextView profileTitle;
    private TextView matches;
    private TextView songs;
    private ColorPicker cp;

    private int selectedSongs;
    private boolean inUserStore;
    private RecyclerView recyclerView;
    private ArrayList<Song> selSongs;
    private ArrayList<String> user_songs;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        queue = Volley.newRequestQueue(getApplicationContext());
        spotifyQueue = Volley.newRequestQueue(getApplicationContext());

        matches = findViewById(R.id.num_matches);
        songs = findViewById(R.id.num_songs);
        iconImage = findViewById(R.id.profile_icon);
        username = findViewById(R.id.enter_username);
        profileTitle = findViewById(R.id.username_title);
        Drawable unwrappedIconImage = AppCompatResources.getDrawable(this, R.drawable.profile_circle);
        wrappedIconImage = DrawableCompat.wrap(unwrappedIconImage);

        selectedColorRGB = 0;
        cp = new ColorPicker(ProfileActivity.this, 66, 170, 170);

        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");
        selectedSongs = 0;
        selSongs = new ArrayList<>();
        user_songs = new ArrayList<>();
        RETRIEVE_URL = ADD_URL + USER_ID;
        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            String title = "Set Profile";
            actionBar.setTitle(title);
        }
        if (Objects.requireNonNull(getIntent().getExtras()).getBoolean("FROM_MENU")) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // set recycler view
        recyclerView = findViewById(R.id.selectedSongs);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        selSongs.add(new Song("", "You have no songs", "", "No Album"));
        RecyclerView.Adapter mAdapter = new SongListAdaptor(this, selSongs);
        recyclerView.setAdapter(mAdapter);
        selSongs.remove(0);

        /* Show color picker dialog */
        Button getColour = findViewById(R.id.enter_colour);
        getColour.setOnClickListener(view -> {
            cp.show();
            cp.enableAutoClose();
        });

        /* Set a new Listener called when user click "select" */
        cp.setCallback(color -> {
            selectedColorRGB = color;
            DrawableCompat.setTint(wrappedIconImage, color);
            iconImage.setImageDrawable(wrappedIconImage);
        });

        // save changes into profile
        Button saveBtn = findViewById(R.id.save_profile);
        saveBtn.setOnClickListener(view -> {
            try {
                saveProfileEntries();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        Button addBtn = findViewById(R.id.add_songs);
        addBtn.setOnClickListener(view -> {
            Intent searchIntent = new Intent(ProfileActivity.this, SearchActivity.class);
            searchIntent.putExtra("USER_ID", USER_ID);
            startActivity(searchIntent);
        });

        // Read information on current user if it exists and fill screen entries
        loadProfileEntries();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("added_song"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Toast.makeText(context, "Song Added", Toast.LENGTH_LONG).show();
            String song = intent.getStringExtra("ADDED_SONG");
            selectedSongs ++;
            user_songs.add(song);
            getSong(song, true);
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    // Will attempt to read existing data on current user and fill screen entries
    private void loadProfileEntries() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, RETRIEVE_URL, null, response -> {
            if (response != null) {
                inUserStore = true;
                try {
                    profileTitle.setText((String) response.get("username"));
                    username.setText((String) response.get("username"));
                    JSONArray jsonSongs = response.optJSONArray("songs");
                    int numSongs = jsonSongs.length();
                    songs.setText(Integer.toString(numSongs));
                    selectedSongs = numSongs;

                    for (int i = 0; i < numSongs; i++) {
                        try {
                            user_songs.add(jsonSongs.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (user_songs.size() > 0) {
                        for (int i = 0; i < user_songs.size() - 1; i++) {
                            getSong(user_songs.get(i), false);
                        }
                        getSong(user_songs.get(user_songs.size() - 1), true);
                    }

                    JSONArray jsonMatches = response.optJSONArray("matches");
                    int numMatches = jsonMatches.length();
                    matches.setText(Integer.toString(numMatches));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DrawableCompat.setTint(wrappedIconImage, 0xFF66AAAA);
                iconImage.setImageDrawable(wrappedIconImage);
                inUserStore = false;
            }
        }, error -> {
            inUserStore = false;
        });
        queue.add(jsonObjectRequest);
    }

    /*
     * Fetches a song from spotify, parses its data, and places the songs info in the user given
     * Calls dispMatch on the user if the song is the last of the user's songs
     */
    private void getSong(String song_id, Boolean lastSong) {
        String url = "https://api.spotify.com/v1/tracks/" + song_id;
        Song song = new Song();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            song.setId(song_id);
            try {
                song.setName(response.getString("name"));
                JSONObject album_info = response.getJSONObject("album");
                song.setAlbum(album_info.getString("name"));
                JSONArray artists = album_info.optJSONArray("artists");
                JSONObject artist_info = artists.getJSONObject(0);
                String artist = artist_info.getString("name");
                // Used if a song has multiple artists
                for (int i = 1; i < artists.length(); i++) {
                    artist_info = artists.getJSONObject(i);
                    artist = artist + ", " + artist_info.getString("name");
                }
                song.setArtist(artist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            selSongs.add(song);

            if (lastSong) {
                if (selSongs.size() == 0) {
                    selSongs.add(new Song("", "This user has no songs", "", ""));
                }
                RecyclerView.Adapter mAdapter = new SongListAdaptor(this, selSongs);
                recyclerView.setAdapter(mAdapter);
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error getting songs", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), song_id, Toast.LENGTH_SHORT).show();
            selSongs.add(new Song("", "This user has no songs", "", ""));
            RecyclerView.Adapter mAdapter = new SongListAdaptor(this, selSongs);
            recyclerView.setAdapter(mAdapter);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        spotifyQueue.add(jsonObjectRequest);
    }

    /*
    * If entries are correct, will create a profile for the current user. If profile already exists,
    * then the current entries will be modified to the new entries provided.
    */
    private void saveProfileEntries() throws JSONException {
        String selectedUsername = username.getText().toString().trim();
        if (selectedUsername.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            return;
        } else if (selectedColorRGB == 0) {
            Toast.makeText(getApplicationContext(), "Please select a profile icon colour", Toast.LENGTH_LONG).show();
            return;
        } else if (selectedSongs < 5) {
            Toast.makeText(getApplicationContext(), "Please select at least 5 songs before completing your account", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject user = new JSONObject();
        JsonObjectRequest jsonObjectRequest;
        if(!inUserStore) { // Add the user to the server
            JSONArray songArray = new JSONArray();

            songArray.put(user_songs);

            try {
                user.put("_id", USER_ID);
                user.put("username", selectedUsername);
                user.put("icon_colour", selectedColorRGB);
                user.put("songs", songArray);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Failed to add profile to the server!", Toast.LENGTH_LONG).show();
            }
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ADD_URL, user, response -> {
                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                mainIntent.putExtra("USER_ID", USER_ID);
                startActivity(mainIntent);
            }, error -> {
                Toast.makeText(getApplicationContext(), "Failed to connect to the server!", Toast.LENGTH_LONG).show();
            });

        } else { // If modifying profile then use patch requests
            try {
                user.put("propName", "username");
                user.put("value", selectedUsername);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Failed to add profile to the server!", Toast.LENGTH_LONG).show();
            }
            jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, ADD_URL, user, response -> {
            }, error -> {
                Toast.makeText(getApplicationContext(), "Failed to connect to the server!", Toast.LENGTH_LONG).show();
            });
            queue.add(jsonObjectRequest);

            user = new JSONObject();
            try {
                user.put("propName", "icon_colour");
                user.put("value", selectedColorRGB);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Failed to add profile to the server!", Toast.LENGTH_LONG).show();
            }
            jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, ADD_URL, user, response -> {
            }, error -> {
                Toast.makeText(getApplicationContext(), "Failed to connect to the server!", Toast.LENGTH_LONG).show();
            });
            queue.add(jsonObjectRequest);

            JSONArray addArray = new JSONArray();
            JSONObject songObject = new JSONObject();
            songObject.put("propName", "songs");
            songObject.put("value", user_songs);
            addArray.put(songObject);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PATCH, ADD_URL, addArray, response -> {
                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                mainIntent.putExtra("USER_ID", USER_ID);
                startActivity(mainIntent);
            }, error -> {
                Toast.makeText(getApplicationContext(), "Failed to connect to the server!", Toast.LENGTH_LONG).show();
            });
        }

        queue.add(jsonObjectRequest);
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