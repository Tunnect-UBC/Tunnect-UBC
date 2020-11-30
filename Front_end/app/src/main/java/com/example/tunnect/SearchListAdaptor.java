package com.example.tunnect;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchListAdaptor extends RecyclerView.Adapter<SearchListAdaptor.ViewHolder> {
    private Context context;
    private List<Song> songs;
    private String user_id;
    private LocalBroadcastManager broadcaster;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView songTitle;
        public TextView artist;
        public Button add_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.artist);
            add_btn = itemView.findViewById(R.id.add_btn);
        }
    }

    public SearchListAdaptor(Context context, List<Song> songs, String USER_ID) {
        this.context = context;
        this.songs = songs;
        this.user_id = USER_ID;
        broadcaster = LocalBroadcastManager.getInstance(context);
    }

    @Override
    public SearchListAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_layout, parent, false);

        SearchListAdaptor.ViewHolder viewHolder = new SearchListAdaptor.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songTitle.setText(song.getName());
        holder.artist.setText(song.getArtist());

        holder.add_btn.setOnClickListener(view -> {
            addSong(song.getId(), holder);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    /*
    * Adds a song to the users profile
    */
    private void addSong(String song, ViewHolder holder) {
        Intent intent = new Intent("added_song");
        intent.putExtra("ADDED_SONG", song);
        holder.add_btn.setText("Added");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
