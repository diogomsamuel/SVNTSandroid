package com.diogosamuel.svnts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.google.android.material.snackbar.Snackbar;

public class WorkoutPlanActivity extends AppCompatActivity {
    private TextView planNameText;
    private TextView planDescriptionText;
    private RecyclerView workoutList;
    private TrainingPlan currentPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

        // Enable up navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Workout Plan");
        }

        initializeViews();
        loadPlanData();
        setupWorkoutList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        planNameText = findViewById(R.id.plan_name);
        planDescriptionText = findViewById(R.id.plan_description);
        workoutList = findViewById(R.id.workout_list);
        workoutList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadPlanData() {
        long planId = getIntent().getLongExtra("plan_id", -1);
        if (planId == -1) {
            finish();
            return;
        }

        // TODO: In a real app, load from database
        // For now, just recreate the plan data
        currentPlan = findPlanById(planId);
        if (currentPlan == null) {
            finish();
            return;
        }

        planNameText.setText(currentPlan.getName());
        planDescriptionText.setText(currentPlan.getDescription());
    }

    private void setupWorkoutList() {
        if (currentPlan != null && currentPlan.getWorkouts() != null) {
            WorkoutAdapter adapter = new WorkoutAdapter(currentPlan.getWorkouts(), this::onWorkoutSelected);
            workoutList.setAdapter(adapter);
        }
    }

    private void onWorkoutSelected(Workout workout) {
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("workout_id", workout.getId());
        startActivity(intent);
    }

    // Helper method to find plan by ID (this should be in a repository/database in a real app)
    private TrainingPlan findPlanById(long planId) {
        // Recreate the plans (in a real app, this would come from a database)
        if (planId == 1) {
            List<Workout> workouts = List.of(
                new Workout(1, "Push Day", "Chest, shoulders, and triceps"),
                new Workout(2, "Pull Day", "Back and biceps"),
                new Workout(3, "Legs Day", "Quadriceps, hamstrings, and calves")
            );
            return new TrainingPlan(1, "Hypertrophy Program", 
                "Build muscle with this science-based PPL routine", 
                "Intermediate", 8, false, workouts);
        }
        // Add other plans as needed
        return null;
    }

    private void showTipOfTheDay() {
        String[] tips = {
            "Remember to stay hydrated during your workout! 💧",
            "Focus on form rather than weight for better results 🎯",
            "Get enough rest between workouts for optimal recovery 😴",
            "Track your progress to stay motivated! 📈",
            "Proper warm-up reduces injury risk! 🌡️"
        };
        
        Snackbar.make(workoutList, 
            "Tip: " + tips[(int)(Math.random() * tips.length)], 
            Snackbar.LENGTH_LONG)
            .setAction("Got it", null)
            .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTipOfTheDay();
    }
} 