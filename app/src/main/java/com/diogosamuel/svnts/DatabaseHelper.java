package com.diogosamuel.svnts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "svnts.db";
    private static final int DATABASE_VERSION = 4;

    // Tabela de Utilizadores
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_WEIGHT = "weight";

    // Tabela de Progresso
    public static final String TABLE_PROGRESS = "progress";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_WORKOUT_ID = "workout_id";
    public static final String COLUMN_EXERCISE_ID = "exercise_id";
    public static final String COLUMN_SET_NUMBER = "set_number";
    public static final String COLUMN_WEIGHT_KG = "weight_kg";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_DATE = "date";

    // Table for exercises
    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_EXERCISE_NAME = "name";
    public static final String COLUMN_MUSCLE_GROUP = "muscle_group";
    public static final String COLUMN_INSTRUCTIONS = "instructions";

    // Table for workout exercises
    public static final String TABLE_WORKOUT_EXERCISES = "workout_exercises";
    public static final String COLUMN_ORDER = "exercise_order";

    // Add this with the other table constants
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_WORKOUT_NAME = "name";
    public static final String COLUMN_WORKOUT_DESCRIPTION = "description";

    // Adicionar constante para a nova tabela
    public static final String TABLE_WORKOUT_COMPLETIONS = "workout_completions";

    // Nova coluna
    public static final String COLUMN_PLAN_ID = "plan_id";

    // SQL para criar tabela de utilizadores
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
            COLUMN_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
            COLUMN_LAST_NAME + " TEXT NOT NULL, " +
            COLUMN_GENDER + " TEXT NOT NULL, " +
            COLUMN_HEIGHT + " REAL NOT NULL, " +
            COLUMN_WEIGHT + " REAL NOT NULL);";

    // SQL para criar tabela de progresso
    private static final String CREATE_PROGRESS_TABLE = "CREATE TABLE " + TABLE_PROGRESS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " INTEGER NOT NULL, " +
            COLUMN_WORKOUT_ID + " INTEGER NOT NULL, " +
            COLUMN_EXERCISE_ID + " INTEGER NOT NULL, " +
            COLUMN_SET_NUMBER + " INTEGER NOT NULL, " +
            COLUMN_WEIGHT_KG + " REAL NOT NULL, " +
            COLUMN_REPS + " INTEGER NOT NULL, " +
            COLUMN_NOTES + " TEXT, " +
            COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "));";

    private static final String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
            COLUMN_MUSCLE_GROUP + " TEXT NOT NULL, " +
            COLUMN_INSTRUCTIONS + " TEXT);";

    private static final String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + TABLE_WORKOUTS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WORKOUT_NAME + " TEXT NOT NULL, " +
            COLUMN_WORKOUT_DESCRIPTION + " TEXT, " +
            COLUMN_PLAN_ID + " INTEGER);";

    private static final String CREATE_WORKOUT_EXERCISES_TABLE = "CREATE TABLE " + TABLE_WORKOUT_EXERCISES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WORKOUT_ID + " INTEGER NOT NULL, " +
            COLUMN_EXERCISE_ID + " INTEGER NOT NULL, " +
            COLUMN_ORDER + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + COLUMN_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUTS + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY(" + COLUMN_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISES + "(" + COLUMN_ID + "));";

    // SQL para criar tabela de workout_completions
    private static final String CREATE_WORKOUT_COMPLETIONS_TABLE = "CREATE TABLE " + TABLE_WORKOUT_COMPLETIONS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " INTEGER NOT NULL, " +
            COLUMN_WORKOUT_ID + " INTEGER NOT NULL, " +
            COLUMN_DATE + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY(" + COLUMN_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUTS + "(" + COLUMN_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PROGRESS_TABLE);
        db.execSQL(CREATE_EXERCISES_TABLE);
        db.execSQL(CREATE_WORKOUTS_TABLE);
        db.execSQL(CREATE_WORKOUT_EXERCISES_TABLE);
        db.execSQL(CREATE_WORKOUT_COMPLETIONS_TABLE);
        
        insertDefaultUser(db);
        insertDefaultExercises(db);
        insertDefaultWorkouts(db);
    }

    private void insertDefaultUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, "test@test.com");
        values.put(COLUMN_PASSWORD, "123456"); // Em produção, usar hash
        values.put(COLUMN_FIRST_NAME, "Usuário");
        values.put(COLUMN_LAST_NAME, "Teste");
        values.put(COLUMN_GENDER, "M");
        values.put(COLUMN_HEIGHT, 175);
        values.put(COLUMN_WEIGHT, 70);
        
        db.insert(TABLE_USERS, null, values);
    }

    private void insertDefaultExercises(SQLiteDatabase db) {
        String[][] defaultExercises = {
            {"Bench Press", "Chest", "Lie on bench, grip bar, lower to chest, press up"},
            {"Squat", "Legs", "Stand with bar on shoulders, squat down, stand up"},
            {"Deadlift", "Back", "Stand over bar, grip bar, lift with straight back"},
            {"Overhead Press", "Shoulders", "Stand with bar at shoulders, press overhead"},
            {"Barbell Row", "Back", "Bend over, grip bar, pull to lower chest"}
        };

        for (String[] exercise : defaultExercises) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EXERCISE_NAME, exercise[0]);
            values.put(COLUMN_MUSCLE_GROUP, exercise[1]);
            values.put(COLUMN_INSTRUCTIONS, exercise[2]);
            db.insert(TABLE_EXERCISES, null, values);
        }
    }

    private void insertDefaultWorkouts(SQLiteDatabase db) {
        // Insert default workouts
        ContentValues workoutValues = new ContentValues();
        workoutValues.put(COLUMN_WORKOUT_NAME, "Push Day");
        workoutValues.put(COLUMN_WORKOUT_DESCRIPTION, "Chest, shoulders, and triceps");
        workoutValues.put(COLUMN_PLAN_ID, 1);  // Atribuir ao plano de hipertrofia
        long pushWorkoutId = db.insert(TABLE_WORKOUTS, null, workoutValues);

        workoutValues.clear();
        workoutValues.put(COLUMN_WORKOUT_NAME, "Pull Day");
        workoutValues.put(COLUMN_WORKOUT_DESCRIPTION, "Back and biceps");
        long pullWorkoutId = db.insert(TABLE_WORKOUTS, null, workoutValues);

        workoutValues.clear();
        workoutValues.put(COLUMN_WORKOUT_NAME, "Legs Day");
        workoutValues.put(COLUMN_WORKOUT_DESCRIPTION, "Quadriceps, hamstrings, and calves");
        long legsWorkoutId = db.insert(TABLE_WORKOUTS, null, workoutValues);

        // Link exercises to workouts
        ContentValues exerciseValues = new ContentValues();
        
        // Push Day exercises
        exerciseValues.put(COLUMN_WORKOUT_ID, pushWorkoutId);
        exerciseValues.put(COLUMN_EXERCISE_ID, 1); // Bench Press
        exerciseValues.put(COLUMN_ORDER, 1);
        db.insert(TABLE_WORKOUT_EXERCISES, null, exerciseValues);

        // Pull Day exercises
        exerciseValues.clear();
        exerciseValues.put(COLUMN_WORKOUT_ID, pullWorkoutId);
        exerciseValues.put(COLUMN_EXERCISE_ID, 5); // Barbell Row
        exerciseValues.put(COLUMN_ORDER, 1);
        db.insert(TABLE_WORKOUT_EXERCISES, null, exerciseValues);

        // Legs Day exercises
        exerciseValues.clear();
        exerciseValues.put(COLUMN_WORKOUT_ID, legsWorkoutId);
        exerciseValues.put(COLUMN_EXERCISE_ID, 2); // Squat
        exerciseValues.put(COLUMN_ORDER, 1);
        db.insert(TABLE_WORKOUT_EXERCISES, null, exerciseValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create new tables if upgrading from version 1
            db.execSQL(CREATE_EXERCISES_TABLE);
            db.execSQL(CREATE_WORKOUTS_TABLE);
            db.execSQL(CREATE_WORKOUT_EXERCISES_TABLE);
            
            // Insert default data
            insertDefaultExercises(db);
            insertDefaultWorkouts(db);
        }
        
        // Adicionar upgrade para versão 3
        if (oldVersion < 3) {
            db.execSQL(CREATE_WORKOUT_COMPLETIONS_TABLE);
        }

        if (oldVersion < 4) {
            // Adicionar coluna plan_id à tabela workouts
            db.execSQL("ALTER TABLE " + TABLE_WORKOUTS + 
                      " ADD COLUMN " + COLUMN_PLAN_ID + " INTEGER;");
            
            // Atualizar workouts existentes com seus plan_ids
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAN_ID, 1);  // Atribuir ao plano de hipertrofia
            db.update(TABLE_WORKOUTS, values, 
                     COLUMN_WORKOUT_NAME + " IN (?, ?, ?)", 
                     new String[]{"Push Day", "Pull Day", "Legs Day"});
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop all tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT_COMPLETIONS);
        
        // Recreate everything
        onCreate(db);
    }

    public long saveWorkoutSet(WorkoutSet set, long userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_EXERCISE_ID, set.getExerciseId());
        values.put(COLUMN_SET_NUMBER, set.getSetNumber());
        values.put(COLUMN_WEIGHT_KG, set.getWeightKg());
        values.put(COLUMN_REPS, set.getReps());
        values.put(COLUMN_NOTES, set.getNotes());

        return db.insert(TABLE_PROGRESS, null, values);
    }

    public List<WorkoutSet> getWorkoutHistory(long userId) {
        List<WorkoutSet> history = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT p.*, e." + COLUMN_EXERCISE_NAME + " FROM " + TABLE_PROGRESS + " p " +
                      "JOIN " + TABLE_EXERCISES + " e ON p." + COLUMN_EXERCISE_ID + " = e." + COLUMN_ID + " " +
                      "WHERE " + COLUMN_USER_ID + " = ? " +
                      "ORDER BY " + COLUMN_DATE + " DESC LIMIT 10";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                WorkoutSet set = new WorkoutSet(
                    cursor.getLong(cursor.getColumnIndex(COLUMN_EXERCISE_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_SET_NUMBER)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_WEIGHT_KG)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_REPS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTES))
                );
                set.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                set.setExerciseName(cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME)));
                history.add(set);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return history;
    }

    public List<Exercise> getExercisesForWorkout(long workoutId) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT e.* FROM " + TABLE_EXERCISES + " e " +
                      "JOIN " + TABLE_WORKOUT_EXERCISES + " we ON e." + COLUMN_ID + " = we." + COLUMN_EXERCISE_ID + " " +
                      "WHERE we." + COLUMN_WORKOUT_ID + " = ? " +
                      "ORDER BY we." + COLUMN_ORDER;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(workoutId)});

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(
                    cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_MUSCLE_GROUP)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCTIONS))
                );
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return exercises;
    }

    public List<WorkoutSet> getExerciseHistory(long exerciseId, long userId) {
        List<WorkoutSet> history = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_PROGRESS + 
                      " WHERE " + COLUMN_EXERCISE_ID + " = ? " +
                      " AND " + COLUMN_USER_ID + " = ? " +
                      " ORDER BY " + COLUMN_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, 
            new String[]{String.valueOf(exerciseId), String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                WorkoutSet set = new WorkoutSet(
                    cursor.getLong(cursor.getColumnIndex(COLUMN_EXERCISE_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_SET_NUMBER)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_WEIGHT_KG)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_REPS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTES))
                );
                history.add(set);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return history;
    }

    public void saveWorkoutCompletion(long workoutId, long userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_WORKOUT_ID, workoutId);
        values.put(COLUMN_DATE, System.currentTimeMillis());
        
        db.insert("workout_completions", null, values);
    }

    // Add this method to help with development
    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        // Drop all tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT_COMPLETIONS);
        // Recreate everything
        onCreate(db);
    }

    public int getCompletedWorkoutsCount(long planId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(DISTINCT wc." + COLUMN_WORKOUT_ID + ") FROM " + 
                      TABLE_WORKOUT_COMPLETIONS + " wc " +
                      "JOIN " + TABLE_WORKOUTS + " w ON wc." + COLUMN_WORKOUT_ID + 
                      " = w." + COLUMN_ID + " " +
                      "WHERE w." + COLUMN_PLAN_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(planId)});
        int count = 0;
        
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        
        cursor.close();
        return count;
    }

    public int getCurrentStreak(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        int streak = 0;
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long currentTime = System.currentTimeMillis();
        
        String query = "SELECT date FROM workout_completions " +
                      "WHERE user_id = ? ORDER BY date DESC";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        
        if (cursor.moveToFirst()) {
            long lastWorkout = cursor.getLong(0);
            if ((currentTime - lastWorkout) <= oneDayInMillis) {
                streak = 1;
                while (cursor.moveToNext()) {
                    long nextWorkout = cursor.getLong(0);
                    if ((lastWorkout - nextWorkout) <= oneDayInMillis) {
                        streak++;
                        lastWorkout = nextWorkout;
                    } else {
                        break;
                    }
                }
            }
        }
        
        cursor.close();
        return streak;
    }

    public class WorkoutStats {
        public int totalWorkouts;
        public int totalSets;
        public double totalWeight;
        public String mostTrainedMuscle;
    }

    public WorkoutStats getUserStats(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        WorkoutStats stats = new WorkoutStats();
        
        // Total workouts
        String query = "SELECT COUNT(*) FROM " + TABLE_WORKOUT_COMPLETIONS +
                      " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            stats.totalWorkouts = cursor.getInt(0);
        }
        cursor.close();
        
        // Músculo mais treinado
        query = "SELECT " + COLUMN_MUSCLE_GROUP + ", COUNT(*) as count FROM " + TABLE_PROGRESS +
                " JOIN " + TABLE_EXERCISES + " ON " + TABLE_PROGRESS + "." + COLUMN_EXERCISE_ID +
                " = " + TABLE_EXERCISES + "." + COLUMN_ID +
                " WHERE " + COLUMN_USER_ID + " = ?" +
                " GROUP BY " + COLUMN_MUSCLE_GROUP +
                " ORDER BY count DESC LIMIT 1";
        
        cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            stats.mostTrainedMuscle = cursor.getString(0);
        }
        cursor.close();
        
        return stats;
    }

    public long getFirstWorkoutDate(long planId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT MIN(" + COLUMN_DATE + ") FROM " + TABLE_WORKOUT_COMPLETIONS + " wc " +
                      "JOIN " + TABLE_WORKOUTS + " w ON wc." + COLUMN_WORKOUT_ID + " = w." + COLUMN_ID + " " +
                      "WHERE w." + COLUMN_PLAN_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(planId)});
        long firstDate = System.currentTimeMillis(); // Default to current time if no workouts found
        
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            firstDate = cursor.getLong(0);
        }
        
        cursor.close();
        return firstDate;
    }

    public boolean verifyUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + 
                      " WHERE " + COLUMN_EMAIL + " = ? AND " + 
                      COLUMN_PASSWORD + " = ?";
                      
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public long getUserId(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + 
                      " WHERE " + COLUMN_EMAIL + " = ?";
                      
        Cursor cursor = db.rawQuery(query, new String[]{email});
        long id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getLong(0);
        }
        cursor.close();
        return id;
    }

    public User getUser(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        User user = null;
        
        if (cursor.moveToFirst()) {
            user = new User(
                cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_HEIGHT)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_WEIGHT))
            );
            user.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
        }
        
        cursor.close();
        return user;
    }

    public long createUser(String email, String password, String firstName, String lastName, 
                          String gender, double height, double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_WEIGHT, weight);
        
        return db.insert(TABLE_USERS, null, values);
    }
} 