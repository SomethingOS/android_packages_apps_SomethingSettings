package org.somethingos.somethingsettings.fragments.system;

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


public class System extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

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
        setPreferencesFromResource(R.xml.system_settings, rootKey);

        getActivity().setTitle(R.string.something_system_dashboard_title);

        SwitchPreference enableAdblock = (SwitchPreference) findPreference("enable_adblock");
        if (enableAdblock != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            boolean enableAdblockValue = prefs.getBoolean("enable_adblock", false);
            enableAdblock.setChecked(enableAdblockValue);

            enableAdblock.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (Boolean) newValue;
                prefs.edit().putBoolean("enable_adblock", isChecked).apply();
                dnsHandler(isChecked);
                return true;
            });
        }
    }

    private void dnsHandler(boolean isChecked) {
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
    }
}