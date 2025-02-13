package com.diogosamuel.svnts;

public class WorkoutSet {
    private long id;
    private long exerciseId;
    private String exerciseName;
    private int setNumber;
    private double weightKg;
    private int reps;
    private String notes;

    public WorkoutSet(long exerciseId, int setNumber, double weightKg, int reps, String notes) {
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.weightKg = weightKg;
        this.reps = reps;
        this.notes = notes;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getExerciseId() { return exerciseId; }
    public int getSetNumber() { return setNumber; }
    public double getWeightKg() { return weightKg; }
    public int getReps() { return reps; }
    public String getNotes() { return notes; }
    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }
} 