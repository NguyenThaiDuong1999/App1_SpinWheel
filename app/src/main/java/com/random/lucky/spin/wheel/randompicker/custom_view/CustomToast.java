package com.random.lucky.spin.wheel.randompicker.custom_view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.random.lucky.spin.wheel.randompicker.R;


public class CustomToast {
    static Toast currentToast = null;

    public static void toast(String mes, Context context) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        currentToast = new Toast(context.getApplicationContext());

        currentToast.setGravity(Gravity.CENTER, 0, 20);
        currentToast.setDuration(Toast.LENGTH_LONG);
        currentToast.setView(layout);

        TextView text = layout.findViewById(R.id.text);
        text.setText(mes);

        currentToast.show();
        if (layout != null) {
            layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (currentToast != null) {
                        currentToast.cancel();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public static void toastNormal(String mes, Context context) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(context, mes, Toast.LENGTH_SHORT);
        currentToast.show();
    }
}
