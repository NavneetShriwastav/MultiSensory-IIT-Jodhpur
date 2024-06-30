package com.example.classup.classup.classup.myapplication.questions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classup.classup.classup.myapplication.R;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private final List<Media> mediaList;
    private final OnMediaClickListener onMediaClickListener;

    public MediaAdapter(List<Media> mediaList, OnMediaClickListener onMediaClickListener) {
        this.mediaList = mediaList;
        this.onMediaClickListener = onMediaClickListener;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Media media = mediaList.get(position);
        holder.mediaTitle.setText(media.getTitle());
        holder.playButton.setOnClickListener(v -> onMediaClickListener.onMediaClick(media));
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public interface OnMediaClickListener {
        void onMediaClick(Media media);
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        public TextView mediaTitle;
        public Button playButton;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaTitle = itemView.findViewById(R.id.media_title);
            playButton = itemView.findViewById(R.id.play_button);
        }
    }
}
