package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        Button messagesBtn = findViewById(R.id.messages_btn);
        messagesBtn.setOnClickListener(view -> {
            Intent messageIntent = new Intent(MainActivity.this, MessageListActivity.class);
            startActivity(messageIntent);
        });

        Button profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setOnClickListener(view -> {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            profileIntent.putExtra("FROM_MENU", true);
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
            startActivity(searchIntent);
        });

        Button testBtn = findViewById(R.id.test);
        testBtn.setOnClickListener(view -> {
            String testurl = "http://52.188.167.58:5000/chatservice/35i4h34h5j69jk/1234567";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject user = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, testurl, user, response -> {
            }, error -> {
                Toast.makeText(getApplicationContext(), "BADDDDD", Toast.LENGTH_LONG).show();
            });
            queue.add(jsonObjectRequest);
        });
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

            if (xDistance > this.swipe_Max_Distance
                    || yDistance > this.swipe_Max_Distance)
                return false;

            velocityX = Math.abs(velocityX);
            velocityY = Math.abs(velocityY);
            boolean result = false;

            if (velocityX > this.swipe_Min_Velocity
                    && xDistance > this.swipe_Min_Distance) {
                if (e1.getX() > e2.getX()) // right to left
                    this.onSwipe(SWIPE_LEFT);
                else
                    this.onSwipe(SWIPE_RIGHT);

                result = true;
            } else if (velocityY > this.swipe_Min_Velocity
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
            String showToastMessage = "";

            switch (direction) {

                case SWIPE_RIGHT:
                    showToastMessage = "You have Swiped Right.";
                    break;
                case SWIPE_LEFT:
                    showToastMessage = "You have Swiped Left.";
                    break;
                case SWIPE_DOWN:
                    showToastMessage = "You have Swiped Down.";
                    break;
                case SWIPE_UP:
                    showToastMessage = "You have Swiped Up.";
                    break;

            }
            Toast.makeText(getApplicationContext(), showToastMessage, Toast.LENGTH_SHORT).show();
        }
    }

}