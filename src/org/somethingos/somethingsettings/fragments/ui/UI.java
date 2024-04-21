package org.somethingos.somethingsettings.fragments.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class UI extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private ListPreference bootanimationPreference;

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
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
            setHideQSonSecureLockscreen(hideQSonSecureLockscreen);
        }
    }

    private void setHideQSonSecureLockscreen(SwitchPreference hideQSonSecureLockscreen) {
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