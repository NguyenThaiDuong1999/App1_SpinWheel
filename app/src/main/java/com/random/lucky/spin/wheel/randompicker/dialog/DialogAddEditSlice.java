package com.random.lucky.spin.wheel.randompicker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.adapter.ColorSliceAdapter;
import com.random.lucky.spin.wheel.randompicker.language.SystemUtils;
import com.random.lucky.spin.wheel.randompicker.model.ColorModel;
import com.random.lucky.spin.wheel.randompicker.room_database.ItemSpinModel;

import java.util.ArrayList;

public class DialogAddEditSlice extends Dialog {
    private ArrayList<ColorModel> listColor;
    private ColorSliceAdapter colorAdapter;
    private RecyclerView recyclerView;
    private String title;
    private TextView tvTitle, tvDelete, tvSave, tvSlice;
    private IOnClick iOnClick;
    private int colorCode = Color.parseColor("#F3F3F3");
    private EditText edtSliceName;
    private ImageView imgClose;
    private Toast toast;
    private ItemSpinModel itemSpinModel;

    public DialogAddEditSlice(@NonNull Context context, ItemSpinModel itemSpinModel, String title, IOnClick iOnClick) {
        super(context, R.style.BaseDialog);
        this.title = title;
        this.iOnClick = iOnClick;
        setItemSpinModel(itemSpinModel);
    }

    private void setItemSpinModel(ItemSpinModel itemSpinModel) {
        this.itemSpinModel = new ItemSpinModel();
        this.itemSpinModel.setId(itemSpinModel.getId());
        this.itemSpinModel.setItemName(itemSpinModel.getItemName());
        this.itemSpinModel.setColor(itemSpinModel.getColor());
    }

    public DialogAddEditSlice(@NonNull Context context, String title, IOnClick iOnClick) {
        super(context, R.style.BaseDialog);
        this.title = title;
        this.iOnClick = iOnClick;
    }

    public interface IOnClick {
        void onClickDelete();

        void onClickSave(String sliceTitle, int colorCode, ItemSpinModel itemSpinModel);

        void onClickSaveAddNew(String sliceTitle, int colorCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SystemUtils.setLocale(getContext());
        setContentView(R.layout.dialog_add_edit_slice);
        setCancelable(true);
        recyclerView = findViewById(R.id.recycler_view_color);
        tvTitle = findViewById(R.id.tv_title);
        tvDelete = findViewById(R.id.tv_delete);
        tvSave = findViewById(R.id.tv_save);
        tvSlice = findViewById(R.id.tv_slice);
        edtSliceName = findViewById(R.id.edt_slice_name);
        imgClose = findViewById(R.id.img_close);
        tvTitle.setText(title);
        if (itemSpinModel != null) {
            //edit slice
            tvSlice.setBackgroundColor(itemSpinModel.getColor());
            edtSliceName.setText(itemSpinModel.getItemName());
            colorCode = itemSpinModel.getColor();
            addListColor(false);
            int count = 0;
            for (int i = 0; i < listColor.size(); i++) {
                Log.d("TAG", "checkExistsColor: " + listColor.get(i).getColorCode() + "-" + intToHexColor(colorCode));
                if (listColor.get(i).getColorCode().equals(intToHexColor(colorCode))) {
                    listColor.get(i).setSelect(true);
                    count++;
                } else {
                    listColor.get(i).setSelect(false);
                }
            }
            if (count <= 0) {
                listColor.add(new ColorModel(R.drawable.ic_color_green, intToHexColor(colorCode), true));
            }
        } else {
            //add slice
            tvSlice.setBackgroundColor(colorCode);
            addListColor(true);
            DialogAddEditSlice.this.colorCode = Color.parseColor(listColor.get(1).getColorCode());
        }
        colorAdapter = new ColorSliceAdapter(listColor, getContext(), new ColorSliceAdapter.IOnClickItem() {
            @Override
            public void onClickPickColor() {
                DialogPickColor dialogPickColor = new DialogPickColor(getContext(), colorCode -> {
                    DialogAddEditSlice.this.colorCode = colorCode;
                    colorAdapter.unPickAllColor();
                    if (itemSpinModel != null) {
                        itemSpinModel.setColor(colorCode);
                        tvSlice.setBackgroundColor(itemSpinModel.getColor());
                    } else {
                        tvSlice.setBackgroundColor(DialogAddEditSlice.this.colorCode);
                    }
                });
                dialogPickColor.show();
            }

            @Override
            public void onClickItem(int pos, ColorModel colorModel) {
                DialogAddEditSlice.this.colorCode = Color.parseColor(colorModel.getColorCode());
                if (itemSpinModel != null) {
                    itemSpinModel.setColor(colorCode);
                    tvSlice.setBackgroundColor(itemSpinModel.getColor());
                } else {
                    tvSlice.setBackgroundColor(DialogAddEditSlice.this.colorCode);
                }
                colorAdapter.pickColor(colorModel);
            }
        });
        recyclerView.setAdapter(colorAdapter);
        tvDelete.setOnClickListener(view -> {
            iOnClick.onClickDelete();
            dismiss();
        });
        tvSave.setOnClickListener(view -> {
            String sliceName = edtSliceName.getText().toString().trim();
            if (sliceName.equals("")) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), R.string.name_cannot_be_empty, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (title.equals(getContext().getString(R.string.edit_slice))) {
                    itemSpinModel.setItemName(sliceName);
                    iOnClick.onClickSave(sliceName, colorCode, itemSpinModel);
                } else {
                    iOnClick.onClickSaveAddNew(sliceName, colorCode);
                }
                dismiss();
            }
        });
        imgClose.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void addListColor(boolean isAddNew) {
        listColor = new ArrayList<>();
        listColor.add(new ColorModel(R.drawable.ic_pick_color, ""));
        listColor.add(new ColorModel(R.drawable.ic_color_white, "#F3F3F3", isAddNew));
        listColor.add(new ColorModel(R.drawable.ic_color_black, "#000000", false));
        listColor.add(new ColorModel(R.drawable.ic_color_blue, "#4BD4FF", false));
        listColor.add(new ColorModel(R.drawable.ic_color_orange, "#DC6803", false));
        listColor.add(new ColorModel(R.drawable.ic_color_red, "#D92D20", false));
        listColor.add(new ColorModel(R.drawable.ic_color_green, "#039855", false));
    }

    public String intToHexColor(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
