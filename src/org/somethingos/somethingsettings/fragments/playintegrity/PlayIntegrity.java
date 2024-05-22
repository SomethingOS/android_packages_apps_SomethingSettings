package org.somethingos.somethingsettings.fragments.playintegrity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;
import android.os.SystemProperties;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceCategory;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.io.BufferedReader;
import java.util.List;
import java.lang.reflect.Method;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayIntegrity extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {
    private static final String PROP_PREFIX = "persist.sys.somethingos.gms.";

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
        setPreferencesFromResource(R.xml.playintegrity_settings, rootKey);

        getActivity().setTitle(R.string.something_play_integrity_dashboard_title);

        Preference setPropertiesPreference = findPreference("play_integrity_update");
        if (setPropertiesPreference != null) {
            setPropertiesPreference.setOnPreferenceClickListener(preference -> {
                setPropertiesFromUrl();
                return true;
            });
        }

        PreferenceCategory fingerprintCategory = findPreference("play_fingeprint_category");
        String keysList = SystemProperties.get(PROP_PREFIX + "list");
        String[] keys = keysList.split("\\+");

        for (String key : keys) {
            String value = SystemProperties.get(PROP_PREFIX + key);
            Preference preference = new Preference(getPreferenceManager().getContext());
            preference.setKey(key);
            preference.setTitle(key);
            preference.setSummary(value);
            preference.setSelectable(false);
            fingerprintCategory.addPreference(preference);
        }
        
    }

    public void setPropertiesFromUrl() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String message = "";
                try {
                    URL url = new URL("https://gitlab.com/somethingos/android_vendor_something_certification/-/raw/universal/certification.prop");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith(PROP_PREFIX)) {
                            SystemProperties.set(line.split("=")[0], line.split("=")[1]);
                        }
                    }
                    reader.close();
                    message = "Applied properties, you should pass Play Integrity.";
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Failed to download spoofing properties.";
                }
                return message;
            }

            @Override
            protected void onPostExecute(String message) {
                killGmsProcess();
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                reloadPreferences();
            }
        }.execute();
    }

    public void reloadPreferences() {
        PreferenceCategory fingerprintCategory = findPreference("play_fingeprint_category");
        fingerprintCategory.removeAll();
        String keysList = SystemProperties.get(PROP_PREFIX + "list");
        String[] keys = keysList.split("\\+");
    
        for (String key : keys) {
            String value = SystemProperties.get(PROP_PREFIX + key);
            Preference preference = new Preference(getPreferenceManager().getContext());
            preference.setKey(key);
            preference.setTitle(key);
            preference.setSummary(value);
            preference.setSelectable(false);
            fingerprintCategory.addPreference(preference);
        }
    }

    public void killGmsProcess() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String message = "";
                try {
                    ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                    List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
                    for (RunningAppProcessInfo processInfo : runningAppProcesses) {
                        if (processInfo.processName.equals("com.google.android.gms")) {
                            Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
                            forceStopPackage.setAccessible(true);
                            forceStopPackage.invoke(am, "com.google.android.gms");
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Failed to kill Google Play Services. Please kill them manually.";
                }
                return message;
            }

            @Override
            protected void onPostExecute(String message) {
                if (!message.isEmpty()) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}