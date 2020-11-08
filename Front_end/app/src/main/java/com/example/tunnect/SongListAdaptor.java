package com.example.tunnect;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongListAdaptor extends RecyclerView.Adapter<SongListAdaptor.ViewHolder> {
    Context context;
    List<Song> songs;
    RecyclerView songList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView songTitle, artist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.artist);
        }
    }

    public SongListAdaptor(Context context, List<Song> songs, RecyclerView songList) {
        this.context = context;
        this.songs = songs;
        this.songList = songList;
    }

    @Override
    public SongListAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.song_layout, parent, false);

        SongListAdaptor.ViewHolder viewHolder = new SongListAdaptor.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songTitle.setText(song.getName());
        holder.artist.setText(song.getArtist());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
