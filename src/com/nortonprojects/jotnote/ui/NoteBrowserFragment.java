package com.nortonprojects.jotnote.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.nortonprojects.jotnote.bl.JotNotesManager;
import com.nortonprojects.jotnote.bl.NoteListAdapter;
import com.nortonprojects.jotnote.bo.NoteInfo;
import com.nortonprojects.jotnote.ui.SwipeDismissListViewTouchListener.DismissCallbacks;
import com.nortonprojects.quicknotes.R;

public class NoteBrowserFragment extends Fragment implements OnItemClickListener, OnClickListener
{
	private View mCurrentView;
	private JotNotesManager mManager;
	private NoteListAdapter mAdapter;

	final Handler t = new Handler();

	private final Runnable toastRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			final Button b = (Button) mCurrentView.findViewById(R.id.toast_button);
			// optionally add some animations for
			// fading out
			b.setVisibility(View.GONE);
			mManager.executeDelete();
		}
	};

	public static NoteBrowserFragment newInstance()
	{
		return new NoteBrowserFragment();
	}

	@Override
	public void onClick(final View v)
	{
		switch (v.getId())
		{
			case R.id.toast_button:

				t.removeCallbacks(toastRunnable);
				v.setVisibility(View.GONE);

				mAdapter.addItem(mManager.getPendingDelete());
				mManager.removePendingDelete();
				break;
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mManager = new JotNotesManager((MainActivity) getActivity());
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		mCurrentView = inflater.inflate(R.layout.fragment_note_browser, container, false);

		final ListView noteListView = (ListView) mCurrentView.findViewById(R.id.lv_note_list);
		mAdapter = new NoteListAdapter(getActivity(), mManager.getNotes());

		noteListView.setAdapter(mAdapter);
		noteListView.setOnItemClickListener(this);

		final NoteBrowserFragment fragment = this;
		final SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(noteListView, new DismissCallbacks()
		{
			@Override
			public boolean canDismiss(final int position)
			{
				return true;
			}

			@Override
			public void onDismiss(final ListView listView, final int[] reverseSortedPositions)
			{
				for (final int position : reverseSortedPositions)
				{
					final NoteInfo info = (NoteInfo) mAdapter.getItem(position);
					if (mAdapter.remove(info))
					{
						mManager.addPendingDelete(info);

						final Button b = (Button) mCurrentView.findViewById(R.id.toast_button);

						b.setOnClickListener(fragment);
						// optionally add some animations for fading in
						b.setVisibility(View.VISIBLE);

						t.postDelayed(toastRunnable, 5000);
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		});
		noteListView.setOnTouchListener(touchListener);
		noteListView.setOnScrollListener(touchListener.makeScrollListener());

		return mCurrentView;
	}

	@Override
	public void onDestroy()
	{
		mManager.executeDelete();
		super.onDestroy();
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
	{
		NoteInfo info = null;

		info = (NoteInfo) mAdapter.getItem(position);

		if (info != null)
		{
			mManager.loadNote(info);
		}

	}
}
