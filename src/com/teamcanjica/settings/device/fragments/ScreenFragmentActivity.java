/*
 * Copyright (C) 2012 The CyanogenMod Project
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

package com.teamcanjica.settings.device.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.teamcanjica.settings.device.DeviceSettings;
import com.teamcanjica.settings.device.R;
import com.teamcanjica.settings.device.Utils;

public class ScreenFragmentActivity extends PreferenceFragment {

	private static final String TAG = "GalaxyAce2_Settings_Screen";

	private static final String FILE_SWEEP2WAKE_CODINA = "/sys/kernel/bt404/sweep2wake";
	private static final String FILE_SWEEP2WAKE_JANICE = "/sys/kernel/mxt224e/sweep2wake";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.screen_preferences);

		getActivity().getActionBar().setTitle(getResources().getString(R.string.screen_name));
		getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.screen_icon));

		// Compatibility check for codina (Panel Gamma & Touchscreen Sensitivity)
		if (Build.DEVICE == "codina" || Build.DEVICE == "codinap" || Build.MODEL == "GT-I8160"
				|| Build.MODEL == "GT-I8160P" || Build.PRODUCT == "GT-I8160" || Build.PRODUCT == "GT-I8160P") {
			getPreferenceScreen().removePreference(findPreference(DeviceSettings.KEY_SCREEN_COLOURS));

			PreferenceCategory touchscreenCategory = (PreferenceCategory) findPreference(DeviceSettings.KEY_TOUCHSCREEN);
			touchscreenCategory.removePreference(getPreferenceScreen().findPreference(DeviceSettings.KEY_TOUCHSCREEN_SENSITIVITY));
		}
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		String key = preference.getKey();

		Log.w(TAG, "key: " + key);

		if (key.equals(DeviceSettings.KEY_USE_SWEEP2WAKE)) {
			if (Build.DEVICE == "janice" || Build.DEVICE == "janicep" || Build.MODEL == "GT-I9070"
					|| Build.MODEL == "GT-I9070P" || Build.PRODUCT == "GT-I9070" || Build.PRODUCT == "GT-I9070P") {
				Utils.writeValue(FILE_SWEEP2WAKE_JANICE, (((CheckBoxPreference) preference).
						isChecked() ? "on" : "off"));
			} else {
				Utils.writeValue(FILE_SWEEP2WAKE_CODINA, (((CheckBoxPreference) preference).
						isChecked() ? "on" : "off"));
			}
		}

		return true;
	}

	public static void restore(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		if (Build.DEVICE == "janice" || Build.DEVICE == "janicep" || Build.MODEL == "GT-I9070"
				|| Build.MODEL == "GT-I9070P" || Build.PRODUCT == "GT-I9070" || Build.PRODUCT == "GT-I9070P") {
			Utils.writeValue(FILE_SWEEP2WAKE_JANICE, sharedPrefs.getBoolean(
					DeviceSettings.KEY_USE_SWEEP2WAKE, false) ? "on" : "off");
		} else {
			Utils.writeValue(FILE_SWEEP2WAKE_CODINA, sharedPrefs.getBoolean(
					DeviceSettings.KEY_USE_SWEEP2WAKE, false) ? "on" : "off");
		}

	}

}
