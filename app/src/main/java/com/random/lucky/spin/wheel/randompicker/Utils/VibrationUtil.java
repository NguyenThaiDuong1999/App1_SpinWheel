package com.random.lucky.spin.wheel.randompicker.Utils;

import android.content.Context;
import android.os.Vibrator;

public class VibrationUtil {

    public static void vibrateOnce(Context context, long milliseconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(milliseconds);
            }
        }
    }
}
