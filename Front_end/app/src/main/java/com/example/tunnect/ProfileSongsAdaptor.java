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

public class ProfileSongsAdaptor extends RecyclerView.Adapter<ProfileSongsAdaptor.ViewHolder> {
    private Context context;
    private List<Song> songs;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView songTitle;
        public TextView artist;
        public Button dlt_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.artist);
            dlt_btn = itemView.findViewById(R.id.dlt_btn);
        }
    }

    public ProfileSongsAdaptor(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @Override
    public ProfileSongsAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.delete_layout, parent, false);

        ProfileSongsAdaptor.ViewHolder viewHolder = new ProfileSongsAdaptor.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songTitle.setText(song.getName());
        holder.artist.setText(song.getArtist());

        holder.dlt_btn.setOnClickListener(view -> {
            deleteSong(song.getId(), holder);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    /*
    * Adds a song to the users profile
    */
    private void deleteSong(String song, ViewHolder holder) {
        Intent intent = new Intent("edited_song");
        intent.putExtra("DELETED_SONG", song);
        intent.putExtra("ADDING", "False");
        holder.dlt_btn.setText("Deleted");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
