package com.nortonprojects.jotnote.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.nortonprojects.jotnote.bl.JotNotesManager;
import com.nortonprojects.quicknotes.R;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	public static final String NOTE_FRAGMENT_TAG = "NOTE_FRAGMENT";

	public static final String SETTINGS_FRAGMENT_TAG = "SETTINGS_FRAGMENT";

	public static final String NOTE_BROWSER_FRAGMENT_TAG = "NOTE_BROWSER_FRAGMENT";

	public static final String NOTE_FRAGMENT_TITLE = "Compose Note";

	public static final String NOTE_BROWSER_FRAGMENT_TITLE = "Load Note";

	public static final String SETTINGS_FRAGMENT_TITLE = "Settings";

	public static final String NOTE_FRAGMENT_ID = "NOTE_ID";

	private String mCurrentFragmentTag;

	private String noteText = "";
	private String titleText = "";
	private long noteId = 0L;

	public static final int NOTE_FRAGMENT_POSITION = 0;
	public static final int NOTE_BROWSER_FRAGMENT_POSITION = 1;
	public static final int SETTINGS_FRAGMENT_POSITION = 2;

	private static final String CURRENT_FRAGMENT_TAG = "CURRENT_FRAGMENT_TAG";

	public void changeFragment(final Fragment toChange, final String tag, final String title, final int position)
	{
		final FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, toChange, tag);
		transaction.commit();
		mTitle = title;
		mCurrentFragmentTag = tag;

		if (mNavigationDrawerFragment != null)
		{
			mNavigationDrawerFragment.setSelectedItem(position);
		}

	}

	@Override
	public void onConfigurationChanged(final Configuration config)
	{

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		if (!mNavigationDrawerFragment.isDrawerOpen())
		{
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onNavigationDrawerItemSelected(final int position)
	{
		switch (position)
		{
			case 0:
				if (mCurrentFragmentTag != null && !mCurrentFragmentTag.equals(NOTE_FRAGMENT_TAG))
				{
					changeFragment(NoteFragment.newInstance(), NOTE_FRAGMENT_TAG, NOTE_FRAGMENT_TITLE, NOTE_FRAGMENT_POSITION);
				}
				break;
			case 1:
				if (mCurrentFragmentTag == null || !mCurrentFragmentTag.equals(NOTE_BROWSER_FRAGMENT_TAG))
				{
					changeFragment(NoteBrowserFragment.newInstance(), NOTE_BROWSER_FRAGMENT_TAG, NOTE_BROWSER_FRAGMENT_TITLE, NOTE_BROWSER_FRAGMENT_POSITION);
				}
				break;
			case 2:
				if (mCurrentFragmentTag == null || !mCurrentFragmentTag.equals(SETTINGS_FRAGMENT_TAG))
				{
					changeFragment(SettingsFragment.newInstance(), SETTINGS_FRAGMENT_TAG, SETTINGS_FRAGMENT_TITLE, SETTINGS_FRAGMENT_POSITION);
				}
				break;
		}

		getFragmentManager();

		restoreActionBar();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(final Bundle savedInstanceState)
	{
		savedInstanceState.putString(CURRENT_FRAGMENT_TAG, mCurrentFragmentTag);
		savedInstanceState.putString(NOTE_FRAGMENT_TITLE, titleText);
		savedInstanceState.putString(NOTE_FRAGMENT_TAG, noteText);
		savedInstanceState.putLong(NOTE_FRAGMENT_ID, noteId);
	}

	public void onSectionAttached(final int number)
	{
	}

	public void restoreActionBar()
	{
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	public void setTextFields(final long id, final String title, final String note)
	{
		noteId = id;
		noteText = note;
		titleText = title;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		final JotNotesManager manager = new JotNotesManager(this);
		manager.setTheme();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null)
		{
			mCurrentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG);

			titleText = savedInstanceState.getString(NOTE_FRAGMENT_TITLE, titleText);
			noteText = savedInstanceState.getString(NOTE_FRAGMENT_TAG, noteText);
			noteId = savedInstanceState.getLong(NOTE_FRAGMENT_ID, noteId);
		}

		final FragmentManager fm = getFragmentManager();

		final Fragment retainedFragment = fm.findFragmentById(R.id.container);

		if (retainedFragment == null)
		{
			final Fragment noteFragment = NoteFragment.newInstance(noteId, titleText, noteText);

			final FragmentTransaction transaction = fm.beginTransaction();
			transaction.add(R.id.container, noteFragment, NOTE_FRAGMENT_TAG);
			transaction.commit();

			mCurrentFragmentTag = NOTE_FRAGMENT_TAG;
		}

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

}
