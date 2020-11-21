package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.Button;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GestureDetectorCompat mDetector;
    private static String USER_ID;
    private TextView user_name;
    private TextView score_view;
    private List<String> matches;
    private List<Double> scores;
    private JSONObject currObject;
    private int currMatch;
    private SharedPreferences sharedPreferences;

    // TODO: Delete this when we can get songs
    private User fakeUser;

    // RecyclerView definitions
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");

        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        user_name = findViewById(R.id.user_name);
        score_view = findViewById(R.id.user_info_button);
        UserService currUser = new UserService();

        recyclerView = findViewById(R.id.match_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        try {
            getMatches(USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button likeBtn = findViewById(R.id.like_btn);
        likeBtn.setOnClickListener(view -> {
            currUser.like(getApplicationContext());
            dispNextMatch();
        });

        Button dislikeBtn = findViewById(R.id.dislike_btn);
        dislikeBtn.setOnClickListener(view -> {
            currUser.dislike(getApplicationContext());
            dispNextMatch();
        });

        Button messagesBtn = findViewById(R.id.messages_btn);
        messagesBtn.setOnClickListener(view -> {
            Intent messageIntent = new Intent(MainActivity.this, MessageListActivity.class);
            messageIntent.putExtra("USER_ID", USER_ID);
            startActivity(messageIntent);
        });

        Button profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setOnClickListener(view -> {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            profileIntent.putExtra("FROM_MENU", true);
            profileIntent.putExtra("USER_ID", USER_ID);
            startActivity(profileIntent);
        });

        Button settingsBtn = findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(view -> {
            Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
            settingIntent.putExtra("USER_ID", USER_ID);
            startActivity(settingIntent);
        });

        Button testBtn = findViewById(R.id.test);
        testBtn.setOnClickListener(view -> {
            String testurl = "http://52.188.167.58:5000/chatservice/"+USER_ID+"/la12nc34e5";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject user = new JSONObject();
            try {
                Date date = new Date();
                user.put("timeStamp", date.getTime());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to add profile to the server!", Toast.LENGTH_LONG).show();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, testurl, user, response -> {
            }, error -> {
                Toast.makeText(getApplicationContext(), "BADDDDD", Toast.LENGTH_LONG).show();
            });
            queue.add(jsonObjectRequest);
        });
    }

    private void dispMatch(User user, Double score) {
        if (user.getUserId().equals("no_user")) {
            user_name.setText("No Matches Found!");
            return;
        }

        user_name.setText(user.getUsername());
        score_view.setText(score.toString());

        // TODO: Change this to display artists and stuff
        List<Song> fakeSongs = new ArrayList<>();
        List<Song> matchesSongs = user.getSongs();
        if (matchesSongs == null) {
            fakeSongs.add(new Song("", "This user has no songs", "", ""));
        }
        else {
            for (int i = 0; i < matchesSongs.size(); i++) {
                fakeSongs.add(matchesSongs.get(i));
            }
        }
        RecyclerView.Adapter mAdapter = new SongListAdaptor(this, fakeSongs);
        recyclerView.setAdapter(mAdapter);
    }

    private void dispNextMatch() {
        currMatch++;
        int size = matches.size();

        if (size == currMatch) {
            RecyclerView.Adapter mAdapter = new SongListAdaptor(this, new ArrayList<>());
            recyclerView.setAdapter(mAdapter);
            user_name.setText("No Matches Left!");
        } else {
            getUser(matches.get(currMatch), scores.get(currMatch));
        }
    }

    private void getMatches(String userId) throws JSONException {
        String match_url = "http://52.188.167.58:3001/matchmaker/" + userId;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        matches = new ArrayList<>();
        scores = new ArrayList<>();

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
                //scores.add(i, (double) currObject.get("score"));
                scores.add(i, 0.0);
            }
            currMatch = 0;
            if(matches.size() != 0) {
                getUser(matches.get(currMatch), scores.get(currMatch));
            } else {
                User no_user = new User();
                no_user.updateUserId("no_user");
                dispMatch(no_user, 0.0);
            }

        }, error -> {
            Log.d("matches", "failure");
        });
        queue.add(jsonArrayRequest);
    }

    // Gets a potential matches user info
    private void getUser(String userId, double score) {
        User user = new User();
        String get_url = "http://52.188.167.58:3000/userstore/" + userId;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_url, null, response -> {
            JSONObject user_info = response;
            try {
                user.updateUserId((String) user_info.get("_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                user.updateUsername((String) user_info.get("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray json_songs = user_info.optJSONArray("songs");
            List<String> user_songs = new ArrayList<>();
            for (int i = 0; i < json_songs.length(); i++) {
                try {
                    user_songs.add(json_songs.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (user_songs.size() > 0) {
                for (int i = 0; i < user_songs.size() - 1; i++) {
                    getSong(user, score, user_songs.get(i), false);
                }
                getSong(user, score, user_songs.get(user_songs.size() - 1), true);
            }
            else {
                user.addSong(new Song("", "This user has no songs", "", ""));
                dispMatch(user, score);
            }
        }, error -> {
            // TODO: error handling here
        });
        queue.add(jsonObjectRequest);
    }

    // Get song the info of a song
    private void getSong(User user, Double score, String song_id, Boolean lastSong) {
        String url = "https://api.spotify.com/v1/tracks/" + song_id;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (lastSong) {
                user.addSong(song);
                dispMatch(user, score);
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error getting songs", Toast.LENGTH_SHORT).show();
            dispMatch(user, score);
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
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";
        public final static int SWIPE_UP = 1;
        public final static int SWIPE_DOWN = 2;
        public final static int SWIPE_LEFT = 3;
        public final static int SWIPE_RIGHT = 4;

        // Swipe distances
        private int swipe_Min_Distance = 100;
        private int swipe_Max_Distance = 2000;
        private int swipe_Min_Velocity = 100;


        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            final float xDistance = Math.abs(e1.getX() - e2.getX());
            final float yDistance = Math.abs(e1.getY() - e2.getY());
            float absVelocityX;
            float absVelocityY;

            if (xDistance > this.swipe_Max_Distance
                    || yDistance > this.swipe_Max_Distance)
                return false;

            absVelocityX = Math.abs(velocityX);
            absVelocityY = Math.abs(velocityY);
            boolean result = false;

            if (absVelocityX > this.swipe_Min_Velocity
                    && xDistance > this.swipe_Min_Distance) {
                if (e1.getX() > e2.getX()) // right to left
                    this.onSwipe(SWIPE_LEFT);
                else
                    this.onSwipe(SWIPE_RIGHT);

                result = true;
            } else if (absVelocityY > this.swipe_Min_Velocity
                    && yDistance > this.swipe_Min_Distance) {
                if (e1.getY() > e2.getY()) // bottom to up
                    this.onSwipe(SWIPE_UP);
                else
                    this.onSwipe(SWIPE_DOWN);

                result = true;
            }

            return result;
        }

        private void onSwipe(int direction) {
            //Detect the swipe gestures and display toast
            UserService currUser = new UserService();

            switch (direction) {
                case SWIPE_RIGHT:
                    currUser.like(getApplicationContext());
                    dispNextMatch();
                    break;
                case SWIPE_LEFT:
                    currUser.dislike(getApplicationContext());
                    dispNextMatch();
                    break;
                default:
                    break;

            }
        }
    }

}