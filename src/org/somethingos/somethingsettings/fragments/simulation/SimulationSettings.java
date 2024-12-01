package org.somethingos.somethingsettings.fragments.simulation;

import android.content.Intent;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import android.os.SystemProperties;
import android.os.Bundle;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class SimulationSettings extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String UNLIMITED_GPHOTOS = "persist.sys.sih.unlimited_gphotos";
    private static final String PIXEL_FEATURES = "persist.sys.sih.pixel_features";

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("gphotos_switch")) {
            boolean enabled = (Boolean) newValue;
            SystemProperties.set(UNLIMITED_GPHOTOS, enabled ? "true" : "false");
            return true;
        } else if (preference.getKey().equals("gapps_switch")) {
            boolean enabled = (Boolean) newValue;
            SystemProperties.set(PIXEL_FEATURES, enabled ? "true" : "false");
            return true;
        }
        return false;
    }


    @Override
    public int getMetricsCategory() {
        return 0;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.simulation_settings, rootKey);

        getActivity().setTitle(R.string.something_simulation_title);
        
        SwitchPreference gPhotosPreference = (SwitchPreference) findPreference("gphotos_switch");
        SwitchPreference pixelFeaturesPreference = (SwitchPreference) findPreference("gapps_switch");

        if (gPhotosPreference != null) gPhotosPreference.setChecked(SystemProperties.getBoolean(UNLIMITED_GPHOTOS, false));
        if (pixelFeaturesPreference != null) pixelFeaturesPreference.setChecked(SystemProperties.getBoolean(PIXEL_FEATURES, false));

        gPhotosPreference.setOnPreferenceChangeListener(this);
        pixelFeaturesPreference.setOnPreferenceChangeListener(this);
    }
}
