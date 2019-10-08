package com.darkeyes.tricks;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import java.io.File;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    private EditTextPreference customCarrierText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSharedPreferences("com.darkeyes.tricks_preferences", MODE_PRIVATE);
        getPreferenceManager().setStorageDeviceProtected();

        addPreferencesFromResource(R.xml.pref_tricks);

        PreferenceScreen prefScreen = (PreferenceScreen) findPreference("prefScreen");
        SwitchPreference forceDarkTheme = (SwitchPreference) findPreference("trick_forceDarkTheme");
        SwitchPreference useKeyguardPhone = (SwitchPreference) findPreference("trick_useKeyguardPhone");
        SwitchPreference navbarAlwaysRight = (SwitchPreference) findPreference("trick_navbarAlwaysRight");
        SwitchPreference hideBuildVersion = (SwitchPreference) findPreference("trick_hideBuildVersion");
        customCarrierText = (EditTextPreference) findPreference("trick_customCarrierText");

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        updateSummary();

        if (Build.VERSION.SDK_INT != 27) {
            prefScreen.removePreference(forceDarkTheme);
        }
        if (Build.VERSION.SDK_INT >= 28) {
            prefScreen.removePreference(useKeyguardPhone);
        }
        if (Build.VERSION.SDK_INT == 29) {
            prefScreen.removePreference(navbarAlwaysRight);
        }
        if (Build.VERSION.SDK_INT != 29) {
            prefScreen.removePreference(hideBuildVersion);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        File mainFolder = new File("/data/user_de/0/com.darkeyes.tricks");
        if (mainFolder.exists()) {
            mainFolder.setReadable(true, false);
            mainFolder.setExecutable(true, false);
        }
        File prefFolder = new File("/data/user_de/0/com.darkeyes.tricks/shared_prefs");
        if (prefFolder.exists()) {
            prefFolder.setReadable(true, false);
            prefFolder.setExecutable(true, false);
        }
        File prefFile = new File("/data/user_de/0/com.darkeyes.tricks/shared_prefs/com.darkeyes.tricks_preferences.xml");
        if (prefFile.exists()) {
            prefFile.setReadable(true, false);
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSummary();
    }

    private void updateSummary() {
        String carrierText = customCarrierText.getText();
        if (carrierText == null || carrierText.isEmpty()) {
            customCarrierText.setSummary("Default");
        } else if (carrierText.trim().isEmpty()) {
            customCarrierText.setSummary("Empty");
        } else {
            customCarrierText.setSummary(carrierText);
        }
    }
}