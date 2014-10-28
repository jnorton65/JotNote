package com.nortonprojects.jotnote.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nortonprojects.jotnote.bl.JotNotesManager;
import com.nortonprojects.quicknotes.R;

public class SettingsFragment extends Fragment implements OnClickListener
{
	private JotNotesManager mManager;
	private View mCurrentView;

	public static SettingsFragment newInstance()
	{
		return new SettingsFragment();
	}

	@Override
	public void onClick(final View v)
	{
		switch (v.getId())
		{
			case R.id.btn_theme_dark:
				mManager.changeTheme(android.R.style.Theme_DeviceDefault);
				break;
			case R.id.btn_theme_light:
				mManager.changeTheme(android.R.style.Theme_DeviceDefault_Light);
				break;
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mManager = new JotNotesManager((MainActivity) getActivity());
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		// inflater.inflate(R.menu.note_fragment_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		mCurrentView = inflater.inflate(R.layout.fragment_settings, container, false);

		setHasOptionsMenu(true);

		mCurrentView.findViewById(R.id.btn_theme_dark).setOnClickListener(this);
		mCurrentView.findViewById(R.id.btn_theme_light).setOnClickListener(this);

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

		return handled;
	}
}
