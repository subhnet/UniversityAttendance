package com.example.universityattendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class AdminPage extends Activity{
	EditText txtCoursename,txtTeacherID,txtSem;
	DBAdapter dbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminpage);
		
		txtCoursename = (EditText) findViewById(R.id.editCoursename);
		txtTeacherID = (EditText) findViewById(R.id.editTid);
		txtSem = (EditText) findViewById(R.id.editSem);
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		
	}
	public void addCrs(View v){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtCoursename.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtTeacherID.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtSem.getWindowToken(), 0);
		try {

			String coursename = txtCoursename.getText().toString();
			String teacherID = txtTeacherID.getText().toString();
			String semester = txtSem.getText().toString();
			
			long i = dbAdapter.addCourse(coursename, Integer.parseInt(teacherID), Integer.parseInt(semester));
			if (i != -1)
				Toast.makeText(AdminPage.this,
						"You have successfully added Course", Toast.LENGTH_LONG)
						.show();
			Intent i1 = new Intent(AdminPage.this, MemberActivity.class);
			startActivity(i1);
			finish();
			dbAdapter.close();

		} catch (SQLException e) {
			Toast.makeText(AdminPage.this, "Some problem occurred",
					Toast.LENGTH_LONG).show();

		}
	}
	public void addStud(View v){
		Intent i_A = new Intent(AdminPage.this,AdminAddStudent.class);
		startActivity(i_A);
		finish();
	}

	
	
}
