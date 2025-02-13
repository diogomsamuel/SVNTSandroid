package com.diogosamuel.svnts;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private double height;
    private double weight;
    private List<WeightEntry> weightHistory;
    private List<BodyMeasurement> measurements;
    private List<PersonalRecord> personalRecords;
    private String profileImagePath;

    // Construtor
    public User(String email, String firstName, String lastName, String gender, double height, double weight) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.weightHistory = new ArrayList<>();
        this.measurements = new ArrayList<>();
        this.personalRecords = new ArrayList<>();
        
        // Add initial weight entry
        weightHistory.add(new WeightEntry(weight, System.currentTimeMillis()));
    }

    // Getters e Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }

    // Método para calcular BMI
    public double calculateBMI() {
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    // Método para calcular calorias de manutenção
    public int calculateMaintenanceCalories() {
        double bmr;
        if (gender.equalsIgnoreCase("M")) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * 25);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * 25);
        }
        return (int) (bmr * 1.55); // Assumindo nível de atividade moderado
    }

    public void addWeightEntry(double weight) {
        weightHistory.add(new WeightEntry(weight, System.currentTimeMillis()));
        this.weight = weight;
    }

    public void addMeasurement(BodyMeasurement measurement) {
        measurements.add(measurement);
    }

    public void addPersonalRecord(PersonalRecord pr) {
        personalRecords.add(pr);
    }

    public List<WeightEntry> getWeightHistory() {
        return weightHistory;
    }

    public List<BodyMeasurement> getMeasurements() {
        return measurements;
    }

    public List<PersonalRecord> getPersonalRecords() {
        return personalRecords;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String path) {
        this.profileImagePath = path;
    }

    // Inner classes for user-related data
    public static class WeightEntry {
        private double weight;
        private long timestamp;

        public WeightEntry(double weight, long timestamp) {
            this.weight = weight;
            this.timestamp = timestamp;
        }

        public double getWeight() { return weight; }
        public long getTimestamp() { return timestamp; }
    }

    public static class BodyMeasurement {
        private String type; // e.g., "chest", "biceps", "waist"
        private double value;
        private long timestamp;

        public BodyMeasurement(String type, double value, long timestamp) {
            this.type = type;
            this.value = value;
            this.timestamp = timestamp;
        }

        public String getType() { return type; }
        public double getValue() { return value; }
        public long getTimestamp() { return timestamp; }
    }

    public static class PersonalRecord {
        private String exerciseName;
        private double weight;
        private int reps;
        private long timestamp;

        public PersonalRecord(String exerciseName, double weight, int reps, long timestamp) {
            this.exerciseName = exerciseName;
            this.weight = weight;
            this.reps = reps;
            this.timestamp = timestamp;
        }

        public String getExerciseName() { return exerciseName; }
        public double getWeight() { return weight; }
        public int getReps() { return reps; }
        public long getTimestamp() { return timestamp; }
    }
} 