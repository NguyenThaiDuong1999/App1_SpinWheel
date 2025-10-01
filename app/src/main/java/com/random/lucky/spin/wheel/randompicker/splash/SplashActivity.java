package com.random.lucky.spin.wheel.randompicker.splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.language.LanguageActivity;
import com.random.lucky.spin.wheel.randompicker.main.MainActivity;

public class SplashActivity extends MyBaseActivity {
    public Context mContext;
    ProgressBar progressBar;

    private void startNextAct() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)
        ) {
            finish();
            return;
        }

        this.mContext = this;
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.MULTIPLY);

        new Handler().postDelayed(this::startNextAct, 1500);
    }
}

