package com.nortonprojects.jotnote.bo;

public class NoteInfo extends DBInfo
{
	private String mTitle;
	private String mNote;

	public NoteInfo()
	{

	}

	public NoteInfo(final long id, final String title, final String note)
	{
		setId(id);
		setTitle(title);
		setNote(note);
	}

	public NoteInfo(final String title, final String note)
	{
		this(0L, title, note);
	}

	public String getNote()
	{
		return mNote;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public void setNote(final String note)
	{
		mNote = note;
	}

	public void setTitle(final String title)
	{
		mTitle = title;
	}
}
