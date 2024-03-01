package org.somethingos.somethingsettings.fragments.spoofing;

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

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.util.List;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Spoofing extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

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
        setPreferencesFromResource(R.xml.spoofing_settings, rootKey);

        getActivity().setTitle(R.string.something_spoofing_dashboard_title);

        SwitchPreference spoofGphotosPreference = (SwitchPreference) findPreference("spoof_gphotos");
        if (spoofGphotosPreference != null) {
            spoofGphotosPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                if ((Boolean) newValue) {
                    SystemProperties.set("persist.sys.somethingos.gphotos", "true");
                } else {
                    SystemProperties.set("persist.sys.somethingos.gphotos", "false");
                }
                return true;
            });
        }

        SwitchPreference spoofGoogleAppsPreference = (SwitchPreference) findPreference("spoof_google_apps");
        if (spoofGoogleAppsPreference != null) {
            spoofGoogleAppsPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                if ((Boolean) newValue) {
                    SystemProperties.set("persist.sys.somethingos.gapps", "true");
                } else {
                    SystemProperties.set("persist.sys.somethingos.gapps", "false");
                }
                return true;
            });
        }
    }
}