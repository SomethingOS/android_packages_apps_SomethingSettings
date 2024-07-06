/*
 * Copyright (C) 2014-2021 The BlissRoms Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.somethingos.somethingsettings;

import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

@SearchIndexable(forTarget = SearchIndexable.ALL & ~SearchIndexable.ARC)
public class Something extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.something);
        onSetPrefCard();
    }

    @Override
    public int getMetricsCategory() {
        return 0;
    }

    private void onSetPrefCard() {
        final boolean newDesign = Settings.Secure.getInt(getContext().getContentResolver(),
                Settings.Secure.NEW_SETTINGS_LAYOUT, 0) == 1;
        final PreferenceScreen screen = getPreferenceScreen();
        final int count = screen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            final Preference preference = screen.getPreference(i);
            String key = preference.getKey();
            if (newDesign) {
                if (key.equals("pintegrity_category")){
                    preference.setLayoutResource(R.layout.something_dashboard_preference_top);
                } else if (key.equals("spoofing_category")
                    || key.equals("ui_category")
                    || key.equals("system_category")){
                    preference.setLayoutResource(R.layout.something_dashboard_preference_middle);
                } else if (key.equals("about_something")){
                    preference.setLayoutResource(R.layout.something_dashboard_preference_bottom);
                }
            }
       }
    }

    /**
     * For Search.
     */

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.something);
}
