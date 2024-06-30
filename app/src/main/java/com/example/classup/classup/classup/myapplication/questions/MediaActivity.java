package com.example.classup.classup.classup.myapplication.questions;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classup.classup.classup.myapplication.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MediaActivity extends AppCompatActivity {
    private VideoView videoView;
    private RecyclerView mediaRecyclerView;
    private MediaAdapter mediaAdapter;
    private MediaPlayer musicPlayer;
    private ImageView arrowBack;
    private ImageView musicPlayButton;
    private ImageView musicPauseButton;
    private ImageView musicReloadButton;
    private SeekBar musicSeekBar;
    private TextView musicCurrentTimeTextView;
    private TextView musicTotalTimeTextView;
    private Handler musicHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        videoView = findViewById(R.id.video_view);
        mediaRecyclerView = findViewById(R.id.recycler_view_media);
        arrowBack = findViewById(R.id.arrowBack);

        musicSeekBar = findViewById(R.id.musicSeekBar);
        musicCurrentTimeTextView = findViewById(R.id.musicCurrentTime);
        musicTotalTimeTextView = findViewById(R.id.musicTotalTime);
        musicPlayButton = findViewById(R.id.musicPlay);
        musicPauseButton = findViewById(R.id.musicPause);
        musicReloadButton = findViewById(R.id.musicReload);

        // Get the answers from the Intent
        int[] answers = getIntent().getIntArrayExtra("ANSWERS");
        if (answers == null || answers.length != 5) {
            throw new IllegalArgumentException("ANSWERS array must be provided and have exactly 5 elements");
        }

        int moodScore = calculateMoodScore(answers);
        List<Media> mediaList = getMediaListForState(moodScore);

        // Sort the media list based on mood score
        mediaList.sort(Comparator.comparingInt(this::getMoodScore).reversed());

        // Show only the top 3 videos
        List<Media> topMediaList = mediaList.subList(0, Math.min(3, mediaList.size()));

        // Log the media list size for debugging
        Log.d("MediaActivity", "Top media list size: " + topMediaList.size());

        mediaAdapter = new MediaAdapter(topMediaList, media -> {
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(media.getVideoUrl()));
            videoView.start();
            videoView.setOnCompletionListener(mp -> videoView.start());  // Loop the video

            if (musicPlayer != null) {
                musicPlayer.stop();
                musicPlayer.release();
            }

            musicPlayer = MediaPlayer.create(MediaActivity.this, media.getMusicResId());
            musicPlayer.setLooping(true);
            musicPlayer.start();

            int musicTotalDuration = musicPlayer.getDuration();
            musicTotalTimeTextView.setText(formatTime(musicTotalDuration));
            musicSeekBar.setMax(musicTotalDuration);

            // Initialize SeekBar
            musicSeekBar.setProgress(0);
            musicCurrentTimeTextView.setText(formatTime(0));

            musicPlayButton.setOnClickListener(v -> {
                if (musicPlayer != null) {
                    musicPlayer.start();
                    videoView.start();  // Ensure video is playing as well
                    updateMusicControls();
                    updateSeekBar();
                }
            });

            musicPauseButton.setOnClickListener(v -> {
                if (musicPlayer != null) {
                    musicPlayer.pause();
                    videoView.pause();  // Pause the video along with music
                    updateMusicControls();
                }
            });

            musicReloadButton.setOnClickListener(v -> {
                if (musicPlayer != null) {
                    musicPlayer.seekTo(0);
                    musicPlayer.start();
                    videoView.seekTo(0);  // Restart the video from the beginning
                    videoView.start();
                    updateMusicControls();
                    updateSeekBar();
                }
            });

            musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && musicPlayer != null) {
                        musicPlayer.seekTo(progress);
                        musicCurrentTimeTextView.setText(formatTime(progress));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            musicPlayer.setOnPreparedListener(mp -> {
                updateMusicControls();
                updateSeekBar();
            });

            musicPlayer.setOnCompletionListener(mp -> {
                musicPlayer.seekTo(0);
                musicPlayer.start();
                videoView.seekTo(0);  // Ensure video restarts as well
                videoView.start();
                updateMusicControls();
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(MediaActivity.this, "Error playing video", Toast.LENGTH_SHORT).show();
                return true;
            });

        });

        mediaRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mediaRecyclerView.setAdapter(mediaAdapter);

        arrowBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure that the music player is released when the activity is destroyed
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
        }
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updateMusicControls() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayButton.setVisibility(View.GONE);
            musicPauseButton.setVisibility(View.VISIBLE);
        } else {
            musicPlayButton.setVisibility(View.VISIBLE);
            musicPauseButton.setVisibility(View.GONE);
        }
    }

    private void updateSeekBar() {
        musicHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (musicPlayer != null && musicPlayer.isPlaying()) {
                    musicSeekBar.setProgress(musicPlayer.getCurrentPosition());
                    musicCurrentTimeTextView.setText(formatTime(musicPlayer.getCurrentPosition()));
                    musicHandler.postDelayed(this, 1000);
                }
            }
        }, 0);
    }

    // Calculate the mood score from the answers to the 5 questions
    private int calculateMoodScore(int[] answers) {
        int totalScore = 0;
        for (int answer : answers) {
            totalScore += answer;
        }

        // Define mood score based on total score. Adjust ranges as needed.
        if (totalScore <= 5) {
            return 0;  // Sad mood
        } else if (totalScore <= 10) {
            return 1;  // Neutral mood
        } else if (totalScore <= 15) {
            return 2;  // Happy mood
        } else {
            return 3;  // Very happy mood
        }
    }

    private List<Media> getMediaListForState(int moodScore) {
        List<Media> mediaList = new ArrayList<>();

        // Add media based on the mood score
        switch (moodScore) {
            case 0:  // Sad mood
                mediaList.add(new Media("Rain", R.raw.rainc, "android.resource://" + getPackageName() + "/" + R.raw.rain));
                mediaList.add(new Media("Sea", R.raw.seac, "android.resource://" + getPackageName() + "/" + R.raw.sea));
                mediaList.add(new Media("Study", R.raw.studyc, "android.resource://" + getPackageName() + "/" + R.raw.study));
                break;
            case 1:  // Neutral mood
                mediaList.add(new Media("Birds", R.raw.birdsc, "android.resource://" + getPackageName() + "/" + R.raw.birds));
                mediaList.add(new Media("Flowers", R.raw.flowersc, "android.resource://" + getPackageName() + "/" + R.raw.flowers));
                mediaList.add(new Media("Planet", R.raw.birdsc, "android.resource://" + getPackageName() + "/" + R.raw.planet));
                break;
            case 2:  // Happy mood
                mediaList.add(new Media("Mountains", R.raw.mountainsc, "android.resource://" + getPackageName() + "/" + R.raw.mountains));
                mediaList.add(new Media("Rainbow", R.raw.rainbowc, "android.resource://" + getPackageName() + "/" + R.raw.rainbow));
                mediaList.add(new Media("God", R.raw.godc, "android.resource://" + getPackageName() + "/" + R.raw.god));
                break;
            case 3:  // Very happy mood
                mediaList.add(new Media("Mountains", R.raw.mountainsc, "android.resource://" + getPackageName() + "/" + R.raw.mountains));
                mediaList.add(new Media("Rainbow", R.raw.rainbowc, "android.resource://" + getPackageName() + "/" + R.raw.rainbow));
                mediaList.add(new Media("God", R.raw.godc, "android.resource://" + getPackageName() + "/" + R.raw.god));
                break;
        }

        // Log the media list for debugging
        for (Media media : mediaList) {
            Log.d("MediaActivity", "Media: " + media.getTitle() + ", Video URL: " + media.getVideoUrl());
        }

        return mediaList;
    }

    private int getMoodScore(Media media) {
        // Define mood score based on the media title
        switch (media.getTitle()) {
            case "Mountains": return 5;
            case "Birds": return 4;
            case "Flowers": return 3;
            case "Sea": return 2;
            case "Study": return 1;
            case "Rain": return 0;
            case "Rainbow": return 5;
            case "God": return 4;
            case "Planet": return 2;
            default: return 0;
        }
    }
}
