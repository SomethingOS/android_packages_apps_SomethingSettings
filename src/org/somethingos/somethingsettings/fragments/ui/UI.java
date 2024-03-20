package org.somethingos.somethingsettings.fragments.ui;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;


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

        SwitchPreference enableAdblock = (SwitchPreference) findPreference("enable_adblock");
        if (enableAdblock != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            boolean enableAdblockValue = prefs.getBoolean("enable_adblock", false);
            enableAdblock.setChecked(enableAdblockValue);

            enableAdblock.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (Boolean) newValue;
                prefs.edit().putBoolean("enable_adblock", isChecked).apply();

                if (isChecked) {
                    Settings.Global.putString(
                        getContext().getContentResolver(),
                        Settings.Global.PRIVATE_DNS_MODE,
                        "hostname"
                    );
                    Settings.Global.putString(
                        getContext().getContentResolver(),
                        Settings.Global.PRIVATE_DNS_SPECIFIER,
                        "dns.adguard.com"
                    );
                } else {
                    Settings.Global.putString(
                        getContext().getContentResolver(),
                        Settings.Global.PRIVATE_DNS_MODE,
                        "off"
                    );
                }

                return true;
            });
        }
    }
}