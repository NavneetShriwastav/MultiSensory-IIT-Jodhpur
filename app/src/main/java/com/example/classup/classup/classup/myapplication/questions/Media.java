package com.example.classup.classup.classup.myapplication.questions;

public class Media {
    private final String title;
    private final int musicResId;
    private final String videoUrl;

    public Media(String title, int musicResId, String videoUrl) {
        this.title = title;
        this.musicResId = musicResId;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getMusicResId() {
        return musicResId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
