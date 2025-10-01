package com.random.lucky.spin.wheel.randompicker.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.random.lucky.spin.wheel.randompicker.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

public class MagicTextView extends TextView {
    private WeakHashMap<String, Pair<Canvas, Bitmap>> canvasStore;
    private Drawable foregroundDrawable;
    private boolean frozen = false;
    private ArrayList<Shadow> innerShadows;
    private int[] lockedCompoundPadding;
    private ArrayList<Shadow> outerShadows;
    private Integer strokeColor;
    private Paint.Join strokeJoin;
    private float strokeMiter;
    private float strokeWidth;
    private Bitmap tempBitmap;
    private Canvas tempCanvas;

    public static class Shadow {
        int color;
        float dx;
        float dy;
        float r;

        public Shadow(float f, float f2, float f3, int i) {
            this.r = f;
            this.dx = f2;
            this.dy = f3;
            this.color = i;
        }
    }

    public MagicTextView(Context context) {
        super(context);
        init(null);
    }

    public MagicTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public MagicTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    public void init(AttributeSet attributeSet) {
        Paint.Join join;
        this.outerShadows = new ArrayList<>();
        this.innerShadows = new ArrayList<>();
        if (this.canvasStore == null) {
            this.canvasStore = new WeakHashMap<>();
        }
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.MagicTextView);
            String string = obtainStyledAttributes.getString(14);
            if (string != null) {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), String.format("fonts/%s.ttf", new Object[]{string})));
            }
            if (obtainStyledAttributes.hasValue(1)) {
                Drawable drawable = obtainStyledAttributes.getDrawable(1);
                if (drawable != null) {
                    setForegroundDrawable(drawable);
                } else {
                    setTextColor(obtainStyledAttributes.getColor(1, ViewCompat.MEASURED_STATE_MASK));
                }
            }
            if (obtainStyledAttributes.hasValue(2)) {
                addInnerShadow((float) obtainStyledAttributes.getDimensionPixelSize(5, 0), (float) obtainStyledAttributes.getDimensionPixelOffset(3, 0), (float) obtainStyledAttributes.getDimensionPixelOffset(4, 0), obtainStyledAttributes.getColor(2, ViewCompat.MEASURED_STATE_MASK));
            }
            if (obtainStyledAttributes.hasValue(6)) {
                addOuterShadow((float) obtainStyledAttributes.getDimensionPixelSize(9, 0), (float) obtainStyledAttributes.getDimensionPixelOffset(7, 0), (float) obtainStyledAttributes.getDimensionPixelOffset(8, 0), obtainStyledAttributes.getColor(6, ViewCompat.MEASURED_STATE_MASK));
            }
            if (obtainStyledAttributes.hasValue(10)) {
                float dimensionPixelSize = (float) obtainStyledAttributes.getDimensionPixelSize(13, 1);
                int color = obtainStyledAttributes.getColor(10, ViewCompat.MEASURED_STATE_MASK);
                float dimensionPixelSize2 = (float) obtainStyledAttributes.getDimensionPixelSize(12, 10);
                switch (obtainStyledAttributes.getInt(11, 0)) {
                    case 0:
                        join = Paint.Join.MITER;
                        break;
                    case 1:
                        join = Paint.Join.BEVEL;
                        break;
                    case 2:
                        join = Paint.Join.ROUND;
                        break;
                    default:
                        join = null;
                        break;
                }
                setStroke(dimensionPixelSize, color, join, dimensionPixelSize2);
            }
        }
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        if (this.innerShadows.size() > 0 || this.foregroundDrawable != null) {
            setLayerType(1, null);
        }
    }

    public void setStroke(float f, int i, Paint.Join join, float f2) {
        this.strokeWidth = f;
        this.strokeColor = Integer.valueOf(i);
        this.strokeJoin = join;
        this.strokeMiter = f2;
    }

    public void setStroke(float f, int i) {
        setStroke(f, i, Paint.Join.MITER, 10.0f);
    }

    public void addOuterShadow(float f, float f2, float f3, int i) {
        if (f == 0.0f) {
            f = 1.0E-4f;
        }
        this.outerShadows.add(new Shadow(f, f2, f3, i));
    }

    public void addInnerShadow(float f, float f2, float f3, int i) {
        if (f == 0.0f) {
            f = 1.0E-4f;
        }
        this.innerShadows.add(new Shadow(f, f2, f3, i));
    }

    public void clearInnerShadows() {
        this.innerShadows.clear();
    }

    public void clearOuterShadows() {
        this.outerShadows.clear();
    }

    public void setForegroundDrawable(Drawable drawable) {
        this.foregroundDrawable = drawable;
    }

    public Drawable getForeground() {
        return this.foregroundDrawable == null ? this.foregroundDrawable : new ColorDrawable(getCurrentTextColor());
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        freeze();
        Drawable background = getBackground();
        Drawable[] compoundDrawables = getCompoundDrawables();
        int currentTextColor = getCurrentTextColor();
        setCompoundDrawables(null, null, null, null);
        Iterator it = this.outerShadows.iterator();
        while (it.hasNext()) {
            Shadow shadow = (Shadow) it.next();
            setShadowLayer(shadow.r, shadow.dx, shadow.dy, shadow.color);
            super.onDraw(canvas);
        }
        setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        setTextColor(currentTextColor);
        if (this.foregroundDrawable != null && (this.foregroundDrawable instanceof BitmapDrawable)) {
            generateTempCanvas();
            super.onDraw(this.tempCanvas);
            ((BitmapDrawable) this.foregroundDrawable).getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            this.foregroundDrawable.setBounds(canvas.getClipBounds());
            this.foregroundDrawable.draw(this.tempCanvas);
            canvas.drawBitmap(this.tempBitmap, 0.0f, 0.0f, null);
            this.tempCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        if (this.strokeColor != null) {
            TextPaint paint = getPaint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(this.strokeJoin);
            paint.setStrokeMiter(this.strokeMiter);
            setTextColor(this.strokeColor.intValue());
            paint.setStrokeWidth(this.strokeWidth);
            super.onDraw(canvas);
            paint.setStyle(Paint.Style.FILL);
            setTextColor(currentTextColor);
        }
        if (this.innerShadows.size() > 0) {
            generateTempCanvas();
            TextPaint paint2 = getPaint();
            Iterator it2 = this.innerShadows.iterator();
            while (it2.hasNext()) {
                Shadow shadow2 = (Shadow) it2.next();
                setTextColor(shadow2.color);
                super.onDraw(this.tempCanvas);
                setTextColor(ViewCompat.MEASURED_STATE_MASK);
                paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                paint2.setMaskFilter(new BlurMaskFilter(shadow2.r, BlurMaskFilter.Blur.NORMAL));
                this.tempCanvas.save();
                this.tempCanvas.translate(shadow2.dx, shadow2.dy);
                super.onDraw(this.tempCanvas);
                this.tempCanvas.restore();
                canvas.drawBitmap(this.tempBitmap, 0.0f, 0.0f, null);
                this.tempCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                paint2.setXfermode(null);
                paint2.setMaskFilter(null);
                setTextColor(currentTextColor);
                setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            }
        }
        if (compoundDrawables != null) {
            setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        }
        setBackgroundDrawable(background);
        setTextColor(currentTextColor);
        unfreeze();
    }

    private void generateTempCanvas() {
        String format = String.format("%dx%d", new Object[]{Integer.valueOf(getWidth()), Integer.valueOf(getHeight())});
        Pair pair = (Pair) this.canvasStore.get(format);
        if (pair != null) {
            this.tempCanvas = (Canvas) pair.first;
            this.tempBitmap = (Bitmap) pair.second;
            return;
        }
        this.tempCanvas = new Canvas();
        this.tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        this.tempCanvas.setBitmap(this.tempBitmap);
        this.canvasStore.put(format, new Pair(this.tempCanvas, this.tempBitmap));
    }

    public void freeze() {
        this.lockedCompoundPadding = new int[]{getCompoundPaddingLeft(), getCompoundPaddingRight(), getCompoundPaddingTop(), getCompoundPaddingBottom()};
        this.frozen = true;
    }

    public void unfreeze() {
        this.frozen = false;
    }

    public void requestLayout() {
        if (!this.frozen) {
            super.requestLayout();
        }
    }

    public void postInvalidate() {
        if (!this.frozen) {
            super.postInvalidate();
        }
    }

    public void postInvalidate(int i, int i2, int i3, int i4) {
        if (!this.frozen) {
            super.postInvalidate(i, i2, i3, i4);
        }
    }

    public void invalidate() {
        if (!this.frozen) {
            super.invalidate();
        }
    }

    public void invalidate(Rect rect) {
        if (!this.frozen) {
            super.invalidate(rect);
        }
    }

    public void invalidate(int i, int i2, int i3, int i4) {
        if (!this.frozen) {
            super.invalidate(i, i2, i3, i4);
        }
    }

    public int getCompoundPaddingLeft() {
        return !this.frozen ? super.getCompoundPaddingLeft() : this.lockedCompoundPadding[0];
    }

    public int getCompoundPaddingRight() {
        return !this.frozen ? super.getCompoundPaddingRight() : this.lockedCompoundPadding[1];
    }

    public int getCompoundPaddingTop() {
        return !this.frozen ? super.getCompoundPaddingTop() : this.lockedCompoundPadding[2];
    }

    public int getCompoundPaddingBottom() {
        return !this.frozen ? super.getCompoundPaddingBottom() : this.lockedCompoundPadding[3];
    }
}

