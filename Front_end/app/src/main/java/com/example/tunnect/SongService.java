package com.example.tunnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SongService {
    private ArrayList<Song> search_songs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String url;

    // Song service used to access spotify
    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSearchSongs() {
        return search_songs;
    }

    // Search function: Sends get request to spotify then parses the JSON file it gets back and places the tracks into an array list
    public ArrayList<Song> search(final VolleyCallBack callBack) {
        // Clear the search songs array in case it is not the first search
        search_songs = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                // Send a get request to spotify
                (Request.Method.GET, url, null, response -> {

                    JSONObject tracks = null;
                    try {
                        tracks = response.getJSONObject("tracks");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArray = tracks.optJSONArray("items");
                    // Parse through each search result to construct the search_songs array
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            // Parse the search result (have to do some extra gets to get album and artist)
                            JSONObject object = jsonArray.getJSONObject(n);
                            String id = (String) object.get("id");
                            String name = (String) object.get("name");
                            JSONObject album_data = object.getJSONObject("album");
                            String album = (String) album_data.get("name");
                            JSONArray artists = album_data.optJSONArray("artists");
                            JSONObject artist_obj = artists.getJSONObject(0);
                            String artist = (String) artist_obj.get("name");
                            // Used if a song has multiple artists
                            for (int i = 1; i < artists.length(); i++) {
                                artist_obj = artists.getJSONObject(i);
                                artist = artist + ", " + (String) artist_obj.get("name");
                            }
                            Song song_to_add = new Song(id, name, artist, album);
                            search_songs.add(song_to_add);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    Log.d("Search", "Search Error");
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
        return search_songs;
    }

    // Constucts a url for spotify based on the song title given
    public void setURL(String song_title) {
        url = "https://api.spotify.com/v1/search?q=";
        char[] song_title_array = new char[song_title.length()];
        song_title.getChars(0, song_title.length(), song_title_array, 0);
        for (int i = 0; i < song_title.length(); i++) {
            if (song_title_array[i] == ' ') {
                url = url + "%20";
            }
            else {
                url = url + song_title_array[i];
            }
        }
        url = url + "&type=track";
    }
}

// Song class
class Song {

    private String id;
    private String name;
    private String artist;
    private String album;

    public Song(String id, String name, String artist, String album) {
        this.name = name;
        this.id = id;
        this.artist = artist;
        this.album = album;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

}

