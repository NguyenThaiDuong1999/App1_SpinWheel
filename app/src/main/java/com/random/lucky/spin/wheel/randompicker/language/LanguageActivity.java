package com.random.lucky.spin.wheel.randompicker.language;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.main.MainActivity;
import com.random.lucky.spin.wheel.randompicker.tutorial.TutorialActivity;

import java.util.ArrayList;
import java.util.List;

public class LanguageActivity extends MyBaseActivity implements IClickLanguage {
    private LanguageAdapter adapter;
    private LanguageModel model;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private FrameLayout frAds;
    private ImageView ivDone, ivBack, ivDoneCircle;
    private String startScreen = "";
    private FrameLayout fr_ads_banner;
    private TextView tvTitle, tvTitleLangSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtils.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        startScreen = getIntent().getStringExtra("StartScreen");

        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE);
        recyclerView = findViewById(R.id.rcl_language);
        frAds = findViewById(R.id.fr_ads);
        ivDone = findViewById(R.id.iv_done);
        ivBack = findViewById(R.id.iv_back);
        ivDoneCircle = findViewById(R.id.iv_done_circle);
        fr_ads_banner = findViewById(R.id.fr_ads_banner);
        tvTitle = findViewById(R.id.tv_title);
        tvTitleLangSetting = findViewById(R.id.tv_title_lang_setting);

        adapter = new LanguageAdapter(this, setLanguageDefault(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if (startScreen != null && startScreen.equals("MainActivity")) {
            frAds.setVisibility(View.GONE);
            fr_ads_banner.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivDoneCircle.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
            tvTitleLangSetting.setVisibility(View.VISIBLE);
            tvTitleLangSetting.setSelected(true);
            ivDone.setVisibility(View.GONE);
        } else {
            frAds.setVisibility(View.VISIBLE);
            fr_ads_banner.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            ivDoneCircle.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitleLangSetting.setVisibility(View.GONE);
            ivDone.setVisibility(View.VISIBLE);
        }
        ivDone.setOnClickListener(v -> {
            onClickDone();
        });
        ivDoneCircle.setOnClickListener(v -> {
            onClickDone();
        });
        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void onClickDone() {
        if (model != null) {
            SystemUtils.setPreLanguage(this, model.getIsoLanguage());
        }
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("MY_PRE", MODE_PRIVATE).edit();
        editor.putBoolean("nativeLanguage", true);
        editor.apply();
        SystemUtils.setPreIntroOrLanguage(this, true);
        SystemUtils.setLocale(this);
        startNextAct();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(LanguageModel data) {
        adapter.setSelectLanguage(data);
        model = data;
    }

    private List<LanguageModel> setLanguageDefault() {
        List<LanguageModel> lists = new ArrayList<>();
        String key = SystemUtils.getPreLanguage(this);
        lists.add(new LanguageModel("English", "en", R.drawable.ic_english, false));
        lists.add(new LanguageModel("Hindi", "hi", R.drawable.ic_hindi, false));
        lists.add(new LanguageModel("Spanish", "es", R.drawable.ic_spanish, false));
        lists.add(new LanguageModel("French", "fr", R.drawable.ic_france, false));
        lists.add(new LanguageModel("German", "de", R.drawable.ic_german, false));
        lists.add(new LanguageModel("Indonesian", "in", R.drawable.ic_indonesia, false));
        lists.add(new LanguageModel("Portuguese", "pt", R.drawable.ic_portuguese, false));

        for (int i = 0; i < lists.size(); i++) {
            if (key.equals(lists.get(i).getIsoLanguage())) {
                LanguageModel data = lists.get(i);
                data.setCheck(true);
                lists.remove(lists.get(i));
                lists.add(0, data);
                break;
            }
        }
        return lists;
    }

    private void startNextAct() {
        if (startScreen != null && startScreen.equals("MainActivity")) {
            startActivity(new Intent(LanguageActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(LanguageActivity.this, TutorialActivity.class));
        }
        finishAffinity();
    }
}
