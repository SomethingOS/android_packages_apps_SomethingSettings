package org.somethingos.somethingsettings.fragments.ui;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.PreferenceManager;
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class UI extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    //private ListPreference qsShowBrightnessSliderPreference;
    //private ListPreference qsBrightnessSliderPositionPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.ui_settings, rootKey);

        getActivity().setTitle(R.string.something_ui_dashboard_title);

        /*qsShowBrightnessSliderPreference = findPreference("qs_show_brightness_slider");
        qsBrightnessSliderPositionPreference = findPreference("qs_brightness_slider_position");

        int currentShowBrightnessSlider = Settings.Secure.getInt(getActivity().getContentResolver(),
                Settings.Secure.QS_SHOW_BRIGHTNESS_SLIDER,
                1);
        int currentBrightnessSliderPosition = Settings.Secure.getInt(getActivity().getContentResolver(),
                Settings.Secure.QS_BRIGHTNESS_SLIDER_POSITION,
                0);

        qsShowBrightnessSliderPreference.setValue(String.valueOf(currentShowBrightnessSlider));
        qsBrightnessSliderPositionPreference.setValue(String.valueOf(currentBrightnessSliderPosition));

        qsShowBrightnessSliderPreference.setOnPreferenceChangeListener(this);
        qsBrightnessSliderPositionPreference.setOnPreferenceChangeListener(this);*/
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Context context = getActivity().getApplicationContext();
        /*if (preference == qsShowBrightnessSliderPreference) {
            Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.QS_SHOW_BRIGHTNESS_SLIDER, Integer.parseInt((String) newValue));
        } else if (preference == qsBrightnessSliderPositionPreference) {
            Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.QS_BRIGHTNESS_SLIDER_POSITION, Integer.parseInt((String) newValue));
        }*/
        return true;
    }

    @Override
    public int getMetricsCategory() {
        return 0;
    }
}