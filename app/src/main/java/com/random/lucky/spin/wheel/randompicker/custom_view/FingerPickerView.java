package com.random.lucky.spin.wheel.randompicker.custom_view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.Utils.VibrationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FingerPickerView extends FrameLayout {
    private Boolean isAllowTouch = true;
    private final HashMap<Integer, PointerView> listViewPointer = new HashMap<>();
    private int countChooseWinner = 1;
    private final Handler handlerSelectWinner = new Handler();
    private final Runnable runnableSelectWinner = this::selectWinner;

    private void selectWinner() {
        if (countChooseWinner >= listViewPointer.size()) {
            CustomToast.toast(getContext().getString(R.string.please_touch_more_point), getContext());
            return;
        }
        isAllowTouch = false;
        List<Integer> listKey = new ArrayList<>(listViewPointer.keySet());
        Random random = new Random();
        List<Integer> listKeyWinner = new ArrayList<>();

        while (listKeyWinner.size() < countChooseWinner) {
            int randomIndex = random.nextInt(listKey.size());
            int element = listKey.get(randomIndex);
            if (!listKeyWinner.contains(element)) {
                listKeyWinner.add(element);
            }
        }
        for (int key : listKey) {
            if (!listKeyWinner.contains(key)) {
                removeView(listViewPointer.get(key));
                listViewPointer.remove(key);
            } else if (listViewPointer.get(key) != null) {
                Objects.requireNonNull(listViewPointer.get(key)).visibleViewWin();
            }
        }
        new Handler().postDelayed(() -> {
            isAllowTouch = true;
            removeAllViews();
        }, 2000);
    }

    public FingerPickerView(Context context) {
        super(context);
    }

    public FingerPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FingerPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCountWinner(int count) {
        handlerSelectWinner.removeCallbacks(runnableSelectWinner);
        countChooseWinner = count;
        this.isAllowTouch = true;
        removeAllViews();
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (!isAllowTouch)
            return super.onTouchEvent(event);
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                VibrationUtil.vibrateOnce(getContext(), 100);
                addNewPointerView(event, pointerId);
                handlerSelectWinner.removeCallbacks(runnableSelectWinner);
                handlerSelectWinner.postDelayed(runnableSelectWinner, 4000);
                break;
            case MotionEvent.ACTION_MOVE:
                if (listViewPointer.size() != event.getPointerCount()) {
                    removeAllViews();
                    listViewPointer.clear();
                }
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    View viewMove = listViewPointer.get(id);
                    if (viewMove != null) {
                        viewMove.setTranslationX(event.getX(i) - dpToPx(80));
                        viewMove.setTranslationY(event.getY(i) - dpToPx(80));
                    } else {
                        addNewPointerView(event, id);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                removeView(listViewPointer.get(pointerId));
                listViewPointer.remove(pointerId);
                if (listViewPointer.isEmpty()) {
                    handlerSelectWinner.removeCallbacks(runnableSelectWinner);
                }
                break;
        }
        invalidate();
        return true;
    }

    private void addNewPointerView(MotionEvent event, int pointerId) {
        PointerView viewAdd = new PointerView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) dpToPx(160), (int) dpToPx(160));
        viewAdd.setTranslationX(event.getX() - dpToPx(80));
        viewAdd.setTranslationY(event.getY() - dpToPx(80));
        viewAdd.setLayoutParams(params);
        viewAdd.setColor(generateRandomColor());
        viewAdd.startRippleAnimation();
        addView(viewAdd);
        listViewPointer.put(pointerId, viewAdd);
    }

    public void setAllowTouch(Boolean isAllowTouch) {
        this.isAllowTouch = isAllowTouch;
    }

    public float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private int generateRandomColor() {
        Random random = new Random();
        int red = random.nextInt(220);
        int green = random.nextInt(220);
        int blue = random.nextInt(220);
        return Color.rgb(red, green, blue);
    }
}
