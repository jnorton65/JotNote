package com.nortonprojects.jotnote.bl;

import java.util.ArrayList;

import android.app.Fragment;

import com.nortonprojects.jotnote.bo.NoteInfo;
import com.nortonprojects.jotnote.pl.DBAdapter;
import com.nortonprojects.jotnote.pl.SettingsUtil;
import com.nortonprojects.jotnote.ui.MainActivity;
import com.nortonprojects.jotnote.ui.NoteFragment;

public class JotNotesManager
{
	private MainActivity mActivity;
	private DBAdapter mAdapter;

	private NoteInfo mCurrentNote;

	private NoteInfo mPendingDelete;

	public JotNotesManager(final MainActivity context)
	{
		mActivity = context;
		mAdapter = new DBAdapter(context);
	}

	public void addPendingDelete(final NoteInfo info)
	{
		executeDelete();
		mPendingDelete = info;
	}

	public void changeTheme(final int id)
	{
		SettingsUtil.setThemeId(mActivity, id);

		mActivity.recreate();
	}

	public void executeDelete()
	{
		if (mPendingDelete != null)
		{
			mAdapter.deleteNote(mPendingDelete);
		}
	}

	public NoteInfo getCurrentNote()
	{
		return mCurrentNote;
	}

	public ArrayList<NoteInfo> getNotes()
	{
		return mAdapter.getNotes();
	}

	public NoteInfo getPendingDelete()
	{
		return mPendingDelete;
	}

	public int getTheme()
	{
		return SettingsUtil.getThemeId(mActivity);
	}

	public void loadNote(final NoteInfo toLoad)
	{
		setCurrentNote(toLoad);
		final Fragment toChange = NoteFragment.newInstance(toLoad.getId(), toLoad.getTitle(), toLoad.getNote());
		mActivity.changeFragment(toChange, MainActivity.NOTE_FRAGMENT_TAG, MainActivity.NOTE_FRAGMENT_TITLE, MainActivity.NOTE_FRAGMENT_POSITION);
	}

	public void removePendingDelete()
	{
		mPendingDelete = null;
	}

	public void saveNote()
	{
		if (mCurrentNote.getNote() != null && !mCurrentNote.getNote().trim().equals("") || mCurrentNote.getTitle() != null
				&& !mCurrentNote.getTitle().trim().equals(""))
		{
			if (mCurrentNote.getTitle() == null || mCurrentNote.getTitle().trim().equals(""))
			{
				mCurrentNote.setTitle("Default");
			}
			mCurrentNote.setId(mAdapter.createUpdateNote(mCurrentNote));
		}
	}

	public void setCurrentNote(final NoteInfo toSet)
	{
		mCurrentNote = toSet;
	}

	public void setTheme()
	{
		mActivity.setTheme(SettingsUtil.getThemeId(mActivity));
	}

	public void updateCurrentNote(final String title, final String note)
	{
		if (mCurrentNote == null)
		{
			mCurrentNote = new NoteInfo();
		}

		mCurrentNote.setTitle(title);
		mCurrentNote.setNote(note);
	}
}
