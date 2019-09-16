package com.triagung.moviecataloguefinalsub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.triagung.moviecataloguefinalsub.R;
import com.triagung.moviecataloguefinalsub.receiver.ReminderReceiver;

public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private String DAILY_REMINDER;
    private String RELEASE_TODAY_REMINDER;
    private String CHANGE_LANGUAGE;

    public SettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DAILY_REMINDER = getResources().getString(R.string.key_daily_reminder);
        RELEASE_TODAY_REMINDER = getResources().getString(R.string.key_release_today_reminder);
        CHANGE_LANGUAGE = getResources().getString(R.string.key_change_language);

        SwitchPreference dailyReminderPreferences = (SwitchPreference) findPreference(DAILY_REMINDER);
        dailyReminderPreferences.setOnPreferenceChangeListener(this);

        SwitchPreference releaseTodayReminderPreferences = (SwitchPreference) findPreference(RELEASE_TODAY_REMINDER);
        releaseTodayReminderPreferences.setOnPreferenceChangeListener(this);

        Preference changeLanguagePreferences = findPreference(CHANGE_LANGUAGE);
        changeLanguagePreferences.setOnPreferenceClickListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String key = preference.getKey();
        boolean state = (boolean) o;

        ReminderReceiver reminderReceiver = new ReminderReceiver();

        if (key.equals(DAILY_REMINDER)) {
            if (state) {
                reminderReceiver.setDailyReminder(getActivity(), ReminderReceiver.TYPE_DAILY_REMINDER, "07:00");
                Toast.makeText(getActivity(), getResources().getString(R.string.daily_reminder_set_up), Toast.LENGTH_SHORT).show();
            } else {
                reminderReceiver.cancelReminder(getActivity(), ReminderReceiver.TYPE_DAILY_REMINDER);
                Toast.makeText(getActivity(), getResources().getString(R.string.cancel_daily_reminder), Toast.LENGTH_SHORT).show();
            }
        } else if (key.equals(RELEASE_TODAY_REMINDER)) {
            if (state) {
                reminderReceiver.setReleaseTodayReminder(getActivity(), ReminderReceiver.TYPE_RELEASE_TODAY_REMINDER, "08:00");
                Toast.makeText(getActivity(), getResources().getString(R.string.release_today_set_up), Toast.LENGTH_SHORT).show();
            } else {
                reminderReceiver.cancelReminder(getActivity(), ReminderReceiver.TYPE_RELEASE_TODAY_REMINDER);
                Toast.makeText(getActivity(), getResources().getString(R.string.cancel_release_today_reminder), Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(CHANGE_LANGUAGE)) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }

        return true;
    }
}
