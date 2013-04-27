package com.seminarioAndroid.pyamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
public class UpdaterService extends Service {
private static final String TAG = "UpdaterService";
static final int DELAY = 60000; // a minute
private boolean runFlag = false; //
private Updater updater;
private YambaApplication1 yamba;

@Override
public IBinder onBind(Intent intent) {
return null;
}
@Override
public void onCreate() {
super.onCreate();
this.yamba=(YambaApplication1)getApplication();
this.updater = new Updater(); //
Log.d(TAG, "onCreated");
}
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
super.onStartCommand(intent, flags, startId);
this.runFlag = true; //
this.updater.start();
this.yamba.setServiceRunning(true);
Log.d(TAG, "onStarted");
return START_STICKY;
}
@Override
public void onDestroy() {
super.onDestroy();
this.runFlag = false; //

this.updater.interrupt(); //
this.updater = null;
this.yamba.setServiceRunning(false);
Log.d(TAG, "onDestroyed");
}
/**
* Thread that performs the actual update from the online service
*/
private class Updater extends Thread { //
	List<Twitter.Status>timeline;
public Updater() {
super("UpdaterService-Updater"); //
}
@Override
public void run() { //
UpdaterService updaterService = UpdaterService.this; //
while (updaterService.runFlag) { //
Log.d(TAG, "Updater running");
try {
try {
	timeline= yamba.getTwitter().getFriendsTimeline();
} catch (TwitterException e) {
	// TODO: handle exception
	Log.e(TAG, "Failed to connect to twitter service",e);
}
for(Twitter.Status status:timeline){
	Log.d(TAG, String.format("%s : %s", status.user.name, status.text));
}
	// Some work goes here...
Log.d(TAG, "Updater ran");
Thread.sleep(DELAY); //


} catch (InterruptedException e) { //
updaterService.runFlag = false;
}
}
}
} // Updater
}
