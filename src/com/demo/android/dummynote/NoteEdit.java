package com.demo.android.dummynote;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends Activity {

	private NotesDbAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		
		setContentView(R.layout.note_edit);
		findViews();
		showViews(savedInstanceState);
	}
	
	private Long mRowid;
	private void showViews(Bundle savedInstanceState) {
		mRowid = this.getIntent().getExtras().getLong(NotesDbAdapter.KEY_ROWID);
		//mRowid = savedInstanceState != null ? savedInstanceState.getLong(NotesDbAdapter.KEY_ROWID) : null;
		populateFields();
		
		button_confirm.setOnClickListener(clickButtonConfirm);
	}
	private OnClickListener clickButtonConfirm = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mDbHelper.update(mRowid, field_note.getText().toString());
			setResult(RESULT_OK);
			finish();
		}
	};

	private void populateFields() {
		if(mRowid != null) {
			Cursor note = mDbHelper.get(mRowid);
			startManagingCursor(note);
			
			field_note.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_NOTE)));
		}
	}

	private EditText field_note;
	private Button button_confirm;
	
	private void findViews() {
		field_note = (EditText)findViewById(R.id.note);
		button_confirm = (Button)findViewById(R.id.comfirm);
	}


}
