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

    private static class Credit {
        String title;
        String description;
        String link;

        Credit(String title, String description, String link) {
            this.title = title;
            this.description = description;
            this.link = link;
        }
    };

    //List of early donators
    private static final String[] earlyDonators = {
        "Kevin Pirnie",
        "Sehmee2",
	"DigiGoon",
	"Léo Chatron",
	"Eliacim"
    };

    private static final Credit[] somethingOSTeam = {
        new Credit("Dylan AKPINAR", "Founder and Lead Developer", "http://www.github.com/DylanAkp")
    };

    //List of credits
    private static final Credit[] credits = {
        new Credit("Google", "Keep improving Android", "https://android.com"),
        new Credit("ParanoidAndroid Team", "SomethingOS is based on AOSPA", "https://paranoidandroid.co"),
        new Credit("LineageOS Team", "Updater and much more", "https://lineageos.org"),
        new Credit("CrDroid", "Some ideas and code", "https://crdroid.net"),
        new Credit("Lawnchair Team", "They made Lawnchair, the default launcher in SomethingOS", "https://lawnchair.app"),
        new Credit("That Josh Guy", "His amazing Android Wallpaper", "https://thatjoshguy.me/"),
        new Credit("HESEINBERG", "His help on sources", "https://github.com/janakniraula"),
        new Credit("z-huang", "for Innertune", "https://github.com/z-huang/InnerTune"),
        new Credit("zhanghai", "for MaterialFiles", "https://github.com/zhanghai/MaterialFiles"),
        new Credit("And many more", "Check commit authors on Github", "https://github.com/SomethingOS"),
        new Credit("You", "For using SomethingOS", "https://www.somethingos.com/")
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
        setPreferencesFromResource(R.xml.about_something, rootKey);

        getActivity().setTitle(R.string.about_something_title);

        setUrlPreferenceClickListener("something_website", "https://www.somethingos.com/");
        setUrlPreferenceClickListener("something_telegram", "http://www.telegram.me/SomethingOS");
        setUrlPreferenceClickListener("something_twitter", "http://www.twitter.com/SomethingOS");
        setUrlPreferenceClickListener("something_donate", "http://www.buymeacoffee.com/SomethingOS");
        setUrlPreferenceClickListener("something_donate2", "https://www.somethingos.com/paypal");

        // Set the early donators
        PreferenceCategory earlyDonatorsCategory = findPreference("something_early_donators");
        for (String donator : earlyDonators) {
            Preference donatorPreference = new Preference(getContext());
            donatorPreference.setTitle(donator);
            earlyDonatorsCategory.addPreference(donatorPreference);
        }

        // Set the SomethingOS team
        PreferenceCategory somethingTeamCategory = findPreference("something_team");
        for (Credit teamMember : somethingOSTeam) {
            Preference teamMemberPreference = new Preference(getContext());
            teamMemberPreference.setTitle(teamMember.title);
            teamMemberPreference.setSummary(teamMember.description);
            teamMemberPreference.setOnPreferenceClickListener(preference -> {
                return openUrl(teamMember.link);
            });
            somethingTeamCategory.addPreference(teamMemberPreference);
        }

        // Set the credits
        PreferenceCategory creditsCategory = findPreference("something_credits");
        for (Credit credit : credits) {
            Preference creditPreference = new Preference(getContext());
            creditPreference.setTitle(credit.title);
            creditPreference.setSummary(credit.description);
            creditPreference.setOnPreferenceClickListener(preference -> {
                return openUrl(credit.link);
            });
            creditsCategory.addPreference(creditPreference);
        }
    }

    private void setUrlPreferenceClickListener(String preferenceKey, String url) {
        findPreference(preferenceKey).setOnPreferenceClickListener(preference -> {
            openUrl(url);
            return true;
        });
    }

    private boolean openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        return true;
    }
}
