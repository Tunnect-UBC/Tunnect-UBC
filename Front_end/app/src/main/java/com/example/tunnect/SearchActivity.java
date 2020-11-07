package com.example.tunnect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    // RecyclerView definitions
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // UI definitions
    private Button search_button;
    private EditText search_bar;

    // Song service used to access spotify
    private SongService songService;
    // Songs returned from a search
    private ArrayList<Song> search_songs;
    // Value used to track what song is currently displayed
    private int current_song;

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

        recyclerView = findViewById(R.id.song_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        songService = new SongService(getApplicationContext());
        search_bar = (EditText) findViewById(R.id.search_bar);
        search_button = (Button) findViewById(R.id.search_button);

        // Search button functionality
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songService.set_URL(search_bar.getText().toString());
                songService.search(() -> {
                    search_songs = songService.get_search_songs();
                    current_song = 0;
                    if (search_songs == null) {
                        //TODO: Handle no search results
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

    // Displays the info of the given song
    private void dispSongs() {
        mAdapter = new SongListAdaptor(this, search_songs, recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

}
