package com.random.lucky.spin.wheel.randompicker.default_spin_wheel.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.random.lucky.spin.wheel.randompicker.R;
import com.random.lucky.spin.wheel.randompicker.room_database.ItemSpinModel;

import java.util.ArrayList;

public class ItemEditSpinAdapter extends RecyclerView.Adapter<ItemEditSpinAdapter.ViewHolder> {
    private Context context;
    private IOnClick iOnClick;
    private ArrayList<ItemSpinModel> listItemSpinModel;

    public ItemEditSpinAdapter(Context context, ArrayList<ItemSpinModel> listItemSpinModel, IOnClick iOnClick) {
        this.context = context;
        this.iOnClick = iOnClick;
        this.listItemSpinModel = listItemSpinModel;
    }

    public interface IOnClick {
        void onClickItem(ItemSpinModel itemSpinModel, int position);
    }

    @NonNull
    @Override
    public ItemEditSpinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_edit_spin_wheel, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemEditSpinAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemSpinModel itemSpinModel = listItemSpinModel.get(position);
        holder.txtItemName.setSelected(true);
        holder.txtItemName.setText(itemSpinModel.getItemName());
        holder.txtItemName.setBackgroundColor(itemSpinModel.getColor());
        holder.itemView.setOnClickListener(view -> iOnClick.onClickItem(itemSpinModel, position));
    }

    @Override
    public int getItemCount() {
        return listItemSpinModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.txtItemName);
        }
    }
}
