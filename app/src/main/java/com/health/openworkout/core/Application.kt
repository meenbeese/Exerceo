package com.health.openworkout.core

import android.app.Application

import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

import com.health.openworkout.BuildConfig

import timber.log.Timber
import timber.log.Timber.DebugTree

class Application : Application() {
    private var openWorkout: OpenWorkout? = null

    private inner class TimberLogAdapter : DebugTree() {
        override fun isLoggable(tag: String?, priority: Int): Boolean {
            if (BuildConfig.DEBUG || OpenWorkout.DEBUG_MODE) {
                return super.isLoggable(tag, priority)
            }
            return false
        }
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(TimberLogAdapter())

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
