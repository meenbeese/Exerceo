package com.health.openworkout.gui.preference;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.health.openworkout.R;

import java.util.Calendar;

public class TimePreferenceDialog extends PreferenceDialogFragmentCompat {
    private Calendar calendar;
    private TimePicker timePicker;

    public static TimePreferenceDialog newInstance(String key) {
        final TimePreferenceDialog fragment = new TimePreferenceDialog();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        timePicker = view.findViewById(R.id.timePicker);
        calendar = Calendar.getInstance();

        Long timeInMillis = null;
        DialogPreference preference = getPreference();

        if (preference instanceof TimePreference) {
            TimePreference timePreference = (TimePreference) preference;
            timeInMillis = timePreference.getTimeInMillis();
        }

        if (timeInMillis != null) {
            calendar.setTimeInMillis(timeInMillis);
            boolean is24hour = DateFormat.is24HourFormat(getContext());

            timePicker.setIs24HourView(is24hour);
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int hours;
            int minutes;

            hours = timePicker.getHour();
            minutes = timePicker.getMinute();

            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);

            long timeInMillis = calendar.getTimeInMillis();

            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference) {
                TimePreference timePreference = ((TimePreference) preference);
                if (timePreference.callChangeListener(timeInMillis)) {
                    timePreference.setTimeInMillis(timeInMillis);
                    timePreference.setSummary(DateFormat.getTimeFormat(getContext()).format(calendar.getTime()));
                }
            }
        }
    }
}
