package com.seminarioAndroid.pyamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class StatusActivity extends Activity implements OnClickListener, TextWatcher{

	private static final String TAG ="StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	TextView textCount;
	
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
        twitter = new Twitter("student","password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");
        Log.d(TAG, "Set twitter object API root URL");
    }
    //asincronamente
    
    class PostToTwitter extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... statuses) {
			// TODO Auto-generated method stub
			try{
				Twitter.Status status =twitter.updateStatus(statuses[0]);
				return status.text;
			}catch(TwitterException e){
				Log.e(TAG,e.toString());
				e.printStackTrace();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_status, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
String status =editText.getText().toString();
new PostToTwitter().execute(status);
Log.d(TAG, "onClicked");
	}

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

    
}
