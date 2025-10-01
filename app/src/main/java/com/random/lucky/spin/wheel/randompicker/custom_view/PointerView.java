package com.random.lucky.spin.wheel.randompicker.custom_view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.random.lucky.spin.wheel.randompicker.R;

import java.util.ArrayList;

public class PointerView extends RelativeLayout {

    private Paint paint;
    private boolean animationRunning = false;
    private AnimatorSet animatorSet;
    private final ArrayList<RippleView> rippleViewList = new ArrayList<RippleView>();
    TextView tvWin;

    public PointerView(Context context) {
        super(context);
        init();
    }

    public PointerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    private void init() {
        if (isInEditMode()) return;

        int rippleDelay = 3000 / 4;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#FFFFFF"));

        LayoutParams rippleParams = new LayoutParams((int) (2 * (dpToPx(30))), (int) (2 * (dpToPx(30))));
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);

        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        for (int i = 0; i < 4; i++) {
            RippleView rippleView = new RippleView(getContext());
            addView(rippleView, rippleParams);
            rippleViewList.add(rippleView);
            final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, 2.0f);
            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay((long) i * rippleDelay);
            scaleXAnimator.setDuration(3000);
            animatorList.add(scaleXAnimator);
            final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, 2.0f);
            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay((long) i * rippleDelay);
            scaleYAnimator.setDuration(3000);
            animatorList.add(scaleYAnimator);
            final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleView, "Alpha", 1.0f, 0f);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay((long) i * rippleDelay);
            alphaAnimator.setDuration(3000);
            animatorList.add(alphaAnimator);
        }

        RippleView rippleViewDefault = new RippleView(getContext());
        rippleViewDefault.setVisibility(View.VISIBLE);
        addView(rippleViewDefault, rippleParams);
        animatorSet.playTogether(animatorList);
        addTextViewWin();
    }

    private void addTextViewWin() {
        tvWin = new TextView(getContext());
        tvWin.setText(R.string.win);
        tvWin.setBackgroundResource(R.drawable.bg_winner_finger_picker);
        tvWin.setTypeface(ResourcesCompat.getFont(getContext(), R.font.linotte_semi_bold));
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        tvWin.setTextSize(16);
        tvWin.setGravity(Gravity.CENTER);
        tvWin.setTextColor(Color.parseColor("#0085FF"));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        tvWin.setVisibility(View.INVISIBLE);
        tvWin.setPadding((int) dpToPx(12), (int) dpToPx(12), (int) dpToPx(12), (int) dpToPx(12));
        addView(tvWin, params);
    }

    public void visibleViewWin() {
        tvWin.setVisibility(View.VISIBLE);
    }

    private class RippleView extends View {

        public RippleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int radius = (Math.min(getWidth(), getHeight())) / 2;
            canvas.drawCircle(radius, radius, radius, paint);
        }
    }

    public void startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            for (RippleView rippleView : rippleViewList) {
                rippleView.setVisibility(VISIBLE);
            }
            animatorSet.start();
            animationRunning = true;
        }
    }

    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            animatorSet.end();
            animationRunning = false;
        }
    }

    public float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    public boolean isRippleAnimationRunning() {
        return animationRunning;
    }
}
