package org.somethingos.somethingsettings.fragments.ui.bootanimation;

import android.os.Bundle;
import android.os.SystemProperties;
import android.os.Handler;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceCategory;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.widget.VideoPreference;

public class BootAnimation extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

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
        setPreferencesFromResource(R.xml.bootanimation_settings, rootKey);

        getActivity().setTitle(R.string.bootanimation_title);

        String bootanimationProp = SystemProperties.get("persist.sys.somethingos.bootanimation");

        ListPreference bootanimationPreference = findPreference("bootanimation_preference");
        bootanimationPreference.setEntries(R.array.bootanimation_names);
        bootanimationPreference.setEntryValues(R.array.bootanimation_values);
        bootanimationPreference.setValue(bootanimationProp);
        bootanimationPreference.setSummary(bootanimationPreference.getEntry());
        bootanimationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(newValue.toString());
                listPreference.setSummary(listPreference.getEntries()[index]);
                SystemProperties.set("persist.sys.somethingos.bootanimation", newValue.toString());
                setVideo(newValue.toString());
                return true;
            }
        });
        PreferenceCategory previewCategory = findPreference("preview_category");
        createVideoPreference();
        setVideo(bootanimationProp);
    }

    private void createVideoPreference() {
        VideoPreference videoPreference = new VideoPreference(getContext());
        videoPreference.setKey("bootanim_preview");
        PreferenceCategory previewCategory = findPreference("preview_category");
        previewCategory.addPreference(videoPreference);
    }

    private void removeVideoPreference() {
        PreferenceCategory previewCategory = findPreference("preview_category");
        VideoPreference videoPreference = findPreference("bootanim_preview");
        previewCategory.removePreference(videoPreference);
    }

    public void setVideo(String bootanimation) {
        removeVideoPreference();
        createVideoPreference();
        VideoPreference videoPreference = findPreference("bootanim_preview");
        switch (bootanimation) {
            case "somethingos":
                videoPreference.setVideo(R.raw.bootanim_somethingos, 0);
                break;
            case "googlish":
                videoPreference.setVideo(R.raw.bootanim_googlish, 0);
                break;
            case "dot":
                videoPreference.setVideo(R.raw.bootanim_dots, 0);
                break;
            case "pixel":
                videoPreference.setVideo(R.raw.bootanim_pixel, 0);
                break;
            default:
                videoPreference.setVideo(R.raw.bootanim_somethingos, 0);
                break;
        }
    }
}