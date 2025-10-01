package com.random.lucky.spin.wheel.randompicker.default_spin_wheel.edit;

import static com.random.lucky.spin.wheel.randompicker.AppConfiguration.SPIN_WHEEL_MODEL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.custom_view.CustomToast;
import com.random.lucky.spin.wheel.randompicker.custom_view.DefaultCircleDrawablePreview;
import com.random.lucky.spin.wheel.randompicker.custom_view.ItemOffsetDecoration;
import com.random.lucky.spin.wheel.randompicker.custom_view.SelecterOverlayDrawable;
import com.random.lucky.spin.wheel.randompicker.databinding.ActivityEditDefaultSpinWheelBinding;
import com.random.lucky.spin.wheel.randompicker.dialog.DialogAddEditSlice;
import com.random.lucky.spin.wheel.randompicker.room_database.AppDatabase;
import com.random.lucky.spin.wheel.randompicker.room_database.ItemSpinModel;
import com.random.lucky.spin.wheel.randompicker.room_database.SpinWheelModel;

import java.util.ArrayList;
import java.util.Collections;

public class DefaultEditActivity extends MyBaseActivity {
    private ActivityEditDefaultSpinWheelBinding binding;
    private ItemEditSpinAdapter itemEditSpinAdapter;
    private float wedgeSize;
    private DefaultCircleDrawablePreview defaultCircleDrawable;
    private float textSize = 18f;
    private int repeat = 1, time = 1;
    private SpinWheelModel spinWheelModel;
    private ArrayList<ItemSpinModel> listItemSpinModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditDefaultSpinWheelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        spinWheelModel = (SpinWheelModel) getIntent().getSerializableExtra(SPIN_WHEEL_MODEL);
        if (spinWheelModel != null) {
            listItemSpinModel = spinWheelModel.getListItemSpin();
        } else {
            listItemSpinModel = new ArrayList<>();
        }
        initView();
        viewListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        createWheel(textSize);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        time = spinWheelModel.getTime();
        repeat = spinWheelModel.getRepeat();
        textSize = spinWheelModel.getTextSize();
        itemEditSpinAdapter = new ItemEditSpinAdapter(this, listItemSpinModel, new ItemEditSpinAdapter.IOnClick() {
            @Override
            public void onClickItem(ItemSpinModel itemSpinModel, int position) {
                Log.d("TAGx", "onClickItem: " + itemSpinModel.getItemName() + "-" + position);
                DialogAddEditSlice dialogAddEditSlice = new DialogAddEditSlice(DefaultEditActivity.this, itemSpinModel, getString(R.string.edit_slice), new DialogAddEditSlice.IOnClick() {
                    @Override
                    public void onClickDelete() {
                        listItemSpinModel.remove(position);
                        itemEditSpinAdapter.notifyDataSetChanged();
                        spinWheelModel.setListItemSpin(listItemSpinModel);
                        checkListSizeToInitSeekbarRepeat();
                        createWheel(textSize);
                        if (listItemSpinModel.size() > 0) {
                            binding.viewWheel.setVisibility(View.VISIBLE);
                            binding.viewOverlay.setVisibility(View.VISIBLE);
                        } else {
                            binding.viewWheel.setVisibility(View.INVISIBLE);
                            binding.viewOverlay.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onClickSave(String sliceTitle, int colorCode, ItemSpinModel itemSpinModel) {
                        Log.d("TAGx", "onClickSave: " + itemSpinModel.getItemName() + "-" + position);
                        listItemSpinModel.set(position, itemSpinModel);
                        itemEditSpinAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onClickSaveAddNew(String sliceTitle, int colorCode) {
                        //not usage
                        //use for adding new slice
                    }
                });
                dialogAddEditSlice.show();
            }
        });
        binding.recyclerViewSlice.setAdapter(itemEditSpinAdapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, com.intuit.sdp.R.dimen._5sdp);
        binding.recyclerViewSlice.addItemDecoration(itemDecoration);

        binding.viewWheel.postDelayed(() -> createWheel(textSize), 500);
        binding.edtListName.setText(spinWheelModel.getName());
        binding.tvSize.setText(String.valueOf(Math.round(textSize)));
        binding.seekbarTextSize.setProgress(Math.round(textSize));
        binding.tvRepeat.setText(String.valueOf(repeat));
        binding.seekbarRepeat.setProgress(repeat);
        binding.tvTime.setText(time + "s");
        binding.seekbarTime.setProgress(time);
        checkListSizeToInitSeekbarRepeat();
    }

    private void checkListSizeToInitSeekbarRepeat() {
        if (listItemSpinModel.size() < 6) {
            binding.seekbarRepeat.setMax(3);
        } else {
            if (listItemSpinModel.size() <= 8) {
                binding.seekbarRepeat.setMax(2);
                if (spinWheelModel.getRepeat() > 2) {
                    spinWheelModel.setRepeat(2);
                    binding.seekbarRepeat.setProgress(2);
                    binding.tvRepeat.setText(String.valueOf(2));
                }
            } else {
                binding.seekbarRepeat.setMax(1);
                if (spinWheelModel.getRepeat() > 1) {
                    spinWheelModel.setRepeat(1);
                    binding.seekbarRepeat.setProgress(1);
                    binding.tvRepeat.setText(String.valueOf(1));
                }
            }
        }
    }

    private void viewListener() {
        binding.linBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.imgAddNew.setOnClickListener(view -> addNewItem());
        binding.tvAddNew.setOnClickListener(view -> addNewItem());
        binding.imgShuffle.setOnClickListener(view -> {
            Collections.shuffle(listItemSpinModel);
            itemEditSpinAdapter.notifyDataSetChanged();
            createWheel(textSize);
        });
        binding.linSave.setOnClickListener(view -> {
            spinWheelModel.setName(binding.edtListName.getText().toString().trim());
            spinWheelModel.setRepeat(repeat);
            spinWheelModel.setTextSize(textSize);
            spinWheelModel.setTime(time);
            spinWheelModel.setListItemSpin(listItemSpinModel);
            AppDatabase.getInstance(DefaultEditActivity.this).spinWheelDAO().updateSpinWheel(spinWheelModel);
            CustomToast.toastNormal(getString(R.string.saved), this);
            Intent myIntent = new Intent("SpinWheelModel");
            myIntent.putExtra(SPIN_WHEEL_MODEL, spinWheelModel);
            LocalBroadcastManager.getInstance(DefaultEditActivity.this).sendBroadcast(myIntent);
            finish();
        });
        binding.seekbarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textSize = seekBar.getProgress();
                binding.tvSize.setText(String.valueOf((int) textSize));
                createWheel(textSize);
            }
        });
        binding.seekbarRepeat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                repeat = progress;
                binding.tvRepeat.setText(String.valueOf(repeat));
                spinWheelModel.setRepeat(repeat);
                createWheel(textSize);
            }
        });
        binding.seekbarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                time = progress;
                binding.tvTime.setText(progress + "s");
            }
        });
    }

    private void addNewItem() {
        DialogAddEditSlice dialogAddEditSlice = new DialogAddEditSlice(this, getString(R.string.new_slice), new DialogAddEditSlice.IOnClick() {
            @Override
            public void onClickDelete() {

            }

            @Override
            public void onClickSave(String sliceTitle, int colorCode, ItemSpinModel itemSpinModel) {
                //not usage
            }

            @Override
            public void onClickSaveAddNew(String sliceTitle, int colorCode) {
                listItemSpinModel.add(new ItemSpinModel(System.currentTimeMillis(), sliceTitle, colorCode));
                itemEditSpinAdapter.notifyDataSetChanged();
                spinWheelModel.setListItemSpin(listItemSpinModel);
                checkListSizeToInitSeekbarRepeat();
                createWheel(textSize);
            }
        });
        dialogAddEditSlice.show();
    }

    public void createWheel(float textSize) {
        if (spinWheelModel.getListItemSpin().size() > 0) {
            for (int i = 0; i < spinWheelModel.getListItemSpin().size(); i++) {
                Log.d("TAG", "createWheel: " + spinWheelModel.getListItemSpin().get(i).getItemName());
            }
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
            wedgeSize = (float) (360f / (float) (spinWheelModel.getListItemSpin().size() * spinWheelModel.getRepeat()));
            float f = wedgeSize;
            float size = wedgeSize * ((float) spinWheelModel.getListItemSpin().size() * (float) spinWheelModel.getRepeat());
            while (size < 360.0f) {
                f += 1.0f;
                size = ((float) spinWheelModel.getListItemSpin().size() * spinWheelModel.getRepeat()) * f;
            }
            wedgeSize = f;
            View view = binding.viewWheel;
            float f2 = (float) width;
            float f3 = (float) height;
            float f4 = (float) i;
            Log.d("TAG", "createWheelaaab: " + this.spinWheelModel.getListItemSpin().size());
            defaultCircleDrawable = new DefaultCircleDrawablePreview(f2, f3, f4, wedgeSize, spinWheelModel, this, textSize);
            view.setBackground(defaultCircleDrawable);
            binding.viewOverlay.setBackground(new SelecterOverlayDrawable(f2, f3, f4));
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }
}
