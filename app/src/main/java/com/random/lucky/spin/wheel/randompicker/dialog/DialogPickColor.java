package com.random.lucky.spin.wheel.randompicker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.adapter.ColorAdapter;
import com.random.lucky.spin.wheel.randompicker.custom_view.pickers.RectangularHSV;
import com.random.lucky.spin.wheel.randompicker.language.SystemUtils;
import com.random.lucky.spin.wheel.randompicker.model.ColorModel;
import com.slaviboy.colorpicker.components.Base;
import com.slaviboy.colorpicker.data.ColorHolder;
import com.slaviboy.colorpicker.main.ColorConverter;
import com.slaviboy.colorpicker.main.Updater;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogPickColor extends Dialog {

    private Updater updater;
    private RectangularHSV rectangularHSV;
    private ColorConverter colorConverter;
    private RecyclerView recyclerView;
    private ColorAdapter colorAdapter;
    private ArrayList<ColorModel> listColor;
    private int colorPicked;
    private TextView tvSave;
    private IOnClickSave iOnClickSave;

    public interface IOnClickSave {
        void onClickSave(int colorCode);
    }

    public DialogPickColor(@NonNull Context context, IOnClickSave iOnClickSave) {
        super(context, R.style.BaseDialog);
        this.iOnClickSave = iOnClickSave;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemUtils.setLocale(getContext());
        setContentView(R.layout.dialog_pick_color);
        setCancelable(true);
        initColorPickers();
        recyclerView = findViewById(R.id.recycler_view);
        tvSave = findViewById(R.id.tv_save);
        addListColor();
        colorAdapter = new ColorAdapter(listColor, getContext(), (pos, colorModel) -> {
            colorAdapter.pickColor(colorModel);
            colorPicked = Color.parseColor(colorModel.getColorCode());
        });
        recyclerView.setAdapter(colorAdapter);
        tvSave.setOnClickListener(view -> {
            iOnClickSave.onClickSave(colorPicked);
            dismiss();
        });
    }

    private void addListColor() {
        listColor = new ArrayList<>();
        listColor.add(new ColorModel("#EF4444", false));
        listColor.add(new ColorModel("#F97316", false));
        listColor.add(new ColorModel("#FACC15", false));
        listColor.add(new ColorModel("#4ADE80", false));
        listColor.add(new ColorModel("#2DD4BF", false));
        listColor.add(new ColorModel("#3B82F6", false));
        listColor.add(new ColorModel("#4BD4FF", false));
        listColor.add(new ColorModel("#EC4899", false));
        listColor.add(new ColorModel("#F43F5E", false));
        listColor.add(new ColorModel("#D946EF", false));
        listColor.add(new ColorModel("#8B5CF6", false));
        listColor.add(new ColorModel("#0EA5E9", false));
        listColor.add(new ColorModel("#10B981", false));
        listColor.add(new ColorModel("#84CC16", false));
    }

    private void initColorPickers() {
        rectangularHSV = findViewById(R.id.rectangular_hsv);
        colorConverter = new ColorConverter("#1f538cb5");
        updater = new Updater(colorConverter, new ColorHolder(), new ArrayList<>(), new HashMap<>());

        // attach updater to all color pickers
        rectangularHSV.attach(updater);

        updater.setOnUpdateListener(new Updater.OnUpdateListener() {
            @Override
            public void onTextViewUpdate(@NonNull TextView textView) {

            }

            @Override
            public void onColorWindowUpdate(@NonNull Base colorWindow) {
                TextView color_code = findViewById(R.id.color_code);
                TextView r_value = findViewById(R.id.r_value);
                TextView g_value = findViewById(R.id.g_value);
                TextView b_value = findViewById(R.id.b_value);
                TextView a_value = findViewById(R.id.a_value);

                int r = Integer.parseInt(r_value.getText().toString());
                int g = Integer.parseInt(g_value.getText().toString());
                int b = Integer.parseInt(b_value.getText().toString());
                int a = Integer.parseInt(a_value.getText().toString());
                colorPicked = Color.rgb(r, g, b);
            }
        });
    }
}
