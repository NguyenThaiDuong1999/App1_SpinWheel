package com.random.lucky.spin.wheel.randompicker.room_database;

import java.io.Serializable;

public class ItemSpinModel implements Serializable {
    private long id;
    private String itemName;
    private int color;

    public ItemSpinModel(long id, String itemName, int color) {
        this.id = id;
        this.itemName = itemName;
        this.color = color;
    }

    public ItemSpinModel() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
