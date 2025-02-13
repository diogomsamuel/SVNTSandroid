package com.diogosamuel.svnts;

import java.util.List;

public class Workout {
    private long id;
    private String name;
    private String description;
    private List<Exercise> exercises;

    public Workout(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters
    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Exercise> getExercises() { return exercises; }

    // Setter for exercises list
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
} 