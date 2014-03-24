package com.example.universityattendance;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AttendanceActivity extends ListActivity implements OnClickListener {

	DBAdapter db;
	Cursor c;
	String[] crs;
	String TAG = "Shubh";
	String t_id,class_id;
	String coursename;
	Button submitButton;
	Integer sem;
	ListView list;
	String my_sel_items;
	private SparseBooleanArray a;
	ArrayList<String> Selected_Roll_array = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendancelist);
		submitButton = (Button) findViewById(R.id.submit);
		submitButton.setOnClickListener(this);
		Bundle extras = getIntent().getExtras();
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				t_id = (String) null;
				coursename = (String) null;

			} else {
				t_id = extras.getString("keyTID");
				coursename = extras.getString("keyCOURSE");
			}
		} else {
			t_id = (String) savedInstanceState.getSerializable("keyTID");
			coursename = (String) savedInstanceState
					.getSerializable("keyCOURSE");

		}

		db = new DBAdapter(this);
		try {
			db.open();
			sem = Integer.parseInt(db.getSEMByCourse(coursename));

			c = db.getStudentsBySEM(sem);// Getting the list of roll no in a
											// particular subject
			crs = new String[c.getCount()];
			Log.i("SHUBH", "GETTING " + c.getCount() + " no. of students ");
			for (int i = 0; i < c.getCount(); ++i) {
				crs[i] = c.getString(1);
				c.moveToNext();
			}
			c.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Could not retrieve course Details", Toast.LENGTH_LONG)
					.show();
		}

		try {

			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_checked, new ArrayList()));
			ListView listView = getListView();
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			new AddStringTask().execute();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String currtime = DateFormat.getDateTimeInstance().format(new Date());
		String checked = "";
		a = new SparseBooleanArray();
		a.clear();
		a = getListView().getCheckedItemPositions();
		int cntChoice = getListAdapter().getCount();

		for (int i = 0; i < cntChoice; i++) {
			if (a.get(i) == true) {
				Selected_Roll_array.add(getListView().getItemAtPosition(i).toString());
				checked += getListView().getItemAtPosition(i).toString() + "\n";
			}

		}
		try {
			//add a Class in the class table
			db.addClassDatabase(coursename,t_id,currtime);

			Log.i("SHUBH", "the current time is " + currtime);
			class_id = db.getClassID(currtime);
			Log.i("SHUBH", "the fetched class id is " + class_id);
			
			//add to attendance table
			db.addAttendanceData(Selected_Roll_array,sem,class_id);
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_LONG)
			.show();
			e.printStackTrace();
		}
		
		Toast.makeText(getApplicationContext(), "Attendance taken", Toast.LENGTH_LONG)
				.show();
		Log.i("SHUBH", "U selected " + checked);
		Intent i1 = new Intent(AttendanceActivity.this, MemberActivity.class);
		startActivity(i1);

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

}
