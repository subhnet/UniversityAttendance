package com.example.universityattendance;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MemberActivity extends Activity {
	DBAdapter dbAdapter;
	EditText txtUserName;
	EditText txtPassword;
	Button btnLogin;
	Button btnRegister;
	String t_id;
	String TAG = "Shubh";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacherlogin);

		txtUserName = (EditText) findViewById(R.id.editTname);
		txtPassword = (EditText) findViewById(R.id.editTPassword);

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
	}

	public void login(View v) {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtUserName.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
		String username = txtUserName.getText().toString();
		String password = txtPassword.getText().toString();
		if (username.length() > 0 && password.length() > 0) {
			try {

				if (dbAdapter.Login(username, password)) {
					Toast.makeText(MemberActivity.this,
							"Successfully Logged In", Toast.LENGTH_LONG).show();
					Intent i_login = new Intent(MemberActivity.this,
							CourseActivity.class);
					try {
						t_id = dbAdapter.getTID(username, password, MemberActivity.this);

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Log.d(TAG, "putting the extra          " + t_id);
					i_login.putExtra("key", t_id);
					startActivity(i_login);
					finish();
				} else {
					Toast.makeText(MemberActivity.this,
							"Invalid username or password", Toast.LENGTH_LONG)
							.show();
				}

			} catch (Exception e) {
				Toast.makeText(MemberActivity.this, "Some problem occurred",
						Toast.LENGTH_LONG).show();

			}
		} else {
			Toast.makeText(MemberActivity.this,
					"Username or Password is left empty", Toast.LENGTH_LONG).show();
		}
	}

	public void register(View v) {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtUserName.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
		try {

			String username = txtUserName.getText().toString();
			String password = txtPassword.getText().toString();
			long i = dbAdapter.register(username, password);
			if (i != -1)
				Toast.makeText(MemberActivity.this,
						"You have successfully registered", Toast.LENGTH_LONG)
						.show();
			dbAdapter.close();

		} catch (SQLException e) {
			Toast.makeText(MemberActivity.this, "Some problem occurred",
					Toast.LENGTH_LONG).show();

		}

	}
	public void admin(View v){
		Intent i_A = new Intent(MemberActivity.this,AdminPage.class);
		startActivity(i_A);
		finish();
	}

}
