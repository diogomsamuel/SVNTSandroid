package com.diogosamuel.svnts;

public class Exercise {
    private long id;
    private String name;
    private String muscleGroup;
    private String instructions;

    public Exercise(long id, String name, String muscleGroup, String instructions) {
        this.id = id;
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.instructions = instructions;
    }

    // Getters
    public long getId() { return id; }
    public String getName() { return name; }
    public String getMuscleGroup() { return muscleGroup; }
    public String getInstructions() { return instructions; }
} 