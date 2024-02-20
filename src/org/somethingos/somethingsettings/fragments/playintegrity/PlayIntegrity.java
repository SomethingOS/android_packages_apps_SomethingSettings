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

public class PlayIntegrity extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

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

        Preference killGmsPreference = findPreference("play_integrity_killgms");
        if (killGmsPreference != null) {
            killGmsPreference.setOnPreferenceClickListener(preference -> {
                killGmsProcess();
                return true;
            });
        }
    }

    public void setPropertiesFromUrl() {
        new AsyncTask<Void, Void, String>() {    
             
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder keysBuilder = new StringBuilder();
                String message = "";
                try {
                    URL url = new URL("https://raw.githubusercontent.com/HWVSBI/AIHIWB/main/PROPS");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("=");
                        if (parts.length == 2) {
                            String key = parts[0];
                            String value = parts[1];
                            SystemProperties.set("persist.sys.somethingos.gms." + key, value);
                            if (keysBuilder.length() > 0) {
                                keysBuilder.append("+");
                            }
                            keysBuilder.append(key);
                        }
                    }
                    reader.close();
                    SystemProperties.set("persist.sys.somethingos.gms.list", keysBuilder.toString());
                    message = "Applied properties, kill Google Play Services to apply changes.";
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Failed to download spoofing properties.";
                }
                return message;
            }

            @Override
            protected void onPostExecute(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }.execute();
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
                            message = "Killed Google Play Services.";
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Failed to kill Google Play Services.";
                }
                return message;
            }

            @Override
            protected void onPostExecute(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }
}