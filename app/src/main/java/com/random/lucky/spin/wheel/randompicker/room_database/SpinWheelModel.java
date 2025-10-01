package com.random.lucky.spin.wheel.randompicker.room_database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "table_spinwheel")
public class SpinWheelModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private float textSize = 18f;
    private int repeat = 1;
    private int time = 1;
    private ArrayList<ItemSpinModel> listItemSpin;
    private String type;
    private int imgPreview;
    private int imgBackground;

    public SpinWheelModel() {
    }

    public SpinWheelModel(String name, float textSize, int repeat, int time, ArrayList<ItemSpinModel> listItemSpin, String type, int imgPreview) {
        this.name = name;
        this.textSize = textSize;
        this.repeat = repeat;
        this.time = time;
        this.listItemSpin = listItemSpin;
        this.type = type;
        this.imgPreview = imgPreview;
    }

    public SpinWheelModel(String name, float textSize, int repeat, int time, ArrayList<ItemSpinModel> listItemSpin, String type, int imgPreview, int imgBackground) {
        this.name = name;
        this.textSize = textSize;
        this.repeat = repeat;
        this.time = time;
        this.listItemSpin = listItemSpin;
        this.type = type;
        this.imgPreview = imgPreview;
        this.imgBackground = imgBackground;
    }

    public int getImgBackground() {
        return imgBackground;
    }

    public void setImgBackground(int imgBackground) {
        this.imgBackground = imgBackground;
    }

    public int getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(int imgPreview) {
        this.imgPreview = imgPreview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<ItemSpinModel> getListItemSpin() {
        return listItemSpin;
    }

    public void setListItemSpin(ArrayList<ItemSpinModel> listItemSpin) {
        this.listItemSpin = listItemSpin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
