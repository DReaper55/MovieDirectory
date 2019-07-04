package com.example.moviedirectory.Utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    SharedPreferences sharedPreferences;

    public Prefs(Activity activity){
        sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void setString(String search){
        sharedPreferences.edit().putString("search", search).commit();
    }

    public String getString(){
        return sharedPreferences.getString("search", "Spiderman");
    }
}
