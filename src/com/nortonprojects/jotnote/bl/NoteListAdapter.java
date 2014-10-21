package com.nortonprojects.jotnote.bl;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nortonprojects.jotnote.bo.NoteInfo;
import com.nortonprojects.quicknotes.R;

public class NoteListAdapter extends BaseAdapter
{
	private ArrayList<NoteInfo> mNoteList;
	private Context mContext;

	public NoteListAdapter(final Context context, final ArrayList<NoteInfo> notes)
	{
		mContext = context;
		mNoteList = notes;
	}

	public void addItem(final NoteInfo toAdd)
	{
		boolean isInserted = false;
		for (int i = 0; i < mNoteList.size(); ++i)
		{
			if (mNoteList.get(i).getId() > toAdd.getId())
			{
				isInserted = true;
				mNoteList.add(i, toAdd);
				break;
			}
		}

		if (!isInserted)
		{
			mNoteList.add(toAdd);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		int count = 0;

		if (mNoteList != null)
		{
			count = mNoteList.size();
		}
		return count;
	}

	@Override
	public Object getItem(final int position)
	{
		Object item = null;

		if (mNoteList != null)
		{
			item = mNoteList.get(position);
		}
		return item;
	}

	@Override
	public long getItemId(final int position)
	{
		long id = 0;

		if (mNoteList != null)
		{
			id = mNoteList.get(position).getId();
		}

		return id;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.note_list_item, parent, false);
		}

		final TextView title = (TextView) convertView.findViewById(R.id.tv_note_title);

		title.setText(mNoteList.get(position).getTitle());

		return convertView;
	}

	public boolean remove(final Object object)
	{
		return mNoteList.remove(object);
	}
}
