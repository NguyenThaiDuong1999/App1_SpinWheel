package com.random.lucky.spin.wheel.randompicker.tutorial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.main.MainActivity;

public class TutorialActivity extends MyBaseActivity {

    private ViewPager viewPager;
    LinearLayout dotsLayout;
    int[] layouts;
    private ImageView[] dots;
    private MyViewPagerAdapter myViewPagerAdapter;

    ImageView imgNext1, imgNext2;
    private RelativeLayout imgDone;
    private FrameLayout frAds;

    @SuppressLint("WrongConstant")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(getResources().getColor(R.color.status));
        }
        setContentView((int) R.layout.activity_tutorial);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        imgNext1 = findViewById(R.id.imageNext1);
        imgNext2 = findViewById(R.id.imageNext2);
        imgDone = findViewById(R.id.imgDone);
        frAds = findViewById(R.id.fr_ads);

        layouts = new int[]{R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};
        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        imgNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(1);
                viewPager.setCurrentItem(current);
            }
        });

        imgNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(1);
                viewPager.setCurrentItem(current);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        int countCheckInHome = sharedPreferences.getInt("COUNT_CHECK_IN_HOME", 0);
        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorialActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void addBottomDots(int currentPage) {
        if (dotsLayout != null) {
            dotsLayout.removeAllViews();
        }
        dots = new ImageView[layouts.length];
        for (int i = 0; i < layouts.length; i++) {
            dots[i] = new ImageView(this);
            if (i == currentPage)
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.bg_select));
            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.bg_not_select));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            dotsLayout.addView(dots[i], params);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == 0) {
                imgNext1.setVisibility(View.VISIBLE);
                imgNext2.setVisibility(View.INVISIBLE);
                imgDone.setVisibility(View.INVISIBLE);
            } else if (position == layouts.length - 1) {
                imgNext1.setVisibility(View.INVISIBLE);
                imgNext2.setVisibility(View.INVISIBLE);
                imgDone.setVisibility(View.VISIBLE);
            } else {
                imgNext1.setVisibility(View.INVISIBLE);
                imgNext2.setVisibility(View.VISIBLE);
                imgDone.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

