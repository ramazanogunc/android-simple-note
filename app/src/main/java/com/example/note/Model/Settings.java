package com.example.note.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static String viewModeKey = "viewMode";
    private static String themeColorKey = "themeColor";
    private static String pinActiveKey = "pinActive";
    private static String pinKey = "pin";


    public Settings(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }

    private void saveSetting(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    private void saveSettingBool(String key, boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    private String getSetting(String key){
        return preferences.getString(key,"");
    }

    private boolean getSettingBool(String key){
        return preferences.getBoolean(key,false);
    }

    public void setViewMode(String value){
        saveSetting(viewModeKey,value);
    }

    public String getViewMode(){
        return getSetting(viewModeKey);
    }

    public void setThemeColor(String value){
        saveSetting(themeColorKey,value);
    }

    public String getThemeColor(){
        return getSetting(themeColorKey);
    }

    public void setPinActive(boolean value){
        saveSettingBool(pinActiveKey,value);
    }

    public boolean getPinActive(){
        return getSettingBool(pinActiveKey);
    }

    public void setPin(String value){
        saveSetting(pinKey,value);
    }

    public String getPin(){
        return getSetting(pinKey);
    }
}
