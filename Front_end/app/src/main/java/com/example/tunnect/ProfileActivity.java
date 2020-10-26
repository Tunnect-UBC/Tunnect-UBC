package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private JSONObject user_info = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        TextView userid_view = (TextView) findViewById(R.id.userid_view);
        TextView username_view = (TextView) findViewById(R.id.username_view);
        TextView artist_view = (TextView) findViewById(R.id.artist_view);
        TextView colour_view = (TextView) findViewById(R.id.colour_view);

        Button add_user_button = findViewById(R.id.user_add_button);
        add_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String add_url = "http://52.188.167.58:3000/userstore";
                JSONObject user = new JSONObject();
                try {
                    user.put("_id", "1234567");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    user.put("username", "test_user1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    user.put("top_artist", "Bearings");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    user.put("icon_colour", "0xffffffff");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, add_url, user, response -> {
                }, error -> {
                });
                queue.add(jsonObjectRequest);
            }
        });

        Button get_user_button = findViewById(R.id.user_info_button);
        get_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_url = "http://52.188.167.58:3000/userstore/1234567";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_url, null, response -> {
                    user_info = response;
                }, error -> {
                });
                queue.add(jsonObjectRequest);

                try {
                    userid_view.setText((String) user_info.get("_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    username_view.setText((String) user_info.get("username"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    artist_view.setText((String) user_info.get("top_artist"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    colour_view.setText((String) user_info.get("colour"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button delete_user_button = findViewById(R.id.user_delete_button);
        delete_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String delete_url = "http://52.188.167.58:3000/userstore/1234567";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, delete_url, null, response -> {
                    user_info = response;
                }, error -> {
                });
                queue.add(jsonObjectRequest);
            }
        });

    }
}