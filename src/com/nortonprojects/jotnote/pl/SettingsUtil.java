package com.nortonprojects.jotnote.pl;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsUtil
{
	private static String THEME = "THEME";
	private static String PREFS = "PREFS";

	public static int getThemeId(final Context context)
	{
		int id = android.R.style.Theme_DeviceDefault;

		final SharedPreferences settings = context.getSharedPreferences(PREFS, 0);

		id = settings.getInt(THEME, id);

		return id;
	}

	public static void setThemeId(final Context context, final int themeId)
	{
		final SharedPreferences settings = context.getSharedPreferences(PREFS, 0);

		settings.edit().putInt(THEME, themeId).commit();
	}
}
