package com.health.openworkout.gui.preference;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.health.openworkout.R;

public class SoundPreferences extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.sound_preferences, rootKey);
    }

}
