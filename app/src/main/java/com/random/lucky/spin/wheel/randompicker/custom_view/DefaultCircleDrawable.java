package com.random.lucky.spin.wheel.randompicker.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.random.lucky.spin.wheel.randompicker.AppConfiguration;
import com.random.lucky.spin.wheel.randompicker.room_database.SpinWheelModel;

public class DefaultCircleDrawable extends Drawable {
    private static final float GESTURE_THRESHOLD_DIP = 16.0f;
    private Context context;
    Typeface face;
    private SpinWheelModel spinWheelModel;
    private Paint mPaint;
    private int numSlices;
    private float r;
    private float wedgeSize;
    private float x;
    private float y;
    private float textSize = 18f;

    @SuppressLint({"WrongConstant"})
    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public DefaultCircleDrawable() {
        this.mPaint = new Paint();
        this.y = 0.0f;
        this.x = 0.0f;
        this.r = 100.0f;
    }

    public DefaultCircleDrawable(float f, float f2, float f3, float f4, SpinWheelModel spinWheelModel, Context context2, float textSize) {
        this.mPaint = new Paint();
        this.mPaint.setStrokeWidth(1.5f);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.x = f;
        this.y = f2;
        this.r = f3;
        this.context = context2;
        this.numSlices = spinWheelModel.getListItemSpin().size();
        this.wedgeSize = f4;
        this.spinWheelModel = spinWheelModel;
        this.textSize = (context2.getResources().getDisplayMetrics().density * textSize) + 0.5f;
    }

    public void draw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        Canvas canvas2 = canvas;
        getLevel();
        getBounds();
        this.y = this.r;
        Paint paint = new Paint();
        paint.setColor(-1);
        LinearGradient linearGradient = new LinearGradient(this.x - this.r, this.y - this.r, this.x + this.r, this.y + this.r, -1, -1, Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
        float f4 = this.r / 30.0f;
        canvas2.drawCircle(this.x, this.y, this.r, paint);
        int i = ((int) this.r) / 20;
        float f5 = 360.0f;
        int i2 = 0;
        float f6 = 360.0f;
        while (i2 < this.numSlices) {
            this.mPaint.setStyle(Paint.Style.FILL);
            float f7 = (this.x - this.r) + f4;
            float f8 = (this.y - this.r) + f4;
            float f9 = (this.x + this.r) - f4;
            float f10 = (this.y + this.r) - f4;
            Log.d("asdllkl", "draw: " + spinWheelModel.getListItemSpin().get(i2).getColor());
            this.mPaint.setColor(spinWheelModel.getListItemSpin().get(i2).getColor());
            if (this.wedgeSize * ((float) i2) > f5) {
                f2 = f10;
                f = f9;
                f3 = f8;
                canvas.drawArc(new RectF(f7, f8, f9, f10), f6, 0.0f, true, this.mPaint);
            } else {
                f2 = f10;
                f = f9;
                f3 = f8;
                canvas.drawArc(new RectF(f7, f3, f, f2), f6, -this.wedgeSize, true, this.mPaint);
            }
            float f11 = (float) i;
            float f12 = f7 + f11;
            float f13 = f3 + f11;
            float f14 = f - f11;
            float f15 = f2 - f11;
            float f16 = f6;
            float f17 = f11;
            canvas.drawArc(new RectF(f12, f13, f14, f15), f16, -this.wedgeSize, true, this.mPaint);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(-1);
            canvas.drawArc(new RectF(f12 - f17, f13 - f17, f14 + f17, f15 + f17), f16, -this.wedgeSize, true, this.mPaint);
            f6 -= this.wedgeSize;
            i2++;
            f5 = 360.0f;
        }
        this.mPaint.setStyle(Paint.Style.FILL);
        canvas.save();
        canvas2.rotate(this.wedgeSize / 2.0f, this.x, this.y);
        this.mPaint.setColor(-1);
        Paint paint2 = this.mPaint;
        RadialGradient radialGradient = new RadialGradient(this.x, this.y, this.r, 0, 0, Shader.TileMode.CLAMP);
        paint2.setShader(radialGradient);
        canvas2.drawCircle(this.x, this.y, this.r, this.mPaint);
        canvas.save();
        this.face = Typeface.createFromAsset(AppConfiguration.getContext().getAssets(), "fonts/sarysoft.otf");
        this.mPaint.setShader(null);
        this.mPaint.setColor(-1);
        this.mPaint.setTypeface(this.face);
        int i3 = ((int) this.r) / 3;
        for (int i4 = 0; i4 < this.numSlices; i4++) {
            this.mPaint.setColor(Color.parseColor("#000000"));
            this.mPaint.setTextSize(textSize);
            canvas.save();
            canvas2.rotate(-this.wedgeSize, this.x, this.y);
            canvas2.drawText(spinWheelModel.getListItemSpin().get(i4).getItemName(), this.x + ((float) i3), this.y, this.mPaint);
        }
        this.mPaint.setColor(Color.parseColor("#6d235b"));
        canvas2.drawCircle(this.x, this.y, this.r / 5.5f, this.mPaint);
    }

    private int getTextSize() {
        return (int) ((this.context.getResources().getDisplayMetrics().density * 10.0f) + 0.5f);
    }


    public boolean onLevelChange(int i) {
        invalidateSelf();
        return true;
    }
}

