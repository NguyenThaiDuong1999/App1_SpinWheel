package com.random.lucky.spin.wheel.randompicker.custom_view;

import android.annotation.SuppressLint;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.random.lucky.spin.wheel.randompicker.AppConfiguration;

public class SelecterOverlayDrawable extends Drawable {
    Typeface face;
    private Paint mPaint = new Paint();
    float r;
    float wedgeSize;
    float x;
    float y;

    @SuppressLint({"WrongConstant"})
    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public SelecterOverlayDrawable() {
        this.mPaint.setStrokeWidth(30.0f);
        this.y = 0.0f;
        this.x = 0.0f;
    }

    public SelecterOverlayDrawable(float f, float f2, float f3) {
        this.mPaint.setStrokeWidth(3.0f);
        this.x = f;
        this.y = f2;
        this.r = f3;
    }

    public void draw(Canvas canvas) {
        this.y = this.r;
        this.mPaint.setStrokeWidth(4.0f);
        this.mPaint.setAntiAlias(true);
        int i = ((int) this.r) / 11;
        int i2 = ((int) this.r) / 8;
        int i3 = ((int) this.r) / 30;
        this.face = Typeface.createFromAsset(AppConfiguration.getContext().getAssets(), "fonts/SHOWG.TTF");
        this.mPaint.setMaskFilter(new BlurMaskFilter(48.0f, BlurMaskFilter.Blur.NORMAL));
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#BE4E4E"));
        canvas.drawCircle(this.x, this.y, this.r / 6.0f, paint);
        paint.setColor(-1);
        canvas.rotate(180.0f, this.x, this.y);
        paint.setTextSize(28.0f);
        paint.setTypeface(this.face);
        canvas.drawText("SPIN", this.x - 28.0f, this.y + 10.0f, paint);
    }


    public boolean onLevelChange(int i) {
        invalidateSelf();
        return true;
    }
}

