package com.seminarioAndroid.pyamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends BaseActivity implements OnClickListener, TextWatcher, LocationListener {

	private static final String TAG ="StatusActivity";
	private static final long LOCATION_MIN_TIME = 3600000; // One hour
	private static final float LOCATION_MIN_DISTANCE = 1000; // One kilometer
	EditText editText;
	Button updateButton;
	Twitter twitter;
	TextView textCount;
	LocationManager locationManager; 
	Location location;
	String provider;
	
	//SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        
        editText =(EditText)findViewById(R.id.editText);
        updateButton=(Button)findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(this);
        
        //caracteres restantes
        textCount=(TextView)findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        editText.addTextChangedListener(this);
        
        Log.d(TAG, "Create Twitter object");
       // twitter = new Twitter("student","password");
        //twitter.setAPIRootUrl("http://yamba.marakana.com/api");
       // prefs=PreferenceManager.getDefaultSharedPreferences(this);
        //prefs.registerOnSharedPreferenceChangeListener(this);
        Log.d(TAG, "Set twitter object API root URL");
    }
    @Override
    protected void onResume() {
      super.onResume();

      // Setup location information
      provider = yamba.getProvider(); // <3>
      if (!YambaApplication1.LOCATION_PROVIDER_NONE.equals(provider)) { // <4>
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); // <5>
      }
      if (locationManager != null) {
        location = locationManager.getLastKnownLocation(provider); // <6>
        locationManager.requestLocationUpdates(provider, LOCATION_MIN_TIME,
            LOCATION_MIN_DISTANCE, this); // <7>
      }

    }
    
    @Override
    protected void onPause() {
      super.onPause();

      if (locationManager != null) {
        locationManager.removeUpdates(this);  // <8>
      }
    }
    
    //asincronamente hilos

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String status = editText.getText().toString();
		new PostToTwitter().execute(status);
		Log.d(TAG, "onClicked");
	}
    
    class PostToTwitter extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... statuses) {
			// TODO Auto-generated method stub
			/*try{
				//Twitter.Status status =twitter.updateStatus(statuses[0]);
				YambaApplication1 yamba =	((YambaApplication1) getApplication());	
				Twitter.Status status = yamba.getTwitter().updateStatus(statuses[0]);
				return status.text;
			}catch(TwitterException e){
				Log.e(TAG,e.toString());
				e.printStackTrace();
				return "Failed to post";
			}*/
			//del libro
		      try {
		          // Check if we have the location
		          if (location != null) { // <9>
		            double latlong[] = {location.getLatitude(), location.getLongitude()};
		            yamba.getTwitter().setMyLocation(latlong);
		          }
		          Twitter.Status status = yamba.getTwitter().updateStatus(statuses[0]);
		          return status.text;
		        } catch (RuntimeException e) {
		          Log.e(TAG, "Failed to connect to twitter service", e);
		          return "Failed to post";
		        }
		}
		@Override
		protected void onProgressUpdate(Integer...values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater =getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
        return true;
    }*/

	@Override
	public void afterTextChanged(Editable statusText) {
		// TODO Auto-generated method stub
		int count =140 - statusText.length();
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN);
		if (count <10 )
			textCount.setTextColor(Color.YELLOW);
		if(count < 0 )
			textCount.setTextColor(Color.RED);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	  // LocationListener methods
	  public void onLocationChanged(Location location) { // <10>
	    this.location = location;
	  }

	  public void onProviderDisabled(String provider) { // <11>
	    if (this.provider.equals(provider))
	      locationManager.removeUpdates(this);
	  }

	  public void onProviderEnabled(String provider) { // <12>
	    if (this.provider.equals(provider))
	      locationManager.requestLocationUpdates(this.provider, LOCATION_MIN_TIME,
	          LOCATION_MIN_DISTANCE, this);
	  }

	  public void onStatusChanged(String provider, int status, Bundle extras) { // <13>
	  }
	/*	@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemToggleService:
		startService(new Intent(this, UpdaterService.class)); //
		break;
		case R.id.itemToggleService:
		stopService(new Intent(this, UpdaterService.class)); //
		break;
		case R.id.itemPrefs:
		startActivity(new Intent(this, PrefsActivity.class));
		break;
		}
		return true;
		}*/

	/*@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		// invalidate Twitter object 
		twitter =null;
	}*/
	

    
}
