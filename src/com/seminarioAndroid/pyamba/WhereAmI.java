package com.seminarioAndroid.pyamba;

import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class WhereAmI extends Activity implements LocationListener {
	LocationManager locationManager; //
	Geocoder geocoder; //
	TextView textOut; //

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textOut = (TextView) findViewById(R.id.textOut);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //
		geocoder = new Geocoder(this); //
		// Initialize with the last known location
		Location lastLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER); //
		if (lastLocation != null)
			onLocationChanged(lastLocation);
	}

	@Override
	protected void onResume() { //
		super.onRestart();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 10, this);
	}

	@Override
	protected void onPause() { //
		super.onPause();
		locationManager.removeUpdates(this);
	}

	// Called when location has changed
	public void onLocationChanged(Location location) { //
		String text = String.format(
				"Lat:\t %f\nLong:\t %f\nAlt:\t %f\nBearing:\t %f",
				location.getLatitude(), location.getLongitude(),
				location.getAltitude(), location.getBearing()); //
		textOut.setText(text);
		// Perform geocoding for this location
		try {
			List<Address> addresses = geocoder.getFromLocation(
					location.getLatitude(), location.getLongitude(), 10); //
			for (Address address : addresses) {
				textOut.append("\n" + address.getAddressLine(0)); //
			}
		} catch (IOException e) {
			Log.e("WhereAmI", "Couldn't get Geocoder data", e);
		}
	}

	// Methods required by LocationListener
	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
