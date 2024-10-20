package com.health.openworkout.gui.preference

import android.os.Bundle

import androidx.preference.PreferenceFragmentCompat

import com.health.openworkout.R

class SoundPreferences : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.sound_preferences, rootKey)
    }
}
