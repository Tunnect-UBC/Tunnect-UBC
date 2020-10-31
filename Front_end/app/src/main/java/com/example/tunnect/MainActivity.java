package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button messagesBtn = findViewById(R.id.messages_btn);
        messagesBtn.setOnClickListener(view -> {
            Intent messageIntent = new Intent(MainActivity.this, MessageListActivity.class);
            startActivity(messageIntent);
        });

        Button profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setOnClickListener(view -> {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
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
}