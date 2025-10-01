package com.random.lucky.spin.wheel.randompicker.default_spin_wheel;

import static com.random.lucky.spin.wheel.randompicker.AppConfiguration.SPIN_WHEEL_MODEL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;

import com.random.lucky.spin.wheel.randompicker.AppConfiguration;
import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.Utils.ObjectApp;
import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.custom_view.CustomToast;
import com.random.lucky.spin.wheel.randompicker.databinding.ActivityDefaultSpinWheelBinding;
import com.random.lucky.spin.wheel.randompicker.default_spin_wheel.detail.DefaultDetailsActivity;
import com.random.lucky.spin.wheel.randompicker.default_spin_wheel.edit.DefaultEditActivity;
import com.random.lucky.spin.wheel.randompicker.language.LanguageActivity;
import com.random.lucky.spin.wheel.randompicker.language.SystemUtils;
import com.random.lucky.spin.wheel.randompicker.rate.SharePrefUtils;
import com.random.lucky.spin.wheel.randompicker.room_database.AppDatabase;
import com.random.lucky.spin.wheel.randompicker.room_database.SpinWheelModel;

import java.util.ArrayList;

public class DefaultSpinWheelActivity extends MyBaseActivity {
    private ActivityDefaultSpinWheelBinding binding;
    private DefaultSpinWheelAdapter defaultSpinWheelAdapter;
    private ArrayList<SpinWheelModel> listSpinWheelModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDefaultSpinWheelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvTitle.setSelected(true);
        viewListener();
    }

    private void viewListener() {
        binding.llBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.llMore.setOnClickListener(view -> {
            onClickMore();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listSpinWheelModel = new ArrayList<>();
        listSpinWheelModel = (ArrayList<SpinWheelModel>) AppDatabase.getInstance(this).spinWheelDAO().getAllSpinWheelByType(AppConfiguration.DEFAULT);
        if (listSpinWheelModel != null) {
            defaultSpinWheelAdapter = new DefaultSpinWheelAdapter(this, listSpinWheelModel, new DefaultSpinWheelAdapter.IOnClickSpinWheel() {
                @Override
                public void onClickItem(SpinWheelModel spinWheelModel, int position) {
                    Intent myIntent = new Intent(DefaultSpinWheelActivity.this, DefaultDetailsActivity.class);
                    myIntent.putExtra(SPIN_WHEEL_MODEL, spinWheelModel);
                    startActivity(myIntent);
                }

                @Override
                public void onClickEdit(SpinWheelModel spinWheelModel, int position) {
                    Intent myIntent = new Intent(DefaultSpinWheelActivity.this, DefaultEditActivity.class);
                    myIntent.putExtra(SPIN_WHEEL_MODEL, spinWheelModel);
                    startActivity(myIntent);
                }
            });
            binding.recyclerView.setAdapter(defaultSpinWheelAdapter);
        }
    }

    private void onClickMore() {
        PopupMenu popupMenu = new PopupMenu(DefaultSpinWheelActivity.this, binding.llMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();
        MenuItem menuRate = menu.findItem(R.id.rate);
        menuRate.setVisible(!SharePrefUtils.isRated(this));
        SystemUtils.setLocale(DefaultSpinWheelActivity.this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("WrongConstant")
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.change_language) {
                    Intent intent2 = new Intent(DefaultSpinWheelActivity.this, LanguageActivity.class);
                    // Note: 67108864 is Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent2.addFlags(67108864);
                    startActivity(intent2);
                    return true;
                } else if (menuItem.getItemId() == R.id.contact) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ObjectApp.moreApp));
                    startActivity(intent);
                    return true;
                } else if (menuItem.getItemId() == R.id.privacy) {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(ObjectApp.policy));
                    startActivity(intent1);
                    return true;
                } else if (menuItem.getItemId() == R.id.rate) {
                    showDialogRateMenu();
                    return true;
                } else if (menuItem.getItemId() == R.id.share) {
                    if (isOnline()) {
                        Intent intent6 = new Intent(Intent.ACTION_SEND); // Using constant is better practice
                        intent6.setType("text/plain");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Hi! I'm using a great Spin To Decide (Decision Roulette) application. Check it out: https://play.google.com/store/apps/details?id=");
                        sb2.append(getPackageName());
                        intent6.putExtra(Intent.EXTRA_TEXT, sb2.toString());
                        // Note: 67108864 is Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent6.addFlags(67108864);
                        startActivity(Intent.createChooser(intent6, "Share with Friends"));
                    } else {
                        CustomToast.toast(getString(R.string.no_internet_connection), DefaultSpinWheelActivity.this);
                    }
                    return true;
                } else {
                    // This handles the 'default' case
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showDialogRateMenu() {
        SystemUtils.showDialogRate(false, DefaultSpinWheelActivity.this, new SystemUtils.IOnRateDone() {
            @Override
            public void onRateDone() {

            }

            @Override
            public void onLater() {

            }
        }, DefaultSpinWheelActivity.class);
    }

    public boolean isOnline() {
        @SuppressLint({"WrongConstant", "MissingPermission"}) NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }
}
