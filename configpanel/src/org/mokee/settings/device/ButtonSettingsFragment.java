/*
 * Copyright (C) 2016 The CyanogenMod Project
 *           (C) 2017 The LineageOS Project
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

package org.mokee.settings.device;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.MenuItem;

import org.mokee.internal.util.FileUtils;
import org.mokee.internal.util.PackageManagerUtils;

public class ButtonSettingsFragment extends PreferenceFragment
        implements OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.button_panel);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object newValue) {
        SwitchPreferenceBackend backend = Constants.sBackendsMap.get(pref.getKey());
        Boolean value = (Boolean) newValue;

        backend.setValue(value);

        return true;
    }

    @Override
    public void addPreferencesFromResource(int preferencesResId) {
        super.addPreferencesFromResource(preferencesResId);

        // Initialize node preferences
        for (String key : Constants.sBackendsMap.keySet()) {
            SwitchPreference pref = (SwitchPreference) findPreference(key);
            if (pref == null) {
                continue;
            }

            pref.setOnPreferenceChangeListener(this);

            SwitchPreferenceBackend backend = Constants.sBackendsMap.get(key);
            if (!backend.isValid()) {
                pref.setEnabled(false);
            } else {
                pref.setChecked(backend.getValue());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }
}
