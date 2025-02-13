package com.diogosamuel.svnts;

public class Guide {
    private String title;
    private String author;
    private String thumbnailUrl;
    private String videoUrl;
    private boolean isPremium;

    public Guide(String title, String author, String thumbnailUrl, String videoUrl, boolean isPremium) {
        this.title = title;
        this.author = author;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.isPremium = isPremium;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getVideoUrl() { return videoUrl; }
    public boolean isPremium() { return isPremium; }
} 