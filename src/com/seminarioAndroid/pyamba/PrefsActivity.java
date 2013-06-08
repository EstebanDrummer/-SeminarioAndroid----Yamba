package com.seminarioAndroid.pyamba;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		extracted();
	}
	private void extracted(){
		addPreferencesFromResource(R.xml.prefs);
	}
}
