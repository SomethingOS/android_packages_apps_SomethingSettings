package org.somethingos.somethingsettings.fragments.ui;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;
import android.os.SystemProperties;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;


import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;


public class UI extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return 0;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.ui_settings, rootKey);

        getActivity().setTitle(R.string.something_ui_dashboard_title);

        SwitchPreference hideQSonSecureLockscreen = (SwitchPreference) findPreference("secure_lockscreen_qs_disabled");
        if (hideQSonSecureLockscreen != null) {
            int currentValue = Settings.System.getIntForUser(
                getContext().getContentResolver(),
                Settings.System.SECURE_LOCKSCREEN_QS_DISABLED,
                0,
                android.os.UserHandle.USER_CURRENT
            );
            hideQSonSecureLockscreen.setChecked(currentValue != 0);

            hideQSonSecureLockscreen.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (Boolean) newValue;
                int value = isChecked ? 1 : 0;
                Settings.System.putIntForUser(
                    getContext().getContentResolver(),
                    Settings.System.SECURE_LOCKSCREEN_QS_DISABLED,
                    value,
                    android.os.UserHandle.USER_CURRENT
                );
                return true;
            });
        }
    }
}