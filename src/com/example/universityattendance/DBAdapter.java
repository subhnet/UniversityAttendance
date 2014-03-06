package com.example.universityattendance;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter extends Activity {
	private static final String DATABASE_TABLE1 = "teacher_data";
	private static final String DATABASE_TABLE2 = "student_data";
	private static final String DATABASE_TABLE3 = "course_data";
	public static final String KEY_TEACHER_ID = "t_id";
	public static final String KEY_TEACHERNAME = "t_name";
	public static final String KEY_TEACHERPASSWORD = "t_pass";
	public static final String KEY_COURSE_ID = "course_id";
	public static final String KEY_COURSE_NAME = "course_name";
	public static final String KEY_STUDENT_NAME = "stud_name";
	public static final String KEY_STUDENT_ROLL = "stud_roll";
	public static final String KEY_SEM = "sem";
	
	

	SQLiteDatabase mDb;
	Context mCtx;
	DBHelper mDbHelper;

	public DBAdapter(Context context) {
		this.mCtx = context;
	}

	public DBAdapter open() throws SQLException {
		mDbHelper = new DBHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long register(String user, String pw) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TEACHERNAME, user);
		initialValues.put(KEY_TEACHERPASSWORD, pw);

		return mDb.insert(DATABASE_TABLE1, null, initialValues);
	}

	public boolean Login(String username, String password) throws SQLException {
		Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE1
				+ " WHERE t_name=? AND t_pass=?", new String[] { username,
				password });
		if (mCursor != null) {
			if (mCursor.getCount() > 0) {
				mCursor.close();
				return true;
			}
		}
		return false;
	}

	public String getTID(String username, String password,
			MemberActivity memberActivity) {
		// TODO Auto-generated method stub
		try {
			String idddd = null;

			String[] columns = { "t_id" };

			// WHERE clause
			String selection = "t_name=? AND t_pass=?";

			// WHERE clause arguments
			String[] selectionArgs = { username, password };

			Cursor cursor = mDb.query(DATABASE_TABLE1, columns, selection,
					selectionArgs, null, null, null);
			if (cursor != null) {
				startManagingCursor(cursor);
				while (cursor.moveToNext()) {
					idddd = cursor.getString(0);
				}
				cursor.close();
				return idddd;
			}
			System.out.println("Cursor NuLL");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Cursor getCourseByTeacherID(String t_id) {
		int tt_id = Integer.parseInt(t_id);
		Log.i("SHUBH", "GETTING COURSE RECORD by " +t_id);
		Cursor mCursor = mDb.query(true,DATABASE_TABLE3, new String[] {KEY_COURSE_ID, KEY_COURSE_NAME }, KEY_TEACHER_ID + " = " + tt_id, null, null,null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	//add course for ADMIN
	public long addCourse(String coursename, int parseInt, int i) {
		// TODO Auto-generated method stub
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_COURSE_NAME, coursename);
		initialValues.put(KEY_TEACHER_ID, parseInt);
		initialValues.put("sem", i);

		return mDb.insert(DATABASE_TABLE3, null, initialValues);
		
	}

	public long addStudent(String studentname, String rollno, int parseInt) {
		// TODO Auto-generated method stub
		ContentValues initialValues = new ContentValues();
		initialValues.put("stud_name", studentname);
		initialValues.put("stud_roll", rollno);
		initialValues.put("sem", parseInt);

		return mDb.insert(DATABASE_TABLE2, null, initialValues);
	}

	public String getSEMByCourse(String coursename) {
		try {
			String semm = null;

			String[] columns = { "sem" };

			// WHERE clause
			String selection = "course_name=?";

			// WHERE clause arguments
			String[] selectionArgs = { coursename };

			Cursor cursor = mDb.query(DATABASE_TABLE3, columns, selection,
					selectionArgs, null, null, null);
			if (cursor != null) {
				startManagingCursor(cursor);
				while (cursor.moveToNext()) {
					semm = cursor.getString(0);
				}
				cursor.close();
				return semm;
			}
			System.out.println("Cursor NuLL");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Cursor getStudentsBySEM(Integer sem) {
		Log.i("SHUBH", "GETTING STUDENTS by " + sem);
		Cursor mCursor = mDb.query(true,DATABASE_TABLE2, new String[] {KEY_STUDENT_NAME, KEY_STUDENT_ROLL }, KEY_SEM + " = " + sem, null, null,null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

}