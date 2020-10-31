package com.example.tunnect;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private RequestQueue queue;
    JSONObject user_info;
    private String newTestID = "59379626979347"; //TODO:Replace with real id which will be passed in as a parameter
    private String RETRIEVE_URL;
    private final String ADD_URL = "http://52.188.167.58:3000/userstore";
    private int selectedColorRGB;
    private Drawable wrappedIconImage;
    private ImageView iconImage;
    private EditText username, faveArtist;
    private TextView profileTitle;
    private boolean returnVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        queue = Volley.newRequestQueue(getApplicationContext());
        iconImage = findViewById(R.id.profile_icon);
        username = findViewById(R.id.enter_username);
        profileTitle = findViewById(R.id.username_title);
        faveArtist = findViewById(R.id.enter_favourite_artist);
        Drawable unwrappedIconImage = AppCompatResources.getDrawable(this, R.drawable.profile_circle);
        wrappedIconImage = DrawableCompat.wrap(unwrappedIconImage);
        selectedColorRGB = 0;
        ColorPicker cp = new ColorPicker(ProfileActivity.this, 66, 170, 170);

        RETRIEVE_URL = ADD_URL+newTestID;

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            String title = "Profile";
            actionBar.setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
            if(saveProfileEntries()) {
                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        // Read information on current user if it exists and fill screen entries
        loadProfileEntries();
/*
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
        }); */
    }

    // Will attempt to read existing data on current user and fill screen entries
    private void loadProfileEntries() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, RETRIEVE_URL, null, response -> {
            user_info = response;
        }, error -> {
        });
        queue.add(jsonObjectRequest);

        if (user_info != null) {
            try {
                profileTitle.setText((String) user_info.get("username"));
                username.setText((String) user_info.get("username"));
                faveArtist.setText((String) user_info.get("top_artist"));
                DrawableCompat.setTint(wrappedIconImage, (int) user_info.get("colour"));
                iconImage.setImageDrawable(wrappedIconImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            DrawableCompat.setTint(wrappedIconImage, 0xFF66AAAA);
            iconImage.setImageDrawable(wrappedIconImage);
        }
    }

    // If entries are correct, will create a profile for the current user
    // TODO: Allow this function to append the profile if it exists already
    private boolean saveProfileEntries() {
        String selectedUsername = username.getText().toString().trim();
        String selectedArtist = faveArtist.getText().toString().trim();
        if (selectedUsername.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            return false;
        } else if (selectedArtist.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a favourite artist", Toast.LENGTH_LONG).show();
            return false;
        } else if (selectedColorRGB == 0) {
            Toast.makeText(getApplicationContext(), "Please select a profile icon colour", Toast.LENGTH_LONG).show();
            return false;
        }

        returnVal = true;

        // Add the user to the server
        JSONObject user = new JSONObject();
        try {
            user.put("_id", newTestID);
            user.put("username", selectedUsername);
            user.put("top_artist", selectedArtist);
            user.put("icon_colour", selectedColorRGB);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ADD_URL, user, response -> {
        }, error -> {
            Toast.makeText(getApplicationContext(), "Failed to add profile to the server!", Toast.LENGTH_LONG).show();
            //returnVal = false;
        });
        queue.add(jsonObjectRequest);

        return returnVal;
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