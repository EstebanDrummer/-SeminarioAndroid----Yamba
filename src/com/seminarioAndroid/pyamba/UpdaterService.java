package com.seminarioAndroid.pyamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends IntentService {
	
	public static final String NEW_STATUS_INTENT="com.seminarioAndroid.pyamba.NEW_STATUS";
	public static final String NEW_STATUS_EXTRA_COUNT = "NEW_STATUS_EXTRA_COUNT";
	private static final String TAG = "UpdaterService";
	public static final String RECEIVE_TIMELINE_NOTIFICATIONS = "com.seminarioAndroid.pyamba.RECEIVE_TIMELINE_NOTIFICATIONS";
	
	 private NotificationManager notificationManager; // <1>
	  private Notification notification; // <2>
	
	static final int DELAY = 10000; // a minute
	private boolean runFlag = false; //
	private Updater updater;
	private YambaApplication1 yamba;

	//DbHelper1 dbHelper;
	//SQLiteDatabase db;
	  public UpdaterService() { // <2>
		    super(TAG);

		    Log.d(TAG, "UpdaterService constructed");
		  }
	  
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.yamba = (YambaApplication1) getApplication();
		this.updater = new Updater(); //
		//dbHelper = new DbHelper1(this);//
		Log.d(TAG, "onCreated");
	}
	  @Override
	  protected void onHandleIntent(Intent inIntent) { // <3>
	    Intent intent;
	    this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // <3>
	    this.notification = new Notification(android.R.drawable.stat_notify_chat,
	        "", 0); // <4>
	    Log.d(TAG, "onHandleIntent'ing");
	    YambaApplication1 yamba = (YambaApplication1) getApplication();
	    int newUpdates = yamba.fetchStatusUpdates();
	    if (newUpdates > 0) { 
	      Log.d(TAG, "We have a new status");
	      intent = new Intent(NEW_STATUS_INTENT);
	      intent.putExtra(NEW_STATUS_EXTRA_COUNT, newUpdates);
	      sendBroadcast(intent, RECEIVE_TIMELINE_NOTIFICATIONS);
	      sendTimelineNotification(newUpdates); 
	    }
	  }

	  /**
	   * Creates a notification in the notification bar telling user there are new
	   * messages
	   * 
	   * @param timelineUpdateCount
	   *          Number of new statuses
	   */
	  private void sendTimelineNotification(int timelineUpdateCount) {
	    Log.d(TAG, "sendTimelineNotification'ing");
	    PendingIntent pendingIntent = PendingIntent.getActivity(this, -1,
	        new Intent(this, TimelineActivity.class),
	        PendingIntent.FLAG_UPDATE_CURRENT); // <6>
	    this.notification.when = System.currentTimeMillis(); // <7>
	    this.notification.flags |= Notification.FLAG_AUTO_CANCEL; // <8>
	    CharSequence notificationTitle = this
	        .getText(R.string.msgNotificationTitle); // <9>
	    CharSequence notificationSummary = this.getString(
	        R.string.msgNotificationMessage, timelineUpdateCount);
	    this.notification.setLatestEventInfo(this, notificationTitle,
	        notificationSummary, pendingIntent); // <10>
	    this.notificationManager.notify(0, this.notification);
	    Log.d(TAG, "sendTimelineNotificationed");
	  }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		if (!runFlag) {
			this.runFlag = true;
			this.updater.start();
			((YambaApplication1) super.getApplication()).setServiceRunning(true);
			Log.d(TAG, "onStarted");
		}
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
		static final String RECEIVE_TIMELINE_NOTIFICATIONS = "com.seminarioAndroid.pyamba.RECEIVE_TIMELINE_NOTIFICATIONS";
		Intent intent;

		public Updater() {
			super("UpdaterService-Updater"); //
		}

		@Override
		public void run() { //
			UpdaterService updaterService = UpdaterService.this; //
			while (updaterService.runFlag) { //
				Log.d(TAG, "Updater running");
				try {
					YambaApplication1 yamba = (YambaApplication1) updaterService.getApplication();
					int newUpdates = yamba.fetchStatusUpdates();
					if(newUpdates>0){
						Log.d(TAG, "We have a new status");
						intent = new Intent(NEW_STATUS_INTENT);
						intent.putExtra(NEW_STATUS_EXTRA_COUNT, newUpdates);
						updaterService.sendBroadcast(intent, RECEIVE_TIMELINE_NOTIFICATIONS);
					}
					Log.d(TAG, "Updater Ran");
					Thread.sleep(DELAY);
				}catch(InterruptedException e){
					
					updaterService.runFlag=false;
				} 
					
				/*	
					
					try {
						timeline = yamba.getTwitter().getFriendsTimeline();
					} catch (TwitterException e) {
						// TODO: handle exception
						Log.e(TAG, "Failed to connect to twitter service", e);
					}
					
					db=dbHelper.getWritableDatabase();
					
					ContentValues values = new ContentValues();
					
					for (Twitter.Status status : timeline) {
						values.clear();
						values.put(DbHelper1.C_ID, status.id);
						values.put(DbHelper1.C_CREATED_AT, status.createdAt.getTime());
						values.put(DbHelper1.C_TEXT, status.text);
						values.put(DbHelper1.C_USER, status.user.name);
						try {
							db.insertOrThrow(DbHelper1.TABLE, null, values);
							Log.d(TAG, String.format("%s : %s", status.user.name,
									status.text));
						} catch (SQLException e) {
							// TODO: handle exception
						}

					}
					
					db.close();
					
					// Some work goes here...
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY); //

				} catch (InterruptedException e) { //
					updaterService.runFlag = false;
				}*/
			}
		}
	} // Updater
}
