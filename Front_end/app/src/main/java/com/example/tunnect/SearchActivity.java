package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    // UI definitions
    private TextView userView;
    private TextView song_album;
    private TextView song_title;
    private TextView song_artist;
    private Button add_button;
    private Button search_button;
    private EditText search_bar;
    private Button next_song_button;

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

        songService = new SongService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        song_title = (TextView) findViewById(R.id.song_title);
        song_album = (TextView) findViewById(R.id.song_album);
        song_artist = (TextView) findViewById(R.id.song_artist);
        add_button = (Button) findViewById(R.id.add);
        search_bar = (EditText) findViewById(R.id.search_bar);
        next_song_button = (Button) findViewById(R.id.next_song_button);
        search_button = (Button) findViewById(R.id.search_button);

        // Set the user id to display
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        // Search button functionality
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songService.set_URL(search_bar.getText().toString());
                songService.search(() -> {
                    search_songs = songService.get_search_songs();
                    current_song = 0;
                    if (search_songs == null) {
                        song_title.setText("Search");
                        song_album.setText("Failed");
                        song_artist.setText(":(");
                    }
                    else {
                        dispSong(search_songs.get(current_song));
                    }
                });
            }
        });

        // Next song button functionality
        next_song_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSong();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SongService.add_song(search_songs.get(current_song));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Updates the currently displayed song
    private void updateSong() {
        current_song++;
        if (current_song < search_songs.size()) {
            dispSong(search_songs.get(current_song));
        }
        else {
            current_song = 0;
            dispSong(search_songs.get(current_song));
        }
    }

    // Displays the info of the given song
    private void dispSong(Song song) {
        song_title.setText(song.getName());
        song_album.setText(song.getAlbum());
        song_artist.setText(song.getArtist());
    }

}
