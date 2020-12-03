package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String USER_ID;
    private TextView user_name;
    private TextView genre_view;
    private List<String> matches;
    private JSONObject currObject;
    private int currMatch;
    private User displayedUser;
    private String currUsername;

    // Volley queues
    private RequestQueue userQueue;
    private RequestQueue matchQueue;

    // RecyclerView definitions
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");

        setContentView(R.layout.activity_main);

        user_name = findViewById(R.id.user_name);
        genre_view = findViewById(R.id.user_info_button);

        recyclerView = findViewById(R.id.match_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // Setup Volley queues
        userQueue = Volley.newRequestQueue(getApplicationContext());
        matchQueue = Volley.newRequestQueue(getApplicationContext());

        // Get the current users username
        getCurrUser();

        try {
            getMatches(USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Like Button
        Button likeBtn = findViewById(R.id.like_btn);
        likeBtn.setOnClickListener(view -> {
            if (!displayedUser.getUserId().equals("no_user")) {
                like(displayedUser);
                dispNextMatch();
            }
        });

        // Dislike Button
        Button dislikeBtn = findViewById(R.id.dislike_btn);
        dislikeBtn.setOnClickListener(view -> {
            if (!displayedUser.getUserId().equals("no_user")) {
                dislike(displayedUser);
                dispNextMatch();
            }
        });

        // Messages Button
        Button messagesBtn = findViewById(R.id.messages_btn);
        messagesBtn.setOnClickListener(view -> {
            Intent messageIntent = new Intent(MainActivity.this, MessageListActivity.class);
            messageIntent.putExtra("USER_ID", USER_ID);
            startActivity(messageIntent);
        });

        // Profile Button
        Button profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setOnClickListener(view -> {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            profileIntent.putExtra("FROM_MENU", true);
            profileIntent.putExtra("USER_ID", USER_ID);
            startActivity(profileIntent);
        });

        // Settings Button
        Button settingsBtn = findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(view -> {
            Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
            settingIntent.putExtra("USER_ID", USER_ID);
            startActivity(settingIntent);
        });
    }

    /*
    * Displays a potential match
    * Sends a users songs to the SongListAdaptor to be displayed
    */
    private void dispMatch(User user) {
        displayedUser = user;
        if (user.getUserId().equals("no_user")) {
            user_name.setText("No Matches Found!");
            genre_view.setText("Try Again Later");
            return;
        }
        user_name.setText(user.getUsername());
        genre_view.setText("Prefers " + user.getFavGenre() + " Music");

        List<Song> matchesSongs = user.getSongs();
        if (matchesSongs == null) {
            matchesSongs = new ArrayList<>();
            matchesSongs.add(new Song("", "This user has no songs", "", "", new ArrayList<>(), ""));
        }
        RecyclerView.Adapter mAdapter = new SongListAdaptor(this, matchesSongs);
        recyclerView.setAdapter(mAdapter);
    }

    /*
    * Increments the currMatch counter and begins the process of displaying the next match
    * by calling getUser
    */
    private void dispNextMatch() {
        currMatch++;

        if (matches.size() == currMatch) {
            RecyclerView.Adapter mAdapter = new SongListAdaptor(this, new ArrayList<>());
            recyclerView.setAdapter(mAdapter);
            user_name.setText("No Matches Left!");
        } else {
            getUser(matches.get(currMatch));
        }
    }

    /*
    * Gets a users potential matches and places their userIds in the matches list.
    * Calls getUser for the first match
    * Calls dispMatch if the user has no matches
    */
    private void getMatches(String userId) throws JSONException {
        String match_url = "http://52.188.167.58:3001/matchmaker/" + userId;

        matches = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, match_url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    currObject = response.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    matches.add(i, currObject.get("_id").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            currMatch = 0;
            if(matches.size() != 0) {
                getUser(matches.get(currMatch));
            } else {
                User no_user = new User();
                no_user.updateUserId("no_user");
                dispMatch(no_user);
            }

        }, error -> {
            User no_user = new User();
            no_user.updateUserId("no_user");
            dispMatch(no_user);
        });
        matchQueue.add(jsonArrayRequest);
    }

    /*
    * Fetches a users info and places the user info in user
    * Calls getSong on each of the users songs
    * If the user has no songs then it calls dispMatch
    */
    private void getUser(String userId) {
        User user = new User();
        String get_url = "http://52.188.167.58:3000/userstore/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_url, null, response -> {
            JSONObject user_info = response;
            try {
                user.updateUserId((String) user_info.get("_id"));
                user.updateUsername((String) user_info.get("username"));
                user.setNotifId((String) user_info.get("notifId"));
                user.setFavGenre((String) user_info.get("favGenre"));
                JSONArray jsonMatches = user_info.optJSONArray("matches");
                for (int i = 0; i < jsonMatches.length(); i++) {
                    user.addMatch(jsonMatches.get(i).toString());
                }
                JSONArray jsonLikes = user_info.optJSONArray("likes");
                for (int i = 0; i < jsonLikes.length(); i++) {
                    user.addLike(jsonLikes.get(i).toString());
                }
                JSONArray jsonDislikes = user_info.optJSONArray("dislikes");
                for (int i = 0; i < jsonDislikes.length(); i++) {
                    user.addDislike(jsonDislikes.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray json_songs = user_info.optJSONArray("songs");
            for (int i = 0; i < json_songs.length(); i++) {
                try {
                    user.addSong(parseSong((JSONObject) json_songs.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (user.getSongs().size() <= 0) {
                user.addSong(new Song("", "This user has no songs", "", "", new ArrayList<>(), ""));
            }
            dispMatch(user);
        }, error -> {
            Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
        });
        userQueue.add(jsonObjectRequest);
    }

    private Song parseSong(JSONObject songInfo) throws JSONException {
        Song song = new Song();
        song.setId(songInfo.getString("_id"));
        song.setArtist(songInfo.getString("artist"));
        song.setName(songInfo.getString("name"));
        JSONArray relatedArtists = songInfo.getJSONArray("relatedArtists");
        for (int i = 0; i < relatedArtists.length(); i++) {
            song.addRelatedArtist(relatedArtists.getString(i));
        }
        return song;
    }

    /*
    * Handles like functionality
    */
    public void like(User likedUser) {
        if (likedUser.getLikes().contains(USER_ID)) {
            try {
                match(likedUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String like_url = "http://52.188.167.58:3000/userstore/" + USER_ID + "/addLike/" + likedUser.getUserId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, like_url, null, response -> {
        }, error -> {
            Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
        });
        userQueue.add(jsonObjectRequest);
    }

    /*
    * Handles dislike functionality
    */
    public void dislike(User dislikedUser) {
        String dislike_url = "http://52.188.167.58:3000/userstore/" + USER_ID + "/addDislike/" + dislikedUser.getUserId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, dislike_url, null, response -> {
        }, error -> {
            Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
        });
        userQueue.add(jsonObjectRequest);
    }

    /*
    * Removes the current user from the matched users likes
    * Adds both users to the others matches list
    */
    public void match(User matchedUser) throws JSONException {
        Toast.makeText(getApplicationContext(), "You matched with " + matchedUser.getUsername(), Toast.LENGTH_LONG).show();
        String like_url = "http://52.188.167.58:3000/userstore/" + USER_ID + "/removeLike/" + matchedUser.getUserId();
        String match_url1 = "http://52.188.167.58:3000/userstore/" + USER_ID + "/addMatch/" + matchedUser.getUserId();
        String match_url2 = "http://52.188.167.58:3000/userstore/" + matchedUser.getUserId() + "/addMatch/" + USER_ID;
        JSONObject notifId1 = new JSONObject();
        notifId1.put("notifId", matchedUser.getNotifId());
        notifId1.put("username", currUsername);
        JsonObjectRequest removeLikeRequest = new JsonObjectRequest(Request.Method.PATCH, like_url, notifId1, response -> {
        }, error -> {
            Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
        });
        JSONObject notifId2 = new JSONObject();
        notifId2.put("notifId", "0");
        notifId2.put("username", matchedUser.getUsername());
        JsonObjectRequest matchRequest1 = new JsonObjectRequest(Request.Method.PATCH, match_url1, notifId1, response -> {
        }, error -> {
            Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
        });

        JsonObjectRequest matchRequest2 = new JsonObjectRequest(Request.Method.PATCH, match_url2, notifId2, response -> {
        }, error -> {
            Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
        });
        userQueue.add(removeLikeRequest);
        userQueue.add(matchRequest1);
        userQueue.add(matchRequest2);
        createChat(USER_ID, matchedUser.getUserId());
    }

    /*
    * Creates a chat between user1 and user2
    */
    private void createChat(String userId1, String userId2) {
        String chatUrl = "http://52.188.167.58:5000/chatservice/" + userId1 + "/" + userId2;
        JSONObject user = new JSONObject();
        try {
            Date date = new Date();
            user.put("timeStamp", date.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, chatUrl, user, response -> {
        }, error -> {
            Toast.makeText(getApplicationContext(), "Failed to create chat", Toast.LENGTH_LONG).show();
        });
        userQueue.add(jsonObjectRequest);
    }

    /*
    * Fetches the current users username and places it in currUsername
    */
    private void getCurrUser() {
        String get_url = "http://52.188.167.58:3000/userstore/" + USER_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_url, null, response -> {
            try {
                currUsername = response.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
        });
        userQueue.add(jsonObjectRequest);
    }

}