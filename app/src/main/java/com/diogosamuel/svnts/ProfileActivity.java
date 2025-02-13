package com.diogosamuel.svnts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView nameText;
    private TextView bmiText;
    private TextView caloriesText;
    private TextView currentWeightText;
    private Button updateWeightButton;
    private RecyclerView progressList;
    private Button logoutButton;
    private DatabaseHelper dbHelper;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Enable up navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }

        initializeViews();
        loadUserData();
        setupProgressList();
        setupButtons();
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
        nameText = findViewById(R.id.profile_name);
        bmiText = findViewById(R.id.profile_bmi);
        caloriesText = findViewById(R.id.profile_calories);
        currentWeightText = findViewById(R.id.current_weight);
        updateWeightButton = findViewById(R.id.update_weight_button);
        progressList = findViewById(R.id.progress_list);
        logoutButton = findViewById(R.id.logout_button);
        dbHelper = new DatabaseHelper(this);
    }

    private void loadUserData() {
        try {
            // Get logged in user ID from session
            SessionManager sessionManager = new SessionManager(this);
            long userId = sessionManager.getUserId();
            
            // Get user data from database
            dbHelper = new DatabaseHelper(this);
            currentUser = dbHelper.getUser(userId);
            
            if (currentUser == null) {
                Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show();
                sessionManager.logout();
                return;
            }
            
            updateUserInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateUserInfo() {
        nameText.setText(String.format("%s %s", currentUser.getFirstName(), currentUser.getLastName()));
        bmiText.setText(String.format("BMI: %.1f", currentUser.calculateBMI()));
        caloriesText.setText(String.format("Daily Calories: %d kcal", currentUser.calculateMaintenanceCalories()));
        currentWeightText.setText(String.format("Current Weight: %.1f kg", currentUser.getWeight()));
    }

    private void setupProgressList() {
        progressList.setLayoutManager(new LinearLayoutManager(this));
        
        // Temporary empty state message
        View emptyState = getLayoutInflater().inflate(R.layout.layout_coming_soon, 
            (ViewGroup) progressList.getParent());
        TextView messageView = emptyState.findViewById(R.id.message);
        messageView.setText("Progress tracking will be available in the next update!\n\n" +
                           "Features coming soon:\n" +
                           "• Detailed progress graphs\n" +
                           "• Personal records tracking\n" +
                           "• Progress sharing\n" +
                           "• Export workout data");
        
        // Setup feedback button
        Button feedbackButton = emptyState.findViewById(R.id.feedback_button);
        feedbackButton.setOnClickListener(v -> showFeedbackDialog());
        
        // Hide RecyclerView and show empty state with animation
        progressList.setVisibility(View.GONE);
        View container = emptyState.findViewById(R.id.coming_soon_container);
        ImageView icon = emptyState.findViewById(R.id.coming_soon_icon);
        
        // Fade in animation
        container.setAlpha(0f);
        container.setVisibility(View.VISIBLE);
        container.animate()
                .alpha(1f)
                .setDuration(500)
                .start();
        
        // Rotate icon animation
        icon.animate()
                .rotation(360f)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_feedback, null);
        EditText feedbackInput = view.findViewById(R.id.feedback_input);
        
        builder.setView(view)
               .setTitle("Send Feedback")
               .setPositiveButton("Send", (dialog, which) -> {
                   String feedback = feedbackInput.getText().toString();
                   if (!feedback.isEmpty()) {
                       // TODO: Send feedback to backend
                       Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void setupButtons() {
        updateWeightButton = findViewById(R.id.update_weight_button);
        logoutButton = findViewById(R.id.logout_button);

        updateWeightButton.setOnClickListener(v -> showUpdateWeightDialog());
        logoutButton.setOnClickListener(v -> logout());
    }

    private void showUpdateWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_update_weight, null);
        EditText weightInput = view.findViewById(R.id.weight_input);
        
        builder.setView(view)
               .setTitle("Update Weight")
               .setPositiveButton("Update", (dialog, which) -> {
                   try {
                       double newWeight = Double.parseDouble(weightInput.getText().toString());
                       updateWeight(newWeight);
                   } catch (NumberFormatException e) {
                       Toast.makeText(this, "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void updateWeight(double newWeight) {
        // TODO: Update in database
        currentUser = new User(
            currentUser.getEmail(),
            currentUser.getFirstName(),
            currentUser.getLastName(),
            currentUser.getGender(),
            currentUser.getHeight(),
            newWeight
        );
        updateUserInfo();
        Toast.makeText(this, "Weight updated successfully", Toast.LENGTH_SHORT).show();
        
        // Refresh progress list as weight history might have changed
        setupProgressList();
    }

    private void logout() {
        // Clear user session
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.logout();
        
        // Clear activity stack and go to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 