package com.health.openworkout.gui.preference

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.Navigation.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.SwitchPreferenceCompat

import com.health.openworkout.BuildConfig
import com.health.openworkout.R
import com.health.openworkout.gui.utils.FileDialogHelper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class MainPreferences : PreferenceFragmentCompat() {
    private var fileDialogHelper: FileDialogHelper? = null

    private val TAG: String = javaClass.simpleName

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey)

        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorControlNormal, typedValue, true)
        val color = ContextCompat.getColor(requireContext(), typedValue.resourceId)

        tintIcons(preferenceScreen, color)

        fileDialogHelper = FileDialogHelper(this)

        val prefDarkTheme = findPreference<SwitchPreferenceCompat>("darkTheme")
        prefDarkTheme!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, _: Any? ->
            if (!prefDarkTheme.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }

        val prefReminder = findPreference<Preference>("reminder")
        prefReminder!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val action = MainPreferencesDirections.actionMainPreferencesFragmentToReminderPreferencesFragment()
            findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
            true
        }

        val prefSound = findPreference<Preference>("sound")
        prefSound!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val action = MainPreferencesDirections.actionMainPreferencesFragmentToSoundPreferencesFragment()
            findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
            true
        }

        val prefDebugLogging = findPreference<SwitchPreferenceCompat>("debugLogging")
        prefDebugLogging!!.isChecked = BuildConfig.DEBUG
        prefDebugLogging.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, _: Any? ->
            if (!prefDebugLogging.isChecked) {
                val format: DateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm")
                val fileName = String.format("openWorkout_%s.txt", format.format(Date()))

                fileDialogHelper!!.openDebugFileDialog(fileName)
            }
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        fileDialogHelper!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (fileDialogHelper!!.onActivityResult(requestCode, resultCode, data)) {
            Log.d(TAG, "Debug log enabled, ${resources.getString(R.string.app_name)} v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}), SDK ${Build.VERSION.SDK_INT}, ${Build.MANUFACTURER} ${Build.MODEL}")
        }
    }

    companion object {
        private fun tintIcons(preference: Preference, color: Int) {
            if (preference is PreferenceGroup) {
                for (i in 0 until preference.preferenceCount) {
                    tintIcons(preference.getPreference(i), color)
                }
            } else {
                val icon = preference.icon
                if (icon != null) {
                    DrawableCompat.setTint(icon, color)
                }
            }
        }
    }
}
