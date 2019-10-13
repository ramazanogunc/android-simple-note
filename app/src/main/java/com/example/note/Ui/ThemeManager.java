package com.example.note.Ui;

import android.content.Context;
import com.example.note.Activities.MainActivity;
import com.example.note.Model.Settings;
import com.example.note.R;

public class ThemeManager {
    Context context;
    String currentTheme;

    public ThemeManager(Context context) {
        this.context = context;
        currentTheme = new Settings(context).getThemeColor();
    }

    public void prepareTheme(){

        switch (currentTheme){
            case "Default":
                ((MainActivity)context).setTheme(R.style.ThemeGreen);
                break;
            case "Blue":
                ((MainActivity)context).setTheme(R.style.ThemeBlue);
                break;
            case "Red":
                ((MainActivity)context).setTheme(R.style.ThemeRed);
                break;
            case "Orange":
                break;
            case "Green":
                ((MainActivity)context).setTheme(R.style.ThemeGreen);
                break;
            case "Purple":
                ((MainActivity)context).setTheme(R.style.ThemePurple);
                break;
            default:
                ((MainActivity)context).setTheme(R.style.ThemeGreen);
                break;
        }
    }

    public void themeChanged(){
        prepareTheme();
        ((MainActivity)context).recreate();
    }

}
