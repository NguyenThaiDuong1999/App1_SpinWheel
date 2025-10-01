package com.random.lucky.spin.wheel.randompicker.custom_view.pickers;

import android.content.Context;
import android.util.AttributeSet;

import com.random.lucky.spin.wheel.randompicker.R;
import com.slaviboy.colorpicker.main.ColorPicker;

public class RectangularHSV extends ColorPicker {

    public RectangularHSV(Context context) {
        super(context);
        init(context);
    }

    public RectangularHSV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RectangularHSV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setViews(context, R.layout.color_picker_hsv_rectangular);
    }
}
