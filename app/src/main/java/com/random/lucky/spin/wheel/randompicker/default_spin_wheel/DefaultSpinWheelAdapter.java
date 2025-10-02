package com.random.lucky.spin.wheel.randompicker.default_spin_wheel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.room_database.SpinWheelModel;

import java.util.ArrayList;

public class DefaultSpinWheelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SpinWheelModel> listSpinWheelModel;
    private IOnClickSpinWheel iOnClickSpinWheel;
    private final int TYPE_ADS = 0;
    private final int TYPE_ITEM = 1;
    private Context context;

    public DefaultSpinWheelAdapter(Context context, ArrayList<SpinWheelModel> listSpinWheelModel, IOnClickSpinWheel iOnClickSpinWheel) {
        this.listSpinWheelModel = listSpinWheelModel;
        this.iOnClickSpinWheel = iOnClickSpinWheel;
        this.context = context;
    }

    public interface IOnClickSpinWheel {
        void onClickItem(SpinWheelModel spinWheelModel, int position);

        void onClickEdit(SpinWheelModel spinWheelModel, int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default_spin_wheel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof ViewHolder) {
            SpinWheelModel spinWheelModel = listSpinWheelModel.get(position);
            ((ViewHolder) holder).imgVip.setVisibility(View.GONE);
            holder.itemView.setBackgroundResource(spinWheelModel.getImgBackground());
            ((ViewHolder) holder).imgPreview.setImageResource(spinWheelModel.getImgPreview());
            ((ViewHolder) holder).tvSpinWheelName.setText(spinWheelModel.getName());
            holder.itemView.setOnClickListener(view -> iOnClickSpinWheel.onClickItem(spinWheelModel, position));
            ((ViewHolder) holder).imgEdit.setOnClickListener(view -> iOnClickSpinWheel.onClickEdit(spinWheelModel, position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listSpinWheelModel.get(position).getName() == null) {
            return TYPE_ADS;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return listSpinWheelModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSpinWheelName;
        ImageView imgEdit, imgPreview, imgVip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSpinWheelName = itemView.findViewById(R.id.tv_spin_wheel_name);
            imgEdit = itemView.findViewById(R.id.img_edit);
            imgPreview = itemView.findViewById(R.id.img_preview);
            imgVip = itemView.findViewById(R.id.img_vip);
        }
    }

    public class ViewHolderAds extends RecyclerView.ViewHolder {
        FrameLayout frAds;

        public ViewHolderAds(@NonNull View itemView) {
            super(itemView);
            frAds = itemView.findViewById(R.id.fr_ads);
        }
    }
}
