package com.lesports.stadium.sharedpreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint({ "NewApi", "CommitPrefEdits" })
public class PreferenceUtil {

	public static SharedPreferences getDefault(final Context context) {
		return context.getSharedPreferences("login_setting", Context.MODE_PRIVATE);
	}
	public static void save(final Editor editor) {
		if (android.os.Build.VERSION.SDK_INT >= 9) {
			editor.apply();
		} else {
			editor.commit();
		}
	}
	public static int getInt(final String key, final int defaultValue,
			Context context) {
		final SharedPreferences prefs = getDefault(context);
		return prefs.getInt(key, defaultValue);
	}

	public static void putInt(final String key, final int value, Context context) {
		final SharedPreferences prefs = getDefault(context);
		final Editor editor = prefs.edit();
		editor.putInt(key, value);
		save(editor);
	}

	public static long getLong(final String key, final long defaultValue,
			Context context) {
		final SharedPreferences prefs = getDefault(context);
		return prefs.getLong(key, defaultValue);
	}

	public static void putLong(final String key, final long value,
			Context context) {
		final SharedPreferences prefs = getDefault(context);
		final Editor editor = prefs.edit();
		editor.putLong(key, value);
		save(editor);
	}

	public static String getString(final String key, final String defaultValue,
			Context context) {
		final SharedPreferences prefs = getDefault(context);
		return prefs.getString(key, defaultValue);
	}

	public static void putString(final String key, final String value,
			Context context) {
		final SharedPreferences prefs = getDefault(context);
		final Editor editor = prefs.edit();
		editor.putString(key, value);
		save(editor);
	}

	public static boolean getBoolean(final String key,
			final boolean defaultValue, Context context) {
		final SharedPreferences prefs = getDefault(context);
		return prefs.getBoolean(key, defaultValue);
	}

	public static void putBoolean(final String key, final boolean value,
			Context context) {
		final SharedPreferences prefs = getDefault(context);
		final Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		save(editor);
	}

	public static float getFloat(final String key, final float defaultValue,
			Context context) {
		final SharedPreferences prefs = getDefault(context);
		return prefs.getFloat(key, defaultValue);
	}

	public static void putFloat(final String key, final float value,
			Context context) {
		final SharedPreferences prefs = getDefault(context);
		final Editor editor = prefs.edit();
		editor.putFloat(key, value);
		save(editor);
	}

}
