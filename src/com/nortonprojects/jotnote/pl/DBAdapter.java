package com.nortonprojects.jotnote.pl;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nortonprojects.jotnote.bo.NoteInfo;
import com.nortonprojects.jotnote.pl.DBHelper.DBID;
import com.nortonprojects.jotnote.pl.DBHelper.NoteTable;

public class DBAdapter
{
	private DBHelper mHelper;

	public DBAdapter(final Context context)
	{
		mHelper = DBHelper.getInstance(context);
	}

	public long createUpdateNote(final NoteInfo toSave)
	{
		long toReturn = 0L;

		if (toSave != null)
		{
			final ContentValues values = new ContentValues();

			values.put(NoteTable.note.name(), toSave.getNote());
			values.put(NoteTable.title.name(), toSave.getTitle());

			long rowsAffected = 0L;

			if (toSave.getId() != 0L)
			{
				values.put(DBID._id.name(), toSave.getId());

				rowsAffected = update(DBHelper.NOTE_TABLE, values, toSave.getId());
			}

			if (rowsAffected == 0)
			{
				toReturn = insert(DBHelper.NOTE_TABLE, values);
			}
			else
			{
				toReturn = toSave.getId();
			}
		}

		return toReturn;
	}

	public boolean deleteNote(final NoteInfo toDelete)
	{
		boolean toReturn = false;

		if (delete(DBHelper.NOTE_TABLE, toDelete.getId()) > 0)
		{
			toReturn = true;
		}

		return toReturn;
	}

	public ArrayList<NoteInfo> getNotes()
	{
		ArrayList<NoteInfo> toReturn = null;

		final SQLiteDatabase db = getDatabase();

		final Cursor cursor = db.query(DBHelper.NOTE_TABLE, null, null, null, null, null, null);

		if (cursor != null && cursor.moveToFirst())
		{
			toReturn = new ArrayList<NoteInfo>();

			final int idIndex = cursor.getColumnIndex(DBID._id.name());
			final int titleIndex = cursor.getColumnIndex(NoteTable.title.name());
			final int noteIndex = cursor.getColumnIndex(NoteTable.note.name());

			for (; !cursor.isAfterLast(); cursor.moveToNext())
			{
				final long id = cursor.getLong(idIndex);
				final String title = cursor.getString(titleIndex);
				final String note = cursor.getString(noteIndex);
				toReturn.add(new NoteInfo(id, title, note));
			}
		}

		return toReturn;
	}

	private long delete(final String table, final long id)
	{
		final SQLiteDatabase db = getDatabase();
		try
		{
			return db.delete(table, DBID._id.name() + " = ?", new String[] { Long.toString(id) });
		}
		finally
		{
			db.close();
		}
	}

	private SQLiteDatabase getDatabase()
	{
		return mHelper.getDatabase();
	}

	private long insert(final String table, final ContentValues values)
	{
		long result = -1;
		final SQLiteDatabase db = getDatabase();
		try
		{
			result = db.insert(table, null, values);
		}
		finally
		{
			db.close();
		}
		return result;
	}

	private long update(final String table, final ContentValues values, final long id)
	{
		final SQLiteDatabase db = getDatabase();
		try
		{
			return db.update(table, values, DBID._id.name() + " = ?", new String[] { Long.toString(id) });
		}
		finally
		{
			db.close();
		}
	}
}