/*
 * All credits for this part goes to Comatose : https://github.com/tytydraco/Comatose
 */

package org.somethingos.somethingsettings.fragments.system.batteryprofiles;

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


public class BatteryProfiles extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

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
        setPreferencesFromResource(R.xml.batteryprofiles_settings, rootKey);

        getActivity().setTitle(R.string.something_batteryprofiles_dashboard_title);

        
    }

    private fun applyProfile(config: String?) {
        val contentResolver = requireContext().contentResolver
        Settings.Global.putString(
            contentResolver,
            SettingsConstants.DEVICE_IDLE_CONSTANTS,
            config
        )

        with (sharedPrefs.edit()) {
            putString(requireContext().getString(R.string.pref_saved_constants_key), config)
            apply()
        }

        Snackbar.make(requireView(), getString(R.string.snackbar_applied), Snackbar.LENGTH_SHORT).show()
    }

}