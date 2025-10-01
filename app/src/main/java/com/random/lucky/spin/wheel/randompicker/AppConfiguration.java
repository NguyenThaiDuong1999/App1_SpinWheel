package com.random.lucky.spin.wheel.randompicker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import com.random.lucky.spin.wheel.randompicker.room_database.AppDatabase;
import com.random.lucky.spin.wheel.randompicker.room_database.ItemSpinModel;
import com.random.lucky.spin.wheel.randompicker.room_database.SpinWheelModel;

import java.util.ArrayList;

public class AppConfiguration extends Application {
    private static Context mContext;
    private ArrayList<SpinWheelModel> listSpinWheelModel;
    public static String DEFAULT = "Default";
    public static String CUSTOM = "Custom";
    public static String SPIN_WHEEL_MODEL = "SPIN_WHEEL_MODEL";
    public static String SCREEN = "SCREEN";

    private AppDatabase appDatabase;
    private SharedPreferences sharedPreferences;

    public static long timeStart = 0L;

    public void onCreate() {
        super.onCreate();
        mContext = this;
        appDatabase = AppDatabase.getInstance(this);
        sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("Insert_Data", false)) {
            new InsertDefaultData().execute();
        }
    }

    private class InsertDefaultData extends AsyncTask<Void, Void, Void> {
        public InsertDefaultData() {
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            sharedPreferences.edit().putBoolean("Insert_Data", true).apply();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            addListSpinWheel();
            return null;
        }
    }

    public void addListSpinWheel() {
        listSpinWheelModel = new ArrayList<>();
        ArrayList<ItemSpinModel> listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Yes", Color.parseColor("#ffc400")));
        listItemSpinModel.add(new ItemSpinModel(2, "No", Color.parseColor("#93073e")));
        listItemSpinModel.add(new ItemSpinModel(3, "Yes", Color.parseColor("#c90035")));
        listItemSpinModel.add(new ItemSpinModel(4, "No", Color.parseColor("#ff5627")));
        listItemSpinModel.add(new ItemSpinModel(5, "Yes", Color.parseColor("#ffc400")));
        listItemSpinModel.add(new ItemSpinModel(6, "No", Color.parseColor("#93073e")));
        listItemSpinModel.add(new ItemSpinModel(7, "Yes", Color.parseColor("#c90035")));
        listItemSpinModel.add(new ItemSpinModel(8, "No", Color.parseColor("#ff5627")));
        SpinWheelModel spinWheelModel = new SpinWheelModel("Yes / No", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_yes_no, R.drawable.bg_gradient_yes_no);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Cajun Fries", Color.parseColor("#3da4ab")));
        listItemSpinModel.add(new ItemSpinModel(2, "Cheesy Tacos", Color.parseColor("#f6cd61")));
        listItemSpinModel.add(new ItemSpinModel(3, "Punjabi", Color.parseColor("#3da4ab")));
        listItemSpinModel.add(new ItemSpinModel(4, "McNuggets", Color.parseColor("#f6cd61")));
        listItemSpinModel.add(new ItemSpinModel(5, "Pizza", Color.parseColor("#3da4ab")));
        listItemSpinModel.add(new ItemSpinModel(6, "Burgers", Color.parseColor("#f6cd61")));
        listItemSpinModel.add(new ItemSpinModel(7, "Chinese", Color.parseColor("#3da4ab")));
        listItemSpinModel.add(new ItemSpinModel(8, "Pasta", Color.parseColor("#f6cd61")));
        spinWheelModel = new SpinWheelModel("Choose a dish?", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_eat, R.drawable.bg_gradient_what_do);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Monday", Color.parseColor("#feca57")));
        listItemSpinModel.add(new ItemSpinModel(2, "Tuesday", Color.parseColor("#ff9ff3")));
        listItemSpinModel.add(new ItemSpinModel(3, "Wednesday", Color.parseColor("#0e5aa7")));
        listItemSpinModel.add(new ItemSpinModel(4, "Thursday", Color.parseColor("#0e9aa7")));
        listItemSpinModel.add(new ItemSpinModel(5, "Friday", Color.parseColor("#1dd1a1")));
        listItemSpinModel.add(new ItemSpinModel(6, "Saturday", Color.parseColor("#48dbfb")));
        listItemSpinModel.add(new ItemSpinModel(7, "Sunday", Color.parseColor("#ff6b6b")));
        spinWheelModel = new SpinWheelModel("Pick a Day", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_pick_day, R.drawable.bg_gradient_pick_day);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "1", Color.parseColor("#4dc600")));
        listItemSpinModel.add(new ItemSpinModel(2, "2", Color.parseColor("#70d32e")));
        listItemSpinModel.add(new ItemSpinModel(3, "3", Color.parseColor("#93e05c")));
        listItemSpinModel.add(new ItemSpinModel(4, "4", Color.parseColor("#0f5300")));
        listItemSpinModel.add(new ItemSpinModel(5, "5", Color.parseColor("#1e7900")));
        listItemSpinModel.add(new ItemSpinModel(6, "6", Color.parseColor("#32a000")));
        listItemSpinModel.add(new ItemSpinModel(7, "7", Color.parseColor("#4dc600")));
        listItemSpinModel.add(new ItemSpinModel(8, "8", Color.parseColor("#70d32e")));
        listItemSpinModel.add(new ItemSpinModel(9, "9", Color.parseColor("#93e05c")));
        listItemSpinModel.add(new ItemSpinModel(10, "10", Color.parseColor("#1e7900")));
        spinWheelModel = new SpinWheelModel("Pick a Number", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_pick_number, R.drawable.bg_gradient_pick_number);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "AM", Color.parseColor("#cf855e")));
        listItemSpinModel.add(new ItemSpinModel(2, "PM", Color.parseColor("#f9a521")));
        listItemSpinModel.add(new ItemSpinModel(3, "CM", Color.parseColor("#439aae")));
        listItemSpinModel.add(new ItemSpinModel(4, "EAT", Color.parseColor("#004451")));
        listItemSpinModel.add(new ItemSpinModel(5, "FAT", Color.parseColor("#1c1f24")));
        listItemSpinModel.add(new ItemSpinModel(6, "KITE", Color.parseColor("#462f41")));
        listItemSpinModel.add(new ItemSpinModel(7, "MAN", Color.parseColor("#cf855e")));
        listItemSpinModel.add(new ItemSpinModel(8, "NO", Color.parseColor("#f9a521")));
        listItemSpinModel.add(new ItemSpinModel(9, "RGB", Color.parseColor("#439aae")));
        listItemSpinModel.add(new ItemSpinModel(10, "YES", Color.parseColor("#004451")));
        listItemSpinModel.add(new ItemSpinModel(11, "LAN", Color.parseColor("#1c1f24")));
        listItemSpinModel.add(new ItemSpinModel(12, "WAN", Color.parseColor("#462f41")));
        spinWheelModel = new SpinWheelModel("Pick a Letter", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_pick_letter, R.drawable.bg_gradient_pick_letter);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Rock", Color.parseColor("#cf855e")));
        listItemSpinModel.add(new ItemSpinModel(2, "Scissors", Color.parseColor("#f9a521")));
        listItemSpinModel.add(new ItemSpinModel(3, "Paper", Color.parseColor("#439aae")));
        listItemSpinModel.add(new ItemSpinModel(4, "Rock", Color.parseColor("#004451")));
        listItemSpinModel.add(new ItemSpinModel(5, "Scissors", Color.parseColor("#1c1f24")));
        listItemSpinModel.add(new ItemSpinModel(6, "Paper", Color.parseColor("#462f41")));
        listItemSpinModel.add(new ItemSpinModel(7, "Rock", Color.parseColor("#cf855e")));
        listItemSpinModel.add(new ItemSpinModel(8, "Scissors", Color.parseColor("#f9a521")));
        listItemSpinModel.add(new ItemSpinModel(9, "Paper", Color.parseColor("#439aae")));
        listItemSpinModel.add(new ItemSpinModel(10, "Rock", Color.parseColor("#004451")));
        listItemSpinModel.add(new ItemSpinModel(11, "Scissors", Color.parseColor("#1c1f24")));
        listItemSpinModel.add(new ItemSpinModel(12, "Paper", Color.parseColor("#462f41")));
        spinWheelModel = new SpinWheelModel("Rock Paper Scissors", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_rock_paper, R.drawable.bg_gradient_rock_paper);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Tell a joke", Color.parseColor("#e57e88")));
        listItemSpinModel.add(new ItemSpinModel(2, "What is your...", Color.parseColor("#f2bcaa")));
        listItemSpinModel.add(new ItemSpinModel(3, "Don't blink eye...", Color.parseColor("#562e73")));
        listItemSpinModel.add(new ItemSpinModel(4, "Who are you j...", Color.parseColor("#8f2e99")));
        listItemSpinModel.add(new ItemSpinModel(5, "Break two eggs..", Color.parseColor("#b32d91")));
        listItemSpinModel.add(new ItemSpinModel(6, "Get slapped on...", Color.parseColor("#cc5285")));
        listItemSpinModel.add(new ItemSpinModel(7, "Kiss the person..", Color.parseColor("#e57e88")));
        listItemSpinModel.add(new ItemSpinModel(8, "Dance with no...", Color.parseColor("#f2bcaa")));
        listItemSpinModel.add(new ItemSpinModel(9, "Mimic a famous..", Color.parseColor("#562e73")));
        listItemSpinModel.add(new ItemSpinModel(10, "Take a ice c..", Color.parseColor("#562e73")));
        listItemSpinModel.add(new ItemSpinModel(11, "Give a strong..", Color.parseColor("#8f2e99")));
        listItemSpinModel.add(new ItemSpinModel(12, "Eating a one...", Color.parseColor("#b32d91")));
        spinWheelModel = new SpinWheelModel("Truth or Dare", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_truth_dare, R.drawable.bg_gradient_truth_dare);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Tidy up the room", Color.parseColor("#cf855e")));
        listItemSpinModel.add(new ItemSpinModel(2, "Do the laundry", Color.parseColor("#f9a521")));
        listItemSpinModel.add(new ItemSpinModel(3, "Sweep the floor", Color.parseColor("#439aae")));
        listItemSpinModel.add(new ItemSpinModel(4, "Clean the window", Color.parseColor("#004451")));
        listItemSpinModel.add(new ItemSpinModel(5, "Sweep the yard", Color.parseColor("#1c1f24")));
        listItemSpinModel.add(new ItemSpinModel(6, "Do the cooking", Color.parseColor("#462f41")));
        listItemSpinModel.add(new ItemSpinModel(7, "Wash the dishes", Color.parseColor("#cf855e")));
        listItemSpinModel.add(new ItemSpinModel(8, "Water the plants", Color.parseColor("#f9a521")));
        spinWheelModel = new SpinWheelModel("Pick a housework", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_pick_housework, R.drawable.bg_gradient_house_work);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Job A", Color.parseColor("#FD1A1A")));
        listItemSpinModel.add(new ItemSpinModel(2, "Job B", Color.parseColor("#F9861C")));
        listItemSpinModel.add(new ItemSpinModel(3, "Job C", Color.parseColor("#F4E66E")));
        listItemSpinModel.add(new ItemSpinModel(4, "Job D", Color.parseColor("#FF9494")));
        listItemSpinModel.add(new ItemSpinModel(5, "Job E", Color.parseColor("#FD1A1A")));
        listItemSpinModel.add(new ItemSpinModel(6, "Job F", Color.parseColor("#F9861C")));
        listItemSpinModel.add(new ItemSpinModel(7, "Job X", Color.parseColor("#F4E66E")));
        spinWheelModel = new SpinWheelModel("Pick a job", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_pick_job, R.drawable.bg_gradient_job);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Alexandr", Color.parseColor("#FFE602")));
        listItemSpinModel.add(new ItemSpinModel(2, "Louisa", Color.parseColor("#FAFF14")));
        listItemSpinModel.add(new ItemSpinModel(3, "Matilda", Color.parseColor("#FFC700")));
        listItemSpinModel.add(new ItemSpinModel(4, "Andrew", Color.parseColor("#C3FF94")));
        listItemSpinModel.add(new ItemSpinModel(5, "Arnold", Color.parseColor("#FFE602")));
        listItemSpinModel.add(new ItemSpinModel(6, "Richard", Color.parseColor("#FAFF14")));
        listItemSpinModel.add(new ItemSpinModel(7, "Gloria", Color.parseColor("#FFC700")));
        listItemSpinModel.add(new ItemSpinModel(8, "Sophie", Color.parseColor("#C3FF94")));
        listItemSpinModel.add(new ItemSpinModel(9, "Amanda", Color.parseColor("#FFE602")));
        listItemSpinModel.add(new ItemSpinModel(10, "Jasmine", Color.parseColor("#FAFF14")));
        spinWheelModel = new SpinWheelModel("Pick a person", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_pick_person, R.drawable.bg_gradient_person);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Drink 0.5 cup", Color.parseColor("#7102FF")));
        listItemSpinModel.add(new ItemSpinModel(2, "Through the tassel", Color.parseColor("#230087")));
        listItemSpinModel.add(new ItemSpinModel(3, "Spin again", Color.parseColor("#001AFF")));
        listItemSpinModel.add(new ItemSpinModel(4, "Eat something", Color.parseColor("#94B2FF")));
        listItemSpinModel.add(new ItemSpinModel(5, "Drink 2 cups", Color.parseColor("#7102FF")));
        listItemSpinModel.add(new ItemSpinModel(6, "Choose a drinker", Color.parseColor("#230087")));
        spinWheelModel = new SpinWheelModel("Pick a defy", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_pick_drink, R.drawable.bg_gradient_challenge);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "5$", Color.parseColor("#FFE602")));
        listItemSpinModel.add(new ItemSpinModel(2, "1$", Color.parseColor("#FAFF14")));
        listItemSpinModel.add(new ItemSpinModel(3, "Spin again", Color.parseColor("#FFC700")));
        listItemSpinModel.add(new ItemSpinModel(4, "3$", Color.parseColor("#C3FF94")));
        listItemSpinModel.add(new ItemSpinModel(5, "One more spin", Color.parseColor("#FFE602")));
        listItemSpinModel.add(new ItemSpinModel(6, "1 round of applause", Color.parseColor("#FAFF14")));
        spinWheelModel = new SpinWheelModel("Pick a reward", 10f, 1, 2, listItemSpinModel, DEFAULT, R.drawable.img_pick_reward, R.drawable.bg_gradient_reward);
        listSpinWheelModel.add(spinWheelModel);
        listItemSpinModel = new ArrayList<>();
        listItemSpinModel.add(new ItemSpinModel(1, "Yes", Color.parseColor("#ffc400")));
        listItemSpinModel.add(new ItemSpinModel(2, "No", Color.parseColor("#93073e")));
        listItemSpinModel.add(new ItemSpinModel(3, "Yes", Color.parseColor("#c90035")));
        listItemSpinModel.add(new ItemSpinModel(4, "No", Color.parseColor("#ff5627")));
        listItemSpinModel.add(new ItemSpinModel(5, "Yes", Color.parseColor("#ffc400")));
        listItemSpinModel.add(new ItemSpinModel(6, "No", Color.parseColor("#93073e")));
        listItemSpinModel.add(new ItemSpinModel(7, "Yes", Color.parseColor("#c90035")));
        listItemSpinModel.add(new ItemSpinModel(8, "No", Color.parseColor("#ff5627")));
        spinWheelModel = new SpinWheelModel("Yes / No", 18f, 1, 2, listItemSpinModel, CUSTOM, R.drawable.customthumb, R.drawable.bg_gradient_yes_no);
        listSpinWheelModel.add(spinWheelModel);
        for (SpinWheelModel spinWheelModelInsert : listSpinWheelModel) {
            appDatabase.spinWheelDAO().insertSpinWheel(spinWheelModelInsert);
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static int getCountOpenApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getInt("counts", 0);
    }

    public static void increaseCountOpenApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("counts", pre.getInt("counts", 0) + 1);
        editor.commit();
    }
}

