package com.example.tunnect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private String USER_ID;
    // RecyclerView definition
    private RecyclerView recyclerView;
    // Song service used to access spotify
    private SongService songService;
    // Songs returned from a search
    private ArrayList<Song> search_songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Start by setting up a title for the page
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Search Songs");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // UI definitions
        recyclerView = findViewById(R.id.song_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        songService = new SongService(getApplicationContext());
        EditText search_bar = (EditText) findViewById(R.id.search_bar);
        Button search_button = (Button) findViewById(R.id.search_button);

        USER_ID = Objects.requireNonNull(getIntent().getExtras()).getString("USER_ID");

        // Search button functionality
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songService.setURL(search_bar.getText().toString());
                songService.search(() -> {
                    search_songs = songService.getSearchSongs();
                    if (search_songs == null) {
                        //TODO: Handle no search results
                        Log.d("search", "Song list empty");
                    }
                    else {
                        dispSongs();
                    }
                });
            }
        });
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

    // Displays the Search Results
    private void dispSongs() {
        RecyclerView.Adapter mAdapter = new SearchListAdaptor(this, search_songs, USER_ID);
        recyclerView.setAdapter(mAdapter);
    }

}
