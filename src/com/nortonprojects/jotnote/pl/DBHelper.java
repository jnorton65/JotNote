package com.nortonprojects.jotnote.pl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	private static DBHelper mInstance;

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "nortonprojects.quicknotes.db";

	public static final String NOTE_TABLE = "NoteTable";

	private static final String NOTE_TABLE_SQL = "CREATE TABLE " + NOTE_TABLE + " ( " + DBID._id.name() + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NoteTable.title.name() + " TEXT, " + NoteTable.note.name() + " TEXT);";

	private DBHelper(final Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DBHelper getInstance(final Context context)
	{
		if (mInstance == null)
		{
			mInstance = new DBHelper(context);
		}

		return mInstance;
	}

	public SQLiteDatabase getDatabase()
	{
		return super.getWritableDatabase();
	}

	@Override
	public void onCreate(final SQLiteDatabase db)
	{
		db.execSQL(NOTE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion)
	{
		db.execSQL("drop table " + NOTE_TABLE);

		onCreate(db);
	}

	public enum DBID
	{
		_id;
	}

	public enum NoteTable
	{
		title, note;
	}
}