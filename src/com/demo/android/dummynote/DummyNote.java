package com.demo.android.dummynote;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class DummyNote extends ListActivity {
	public static final String TAG = "DummyNote";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Log.v(TAG, "onCreate");
        setContentView(R.layout.main);
		Log.v(TAG, "set Empty list");
        getListView().setEmptyView(findViewById(R.id.empty));
        setAdapter();
    }

    //private String[] note_array = {};
    private String[] note_array = {"gasolin", "crote", "louk", "magicion"};
    private NotesDbAdapter mDbHelper;
    private Cursor mNotesCursor;
	
	private void setAdapter() {
		Log.v(TAG, "setAdapter");
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		
		fillData();

//		Log.v(TAG, "setListAdapter by String[]");
//		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, note_array);
//		setListAdapter(adapter);
		
	}

	private void fillData() {
		Log.v(TAG, "fillData");
		mNotesCursor = mDbHelper.getAll();
		startManagingCursor(mNotesCursor);
		
		String[] from = new String[]{"note"};
		int[] to = new int[]{android.R.id.text1};

		Log.v(TAG, "setListAdapter by notes db");
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, 
				android.R.layout.simple_list_item_1, mNotesCursor, from, to);
		setListAdapter(adapter);
	}
	
	
}