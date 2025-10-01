package com.random.lucky.spin.wheel.randompicker.default_spin_wheel.detail;

import static com.random.lucky.spin.wheel.randompicker.AppConfiguration.SCREEN;
import static com.random.lucky.spin.wheel.randompicker.AppConfiguration.SPIN_WHEEL_MODEL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.custom_view.DefaultCircleDrawable;
import com.random.lucky.spin.wheel.randompicker.custom_view.OutlineTextView;
import com.random.lucky.spin.wheel.randompicker.custom_view.SelecterOverlayDrawable;
import com.random.lucky.spin.wheel.randompicker.databinding.ActivityDefaultDetailsBinding;
import com.random.lucky.spin.wheel.randompicker.default_spin_wheel.edit.DefaultEditActivity;
import com.random.lucky.spin.wheel.randompicker.language.SystemUtils;
import com.random.lucky.spin.wheel.randompicker.main.MainActivity;
import com.random.lucky.spin.wheel.randompicker.rate.SharePrefUtils;
import com.random.lucky.spin.wheel.randompicker.room_database.ItemSpinModel;
import com.random.lucky.spin.wheel.randompicker.room_database.SpinWheelModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class DefaultDetailsActivity extends MyBaseActivity {
    private ActivityDefaultDetailsBinding binding;
    private SpinWheelModel spinWheelModel, spinWheelModelOriginal;
    private boolean isSpinning = false;
    private int fromDegrees = 0;
    private int toDegrees;
    private float wedgeSize;
    private String fileShare;
    private AlertDialog alertDialog;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getAction().equals("SpinWheelModel")) {
                initModel(intent);
                initView();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDefaultDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initModel(getIntent());
        initView();
        viewListener();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("SpinWheelModel"));
        initAlertDialog();
    }

    private void initAlertDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.Grant_Permission));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setMessage(getString(R.string.Please_grant_all_permissions));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.Go_to_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1112);
                }
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1111 || requestCode == 1112) {
            int count = 0;
            for (int i : grantResults) {
                if (i == -1) {
                    count++;
                }
            }
            if (count > 0) {
                alertDialog.show();
            } else {

            }
        }
    }

    private void initModel(Intent intent) {
        SpinWheelModel spinWheelModelGet = (SpinWheelModel) intent.getSerializableExtra(SPIN_WHEEL_MODEL);
        if (spinWheelModelGet != null) {
            setSpinWheelModel(spinWheelModelGet);
            Log.d("TAG", "onResume: " + spinWheelModel.getListItemSpin().size());
            setSpinWheelModelOriginal(spinWheelModel);
            //check
            if (spinWheelModel.getListItemSpin().size() > 0) {
                binding.viewWheel.setVisibility(View.VISIBLE);
                binding.viewOverlay.setVisibility(View.VISIBLE);
            } else {
                binding.viewWheel.setVisibility(View.GONE);
                binding.viewOverlay.setVisibility(View.GONE);
            }
            //repeat
            spinWheelModel.setListItemSpin(addListItemSpinModel(spinWheelModel.getRepeat(), spinWheelModel.getListItemSpin()));
            Log.d("TAG", "onResume: " + spinWheelModel.getListItemSpin().size());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharePrefUtils.isRated(this)) {
            binding.ivRate.setVisibility(View.GONE);
        } else {
            binding.ivRate.setVisibility(View.VISIBLE);
        }
    }

    private void setSpinWheelModel(SpinWheelModel spinWheelModel) {
        this.spinWheelModel = new SpinWheelModel();
        this.spinWheelModel.setId(spinWheelModel.getId());
        this.spinWheelModel.setName(spinWheelModel.getName());
        this.spinWheelModel.setTime(spinWheelModel.getTime());
        this.spinWheelModel.setRepeat(spinWheelModel.getRepeat());
        this.spinWheelModel.setImgPreview(spinWheelModel.getImgPreview());
        this.spinWheelModel.setImgBackground(spinWheelModel.getImgBackground());
        this.spinWheelModel.setTextSize(spinWheelModel.getTextSize());
        this.spinWheelModel.setType(spinWheelModel.getType());
        this.spinWheelModel.setListItemSpin(spinWheelModel.getListItemSpin());
    }

    private void setSpinWheelModelOriginal(SpinWheelModel spinWheelModel) {
        this.spinWheelModelOriginal = new SpinWheelModel();
        this.spinWheelModelOriginal.setId(spinWheelModel.getId());
        this.spinWheelModelOriginal.setName(spinWheelModel.getName());
        this.spinWheelModelOriginal.setTime(spinWheelModel.getTime());
        this.spinWheelModelOriginal.setRepeat(spinWheelModel.getRepeat());
        this.spinWheelModelOriginal.setImgPreview(spinWheelModel.getImgPreview());
        this.spinWheelModelOriginal.setImgBackground(spinWheelModel.getImgBackground());
        this.spinWheelModelOriginal.setTextSize(spinWheelModel.getTextSize());
        this.spinWheelModelOriginal.setType(spinWheelModel.getType());
        this.spinWheelModelOriginal.setListItemSpin(spinWheelModel.getListItemSpin());
    }

    private ArrayList<ItemSpinModel> addListItemSpinModel(int repeat, ArrayList<ItemSpinModel> list) {
        ArrayList<ItemSpinModel> listItemSpinModel = new ArrayList<>();
        for (int i = 0; i < repeat; i++) {
            listItemSpinModel.addAll(list);
        }
        return listItemSpinModel;
    }

    private void initView() {
        if (spinWheelModel != null) {
            binding.toolbarText.setText(spinWheelModel.getName());
        }
        binding.viewWheel.postDelayed(this::createWheel, 500);
        binding.viewWheel.setOnClickListener(view -> {
            if (!isSpinning) {
                binding.ivShare.setVisibility(View.GONE);
                spinWheel();
            }
        });
    }

    private void viewListener() {
        binding.imgHome.setOnClickListener(view -> {
            startActivity(new Intent(DefaultDetailsActivity.this, MainActivity.class));
            finishAffinity();
        });
        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.ivRate.setOnClickListener(view -> {
            SystemUtils.showDialogRate(false, DefaultDetailsActivity.this, new SystemUtils.IOnRateDone() {
                @Override
                public void onRateDone() {
                    binding.ivRate.setVisibility(View.GONE);
                }

                @Override
                public void onLater() {

                }
            }, DefaultDetailsActivity.class);
        });
        binding.ivEdit.setOnClickListener(view -> {
            Intent myIntent = new Intent(DefaultDetailsActivity.this, DefaultEditActivity.class);
            myIntent.putExtra(SPIN_WHEEL_MODEL, spinWheelModelOriginal);
            myIntent.putExtra(SCREEN, "DefaultDetailsActivity");
            startActivity(myIntent);
        });
        binding.ivShare.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != 0) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1111);
                } else {
                    startFileShareIntent(fileShare);
                }
            } else {
                startFileShareIntent(fileShare);
            }
        });
    }

    public void createWheel() {
        if (spinWheelModel.getListItemSpin().size() > 0) {
            binding.viewWheel.getLayoutParams().width = getWindow().getDecorView().getMeasuredWidth();
            binding.viewWheel.getLayoutParams().height = getWindow().getDecorView().getMeasuredHeight();
            binding.viewWheel.setLayoutParams(binding.viewWheel.getLayoutParams());
            int width = binding.viewWheel.getWidth() / 2;
            int height = binding.viewWheel.getHeight() / 2;
            int i = width < height ? width : height;
            if (i <= 0) {
                ((TextView) findViewById(R.id.toolbar_text)).setText(R.string.main_wheel_error);
                return;
            }
            int i2 = i * 2;
            binding.viewWheel.getLayoutParams().height = i2;
            binding.viewWheel.setLayoutParams(binding.viewWheel.getLayoutParams());
            binding.viewOverlay.getLayoutParams().height = i2;
            binding.viewOverlay.setLayoutParams(binding.viewWheel.getLayoutParams());
            wedgeSize = (float) (360f / (float) spinWheelModel.getListItemSpin().size());
            float f = wedgeSize;
            float size = wedgeSize * ((float) spinWheelModel.getListItemSpin().size());
            while (size < 360.0f) {
                f += 1.0f;
                size = ((float) spinWheelModel.getListItemSpin().size()) * f;
            }
            wedgeSize = f;
            View view = binding.viewWheel;
            float f2 = (float) width;
            float f3 = (float) height;
            float f4 = (float) i;
            DefaultCircleDrawable defaultCircleDrawable = new DefaultCircleDrawable(f2, f3, f4, wedgeSize, spinWheelModel, this, spinWheelModel.getTextSize());
            view.setBackground(defaultCircleDrawable);
            binding.viewOverlay.setBackground(new SelecterOverlayDrawable(f2, f3, f4));
        }
    }

    public void spinWheel() {
        binding.txtWinner.setText("");
        this.isSpinning = true;
        toDegrees = getRandomDegrees();
        RotateAnimation rotateAnimation = new RotateAnimation((float) fromDegrees, (float) toDegrees, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(spinWheelModel.getTime() * 1000L);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                int access$300 = toDegrees;
                while (access$300 > 360) {
                    access$300 -= 360;
                }
                fromDegrees = access$300;
                displayWinner((float) access$300);
                isSpinning = false;
            }
        });
        binding.viewWheel.setAnimation(rotateAnimation);
        binding.viewWheel.startAnimation(rotateAnimation);
    }

    public void displayWinner(float f) {
        int size = spinWheelModel.getListItemSpin().size();
        int i = 0;
        while (true) {
            if (i >= size) {
                i = 0;
                break;
            }
            float f2 = (float) i;
            if (f >= wedgeSize * f2 && f < (f2 * this.wedgeSize) + this.wedgeSize) {
                break;
            }
            i++;
        }

        TextView textView = (TextView) findViewById(R.id.txtWinner);
        textView.setText(spinWheelModel.getListItemSpin().get(i).getItemName());

        if (textView.getText() != null) {
            Dialog dialog = new Dialog(this, R.style.CustomDialogAddShortCut);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_show_result);

            ImageView imgGifBgHome = dialog.findViewById(R.id.imgGifBgHome);
            //Glide.with(this).asGif().load(R.drawable.animation_result).into(imgGifBgHome);

            OutlineTextView txtResult = dialog.findViewById(R.id.txtResult);
            RelativeLayout rlResult = dialog.findViewById(R.id.rlResult);
            OutlineTextView txtTitle = dialog.findViewById(R.id.txtTitle);
            txtTitle.setText(spinWheelModel.getName());
            txtResult.setText(spinWheelModel.getListItemSpin().get(i).getItemName());
            rlResult.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        //share
        new Handler().postDelayed(() -> {
            binding.ivShare.setVisibility(View.VISIBLE);
            takeScreenshot();
        }, 1000);
    }

    private int getRandomDegrees() {
        return new Random().nextInt(6000) + 4000;
    }

    private void startFileShareIntent(String filePath) { // pass the file path where the actual file is located.
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");// "*/*" will accepts all types of files, if you want specific then change it on your need.

        shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Sharing file"
        );
        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Sharing file with some description"
        );
        File file = new File(filePath);
        Uri fileURI = FileProvider.getUriForFile(
                this, this.getPackageName() + ".provider",
                file
        );
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileURI);
        startActivity(shareIntent);
    }

    private void takeScreenshot() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
        String dateNow = sdf.format(now);
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Download/SpinWheel");
            myDir.mkdirs();

            String fname = dateNow + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            file.createNewFile();

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            FileOutputStream outputStream = new FileOutputStream(file);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            //Toast.makeText(getBaseContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            fileShare = file.getAbsolutePath();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }
}
