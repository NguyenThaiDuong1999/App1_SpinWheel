package com.random.lucky.spin.wheel.randompicker.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.model.ColorModel;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    private ArrayList<ColorModel> listColor;
    private Context context;
    private IOnClickItem iOnClickItem;

    public ColorAdapter(ArrayList<ColorModel> listColor, Context context, IOnClickItem iOnClickItem) {
        this.listColor = listColor;
        this.context = context;
        this.iOnClickItem = iOnClickItem;
    }

    public interface IOnClickItem {

        void onClickItem(int pos, ColorModel colorModel);
    }

    @NonNull
    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder holder, int position) {
        ColorModel colorModel = listColor.get(position);
        holder.tvColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorModel.getColorCode())));
        if (colorModel.isSelect()) {
            holder.rlItem.setBackgroundResource(R.drawable.bg_item_color_select);
        } else {
            holder.rlItem.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        }
        holder.rlItem.setOnClickListener(view -> {
            iOnClickItem.onClickItem(position, colorModel);
        });
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

    @Override
    public int getItemCount() {
        return listColor.size();
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
