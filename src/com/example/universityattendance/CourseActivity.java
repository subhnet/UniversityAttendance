package com.example.universityattendance;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CourseActivity extends ListActivity {

	DBAdapter db;
	Cursor c;
	String t_id;
	String[] crs;
	String TAG = "Shubh";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courselist);
		Bundle extras = getIntent().getExtras();
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				t_id = (String) null;

			} else {
				t_id = extras.getString("key");
			}
		} else {
			t_id = (String) savedInstanceState.getSerializable("key");
		}

		db = new DBAdapter(this);
		try {
			db.open();
			c = db.getCourseByTeacherID(t_id);
			
			crs = new String[c.getCount()];
			for (int i = 0; i < c.getCount(); ++i) {
				crs[i] = c.getString(1);
				c.moveToNext();
			}
			c.close();
			db.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Could not retrieve course Details", Toast.LENGTH_LONG)
					.show();
		}

		try {
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, new ArrayList()));

			new AddStringTask().execute();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG)
					.show();
		}

	}

	class AddStringTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			for (int i = 0; i < crs.length; i++) {
				publishProgress(crs[i]);
			}
			return (null);
		}

		@Override
		protected void onProgressUpdate(String... item) {
			((ArrayAdapter) getListAdapter()).add(item[0]);
		}

		@Override
		protected void onPostExecute(Void unused) {
			setSelection(3);

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String selectedValue = (String) getListAdapter().getItem(position);
		Toast.makeText(this, "You selected: " + selectedValue, Toast.LENGTH_LONG).show();
		
			Intent ourIntent = new Intent(CourseActivity.this, AttendanceActivity.class);
			Log.d(TAG, "putting the extra items teacher_id a         " + t_id +" and course name : " +selectedValue);
			ourIntent.putExtra("keyTID", t_id);
			ourIntent.putExtra("keyCOURSE", selectedValue);
			
			startActivity(ourIntent);
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(CourseActivity.this, MemberActivity.class);
		startActivity(i);
		finish();
	}
	

}
