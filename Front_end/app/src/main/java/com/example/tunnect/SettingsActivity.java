package com.example.tunnect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private String USER_ID;
    private final String DELETE_URL = "http://52.188.167.58:3000/userstore/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Button to delete current user's account
        Button deleteBtn = findViewById(R.id.delete_account);
        deleteBtn.setOnClickListener(view -> {
            deleteUser();
        });
    }

    public void deleteUser() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, DELETE_URL+USER_ID, null, response -> {
            Toast.makeText(getApplicationContext(), "Your account has been deleted!", Toast.LENGTH_LONG).show();
        }, error -> {
            Toast.makeText(getApplicationContext(), "Failed to delete account!", Toast.LENGTH_LONG).show();
        });
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