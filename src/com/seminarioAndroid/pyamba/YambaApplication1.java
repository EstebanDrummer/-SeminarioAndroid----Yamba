package com.seminarioAndroid.pyamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;


	public class YambaApplication1 extends Application implements OnSharedPreferenceChangeListener { //
	private static final String TAG = YambaApplication1.class.getSimpleName();
	public Twitter twitter; //
	private SharedPreferences prefs;
	
	private boolean serviceRunning;
	public boolean isServiceRunning(){
		return serviceRunning;
	}
	public void setServiceRunning(boolean serviceRunning){
		this.serviceRunning=serviceRunning;
	}

	@Override
	public void onCreate() { //
	super.onCreate();
	this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
	this.prefs.registerOnSharedPreferenceChangeListener(this);
	Log.i(TAG, "onCreated");
	}
	@Override
	public void onTerminate() { //
	super.onTerminate();
	Log.i(TAG, "onTerminated");
	}
	public synchronized Twitter getTwitter() { //
	if (this.twitter == null) {
	String username = this.prefs.getString("username", "");
	String password = this.prefs.getString("password", "");
	String apiRoot = prefs.getString("apiRoot",
	"http://yamba.marakana.com/api");
	if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
	&& !TextUtils.isEmpty(apiRoot)) {
	this.twitter = new Twitter(username, password);
	this.twitter.setAPIRootUrl(apiRoot);
	}
	}
	return this.twitter;
	}
	public synchronized void onSharedPreferenceChanged(
	SharedPreferences sharedPreferences, String key) { //
	this.twitter = null;
	}
	}
	

