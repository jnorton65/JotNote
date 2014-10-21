package com.nortonprojects.jotnote.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nortonprojects.jotnote.bl.JotNotesManager;
import com.nortonprojects.jotnote.bo.NoteInfo;
import com.nortonprojects.quicknotes.R;

public class NoteFragment extends Fragment
{
	private JotNotesManager mManager;
	private View mCurrentView;

	private static final String ID = "ID";

	private static final String TITLE = "TITLE";
	private static final String NOTE = "NOTE";

	private Object lockObject = new Object();

	public static NoteFragment newInstance()
	{
		return new NoteFragment();
	}

	public static NoteFragment newInstance(final long id, final String title, final String note)
	{
		final Bundle bundle = new Bundle();
		bundle.putLong(ID, id);
		bundle.putString(TITLE, title);
		bundle.putString(NOTE, note);

		final NoteFragment toReturn = new NoteFragment();
		toReturn.setArguments(bundle);

		return toReturn;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setRetainInstance(true);

		mManager = new JotNotesManager((MainActivity) getActivity());

		if (savedInstanceState == null)
		{
			final Bundle args = getArguments();
			if (args != null)
			{
				final String title = args.getString(TITLE);
				final String note = args.getString(NOTE);
				final long id = args.getLong(ID);

				mManager.setCurrentNote(new NoteInfo(id, title, note));
			}
		}
		else
		{
			final String title = savedInstanceState.getString(TITLE);
			final String note = savedInstanceState.getString(NOTE);
			final long id = savedInstanceState.getLong(ID);

			mManager.setCurrentNote(new NoteInfo(id, title, note));
		}
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		inflater.inflate(R.menu.note_fragment_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		mCurrentView = inflater.inflate(R.layout.fragment_note, container, false);

		setHasOptionsMenu(true);

		final EditText titleView = (EditText) mCurrentView.findViewById(R.id.et_note_title);
		titleView.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(final Editable s)
			{
				autoSave();
			}

			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after)
			{
			}

			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count)
			{
			}

		});

		final EditText noteView = (EditText) mCurrentView.findViewById(R.id.et_note_text);
		noteView.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(final Editable s)
			{
				autoSave();
			}

			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after)
			{
			}

			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count)
			{
			}

		});

		final NoteInfo info = mManager.getCurrentNote();
		if (info != null)
		{
			titleView.setText(info.getTitle());
			noteView.setText(info.getNote());
		}

		return mCurrentView;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		final boolean handled = false;

		switch (item.getItemId())
		{
			case R.id.itm_new_note:
			{
				mManager.setCurrentNote(new NoteInfo());
				final EditText titleView = (EditText) mCurrentView.findViewById(R.id.et_note_title);
				final EditText noteView = (EditText) mCurrentView.findViewById(R.id.et_note_text);
				titleView.setText("");
				noteView.setText("");

				break;
			}
		}

		return handled;
	}

	@Override
	public void onPause()
	{
		super.onPause();

		final EditText titleView = (EditText) mCurrentView.findViewById(R.id.et_note_title);
		final EditText noteView = (EditText) mCurrentView.findViewById(R.id.et_note_text);

		final String title = titleView.getText().toString();
		final String note = noteView.getText().toString();
		mManager.updateCurrentNote(title, note);

		((MainActivity) getActivity()).setTextFields(mManager.getCurrentNote().getId(), title, note);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		savedInstanceState = new Bundle();
		final NoteInfo info = mManager.getCurrentNote();

		if (info != null)
		{
			savedInstanceState.putLong(ID, info.getId());
			savedInstanceState.putString(TITLE, info.getTitle());
			savedInstanceState.putString(NOTE, info.getNote());
		}

		super.onSaveInstanceState(savedInstanceState);
	}

	private void autoSave()
	{
		final EditText titleView = (EditText) mCurrentView.findViewById(R.id.et_note_title);
		final EditText noteView = (EditText) mCurrentView.findViewById(R.id.et_note_text);

		final String title = titleView.getText().toString();
		final String note = noteView.getText().toString();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				synchronized (lockObject)
				{
					mManager.updateCurrentNote(title, note);
					mManager.saveNote();
				}
			}
		}).start();
	}
}
