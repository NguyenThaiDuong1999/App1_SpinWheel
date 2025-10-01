package com.random.lucky.spin.wheel.randompicker.custom_spin_wheel;

import static com.random.lucky.spin.wheel.randompicker.AppConfiguration.CUSTOM;
import static com.random.lucky.spin.wheel.randompicker.AppConfiguration.SCREEN;
import static com.random.lucky.spin.wheel.randompicker.AppConfiguration.SPIN_WHEEL_MODEL;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.Utils.ObjectApp;
import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.custom_view.CustomToast;
import com.random.lucky.spin.wheel.randompicker.databinding.ActivityCustomSpinWheelBinding;
import com.random.lucky.spin.wheel.randompicker.default_spin_wheel.detail.DefaultDetailsActivity;
import com.random.lucky.spin.wheel.randompicker.default_spin_wheel.edit.DefaultEditActivity;
import com.random.lucky.spin.wheel.randompicker.language.LanguageActivity;
import com.random.lucky.spin.wheel.randompicker.language.SystemUtils;
import com.random.lucky.spin.wheel.randompicker.rate.SharePrefUtils;
import com.random.lucky.spin.wheel.randompicker.room_database.AppDatabase;
import com.random.lucky.spin.wheel.randompicker.room_database.SpinWheelModel;

import java.util.ArrayList;

public class CustomSpinWheelActivity extends MyBaseActivity {
    private ActivityCustomSpinWheelBinding binding;
    private CustomSpinWheelAdapter customSpinWheelAdapter;
    private ArrayList<SpinWheelModel> listSpinWheelModel;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomSpinWheelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvTitle.setSelected(true);
        binding.tvAddNew.setSelected(true);
        viewListener();
    }

    private void viewListener() {
        binding.llBack.setOnClickListener(view -> onBackPressed());
        binding.llAdd.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            addNewCustomSpinWheel();
        });
        binding.llMore.setOnClickListener(view -> {
            onClickMore();
        });
    }

    private void addNewCustomSpinWheel() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_create_list);

        final EditText editText = (EditText) dialog.findViewById(R.id.txtListName);
        editText.setText("");
        editText.selectAll();
        editText.requestFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        LinearLayout ivCancel = dialog.findViewById(R.id.ivCancel);
        LinearLayout ivSave = dialog.findViewById(R.id.ivSave);

        ivSave.setOnClickListener(v -> {
            if (editText.getText().toString().trim().isEmpty()) {
                CustomToast.toast(getString(R.string.list_name_is_not_entered_yet), this);
                return;
            }
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
            SpinWheelModel spinWheelModel = new SpinWheelModel(editText.getText().toString().trim(), 18f, 1, 2, new ArrayList<>(), CUSTOM, R.drawable.customthumb, R.drawable.bg_gradient_yes_no);
            AppDatabase.getInstance(this).spinWheelDAO().insertSpinWheel(spinWheelModel);
            initListCustomSpinWheel();
            CustomToast.toast(getString(R.string.insert_successfully), this);
            dialog.dismiss();
        });

        ivCancel.setOnClickListener(v -> {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
            dialog.cancel();
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListCustomSpinWheel();
    }

    private void initListCustomSpinWheel() {
        listSpinWheelModel = new ArrayList<>();
        listSpinWheelModel = (ArrayList<SpinWheelModel>) AppDatabase.getInstance(this).spinWheelDAO().getAllSpinWheelByType(CUSTOM);
        if (listSpinWheelModel != null) {
            customSpinWheelAdapter = new CustomSpinWheelAdapter(this, listSpinWheelModel, new CustomSpinWheelAdapter.IOnClickSpinWheel() {
                @Override
                public void onClickItem(SpinWheelModel spinWheelModel, int position) {
                    if (spinWheelModel.getListItemSpin().size() > 0) {
                        Intent myIntent = new Intent(CustomSpinWheelActivity.this, DefaultDetailsActivity.class);
                        myIntent.putExtra(SPIN_WHEEL_MODEL, spinWheelModel);
                        startActivity(myIntent);
                    } else {
                        CustomToast.toast(getString(R.string.error_list_no_items), CustomSpinWheelActivity.this);
                    }
                }

                @Override
                public void onClickEdit(SpinWheelModel spinWheelModel, int position) {
                    Intent myIntent = new Intent(CustomSpinWheelActivity.this, DefaultEditActivity.class);
                    myIntent.putExtra(SPIN_WHEEL_MODEL, spinWheelModel);
                    myIntent.putExtra(SCREEN, "CustomSpinWheelActivity");
                    startActivity(myIntent);
                }

                @Override
                public void onClickDelete(SpinWheelModel spinWheelModel, int position) {
                    AppDatabase.getInstance(CustomSpinWheelActivity.this).spinWheelDAO().deleteSpinWheel(spinWheelModel);
                    listSpinWheelModel.remove(spinWheelModel);
                    customSpinWheelAdapter.notifyDataSetChanged();
                    Toast.makeText(CustomSpinWheelActivity.this, R.string.delete_successfully, Toast.LENGTH_SHORT).show();
                }
            });
            binding.recyclerView.setAdapter(customSpinWheelAdapter);
            /*int marginBottomDp = 150;
            binding.recyclerView.addItemDecoration(new LastItemBottomMarginDecorator(this, marginBottomDp));*/
        }
    }

    private void onClickMore() {
        PopupMenu popupMenu = new PopupMenu(CustomSpinWheelActivity.this, binding.llMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();
        MenuItem menuRate = menu.findItem(R.id.rate);
        menuRate.setVisible(!SharePrefUtils.isRated(this));
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("WrongConstant")
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.change_language) {
                    Intent intent2 = new Intent(CustomSpinWheelActivity.this, LanguageActivity.class);
                    // The flag 67108864 is equivalent to Intent.FLAG_ACTIVITY_CLEAR_TOP
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
                        Intent intent6 = new Intent("android.intent.action.SEND");
                        intent6.setType("text/plain");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Hi! I'm using a great Spin To Decide (Decision Roulette) application. Check it out: https://play.google.com/store/apps/details?id=");
                        sb2.append(getPackageName());
                        intent6.putExtra("android.intent.extra.TEXT", sb2.toString());
                        // The flag 67108864 is equivalent to Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent6.addFlags(67108864);
                        startActivity(Intent.createChooser(intent6, "Share with Friends"));
                    } else {
                        CustomToast.toast(getString(R.string.no_internet_connection), CustomSpinWheelActivity.this);
                    }
                    return true;
                } else {
                    // This is the equivalent of the 'default' case
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showDialogRateMenu() {
        SystemUtils.showDialogRate(false, CustomSpinWheelActivity.this, new SystemUtils.IOnRateDone() {
            @Override
            public void onRateDone() {

            }

            @Override
            public void onLater() {

            }
        }, CustomSpinWheelActivity.class);
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
