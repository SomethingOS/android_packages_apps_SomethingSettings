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
import androidx.preference.SwitchPreference;
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

    private static final String[] certifiedProps = {
        "MANUFACTURER",
        "BRAND",
        "DEVICE",
        "MODEL",
        "PRODUCT",
        "FINGERPRINT",
        "SECURITY_PATCH",
        "FIRST_API_LEVEL"
    };

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

        SwitchPreference playIntegritySwitch = findPreference("play_integrity_switch");
        SwitchPreference playIntegrityTrickySwitch = findPreference("play_integrity_tricky_switch");
        PreferenceCategory fingerprintCategory = findPreference("play_fingerprint_category");

        for (String key : certifiedProps) {
            String value = SystemProperties.get("persist.sys.somethingos.gms." + key);
            if (value != null && !value.isEmpty()) {
                Preference preference = new Preference(getPreferenceManager().getContext());
                preference.setKey(key);
                preference.setTitle(key);
                preference.setSummary(value);
                preference.setSelectable(false);
                fingerprintCategory.addPreference(preference);
            }
        }

        if (playIntegritySwitch != null) {
            boolean isEnabled = SystemProperties.getBoolean("persist.sys.somethingos.gms.enabled", true);
            playIntegritySwitch.setChecked(isEnabled);
          
            playIntegritySwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnabledNew = (Boolean) newValue;
                SystemProperties.set("persist.sys.somethingos.gms.enabled", isEnabledNew ? "true" : "false");
                killGmsProcess();
                return true;
            });
        }

        if (playIntegrityTrickySwitch != null) {
            boolean isEnabled = SystemProperties.getBoolean("persist.sys.somethingos.tee.enabled", false);
            playIntegrityTrickySwitch.setChecked(isEnabled);

            playIntegrityTrickySwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnabledNew = (Boolean) newValue;
                SystemProperties.set("persist.sys.somethingos.tee.enabled", isEnabledNew ? "true" : "false");
                killGmsProcess();
                return true;
            });
        }
    }

    private HashMap<String, String> extractProperties(String content) {
        HashMap<String, String> properties = new HashMap<>();
        String[] lines = content.split("\n");
        boolean firstLine = true;
        for (String line : lines) {
            if (firstLine) {
                firstLine = false;
                if (line.contains("PRODUCT_PROPERTY_OVERRIDES +=")) {
                    continue;
                }
            }
            line = line.trim();
            if (!line.isEmpty() && line.contains("=")) {
                String[] keyValue = line.split("=", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\\", "");
                    String value = keyValue[1].trim().replace("\\", "");
                    properties.put(key, value);
                }
            }
        }
        return properties;
    }
    
    public void setPropertiesFromUrl() {
        new AsyncTask<Void, Void, String>() {    
    
            @Override
            protected String doInBackground(Void... voids) {
                String message = "";
                try {
                    URL url = new URL("https://gitlab.com/somethingos/android_vendor_something_certification/-/raw/uvite/certification.mk");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    
                    String content = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content += line + "\n";
                    }
                    reader.close();
    
                    HashMap<String, String> properties = extractProperties(content);
                    for (Map.Entry<String, String> entry : properties.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        SystemProperties.set(key, value);
                    }
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
        PreferenceCategory fingerprintCategory = findPreference("play_fingerprint_category");
        fingerprintCategory.removeAll();

        for (String key : certifiedProps) {
            String value = SystemProperties.get("persist.sys.somethingos.gms." + key);
            if (value != null && !value.isEmpty()) {
                Preference preference = new Preference(getPreferenceManager().getContext());
                preference.setKey(key);
                preference.setTitle(key);
                preference.setSummary(value);
                preference.setSelectable(false);
                fingerprintCategory.addPreference(preference);
            }
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