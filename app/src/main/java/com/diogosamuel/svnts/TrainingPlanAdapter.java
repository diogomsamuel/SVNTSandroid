package com.diogosamuel.svnts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TrainingPlanAdapter extends RecyclerView.Adapter<TrainingPlanAdapter.PlanViewHolder> {
    private List<TrainingPlan> plans;
    private OnPlanSelectedListener listener;

    public interface OnPlanSelectedListener {
        void onPlanSelected(TrainingPlan plan);
    }

    public TrainingPlanAdapter(List<TrainingPlan> plans, OnPlanSelectedListener listener) {
        this.plans = plans;
        this.listener = listener;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_training_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanViewHolder holder, int position) {
        TrainingPlan plan = plans.get(position);
        holder.nameText.setText(plan.getName());
        holder.descriptionText.setText(plan.getDescription());
        holder.difficultyText.setText(plan.getDifficulty());
        holder.durationText.setText(plan.getWeeksCount() + " weeks");
        holder.premiumBadge.setVisibility(plan.isPremium() ? View.VISIBLE : View.GONE);
        
        holder.itemView.setOnClickListener(v -> listener.onPlanSelected(plan));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public List<TrainingPlan> getPlans() {
        return plans;
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView descriptionText;
        TextView difficultyText;
        TextView durationText;
        ImageView premiumBadge;

        PlanViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.plan_name);
            descriptionText = itemView.findViewById(R.id.plan_description);
            difficultyText = itemView.findViewById(R.id.plan_difficulty);
            durationText = itemView.findViewById(R.id.plan_duration);
            premiumBadge = itemView.findViewById(R.id.premium_badge);
        }
    }
} 