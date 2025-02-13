package com.diogosamuel.svnts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<Workout> workouts;
    private OnWorkoutSelectedListener listener;

    public interface OnWorkoutSelectedListener {
        void onWorkoutSelected(Workout workout);
    }

    public WorkoutAdapter(List<Workout> workouts, OnWorkoutSelectedListener listener) {
        this.workouts = workouts;
        this.listener = listener;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.nameText.setText(workout.getName());
        holder.descriptionText.setText(workout.getDescription());
        
        holder.itemView.setOnClickListener(v -> listener.onWorkoutSelected(workout));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView descriptionText;

        WorkoutViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.workout_name);
            descriptionText = itemView.findViewById(R.id.workout_description);
        }
    }
} 