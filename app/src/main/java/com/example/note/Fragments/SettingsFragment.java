package com.example.note.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import com.example.note.Model.NoteDatabase;
import com.example.note.Model.Settings;
import com.example.note.R;
import com.example.note.Ui.ThemeManager;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    Preference allNoteRemove, allArhiveRemove, allDataRemove;
    ListPreference themeColor;
    SwitchPreference pinActive;
    EditTextPreference pin;
    NoteDatabase database;
    SharedPreferences.Editor editor;
    Settings settings;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        init();
        onClickEvents();
        settings = new Settings(getActivity());
    }

    private void init() {
        allNoteRemove = findPreference("all_note_remove");
        allArhiveRemove = findPreference("all_arhive_remove");
        allDataRemove = findPreference("all_data_remove");
        themeColor = (ListPreference) findPreference("theme_color");
        themeColor.setOnPreferenceChangeListener(this);
        pinActive = (SwitchPreference) findPreference("pin_active");
        pinActive.setOnPreferenceChangeListener(this);
        pin = (EditTextPreference) findPreference("pin");
        pin.setOnPreferenceChangeListener(this);
        database = new NoteDatabase(getContext());
    }

    private void onClickEvents() {
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
        alertdialog.setMessage("Are You Sure ?")
                .setTitle("Confirm!");

        allNoteRemove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.deleteAllNote();
                                Toast.makeText(getContext(), "All Notes Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null).show();

                return true;
            }
        });

        allArhiveRemove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.deleteAllArhive();
                                Toast.makeText(getContext(), "All Arhive Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null).show();

                return true;
            }
        });
        allDataRemove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.deleteAll();
                                Toast.makeText(getContext(), "All Note and Arhive Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null).show();

                return true;
            }
        });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()){
            case "theme_color":
                Toast.makeText(getContext(), "Theme changed "+newValue.toString(), Toast.LENGTH_SHORT).show();
                settings.setThemeColor(newValue.toString());
                new ThemeManager(getActivity()).themeChanged();
                return true;
            case "pin_active":
                String resultToast = Boolean.parseBoolean(newValue.toString()) ? "Pin Active": "Pin Deactive";
                Toast.makeText(getContext(), resultToast, Toast.LENGTH_SHORT).show();
                settings.setPinActive(Boolean.parseBoolean(newValue.toString()));

                return true;
            case "pin":
                Toast.makeText(getContext(), ""+newValue.toString(), Toast.LENGTH_SHORT).show();
                settings.setPin(newValue.toString());

                return true;
            default:
                return false;
        }

    }


}
