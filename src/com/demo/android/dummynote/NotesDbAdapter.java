package com.demo.android.dummynote;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// adb shell
// cd data/data/com.demo.android.dummynote/databases
// sqlite3 notes.db
// select * from notes
// .schema notes
public class NotesDbAdapter {
	private static final String DATABASE_NAME = "notes.db";
	private static final int DATABASE_VERSION = 7;
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
			db.execSQL("DROP TABLE IF EXISTS notes;");
			onCreate(db);
			buildSampleData(db);
		}
		
		private void buildSampleData(SQLiteDatabase db) {
			db.execSQL("INSERT INTO notes VALUES(1, 'gg', 20);");
			db.execSQL("INSERT INTO notes VALUES(2, 'ss', 30);");
			db.execSQL("INSERT INTO notes VALUES(3, 'dd', 40);");
		}

	}
	
	private Context mCtx = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NOTE = "note";
	public static final String KEY_CREATED = "created";
	String[] strCols = new String[] {KEY_ROWID, KEY_NOTE, KEY_CREATED};
	
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
		// return db.rawQuery("SELECT * FROM notes", null);
		return db.query(DATABASE_TABLE, strCols, null, null, null, null, null);
	}
	
	public long create(String note) {
		Date now = new Date();
		ContentValues args = new ContentValues();
		args.put(KEY_NOTE, note);
		args.put(KEY_CREATED, now.getTime());
		
		return db.insert(DATABASE_TABLE, null, args);
	}
	
	public boolean delete(long rowid) {
		Log.v(DummyNote.TAG, "delete rowid " + rowid);
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowid, null) > 0;
	}
	
	public Cursor get(long rowid) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, strCols, KEY_ROWID + "=" + rowid, null, null, null, null, null);
		if(mCursor != null) {
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	public boolean update(long rowid, String note) {
		ContentValues args = new ContentValues();
		args.put(KEY_NOTE, note);
		
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowid, null) > 0;
	}
}