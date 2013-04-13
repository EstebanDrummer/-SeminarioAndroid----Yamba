package com.seminarioAndroid.pyamba;

import winterwell.jtwitter.Twitter;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class StatusActivity extends Activity implements OnClickListener{

	private static final String TAG ="StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        
        editText =(EditText)findViewById(R.id.editText);
        updateButton=(Button)findViewById(R.id.buttonUpdate);
        
        updateButton.setOnClickListener(this);
        
        Log.d(TAG, "Create Twitter object");
        twitter = new Twitter("student","password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");
        Log.d(TAG, "Set twitter object API root URL");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_status, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		twitter.setStatus(editText.getText().toString());
		Log.d(TAG, "onClicked");
	}

    
}
