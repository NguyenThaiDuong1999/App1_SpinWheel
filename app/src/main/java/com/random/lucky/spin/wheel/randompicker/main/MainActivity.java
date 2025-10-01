package com.random.lucky.spin.wheel.randompicker.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding4.view.RxView;
import com.random.lucky.spin.wheel.randompicker.AppConfiguration;
import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.Utils.ObjectApp;
import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.custom_spin_wheel.CustomSpinWheelActivity;
import com.random.lucky.spin.wheel.randompicker.default_spin_wheel.DefaultSpinWheelActivity;
import com.random.lucky.spin.wheel.randompicker.finger_picker.FingerPickerActivity;
import com.random.lucky.spin.wheel.randompicker.language.LanguageActivity;
import com.random.lucky.spin.wheel.randompicker.language.SystemUtils;
import com.random.lucky.spin.wheel.randompicker.rate.SharePrefUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MainActivity extends MyBaseActivity {
    LinearLayout btnCustom;
    LinearLayout btnDefault;
    LinearLayout btnFingerPicker;
    Typeface face;
    ImageView imgGifBg;
    LinearLayout llRate, llShare, llContact, llPrivacy, llLanguage;
    private FrameLayout frAds;
    int numberShowRate = 0;
    private TextView tvTitle, tvDefault, tvCustom;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("COUNT_CHECK_IN_HOME", sharedPreferences.getInt("COUNT_CHECK_IN_HOME", 0) + 1).apply();

        this.llRate = findViewById(R.id.ll_rate);
        this.llShare = findViewById(R.id.llShare);
        this.llContact = findViewById(R.id.llContact);
        this.llPrivacy = findViewById(R.id.llPrivacy);
        this.llLanguage = findViewById(R.id.llLanguage);
        this.imgGifBg = findViewById(R.id.imgGifBgHome);
        this.tvTitle = findViewById(R.id.tv_title);
        this.tvDefault = findViewById(R.id.tv_default);
        this.tvCustom = findViewById(R.id.tv_custom);
        this.btnFingerPicker = findViewById(R.id.btn_home_finger_picker);

        tvTitle.setSelected(true);
        tvDefault.setSelected(true);
        tvCustom.setSelected(true);

        this.face = Typeface.createFromAsset(getAssets(), "fonts/CarterOne.ttf");
        this.btnDefault = (LinearLayout) findViewById(R.id.btnDefault);
        this.btnCustom = (LinearLayout) findViewById(R.id.btnCustom);

        frAds = findViewById(R.id.fr_ads);
        btnFingerPicker.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, FingerPickerActivity.class));
        });
        this.btnDefault.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DefaultSpinWheelActivity.class));
            }
        });
        this.btnCustom.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CustomSpinWheelActivity.class));
            }
        });

        //
        RxView.clicks(llRate).throttleFirst(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(unit -> {
            showDialogRateMenu();
        });

        RxView.clicks(llShare).throttleFirst(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(unit -> {
            if (MainActivity.this.isOnline()) {
                Intent intent6 = new Intent("android.intent.action.SEND");
                intent6.setType("text/plain");
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Hi! I'm using a great Spin To Decide (Decision Roulette) application. Check it out: https://play.google.com/store/apps/details?id=");
                sb2.append(MainActivity.this.getPackageName());
                intent6.putExtra("android.intent.extra.TEXT", sb2.toString());
                intent6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainActivity.this.startActivity(Intent.createChooser(intent6, getString(R.string.share_with_friends)));
            } else {
                Toast makeText2 = Toast.makeText(MainActivity.this.getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT);
                makeText2.setGravity(17, 0, 0);
                makeText2.show();
            }
        });

        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(ObjectApp.moreApp));
                startActivity(intent);
            }
        });

        llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(ObjectApp.policy));
                startActivity(intent1);
            }
        });

        llLanguage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LanguageActivity.class);
            intent.putExtra("StartScreen", "MainActivity");
            startActivity(intent);
        });
    }

    public boolean isOnline() {
        @SuppressLint({"WrongConstant", "MissingPermission"}) NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        int count = AppConfiguration.getCountOpenApp(this);

        if (numberShowRate != 0) {
            showAppExitDialog();
            return;
        }
        if (count == 2 || count == 4 || count == 6 || count == 8 || count == 10) {
            if (!SharePrefUtils.isRated(MainActivity.this)) {
                showDialogRate();
                numberShowRate++;
            } else {
                showAppExitDialog();
            }
        } else {
            showAppExitDialog();
        }
    }

    private void showDialogRate() {
        /*RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .session(1)
                .date(1)
                .setNameApp(getString(R.string.app_name))
                .setIcon(R.drawable.ic_logo)
                .setEmail(ObjectApp.email)
                .isShowButtonLater(true)
                .isClickLaterDismiss(true)
                .setTextButtonLater("Maybe Later")
                .setOnlickMaybeLate(new MaybeLaterCallback() {
                    @Override
                    public void onClick() {
                        finish();
                    }
                })
                .ratingButtonColor(R.color.text_color)
                .build();

        //Cancel On Touch Outside
        ratingDialog.setCanceledOnTouchOutside(false);
        //show
        ratingDialog.show();*/


        SystemUtils.showDialogRate(false, MainActivity.this, new SystemUtils.IOnRateDone() {
            @Override
            public void onRateDone() {
                llRate.setVisibility(View.GONE);
            }

            @Override
            public void onLater() {

            }
        }, MainActivity.class);
    }

    private void showAppExitDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_exit);

        LinearLayout ivNo = dialog.findViewById(R.id.ivNo);
        LinearLayout ivYes = dialog.findViewById(R.id.ivYes);

        final FrameLayout flContainer = dialog.findViewById(R.id.flContainer);


        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
                finish();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void showDialogRateMenu() {
        /*RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .session(1)
                .date(1)
                .setNameApp(getString(R.string.app_name))
                .setIcon(R.drawable.ic_logo)
                .setEmail(ObjectApp.email)
                .isShowButtonLater(true)
                .isClickLaterDismiss(true)
                .setTextButtonLater("Maybe Later")
                .setOnlickMaybeLate(new MaybeLaterCallback() {
                    @Override
                    public void onClick() {
                    }
                })
                .ratingButtonColor(R.color.text_color)
                .build();

        //Cancel On Touch Outside
        ratingDialog.setCanceledOnTouchOutside(false);
        //show
        ratingDialog.show();*/

        SystemUtils.showDialogRate(false, MainActivity.this, new SystemUtils.IOnRateDone() {
            @Override
            public void onRateDone() {
                llRate.setVisibility(View.GONE);
            }

            @Override
            public void onLater() {

            }
        }, MainActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharePrefUtils.isRated(this)) {
            llRate.setVisibility(View.GONE);
        } else {
            llRate.setVisibility(View.VISIBLE);
        }
    }
}

