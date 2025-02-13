package com.diogosamuel.svnts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView guidesRecyclerView;
    private Button upgradeButton;
    private User currentUser;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // During development, reset database to get latest schema
        dbHelper = new DatabaseHelper(this);
        dbHelper.resetDatabase();
        
        setContentView(R.layout.activity_main);

        initializeViews();
        loadUserData();
        setupBottomNavigation();
        setupGuidesRecyclerView();
        setupUpgradeButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Garantir que o item Home está selecionado quando voltamos à MainActivity
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void initializeViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        guidesRecyclerView = findViewById(R.id.guides_recycler_view);
        upgradeButton = findViewById(R.id.upgrade_button);
    }

    private void loadUserData() {
        try {
            // Get logged in user from database
            SessionManager sessionManager = new SessionManager(this);
            long userId = sessionManager.getUserId();
            
            dbHelper = new DatabaseHelper(this);
            currentUser = dbHelper.getUser(userId);
            
            if (currentUser == null) {
                // Se algo deu errado, voltar para login
                Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show();
                sessionManager.logout();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading user: " + e.getMessage(), Toast.LENGTH_LONG).show();
            new SessionManager(this).logout();
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Já estamos na home
                return true;
            } else if (itemId == R.id.navigation_workout) {
                startActivity(new Intent(this, WorkoutListActivity.class));
                return false; // Não selecionar o item, pois estamos saindo desta activity
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return false; // Não selecionar o item, pois estamos saindo desta activity
            }
            return false;
        });
    }

    private void setupGuidesRecyclerView() {
        if (currentUser == null) return; // Garantir que temos um usuário

        guidesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        List<Guide> guides = new ArrayList<>();
        guides.add(new Guide(
            "How to Perfect Your Squat Form",
            "Jeff Nippard",
            "https://img.youtube.com/vi/bEv6CCg2BC8/maxresdefault.jpg",
            "https://youtube.com/watch?v=bEv6CCg2BC8",
            false
        ));
        guides.add(new Guide(
            "Science-Based Push Workout",
            "Jeff Nippard",
            "https://img.youtube.com/vi/LwHoNk-sjgs/maxresdefault.jpg",
            "https://youtube.com/watch?v=LwHoNk-sjgs",
            true
        ));
        guides.add(new Guide(
            "Perfect Deadlift Tutorial",
            "Jeff Nippard",
            "https://img.youtube.com/vi/VL5Ab0T07e4/maxresdefault.jpg",
            "https://youtube.com/watch?v=VL5Ab0T07e4",
            false
        ));
        guides.add(new Guide(
            "How to Build Bigger Arms",
            "Jeff Nippard",
            "https://img.youtube.com/vi/ia2WX4T3_YQ/maxresdefault.jpg",
            "https://youtube.com/watch?v=ia2WX4T3_YQ",
            true
        ));
        
        GuideAdapter adapter = new GuideAdapter(guides);
        guidesRecyclerView.setAdapter(adapter);

        // Adicionar mensagem de boas-vindas personalizada
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        
        String greeting;
        if(timeOfDay < 12) {
            greeting = "Good morning";
        } else if(timeOfDay < 16) {
            greeting = "Good afternoon";
        } else {
            greeting = "Good evening";
        }
        
        String welcomeMessage = String.format("%s, %s! Ready to crush your workout?", 
            greeting, currentUser.getFirstName());
            
        adapter.setWelcomeMessage(welcomeMessage);
    }

    private void setupUpgradeButton() {
        upgradeButton.setOnClickListener(v -> {
            Toast.makeText(this, 
                "Premium features will be available in the next update!", 
                Toast.LENGTH_LONG).show();
        });
    }
} 