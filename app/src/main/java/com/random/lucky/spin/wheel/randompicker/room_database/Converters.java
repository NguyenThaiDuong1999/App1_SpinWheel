package com.random.lucky.spin.wheel.randompicker.room_database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public String fromItemSpinModelList(ArrayList<ItemSpinModel> value) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ItemSpinModel>>() {
        }.getType();
        return gson.toJson(value, type);
    }

    @TypeConverter
    public ArrayList<ItemSpinModel> toItemSpinModelList(String value) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ItemSpinModel>>() {
        }.getType();
        return gson.fromJson(value, type);
    }
}
