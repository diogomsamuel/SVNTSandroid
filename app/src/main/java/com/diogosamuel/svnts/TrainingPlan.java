package com.diogosamuel.svnts;

import java.util.List;

public class TrainingPlan {
    private long id;
    private String name;
    private String description;
    private String difficulty;
    private int weeks;
    private boolean isPremium;
    private List<Workout> workouts;

    public TrainingPlan(long id, String name, String description, String difficulty, 
                       int weeks, boolean isPremium, List<Workout> workouts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.weeks = weeks;
        this.isPremium = isPremium;
        this.workouts = workouts;
    }

    // Getters
    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDifficulty() { return difficulty; }
    public int getWeeks() { return weeks; }
    public String getWeeksCount() { return String.valueOf(weeks); }
    public boolean isPremium() { return isPremium; }
    public List<Workout> getWorkouts() { return workouts; }
} 