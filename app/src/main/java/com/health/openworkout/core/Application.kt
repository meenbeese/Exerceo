package com.health.openworkout.core

import android.app.Application

import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class Application : Application() {
    private var openWorkout: OpenWorkout? = null

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("darkTheme", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Create openWorkout instance
        OpenWorkout.createInstance(applicationContext)

        // Hold on to the instance for as long as the application exists
        openWorkout = OpenWorkout.getInstance()
    }
}
