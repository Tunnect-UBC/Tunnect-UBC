package com.example.tunnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchListAdaptor extends RecyclerView.Adapter<SearchListAdaptor.ViewHolder> {
    private Context context;
    private List<Song> songs;

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

    public SearchListAdaptor(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
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
        Intent intent = new Intent("edited_song");
        intent.putExtra("ADDED_SONG", song);
        intent.putExtra("ADDING", "True");
        holder.add_btn.setText("Added");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
