package com.example.tunnect;
import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

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
import android.util.Log;
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
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private RequestQueue queue;
    private RequestQueue spotifyQueue;

    private static String USER_ID;
    private static String RETRIEVE_URL;
    private static final String ADD_URL = "http://52.188.167.58:3000/userstore/";
    private static String token;
    private int selectedColorRGB;
    private Drawable wrappedIconImage;
    private ImageView iconImage;
    private EditText username;
    private EditText favGenre;
    private TextView profileTitle;
    private TextView matches;
    private TextView songs;
    private ColorPicker cp;
    private Button saveBtn;

    private int selectedSongs;
    private boolean inUserStore;
    private RecyclerView recyclerView;
    private ArrayList<Song> selSongs;
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
        favGenre = findViewById(R.id.enter_fav_genre);
        profileTitle = findViewById(R.id.username_title);
        Drawable unwrappedIconImage = AppCompatResources.getDrawable(this, R.drawable.profile_circle);
        wrappedIconImage = DrawableCompat.wrap(unwrappedIconImage);

        selectedColorRGB = 0;
        cp = new ColorPicker(ProfileActivity.this, 66, 170, 170);

        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");
        selectedSongs = 0;
        selSongs = new ArrayList<>();
        RETRIEVE_URL = ADD_URL + USER_ID;
        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            String title = "Profile";
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
        selSongs.add(new Song("", "You have no songs", "", "No Album", new ArrayList<String>(), ""));
        RecyclerView.Adapter mAdapter = new ProfileSongsAdaptor(this, selSongs);
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
        saveBtn = findViewById(R.id.save_profile);
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
                new IntentFilter("edited_song"));

        // Must first check if device can send and receive messages
        if (ProfileActivity.this.checkGooglePlayServices()) {
            FirebaseInstanceId id = FirebaseInstanceId.getInstance();
            id.getInstanceId().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    token = Objects.requireNonNull(task.getResult()).getToken();
                } else {
                    token = "0";
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "This device cannot receive notifications! Must have google play services.", Toast.LENGTH_LONG).show();
            token = "0";
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String adding = intent.getStringExtra("ADDING");
            if (adding.equals("True")) {
                String song = intent.getStringExtra("ADDED_SONG");
                if (containsSong(song)) {
                    Toast.makeText(context, "Song Already Added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Song Added", Toast.LENGTH_LONG).show();
                    selectedSongs++;
                    songs.setText(Integer.toString(selectedSongs));
                    getSong(song);
                }
            } else {
                Toast.makeText(context, "Song Deleted", Toast.LENGTH_LONG).show();
                String song = intent.getStringExtra("DELETED_SONG");
                selectedSongs--;
                songs.setText(Integer.toString(selectedSongs));
                deleteSong(song);
            }
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
                    favGenre.setText((String) response.get("favGenre"));
                    JSONArray jsonSongs = response.optJSONArray("songs");
                    int numSongs = jsonSongs.length();
                    songs.setText(Integer.toString(numSongs));
                    selectedSongs = numSongs;

                    JSONObject jsonSong;
                    Song song;
                    for (int i = 0; i < numSongs; i++) {
                        jsonSong = (JSONObject) jsonSongs.get(i);
                        song = new Song();
                        song.setId(jsonSong.getString("_id"));
                        song.setArtist(jsonSong.getString("artist"));
                        song.setName(jsonSong.getString("name"));
                        selSongs.add(song);
                    }
                    if (numSongs > 0) {
                        RecyclerView.Adapter mAdapter = new ProfileSongsAdaptor(this, selSongs);
                        recyclerView.setAdapter(mAdapter);
                    }


                    JSONArray jsonMatches = response.optJSONArray("matches");
                    int numMatches = jsonMatches.length();
                    matches.setText(Integer.toString(numMatches));

                    Integer iconColour = (Integer) response.get("iconColour");
                    DrawableCompat.setTint(wrappedIconImage, Integer.parseInt(iconColour.toString()));
                    selectedColorRGB = Integer.parseInt(iconColour.toString());
                    iconImage.setImageDrawable(wrappedIconImage);
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
            saveBtn.setText("Create Profile");
        });
        queue.add(jsonObjectRequest);
    }

    /*
     * Fetches a song from spotify, parses its data, and calls getArtistInfo
     */
    private void getSong(String song_id) {
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
                song.setArtistId((String) artist_info.get("id"));
                String artist = artist_info.getString("name");
                // Used if a song has multiple artists
                for (int i = 1; i < artists.length(); i++) {
                    artist_info = artists.getJSONObject(i);
                    artist = artist + ", " + artist_info.getString("name");
                }
                song.setArtist(artist);
                getArtistInfo(song);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error getting songs", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), song_id, Toast.LENGTH_SHORT).show();
            selSongs.add(new Song("", "This user has no songs", "", "", new ArrayList<>(), ""));
            RecyclerView.Adapter mAdapter = new ProfileSongsAdaptor(this, selSongs);
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
    * Fetches an artists info from spotify and adds the related artists to the song object
    * then adds the son to selSongs
    */
    private void getArtistInfo(Song song) {
        String url = "https://api.spotify.com/v1/artists/" + song.getArtistId() + "/related-artists";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray artists = response.getJSONArray("artists");
                for (int i = 0; i < 5; i++) {
                    JSONObject relatedArtist = (JSONObject) artists.get(i);
                    song.addRelatedArtist((String) relatedArtist.get("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            selSongs.add(song);
            RecyclerView.Adapter mAdapter = new ProfileSongsAdaptor(this, selSongs);
            recyclerView.setAdapter(mAdapter);

        }, error -> {
            Toast.makeText(getApplicationContext(), "Could not fetch related artists", Toast.LENGTH_LONG).show();
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
    * Deletes a song from selSongs given the songId
    */
    private void deleteSong(String songId) {
        for (int i = 0; i < selSongs.size(); i++) {
            if (songId.equals(selSongs.get(i).getId())) {
                selSongs.remove(i);
                break;
            }
        }
    }

    /*
    * Returns true if selSongs contains the given songId
    * returns false otherwise
    */
    private boolean containsSong(String songId) {
        for (int i = 0; i < selSongs.size(); i++) {
            if (songId.equals(selSongs.get(i).getId())) {
                return true;
            }
        }
        return false;
    }

    /*
    * If entries are correct, will create a profile for the current user. If profile already exists,
    * then the current entries will be modified to the new entries provided.
    */
    private void saveProfileEntries() throws JSONException {
        String selectedUsername = username.getText().toString().trim();
        String selectedGenre = favGenre.getText().toString().trim();
        if (selectedUsername.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            return;
        } else if (selectedGenre.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your favourite genre", Toast.LENGTH_LONG).show();
            return;
        } else if (selectedColorRGB == 0) {
            Toast.makeText(getApplicationContext(), "Please select a profile icon colour", Toast.LENGTH_LONG).show();
            return;
        } else if (selectedSongs < 5) {
            Toast.makeText(getApplicationContext(), "Please select at least 5 songs before saving your account", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject user = new JSONObject();
        JsonObjectRequest jsonObjectRequest;
        JSONArray songs = new JSONArray();
        JSONObject song;
        JSONArray relatedArtists;
        for(int i = 0; i < selSongs.size(); i++) {
            relatedArtists = new JSONArray();
            song = new JSONObject();
            song.put("_id", selSongs.get(i).getId());
            song.put("artist", selSongs.get(i).getArtist());
            song.put("name", selSongs.get(i).getName());
            song.put("genre", "Awmsonmeness");
            for (int j = 0; j < selSongs.get(i).getRelatedArtists().size(); j++) {
                relatedArtists.put(selSongs.get(i).getRelatedArtists().get(j));
            }
            song.put("relatedArtists", relatedArtists);
            songs.put(i, song);
        }
        if(!inUserStore) { // Add the user to the server
            try {
                user.put("_id", USER_ID);
                user.put("username", selectedUsername);
                user.put("favGenre", selectedGenre);
                user.put("iconColour", Integer.toString(selectedColorRGB));
                user.put("songs", songs);
                user.put("notifId", token);
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
            queue.add(jsonObjectRequest);

        } else { // If modifying profile then use patch requests
            JSONArray patchArray = new JSONArray();
            try {
                user.put("propName", "username");
                user.put("value", selectedUsername);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Failed to add profile to the server!", Toast.LENGTH_LONG).show();
            }
            patchArray.put(user);
            user = new JSONObject();
            try {
                user.put("propName", "favGenre");
                user.put("value", selectedGenre);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Failed to add profile to the server!", Toast.LENGTH_LONG).show();
            }
            patchArray.put(user);
            user = new JSONObject();
            try {
                user.put("propName", "iconColour");
                user.put("value", selectedColorRGB);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Failed to add profile to the server!", Toast.LENGTH_LONG).show();
            }
            patchArray.put(user);

            JSONObject songObject = new JSONObject();
            songObject.put("propName", "songs");
            songObject.put("value", songs);
            patchArray.put(songObject);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PATCH, ADD_URL + USER_ID, patchArray, response -> {
                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                mainIntent.putExtra("USER_ID", USER_ID);
                startActivity(mainIntent);
            }, error -> {
                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                mainIntent.putExtra("USER_ID", USER_ID);
                startActivity(mainIntent);
            });
            queue.add(jsonArrayRequest);
        }
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