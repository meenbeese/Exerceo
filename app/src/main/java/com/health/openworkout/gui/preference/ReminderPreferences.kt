package com.health.openworkout.gui.preference

import android.content.ComponentName
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.pm.PackageManager
import android.os.Bundle

import androidx.fragment.app.DialogFragment
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.Preference.SummaryProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

import com.health.openworkout.R
import com.health.openworkout.core.alarm.AlarmHandler
import com.health.openworkout.core.alarm.ReminderBootReceiver
import com.health.openworkout.gui.preference.TimePreferenceDialog.Companion.newInstance

class ReminderPreferences : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    private var reminderEnable: SwitchPreferenceCompat? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.reminder_preferences, rootKey)

        val prefDays = findPreference<MultiSelectListPreference>("reminderDays")

        prefDays?.summaryProvider = SummaryProvider { preference: MultiSelectListPreference ->
            preference.values.toString()
        }

        reminderEnable = findPreference("reminderEnable")
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        var dialogFragment: DialogFragment? = null

        if (preference is TimePreference) {
            dialogFragment = newInstance(preference.getKey())
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, "timePreferenceDialog")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        updateAlarmPreferences()
    }

    private fun updateAlarmPreferences() {
        val receiver = ComponentName(requireActivity().applicationContext, ReminderBootReceiver::class.java)
        val pm = requireActivity().applicationContext.packageManager

        val alarmHandler = AlarmHandler()

        if (reminderEnable!!.isChecked) {
            alarmHandler.scheduleAlarms(activity)

            pm.setComponentEnabledSetting(
                receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } else {
            alarmHandler.disableAllAlarms(activity)

            pm.setComponentEnabledSetting(
                receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}
