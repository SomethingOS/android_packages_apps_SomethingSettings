package org.somethingos.somethingsettings.fragments.ui.qs;

import android.os.Bundle;

import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;


public class QuickSettings extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {
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
        setPreferencesFromResource(R.xml.qs_settings, rootKey);

        getActivity().setTitle(R.string.something_quick_settings_dashboard_title);
    }
}