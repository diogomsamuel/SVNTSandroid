package com.diogosamuel.svnts.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import com.diogosamuel.svnts.R;

public class UIUtils {
    private static View loadingView;
    
    public static void showLoading(Context context, String message) {
        if (!(context instanceof Activity)) return;
        
        Activity activity = (Activity) context;
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        
        if (loadingView == null) {
            loadingView = LayoutInflater.from(context).inflate(R.layout.layout_loading, rootView, false);
        }
        
        TextView messageView = loadingView.findViewById(R.id.loading_message);
        messageView.setText(message);
        
        if (loadingView.getParent() == null) {
            rootView.addView(loadingView);
        }
    }
    
    public static void hideLoading() {
        if (loadingView != null && loadingView.getParent() != null) {
            ((FrameLayout) loadingView.getParent()).removeView(loadingView);
        }
    }
    
    public static void showError(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.error))
            .show();
    }
    
    public static void showSuccess(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.success))
            .show();
    }
} 