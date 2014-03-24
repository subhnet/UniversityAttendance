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

public class AdminAddStudent extends Activity {
	EditText txtStudentname,txtRoll,txtSem;
	DBAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminaddstud);
		
		txtStudentname = (EditText) findViewById(R.id.editStudentname);
		txtRoll = (EditText) findViewById(R.id.editRoll);
		txtSem = (EditText) findViewById(R.id.editSem1);
		
		dbAdapter = new DBAdapter(this);
		
	}
	public void addStudent(View v){
		dbAdapter.open();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtStudentname.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtRoll.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtSem.getWindowToken(), 0);
		try {

			String studentname = txtStudentname.getText().toString();
			String rollno = txtRoll.getText().toString();
			String semester = txtSem.getText().toString();
			
			long i = dbAdapter.addStudent(studentname, rollno, Integer.parseInt(semester));
			if (i != -1)
				Toast.makeText(AdminAddStudent.this,
						"You have successfully added a Student", Toast.LENGTH_LONG)
						.show();
			dbAdapter.close();

		} catch (SQLException e) {
			Toast.makeText(AdminAddStudent.this, "Some problem occurred",
					Toast.LENGTH_LONG).show();

		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(AdminAddStudent.this, AdminPage.class);
		startActivity(i);
		finish();
	}
}
