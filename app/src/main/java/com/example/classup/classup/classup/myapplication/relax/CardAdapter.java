package com.example.classup.classup.classup.myapplication.relax;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classup.classup.classup.myapplication.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private String[] cardContents;
    private String[] videoUrls;

    public CardAdapter(String[] cardContents, String[] videoUrls) {
        this.cardContents = cardContents;
        this.videoUrls = videoUrls;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview1, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(cardContents[position], videoUrls[position]);
    }

    @Override
    public int getItemCount() {
        return cardContents.length;
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private VideoView videoView;
        private String videoUrl; // Store video URL

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            videoView = itemView.findViewById(R.id.videoView);
        }

        public void bind(String content, String videoUrl) {
            this.videoUrl = videoUrl; // Store video URL
            textView.setText(content);
            loadVideo();
        }

        public void loadVideo() {
            videoView.setVideoURI(Uri.parse(videoUrl));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start(); // Start video when it is ready for playback
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.start(); // Loop the video
                }
            });
        }
    }
}
