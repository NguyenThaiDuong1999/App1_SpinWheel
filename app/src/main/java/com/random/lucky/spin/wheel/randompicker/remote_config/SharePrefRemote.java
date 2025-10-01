package com.random.lucky.spin.wheel.randompicker.remote_config;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefRemote {
    public static String collap_reload_interval = "collap_reload_interval";
    public static String banner_splash = "banner_splash";
    public static String open_splash = "open_splash";
    public static String inter_splash = "inter_splash";
    public static String native_language = "native_language";
    public static String native_intro = "native_intro";
    public static String inter_intro = "inter_intro";
    public static String appopen_resume = "appopen_resume";
    public static String banner_all = "banner_all";
    public static String inter_back = "inter_back";
    public static String native_home = "native_home";
    public static String collapse_banner_home = "collapse_banner_home";
    public static String inter_predefined = "inter_predefined";
    public static String inter_picker = "inter_picker";
    public static String native_picker = "native_picker";
    public static String rewarded_predefined = "rewarded_predefined";
    public static String native_custom_predefined = "native_custom_predefined";
    public static String collapse_banner_custom_predefined = "collapse_banner_custom_predefined";
    public static String inter_custom = "inter_custom";
    public static String native_spin = "native_spin";
    public static String inter_spin = "inter_spin";
    public static String inter_detail = "inter_detail";
    public static String interval_between_interstitial = "interval_between_interstitial";
    public static String interval_interstitial_from_start = "interval_interstitial_from_start";
    public static String rate_aoa_inter_splash  = "rate_aoa_inter_splash ";
    public static boolean get_config(Context context, String name_config) {
        SharedPreferences pre = context.getSharedPreferences("remote_fill", Context.MODE_PRIVATE);
        if (name_config.equals("style_screen"))
            return pre.getBoolean(name_config, false);
        else
            return pre.getBoolean(name_config, true);
    }

    public static void set_config(Context context, String name_config, boolean config) {
        SharedPreferences pre = context.getSharedPreferences("remote_fill", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean(name_config, config);
        editor.apply();
    }

    public static void set_config(Context context, String name_config, long config) {
        SharedPreferences pre = context.getSharedPreferences("remote_fill", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putLong(name_config, config);
        editor.apply();
    }

    public static long get_config_long(Context context, String name_config) {
        SharedPreferences pre = context.getSharedPreferences("remote_fill", Context.MODE_PRIVATE);
        return pre.getLong(name_config, 15);
    }

    public static String get_config_string(Context context, String name_config) {
        SharedPreferences pre = context.getSharedPreferences("remote_fill", Context.MODE_PRIVATE);
        return pre.getString(name_config, "");
    }

    public static void set_config_string(Context context, String name_config, String config) {
        SharedPreferences pre = context.getSharedPreferences("remote_fill", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(name_config, config);
        editor.apply();
    }
}