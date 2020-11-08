package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.Button;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GestureDetectorCompat mDetector;
    private static String USER_ID;
    private TextView user_name;
    private TextView score_view;
    private JSONObject matches;

    // RecyclerView definitions
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");

        setContentView(R.layout.activity_main);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        user_name = findViewById(R.id.user_name);
        score_view = findViewById(R.id.user_info_button);
        UserService currUser = new UserService();

        recyclerView = findViewById(R.id.match_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        try {
            getMatches("1234567");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // TODO: Delete all this once getMatches works
        Song song1 = new Song("song1", "Song1", "Artist1", "Album1");
        Song song2 = new Song("song2", "Song2", "Artist2", "Album2");
        Song song3 = new Song("song3", "Song3", "Artist3", "Album3");
        Song song4 = new Song("song4", "Song4", "Artist4", "Album4");
        Song song5 = new Song("song5", "Song5", "Artist5", "Album5");
        Song song6 = new Song("song6", "Song6", "Artist6", "Album6");
        Song song7 = new Song("song7", "Song7", "Artist7", "Album7");
        Song song8 = new Song("song8", "Song8", "Artist8", "Album8");
        List<Song> fakeSongs = new ArrayList<>();
        fakeSongs.add(song1);
        fakeSongs.add(song2);
        fakeSongs.add(song3);
        fakeSongs.add(song4);
        fakeSongs.add(song5);
        fakeSongs.add(song6);
        fakeSongs.add(song7);
        fakeSongs.add(song8);
        User fakeUser = new User("fakeId", "ExampleUser", "Example", fakeSongs);
        dispMatch(fakeUser);

        Button likeBtn = findViewById(R.id.like_btn);
        likeBtn.setOnClickListener(view -> {
            currUser.like(getApplicationContext());
        });

        Button dislikeBtn = findViewById(R.id.dislike_btn);
        dislikeBtn.setOnClickListener(view -> {
            currUser.dislike(getApplicationContext());
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
            Intent settingsIntent = new Intent(MainActivity.this, TestActivity.class);
            startActivity(settingsIntent);
        });

        Button searchBtn = findViewById(R.id.goto_search_btn);
        searchBtn.setOnClickListener(view -> {
            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
            searchIntent.putExtra("USER_ID", USER_ID);
            startActivity(searchIntent);
        });

        Button testBtn = findViewById(R.id.test);
        testBtn.setOnClickListener(view -> {
            String testurl = "http://52.188.167.58:5000/chatservice/35i4h34h5j69jk/1234567";
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

    private void dispMatch(User user) {
        user_name.setText(user.getUsername());
        score_view.setText(user.getTopArtist());

        RecyclerView.Adapter mAdapter = new SongListAdaptor(this, user.getSongs());
        recyclerView.setAdapter(mAdapter);
    }

    private void getMatches(String userId) throws JSONException {
        String match_url = "http://52.188.167.58:3001/matchmaker";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JSONObject hostId = new JSONObject();
        hostId.put("hostId", userId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, match_url, hostId, response -> {
            matches = response;
        }, error -> {
            Log.d("matches", "failure");
        });
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
                    break;
                case SWIPE_LEFT:
                    currUser.dislike(getApplicationContext());
                    break;
                default:
                    break;

            }
        }
    }

}