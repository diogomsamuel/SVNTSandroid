package com.diogosamuel.svnts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {
    private List<WorkoutSet> history;

    public ProgressAdapter(List<WorkoutSet> history) {
        this.history = history;
    }

    @Override
    public ProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_progress, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgressViewHolder holder, int position) {
        WorkoutSet set = history.get(position);
        holder.exerciseText.setText(set.getExerciseName());
        holder.weightText.setText(String.format("%.1f kg", set.getWeightKg()));
        holder.repsText.setText(String.format("%d reps", set.getReps()));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseText;
        TextView weightText;
        TextView repsText;

        ProgressViewHolder(View itemView) {
            super(itemView);
            exerciseText = itemView.findViewById(R.id.exercise_name);
            weightText = itemView.findViewById(R.id.weight_value);
            repsText = itemView.findViewById(R.id.reps_value);
        }
    }
} 