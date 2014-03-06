package com.example.universityattendance;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
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
	String t_id;
	String coursename;
	Button submitButton;
	Integer sem;
	ListView list;
	String my_sel_items;
	private SparseBooleanArray a;
	ArrayList<String> presentRoll = new ArrayList<String>();

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

			c = db.getStudentsBySEM(sem);
			crs = new String[c.getCount()];
			Log.i("SHUBH", "GETTING " + c.getCount() + " no. of students ");
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
					android.R.layout.simple_list_item_multiple_choice,
					new ArrayList()));

			new AddStringTask().execute();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG)
					.show();
		}

		/*
		 * list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		 * public void onItemClick(AdapterView<?> myAdapter, View myView, int
		 * myItemInt, long mylng) {
		 * 
		 * SparseBooleanArray sp = list.getCheckedItemPositions();
		 * 
		 * my_sel_items = new String("Selected Items"); for (int i = 0; i <
		 * sp.size(); i++) { if (sp.valueAt(i)) { my_sel_items = my_sel_items +
		 * "," + (String) list.getAdapter().getItem(i); } }
		 * Log.v("selected Roll", my_sel_items);
		 * 
		 * } });
		 */

		/*
		 * list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) {
		 * 
		 * if (!mSelectedItemsIds.get(position)) { list.setItemChecked(position,
		 * true); presentRoll.add(list.getItemAtPosition(position);
		 * 
		 * } else { list.setItemChecked(position, false);
		 * mSelectedItemsIds.delete(position); presentRoll.remove(position); }
		 * 
		 * 
		 * } });
		 */

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		ListAdapter lAdapter = getListAdapter();
		StringBuilder choicesString = new StringBuilder();
		a = new SparseBooleanArray();
		a.clear();
		a = ((ListView) lAdapter).getCheckedItemPositions();

		for (int i = 0; i < a.size(); i++) {
			// added if statement to check for true. The SparseBooleanArray
			// seems to maintain the keys for the checked items, but it sets
			// the value to false. Adding a boolean check returns the correct
			// result.
			if (a.valueAt(i) == true)
				choicesString.append(a.keyAt(i)).append(" ");

		}
		Toast.makeText(AttendanceActivity.this, choicesString,
				Toast.LENGTH_LONG).show();

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
