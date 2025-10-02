package com.random.lucky.spin.wheel.randompicker.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.random.lucky.spin.wheel.randompicker.R;

import java.lang.reflect.Field;

@SuppressLint("AppCompatCustomView")
public class OutlineTextView extends TextView {
    private Field colorField;
    private int outlineColor;
    private int textColor;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int outlineColor;
        float outlineWidth;
        int textColor;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.textColor = parcel.readInt();
            this.outlineColor = parcel.readInt();
            this.outlineWidth = parcel.readFloat();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.textColor);
            parcel.writeInt(this.outlineColor);
            parcel.writeFloat(this.outlineWidth);
        }
    }

    public OutlineTextView(Context context) {
        this(context, null);
    }

    public OutlineTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842884);
    }

    @SuppressLint("SoonBlockedPrivateApi")
    public OutlineTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        try {
            this.colorField = TextView.class.getDeclaredField("mCurTextColor");
            this.colorField.setAccessible(true);
            this.textColor = getTextColors().getDefaultColor();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.OutlineTextView);
            this.outlineColor = obtainStyledAttributes.getColor(0, 0);
            setOutlineStrokeWidth((float) obtainStyledAttributes.getDimensionPixelSize(1, 0));
            obtainStyledAttributes.recycle();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            this.colorField = null;
        }
    }

    public void setTextColor(int i) {
        this.textColor = i;
        super.setTextColor(i);
    }

    public void setOutlineColor(int i) {
        this.outlineColor = i;
        invalidate();
    }

    public void setOutlineWidth(float f) {
        setOutlineStrokeWidth(f);
        invalidate();
    }

    private void setOutlineStrokeWidth(float f) {
        getPaint().setStrokeWidth((f * 2.0f) + 1.0f);
    }


    public void onDraw(Canvas canvas) {
        if (this.colorField != null) {
            setColorField(this.outlineColor);
            getPaint().setStyle(Paint.Style.STROKE);
            super.onDraw(canvas);
            setColorField(this.textColor);
            getPaint().setStyle(Paint.Style.FILL);
        }
        super.onDraw(canvas);
    }

    private void setColorField(int i) {
        try {
            this.colorField.setInt(this, i);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.textColor = this.textColor;
        savedState.outlineColor = this.outlineColor;
        savedState.outlineWidth = getPaint().getStrokeWidth();
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.textColor = savedState.textColor;
        this.outlineColor = savedState.outlineColor;
        getPaint().setStrokeWidth(savedState.outlineWidth);
    }
}

