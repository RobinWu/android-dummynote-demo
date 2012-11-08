package com.demo.android.dummynote;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class DummyNote extends ListActivity {

	public static final String TAG = "DummyNote";
	private int mNoteNumber = 1;
	protected static final int MENU_INSERT = Menu.FIRST;
	protected static final int MENU_DELETE = Menu.FIRST + 1;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_INSERT, 0, R.string.label_menu_new);
		menu.add(0, MENU_DELETE, 0, R.string.label_menu_delete);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_INSERT:
			String noteName = "Note " + mNoteNumber++;
			mDbHelper.create(noteName);
			fillData();
			return true;
		case MENU_DELETE:
			Log.v(TAG, "delete record");
			mDbHelper.delete(getListView().getSelectedItemId());
			fillData();
			return true;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Log.v(TAG, "onCreate");
        setContentView(R.layout.main);
		Log.v(TAG, "set Empty list");
        ListView list = (ListView)getListView();
        list.setEmptyView(findViewById(R.id.empty));
        registerForContextMenu(list);
        setAdapter();
    }

	//private String[] note_array = {};
    private String[] note_array = {"gasolin", "crote", "louk", "magicion"};
    private NotesDbAdapter mDbHelper;
    private Cursor mNotesCursor;
    private int iSelectedId;
	
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
		
		String[] from = new String[]{NotesDbAdapter.KEY_NOTE};
		int[] to = new int[]{android.R.id.text1};

		Log.v(TAG, "setListAdapter by notes db");
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, 
				android.R.layout.simple_list_item_1, mNotesCursor, from, to);
		setListAdapter(adapter);
	}

	private static final int ACTIVITY_EDIT = 0x1001;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, NoteEdit.class);
		intent.putExtra(NotesDbAdapter.KEY_ROWID, id);
		startActivityForResult(intent, ACTIVITY_EDIT);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		AdapterContextMenuInfo info;
		info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch (item.getItemId()) {
		case MENU_DELETE:
			Log.v(TAG, "MENU item " + info.id);
			
			mDbHelper.delete(info.id);
			fillData();
			break;

		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_DELETE, 0, R.string.label_menu_delete);
		menu.setHeaderTitle(R.string.contextmenu_title	);
	}
	
	
}