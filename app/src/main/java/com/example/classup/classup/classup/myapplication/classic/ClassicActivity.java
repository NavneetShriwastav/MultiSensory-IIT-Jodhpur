package com.example.classup.classup.classup.myapplication.classic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.classup.classup.classup.myapplication.R;
import com.example.classup.classup.classup.myapplication.RelaxActivity;
import com.example.classup.classup.classup.myapplication.relax.CardAdapter;
import com.example.classup.classup.classup.myapplication.relax.NatureActivity;

public class ClassicActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImageView backArrow;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ImageView playButton, pauseButton, reloadButton;
    private TextView currentTimeTextView, totalTimeTextView;

    private Handler handler = new Handler();
    private Runnable updateSeekBarRunnable;

    private int[] songResources = {
            R.raw.guitar,  // First video -> guitar
            R.raw.violin,  // Second video -> violin
            R.raw.dj,      // Third video -> dj
            R.raw.piano,   // Fourth video -> piano
            R.raw.flute    // Fifth video -> flute
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nature);

        viewPager = findViewById(R.id.viewPager);
        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.play);
        pauseButton = findViewById(R.id.pause);
        reloadButton = findViewById(R.id.reload);
        currentTimeTextView = findViewById(R.id.currentTime);
        totalTimeTextView = findViewById(R.id.totalTime);
        backArrow = findViewById(R.id.arrowBack);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassicActivity.this, RelaxActivity.class);
                startActivity(intent);
            }
        });

        String[] cardContents = {"Card 1", "Card 2", "Card 3", "Card 4", "Card 5"};
        String[] videoUrls = {
                "https://videos.pexels.com/video-files/5659592/5659592-uhd_1440_2732_25fps.mp4",
                "https://videos.pexels.com/video-files/7097489/7097489-uhd_1440_2732_25fps.mp4",
                "https://cdn.pixabay.com/video/2022/07/24/125366-733383427_large.mp4",
                "https://cdn.pixabay.com/video/2023/02/10/150089-797862843_large.mp4",
                "https://videos.pexels.com/video-files/7580053/7580053-hd_1080_1920_25fps.mp4"
        };
        CardAdapter adapter = new CardAdapter(cardContents, videoUrls);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                playSong(position);
            }
        });

        playButton.setOnClickListener(view -> playSong());
        pauseButton.setOnClickListener(view -> pauseSong());
        reloadButton.setOnClickListener(view -> reloadSong());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    updateCurrentTime();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });
    }

    private void playSong(int songIndex) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, songResources[songIndex]);
        mediaPlayer.setLooping(true); // Set looping to true
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        updateTotalTime();
        startSeekBarUpdate();
    }

    private void playSong() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            startSeekBarUpdate();
        }
    }

    private void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void reloadSong() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            startSeekBarUpdate();
        }
    }

    private void startSeekBarUpdate() {
        updateSeekBarRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    updateCurrentTime();
                    if (mediaPlayer.isPlaying()) {
                        handler.postDelayed(this, 50); // Update every 50 milliseconds for smoother sliding
                    }
                }
            }
        };
        handler.post(updateSeekBarRunnable);
    }

    private void updateCurrentTime() {
        if (mediaPlayer != null) {
            int currentTime = mediaPlayer.getCurrentPosition();
            currentTimeTextView.setText(formatTime(currentTime));
        }
    }

    private void updateTotalTime() {
        if (mediaPlayer != null) {
            int totalTime = mediaPlayer.getDuration();
            totalTimeTextView.setText(formatTime(totalTime));
        }
    }

    private String formatTime(int timeInMillis) {
        int minutes = timeInMillis / 1000 / 60;
        int seconds = (timeInMillis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBarRunnable);
        super.onDestroy();
    }
}
