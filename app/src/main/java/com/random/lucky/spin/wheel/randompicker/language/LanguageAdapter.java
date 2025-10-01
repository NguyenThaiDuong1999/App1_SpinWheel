package com.random.lucky.spin.wheel.randompicker.language;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.random.lucky.spin.wheel.randompicker.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<LanguageModel> lists;
    private IClickLanguage iClickLanguage;

    public LanguageAdapter(Context context, List<LanguageModel> lists, IClickLanguage iClickLanguage) {
        this.context = context;
        this.lists = lists;
        this.iClickLanguage = iClickLanguage;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new LanguageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_language, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        LanguageModel data = lists.get(position);
        if (holder instanceof LanguageViewHolder) {
            ((LanguageViewHolder) holder).bind(data);
            ((LanguageViewHolder) holder).relayLanguage.setOnClickListener(v -> iClickLanguage.onClick(data));
            ((LanguageViewHolder) holder).rbBtn.setOnClickListener(v -> iClickLanguage.onClick(data));

            if (data.isCheck()) {
                ((LanguageViewHolder) holder).rbBtn.setChecked(true);
                ((LanguageViewHolder) holder).relayLanguage.setBackgroundResource(R.drawable.border_item_language_select);
            } else {
                ((LanguageViewHolder) holder).rbBtn.setChecked(false);
                ((LanguageViewHolder) holder).relayLanguage.setBackgroundResource(R.drawable.border_item_language);
            }
        }
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvTitle;
        RadioButton rbBtn;
        RelativeLayout relayLanguage;

        public LanguageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.img_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            rbBtn = itemView.findViewById(R.id.rb_language);
            relayLanguage = itemView.findViewById(R.id.relay_language);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bind(LanguageModel data) {
            ivAvatar.setImageDrawable(context.getDrawable(data.getImage()));
            tvTitle.setText(data.getLanguageName());
            rbBtn.setChecked(data.isCheck());
        }
    }

    public void setSelectLanguage(LanguageModel model) {
        for (LanguageModel data : lists) {
            if (data.getLanguageName().equals(model.getLanguageName())) {
                data.setCheck(true);
            } else {
                data.setCheck(false);
            }
        }
        notifyDataSetChanged();
    }
}
