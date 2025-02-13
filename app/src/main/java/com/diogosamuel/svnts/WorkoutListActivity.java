package com.diogosamuel.svnts;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.view.View;
import android.view.MenuItem;

public class WorkoutListActivity extends AppCompatActivity {
    private RecyclerView planList;
    private Button createWorkoutButton;
    private TrainingPlanAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        planList = findViewById(R.id.plan_list);
        createWorkoutButton = findViewById(R.id.create_workout_button);
        dbHelper = new DatabaseHelper(this);

        setupTrainingPlans();
        setupCreateButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgress(); // Atualizar progresso quando voltar do treino
    }

    private void setupTrainingPlans() {
        List<TrainingPlan> plans = new ArrayList<>();
        
        // Hypertrophy Program
        List<Workout> hypertrophyWorkouts = new ArrayList<>();
        hypertrophyWorkouts.add(new Workout(1, "Push Day", "Chest, shoulders, and triceps"));
        hypertrophyWorkouts.add(new Workout(2, "Pull Day", "Back and biceps"));
        hypertrophyWorkouts.add(new Workout(3, "Legs Day", "Quadriceps, hamstrings, and calves"));
        
        plans.add(new TrainingPlan(
            1,
            "Hypertrophy Program",
            "Build muscle with this science-based PPL routine",
            "Intermediate",
            8,
            false,
            hypertrophyWorkouts
        ));
        
        // Strength Program
        List<Workout> strengthWorkouts = new ArrayList<>();
        strengthWorkouts.add(new Workout(4, "Upper Body A", "Chest and back focus"));
        strengthWorkouts.add(new Workout(5, "Lower Body A", "Quad and hamstring focus"));
        strengthWorkouts.add(new Workout(6, "Upper Body B", "Shoulder and arm focus"));
        strengthWorkouts.add(new Workout(7, "Lower Body B", "Posterior chain focus"));
        
        plans.add(new TrainingPlan(
            2,
            "Strength Program",
            "Get stronger with this upper/lower split",
            "Advanced",
            12,
            true,
            strengthWorkouts
        ));
        
        // Beginner Program
        List<Workout> beginnerWorkouts = new ArrayList<>();
        beginnerWorkouts.add(new Workout(8, "Full Body A", "Basic compound movements"));
        beginnerWorkouts.add(new Workout(9, "Full Body B", "Alternative compound movements"));
        beginnerWorkouts.add(new Workout(10, "Full Body C", "Progressive overload focus"));
        
        plans.add(new TrainingPlan(
            3,
            "Beginner Program",
            "Perfect for those starting their fitness journey",
            "Beginner",
            6,
            false,
            beginnerWorkouts
        ));

        adapter = new TrainingPlanAdapter(plans, this::onPlanSelected);
        planList.setLayoutManager(new LinearLayoutManager(this));
        planList.setAdapter(adapter);
        
        // Add beta notice
        Snackbar.make(planList, 
            "Beta version: More workout plans will be added in future updates!", 
            Snackbar.LENGTH_LONG)
            .setAction("OK", v -> {})
            .show();
    }

    private void setupCreateButton() {
        createWorkoutButton.setOnClickListener(v -> {
            // TODO: Implement workout creation
            // startActivity(new Intent(this, CreateWorkoutActivity.class));
        });
    }

    private void onPlanSelected(TrainingPlan plan) {
        if (plan.isPremium()) {
            // Show premium message
            Toast.makeText(this, 
                "This program is available only for premium members", 
                Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, WorkoutPlanActivity.class);
        intent.putExtra("plan_id", plan.getId());
        startActivity(intent);
    }

    private void updateProgress() {
        for (TrainingPlan plan : adapter.getPlans()) {
            updateProgress(plan);
        }
    }

    private void updateProgress(TrainingPlan plan) {
        int completedWorkouts = dbHelper.getCompletedWorkoutsCount(plan.getId());
        int totalWorkouts = plan.getWorkouts().size() * plan.getWeeks();
        
        // Atualizar texto de progresso com porcentagem
        String progress = String.format("%d/%d workouts (%d%%)", 
            completedWorkouts, totalWorkouts,
            (int)((completedWorkouts / (float)totalWorkouts) * 100));
        
        // Adicionar estimativa de conclusão
        if (completedWorkouts > 0) {
            int daysLeft = estimateRemainingDays(plan, completedWorkouts);
            progress += String.format("\nEstimated completion: %d days", daysLeft);
        }
        
        TextView progressText = findViewById(R.id.progress_text);
        progressText.setText(progress);
        
        // Atualizar barra de progresso
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        int progressPercentage = (int)((completedWorkouts / (float)totalWorkouts) * 100);
        progressBar.setProgress(progressPercentage);
        
        // Atualizar streak com ícone
        int streak = dbHelper.getCurrentStreak(1); // TODO: Get actual user ID
        if (streak > 0) {
            TextView streakText = findViewById(R.id.streak_text);
            streakText.setText(String.format("%d 🔥", streak));
            streakText.setVisibility(View.VISIBLE);
        }
    }

    private int estimateRemainingDays(TrainingPlan plan, int completedWorkouts) {
        long firstWorkoutDate = dbHelper.getFirstWorkoutDate(plan.getId());
        long now = System.currentTimeMillis();
        
        // Se não houver treinos ou se começou hoje
        if (firstWorkoutDate == now) {
            return plan.getWorkouts().size() * plan.getWeeks(); // Retorna total de treinos como estimativa
        }
        
        long daysElapsed = (now - firstWorkoutDate) / (24 * 60 * 60 * 1000);
        if (daysElapsed == 0) return 0;
        
        float workoutsPerDay = completedWorkouts / (float)daysElapsed;
        if (workoutsPerDay == 0) return 0; // Evitar divisão por zero
        
        int remainingWorkouts = (plan.getWorkouts().size() * plan.getWeeks()) - completedWorkouts;
        return Math.max(1, (int)(remainingWorkouts / workoutsPerDay)); // Garantir pelo menos 1 dia
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Quando o usuário clica no botão de voltar da action bar
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Não é necessário fazer nada aqui pois a MainActivity
        // vai atualizar o BottomNavigationView no onResume
    }
} 