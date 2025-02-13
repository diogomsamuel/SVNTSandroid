package com.diogosamuel.svnts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;
    private OnSetCompletedListener listener;

    public interface OnSetCompletedListener {
        void onSetCompleted(Exercise exercise, WorkoutSet set);
    }

    public ExerciseAdapter(List<Exercise> exercises, OnSetCompletedListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.nameText.setText(exercise.getName());
        holder.muscleGroupText.setText(exercise.getMuscleGroup());
        holder.instructionsText.setText(exercise.getInstructions());

        holder.logSetButton.setOnClickListener(v -> {
            try {
                double weight = Double.parseDouble(holder.weightInput.getText().toString());
                int reps = Integer.parseInt(holder.repsInput.getText().toString());
                
                WorkoutSet set = new WorkoutSet(
                    exercise.getId(),
                    holder.getAdapterPosition() + 1,
                    weight,
                    reps,
                    ""
                );
                
                listener.onSetCompleted(exercise, set);
                
                // Clear inputs after logging
                holder.weightInput.getText().clear();
                holder.repsInput.getText().clear();
                
            } catch (NumberFormatException e) {
                // TODO: Show error message
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    private void animateSetCompletion(View itemView) {
        itemView.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(200)
            .withEndAction(() -> {
                itemView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start();
            })
            .start();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView muscleGroupText;
        TextView instructionsText;
        EditText weightInput;
        EditText repsInput;
        Button logSetButton;

        ExerciseViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.exercise_name);
            muscleGroupText = itemView.findViewById(R.id.muscle_group);
            instructionsText = itemView.findViewById(R.id.instructions);
            weightInput = itemView.findViewById(R.id.weight_input);
            repsInput = itemView.findViewById(R.id.reps_input);
            logSetButton = itemView.findViewById(R.id.log_set_button);
        }
    }
} 