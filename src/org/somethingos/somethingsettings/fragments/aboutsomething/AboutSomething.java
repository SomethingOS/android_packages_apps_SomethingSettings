package org.somethingos.somethingsettings.fragments.aboutsomething;

import android.content.Intent;
import android.net.Uri;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceCategory;
import android.os.Bundle;


import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;


public class AboutSomething extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

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
        setPreferencesFromResource(R.xml.about_something, rootKey);

        getActivity().setTitle(R.string.about_something_title);

        findPreference("something_website").setOnPreferenceClickListener(preference -> openWebsite());
        findPreference("something_telegram").setOnPreferenceClickListener(preference -> openTelegram());
        findPreference("something_twitter").setOnPreferenceClickListener(preference -> openTwitter());
        findPreference("something_donate").setOnPreferenceClickListener(preference -> openDonate());
        findPreference("dylanakp").setOnPreferenceClickListener(preference -> openDylanAkp());
    }

    private boolean openWebsite() {
        openUrl("https://www.somethingos.com/");
        return true;
    }

    private boolean openTelegram() {
        openUrl("http://www.telegram.me/SomethingOS");
        return true;
    }

    private boolean openTwitter() {
        openUrl("http://www.twitter.com/SomethingOS");
        return true;
    }

    private boolean openDonate() {
        openUrl("http://www.buymeacoffee.com/SomethingOS");
        return true;
    }

    private boolean openDylanAkp() {
        openUrl("http://www.github.com/DylanAkp");
        return true;
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}