package com.random.lucky.spin.wheel.randompicker.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.model.ColorModel;

import java.util.ArrayList;

public class ColorSliceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ColorModel> listColor;
    private Context context;
    private IOnClickItem iOnClickItem;
    private int TYPE_PICK_COLOR = 0;
    private int TYPE_ITEM = 1;

    public ColorSliceAdapter(ArrayList<ColorModel> listColor, Context context, IOnClickItem iOnClickItem) {
        this.listColor = listColor;
        this.context = context;
        this.iOnClickItem = iOnClickItem;
    }

    public interface IOnClickItem {
        void onClickPickColor();

        void onClickItem(int pos, ColorModel colorModel);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PICK_COLOR) {
            return new ViewHolderPickColor(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick_color, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ColorModel colorModel = listColor.get(position);
        if (holder instanceof ViewHolderPickColor) {
            Glide.with(context).load(colorModel.getImgPreview()).into(((ViewHolderPickColor) holder).imgPick);
            ((ViewHolderPickColor) holder).imgPick.setOnClickListener(view -> {
                iOnClickItem.onClickPickColor();
            });
        } else if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorModel.getColorCode())));
            if (colorModel.isSelect()) {
                ((ViewHolder) holder).rlItem.setBackgroundResource(R.drawable.bg_item_color_select);
            } else {
                ((ViewHolder) holder).rlItem.setBackgroundResource(R.drawable.bg_item_color_unselect);
            }
            ((ViewHolder) holder).itemView.setOnClickListener(view -> {
                iOnClickItem.onClickItem(position, colorModel);
            });
        }
    }

    public void pickColor(ColorModel colorModel) {
        for (ColorModel colorModel1 : listColor) {
            if (colorModel1.getColorCode().equals(colorModel.getColorCode())) {
                colorModel1.setSelect(true);
            } else {
                colorModel1.setSelect(false);
            }
        }
        notifyDataSetChanged();
    }

    public void unPickAllColor() {
        for (ColorModel colorModel : listColor) {
            colorModel.setSelect(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_PICK_COLOR;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return listColor.size();
    }

    public class ViewHolderPickColor extends RecyclerView.ViewHolder {
        ImageView imgPick;

        public ViewHolderPickColor(@NonNull View itemView) {
            super(itemView);
            imgPick = itemView.findViewById(R.id.img_color);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlItem;
        TextView tvColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rl_item);
            tvColor = itemView.findViewById(R.id.tv_color);
        }
    }
}
