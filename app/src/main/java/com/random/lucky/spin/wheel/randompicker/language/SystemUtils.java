package com.random.lucky.spin.wheel.randompicker.language;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.rate.RatingDialog;
import com.random.lucky.spin.wheel.randompicker.rate.SharePrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SystemUtils {
    public static boolean checkAppOpen = false;

    private static ReviewManager manager = null;
    private static ReviewInfo reviewInfo = null;

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private static Locale myLocale;

    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    public static void setLocale(Context context) {
        String language = getPreLanguage(context);
        if (language.equals("")) {
            Configuration config = new Configuration();
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            config.locale = locale;
            context.getResources()
                    .updateConfiguration(config, context.getResources().getDisplayMetrics());
        } else {
            changeLang(language, context);
        }
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    public static void changeLang(String lang, Context context) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(context, lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void saveLocale(Context context, String lang) {
        setPreLanguage(context, lang);
    }

    public static String getPreLanguage(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("MY_PRE", Context.MODE_MULTI_PROCESS);
        Locale.getDefault().getDisplayLanguage();
        String lang;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lang = Resources.getSystem().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            lang = Resources.getSystem().getConfiguration().locale.getLanguage();
        }
        if (!getLanguageApp().contains(lang)) {
            return preferences.getString("KEY_LANGUAGE", "en");
        } else {
            return preferences.getString("KEY_LANGUAGE", lang);
        }
    }

    public static void setPreLanguage(Context context, String language) {
        if (language == null || language.equals("")) {
            return;
        } else {
            SharedPreferences preferences = context.getSharedPreferences("MY_PRE", Context.MODE_MULTI_PROCESS);
            preferences.edit().putString("KEY_LANGUAGE", language).apply();
        }
    }

    public static List<String> getLanguageApp() {
        List<String> languages = new ArrayList<>();
        languages.add("en");
        languages.add("de");
        languages.add("fr");
        languages.add("pt");
        languages.add("es");
        languages.add("in");
        languages.add("hi");
        return languages;
    }

    public static void setPreIntroOrLanguage(Context context, boolean isCheck) {
        SharedPreferences preferences = context.getSharedPreferences("MY_PRE", Context.MODE_MULTI_PROCESS);
        preferences.edit().putBoolean("PreIntroOrLanguage", true).apply();
    }

    public static boolean getPreIntroOrLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MY_PRE", Context.MODE_MULTI_PROCESS);
        return preferences.getBoolean("PreIntroOrLanguage", false);
    }

    public interface IOnRateDone {
        void onRateDone();

        void onLater();
    }

    public static void showDialogRate(boolean finish, Activity activity, IOnRateDone iOnRateDone, @NonNull Class activityClass) {
        SystemUtils.setLocale(activity);
        RatingDialog ratingDialog = new RatingDialog(activity);
        ratingDialog.init(activity, new RatingDialog.OnPress() {
            @Override
            public void send(float rate) {
                SharePrefUtils.forceRated(activity);
                ratingDialog.dismiss();
                iOnRateDone.onRateDone();
                Toast.makeText(activity, activity.getString(R.string.Thank_you), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rating(float rate) {
                manager = ReviewManagerFactory.create(activity);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    iOnRateDone.onRateDone();
                    if (task.isSuccessful()) {
                        reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                        flow.addOnSuccessListener(aVoid -> {
                            SharePrefUtils.forceRated(activity);
                            ratingDialog.dismiss();
                            if (finish) {
                                activity.finishAffinity();
                            }
                        });
                    } else {
                        ratingDialog.dismiss();
                        if (finish) {
                            activity.finishAffinity();
                        }
                    }
                });
            }

            @Override
            public void cancel() {
                // Empty implementation
            }

            @Override
            public void later() {
                iOnRateDone.onLater();
                ratingDialog.dismiss();
                if (finish) {
                    SharePrefUtils.increaseCountOpenApp(activity);
                    activity.finishAffinity();
                }
            }
        });

        try {
            ratingDialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }
}
