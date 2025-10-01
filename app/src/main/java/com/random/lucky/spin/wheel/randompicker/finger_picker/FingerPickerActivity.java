package com.random.lucky.spin.wheel.randompicker.finger_picker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.random.lucky.spin.wheel.randompicker.base.MyBaseActivity;
import com.random.lucky.spin.wheel.randompicker.databinding.ActivityFingerPickerBinding;

public class FingerPickerActivity extends MyBaseActivity {
    ActivityFingerPickerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFingerPickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setLayoutChoice(1);
        binding.tvChoice1.setOnClickListener(v -> {
            binding.tvCountChoice.setText("1");
            setLayoutChoice(1);
            binding.layoutFingerPicker.setCountWinner(1);
        });
        binding.tvChoice2.setOnClickListener(v -> {
            binding.tvCountChoice.setText("2");
            setLayoutChoice(2);
            binding.layoutFingerPicker.setCountWinner(2);
        });
        binding.tvChoice3.setOnClickListener(v -> {
            binding.tvCountChoice.setText("3");
            setLayoutChoice(3);
            binding.layoutFingerPicker.setCountWinner(3);
        });
        binding.tvChoice4.setOnClickListener(v -> {
            binding.tvCountChoice.setText("4");
            setLayoutChoice(4);
            binding.layoutFingerPicker.setCountWinner(4);
        });
        binding.llCountChoice.setOnClickListener(v -> {
            if (binding.layoutMoreChoice.getVisibility() == View.VISIBLE) {
                binding.layoutFingerPicker.setAllowTouch(true);
                binding.layoutMoreChoice.setVisibility(View.GONE);
                rotationAnimation(binding.ivMoreCountChoice, 90f, 0f, new AnimatorListenerAdapter() {
                });
            } else {
                binding.layoutFingerPicker.setAllowTouch(false);
                rotationAnimation(binding.ivMoreCountChoice, 0f, 90f, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.layoutMoreChoice.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void rotationAnimation(View view, float rotationStart, float rotationEnd, AnimatorListenerAdapter listenerAdapter) {
        @SuppressLint("Recycle") ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", rotationStart, rotationEnd);
        rotateAnimator.setDuration(600);
        rotateAnimator.addListener(listenerAdapter);
        rotateAnimator.start();
    }

    private void setLayoutChoice(int item) {
        binding.layoutMoreChoice.setVisibility(View.GONE);
        binding.ivMoreCountChoice.setRotation(0f);
        binding.tvChoice1.setTextColor(Color.parseColor("#454545"));
        binding.tvChoice2.setTextColor(Color.parseColor("#454545"));
        binding.tvChoice3.setTextColor(Color.parseColor("#454545"));
        binding.tvChoice4.setTextColor(Color.parseColor("#454545"));
        binding.tvChoice1.setBackgroundColor(Color.parseColor("#ffffff"));
        binding.tvChoice2.setBackgroundColor(Color.parseColor("#ffffff"));
        binding.tvChoice3.setBackgroundColor(Color.parseColor("#ffffff"));
        binding.tvChoice4.setBackgroundColor(Color.parseColor("#ffffff"));
        switch (item) {
            case 1:
                binding.tvChoice1.setTextColor(Color.parseColor("#ffffff"));
                binding.tvChoice1.setBackgroundColor(Color.parseColor("#E51515"));
                break;
            case 2:
                binding.tvChoice2.setTextColor(Color.parseColor("#ffffff"));
                binding.tvChoice2.setBackgroundColor(Color.parseColor("#E51515"));
                break;
            case 3:
                binding.tvChoice3.setTextColor(Color.parseColor("#ffffff"));
                binding.tvChoice3.setBackgroundColor(Color.parseColor("#E51515"));
                break;
            case 4:
                binding.tvChoice4.setTextColor(Color.parseColor("#ffffff"));
                binding.tvChoice4.setBackgroundColor(Color.parseColor("#E51515"));
                break;
        }
    }
}
