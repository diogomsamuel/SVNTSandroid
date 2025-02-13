package com.diogosamuel.svnts;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.os.SystemClock;
import androidx.core.content.ContextCompat;

public class WorkoutActivity extends AppCompatActivity {
    private Chronometer workoutTimer;
    private TextView restTimerText;
    private Button startRestTimerButton;
    private RecyclerView exerciseList;
    private Button finishWorkoutButton;
    private DatabaseHelper dbHelper;
    private CountDownTimer restTimer;
    private boolean isRestTimerRunning = false;
    private static final int DEFAULT_REST_TIME = 90; // seconds
    private ProgressBar restTimerProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Enable up navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Workout"); // Or get workout name from intent
        }

        initializeViews();
        setupExerciseList();
        setupTimers();
        setupButtons();
    }

    private void initializeViews() {
        workoutTimer = findViewById(R.id.workout_timer);
        restTimerText = findViewById(R.id.rest_timer);
        startRestTimerButton = findViewById(R.id.start_rest_timer_button);
        exerciseList = findViewById(R.id.exercise_list);
        finishWorkoutButton = findViewById(R.id.finish_workout_button);
        restTimerProgress = findViewById(R.id.rest_timer_progress);
        
        // Setup back button
        findViewById(R.id.back_button).setOnClickListener(v -> {
            if (workoutInProgress()) {
                new AlertDialog.Builder(this)
                    .setTitle("Exit Workout?")
                    .setMessage("Are you sure you want to exit? Your progress will be saved.")
                    .setPositiveButton("Exit", (dialog, which) -> finish())
                    .setNegativeButton("Continue", null)
                    .show();
            } else {
                finish();
            }
        });
        
        dbHelper = new DatabaseHelper(this);
    }

    private void setupExerciseList() {
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
        // Get exercises for this workout
        long workoutId = getIntent().getLongExtra("workout_id", -1);
        List<Exercise> exercises = dbHelper.getExercisesForWorkout(workoutId);
        ExerciseAdapter adapter = new ExerciseAdapter(exercises, this::onSetCompleted);
        exerciseList.setAdapter(adapter);
    }

    private void setupTimers() {
        workoutTimer.setBase(SystemClock.elapsedRealtime());
        workoutTimer.start();
        
        restTimer = new CountDownTimer(DEFAULT_REST_TIME * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateRestTimer(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                restTimerText.setText("Rest Complete!");
                isRestTimerRunning = false;
                startRestTimerButton.setText("Start Rest Timer");
                restTimerText.setTextColor(ContextCompat.getColor(WorkoutActivity.this, R.color.text_primary));
            }
        };
    }

    private void setupButtons() {
        startRestTimerButton.setOnClickListener(v -> toggleRestTimer());
        finishWorkoutButton.setOnClickListener(v -> confirmFinishWorkout());
    }

    private void toggleRestTimer() {
        if (isRestTimerRunning) {
            restTimer.cancel();
            isRestTimerRunning = false;
            startRestTimerButton.setText("Start Rest Timer");
        } else {
            restTimer.start();
            isRestTimerRunning = true;
            startRestTimerButton.setText("Cancel Rest Timer");
        }
    }

    private void onSetCompleted(Exercise exercise, WorkoutSet set) {
        // TODO: Get actual user ID from shared preferences/session
        long userId = 1; // Temporary hardcoded value
        
        dbHelper.saveWorkoutSet(set, userId);
        
        // Check if it's a PR
        if (isPR(exercise, set)) {
            showPRDialog(exercise, set);
        }
        
        // Start rest timer automatically
        if (!isRestTimerRunning) {
            toggleRestTimer();
        }
        
        // Show info about upcoming feature
        Snackbar.make(exerciseList, 
            "Coming soon: View your previous sets and personal records!", 
            Snackbar.LENGTH_LONG)
            .setAction("Learn More", v -> showFeatureDialog())
            .show();

        animateExerciseCompletion(exerciseList);
    }

    private boolean isPR(Exercise exercise, WorkoutSet set) {
        List<WorkoutSet> history = dbHelper.getExerciseHistory(exercise.getId(), 1);
        return history.stream()
                .noneMatch(s -> s.getWeightKg() > set.getWeightKg() && s.getReps() >= set.getReps());
    }

    private void showPRDialog(Exercise exercise, WorkoutSet set) {
        new AlertDialog.Builder(this)
            .setTitle("New Personal Record! 🎉")
            .setMessage(String.format("You just set a new PR for %s: %.1f kg for %d reps!", 
                exercise.getName(), set.getWeightKg(), set.getReps()))
            .setPositiveButton("Awesome!", null)
            .show();
    }

    private void showFeatureDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Coming Soon!")
            .setMessage("In the next update, you'll be able to:\n\n" +
                       "• View your exercise history\n" +
                       "• Track personal records\n" +
                       "• Compare performance\n" +
                       "• Get AI-powered recommendations\n\n" +
                       "Stay tuned for these exciting features!")
            .setPositiveButton("Can't Wait!", null)
            .show();
    }

    private void confirmFinishWorkout() {
        new AlertDialog.Builder(this)
            .setTitle("Finish Workout")
            .setMessage("Are you sure you want to finish this workout?")
            .setPositiveButton("Yes", (dialog, which) -> finishWorkout())
            .setNegativeButton("No", null)
            .show();
    }

    private void finishWorkout() {
        // Save workout completion
        long workoutId = getIntent().getLongExtra("workout_id", -1);
        dbHelper.saveWorkoutCompletion(workoutId, 1);
        
        // Array of motivational messages
        String[] messages = {
            "Amazing workout! 💪 Keep pushing your limits!",
            "Another workout crushed! 🔥 You're getting stronger!",
            "Great job! 🌟 Consistency is key to success!",
            "Beast mode: Activated! 💪 See you next workout!",
            "You're unstoppable! 🚀 Keep up the great work!"
        };
        
        // Show completion dialog with random message
        new AlertDialog.Builder(this)
            .setTitle("Workout Complete!")
            .setMessage(messages[(int)(Math.random() * messages.length)])
            .setPositiveButton("Share Progress", (dialog, which) -> {
                // TODO: Implement sharing in future update
                Toast.makeText(this, "Sharing coming in the next update!", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Done", (dialog, which) -> finish())
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (restTimer != null) {
            restTimer.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Show confirmation if workout is in progress
            if (workoutInProgress()) {
                new AlertDialog.Builder(this)
                    .setTitle("Exit Workout?")
                    .setMessage("Are you sure you want to exit? Your progress will be saved.")
                    .setPositiveButton("Exit", (dialog, which) -> finish())
                    .setNegativeButton("Continue", null)
                    .show();
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean workoutInProgress() {
        // Check if any sets have been logged
        return dbHelper.getWorkoutHistory(1).size() > 0; // TODO: Get actual user ID
    }

    private void updateRestTimer(long millisUntilFinished) {
        int progress = (int)((millisUntilFinished / (float)(DEFAULT_REST_TIME * 1000)) * 100);
        restTimerProgress.setProgress(progress);
        
        if (millisUntilFinished < 10000) {
            restTimerText.setTextColor(ContextCompat.getColor(this, R.color.accent));
        } else {
            restTimerText.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        }
    }

    private void animateExerciseCompletion(View itemView) {
        itemView.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(150)
            .withEndAction(() -> {
                itemView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start();
            })
            .start();
    }
} 