package com.demo.android.dummynote;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// adb shell
// cd data/data/com.demo.android.dummynote/databases
// sqlite3 notes.db
// select * from notes
// .schema notes
public class NotesDbAdapter {
	private static final String DATABASE_NAME = "notes.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE = "notes";
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS notes(_id INTEGER PRIMARY KEY, note TEXT NOT NULL, created INTEGER);";
	private static final String ANDOIRD_METADATA_TABLE = "CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT DEFAULT 'en_US'); INSERT INTO android_metadata VALUES('en_US');";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(ANDOIRD_METADATA_TABLE);
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}
	
	private Context mCtx = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public NotesDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public NotesDbAdapter open () throws SQLException {
		dbHelper = new DatabaseHelper(mCtx);
		db = dbHelper.getWritableDatabase();
		return this;
		
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Cursor getAll() {
		return db.rawQuery("SELECT * FROM notes", null);
	}
}