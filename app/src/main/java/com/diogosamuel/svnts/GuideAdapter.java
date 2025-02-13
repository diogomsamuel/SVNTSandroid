package com.diogosamuel.svnts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {
    private List<Guide> guides;
    private String welcomeMessage;

    public GuideAdapter(List<Guide> guides) {
        this.guides = guides;
        this.welcomeMessage = null;
    }

    public void setWelcomeMessage(String message) {
        this.welcomeMessage = message;
        notifyItemInserted(0);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 && welcomeMessage != null ? 0 : 1;
    }

    @Override
    public GuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_welcome_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_guide, parent, false);
        }
        return new GuideViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(GuideViewHolder holder, int position) {
        if (holder.viewType == 0) {
            holder.welcomeText.setText(welcomeMessage);
            return;
        }

        int guidePosition = welcomeMessage != null ? position - 1 : position;
        Guide guide = guides.get(guidePosition);
        
        holder.titleText.setText(guide.getTitle());
        holder.authorText.setText(guide.getAuthor());
        holder.premiumBadge.setVisibility(guide.isPremium() ? View.VISIBLE : View.GONE);
        
        Glide.with(holder.itemView)
            .load(guide.getThumbnailUrl())
            .centerCrop()
            .into(holder.thumbnailImage);
        
        holder.itemView.setOnClickListener(v -> {
            if (guide.isPremium()) {
                Toast.makeText(v.getContext(), 
                    "This content is for premium members only", 
                    Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(guide.getVideoUrl()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return guides.size() + (welcomeMessage != null ? 1 : 0);
    }

    static class GuideViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImage;
        TextView titleText;
        TextView authorText;
        ImageView premiumBadge;
        TextView welcomeText;
        int viewType;

        GuideViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            
            if (viewType == 0) {
                welcomeText = itemView.findViewById(R.id.welcome_text);
            } else {
                thumbnailImage = itemView.findViewById(R.id.guide_thumbnail);
                titleText = itemView.findViewById(R.id.guide_title);
                authorText = itemView.findViewById(R.id.guide_author);
                premiumBadge = itemView.findViewById(R.id.premium_badge);
            }
        }
    }
} 