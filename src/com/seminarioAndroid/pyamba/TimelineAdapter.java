package com.seminarioAndroid.pyamba;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TimelineAdapter extends SimpleCursorAdapter { //
	static final String[] FROM = { DbHelper1.C_CREATED_AT, DbHelper1.C_USER,
			DbHelper1.C_TEXT }; //
	static final int[] to = { R.id.textCreatedAt, R.id.textUser, R.id.textText }; //
	// Constructor

	public TimelineAdapter(Context context, Cursor c) { //
		super(context, R.layout.row, c, FROM, to);
	}

	// This is where the actual binding of a cursor to view happens
	@Override
	public void bindView(View row, Context context, Cursor cursor) { //
		super.bindView(row, context, cursor);
		// Manually bind created at timestamp to its view
		long timestamp = cursor.getLong(cursor.getColumnIndex(DbHelper1.C_CREATED_AT)); //
		TextView textCreatedAt = (TextView) row.findViewById(R.id.textCreatedAt); //
		textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timestamp)); //
	}
}