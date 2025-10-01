package com.random.lucky.spin.wheel.randompicker.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class LastItemBottomMarginDecorator extends RecyclerView.ItemDecoration {
    private final Context context;
    private final int marginBottom;

    public LastItemBottomMarginDecorator(Context context, int marginBottom) {
        this.context = context;
        this.marginBottom = marginBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
        if (position == itemCount - 1) {
            outRect.bottom = dpToPx(context, marginBottom);
        } else {
            outRect.bottom = 0;
        }
    }

    private int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
