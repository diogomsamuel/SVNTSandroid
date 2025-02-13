package com.diogosamuel.svnts;

import java.util.ArrayList;
import java.util.List;

public class Achievement {
    private String title;
    private String description;
    private boolean unlocked;
    private int progress;
    private int target;
    
    public Achievement(String title, String description, int target) {
        this.title = title;
        this.description = description;
        this.target = target;
        this.progress = 0;
        this.unlocked = false;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isUnlocked() { return unlocked; }
    public int getProgress() { return progress; }
    public int getTarget() { return target; }

    public void setProgress(int progress) {
        this.progress = progress;
        this.unlocked = progress >= target;
    }
    
    public static List<Achievement> getDefaultAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement(
            "First Step",
            "Complete your first workout",
            1
        ));
        achievements.add(new Achievement(
            "Consistency is Key",
            "Complete workouts 3 days in a row",
            3
        ));
        achievements.add(new Achievement(
            "Getting Stronger",
            "Complete 10 workouts",
            10
        ));
        return achievements;
    }
} 