package com.zhenquan.telephonesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDao {

	private AppUnLockOpenHelper openHelper;

	public AppLockDao(Context context) {
		super();
		openHelper = new AppUnLockOpenHelper(context);
	}

	public boolean insert(String packageName) {
		SQLiteDatabase database = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(AppLockConstants.COLUMN_PACKAGE_NAME, packageName);
		long insert = database.insert(AppLockConstants.TABLE_NAME, null, values);
		database.close();
		/*
		 * if (insert!=-1) { return true; }else { return false; }
		 */

		return insert != -1;
	}

	public boolean delete(String packageName) {
		SQLiteDatabase database = openHelper.getWritableDatabase();
		String whereClause = AppLockConstants.COLUMN_PACKAGE_NAME + "=?";
		String[] whereArgs = new String[] { packageName };
		int delete = database.delete(AppLockConstants.TABLE_NAME, whereClause,
				whereArgs);
		return delete != 0;
	}
	// 查询对应的应用是否加锁了
		public boolean query2(String packageName) {
			// select packageName from applock where packageName =
			// 'com.itheima.xxx';
			SQLiteDatabase database = openHelper.getWritableDatabase();
			String selection = AppLockConstants.COLUMN_PACKAGE_NAME + " = ?";
			String[] selectionArgs = new String[] { packageName };
			Cursor cursor = database.query(AppLockConstants.TABLE_NAME,
					new String[] { AppLockConstants.COLUMN_PACKAGE_NAME },
					selection, selectionArgs, null, null, null);
			boolean result = false;
			if (cursor != null && cursor.moveToNext()) {
				result = true;
			}
			cursor.close();
			database.close();
			return result;
		}
	public boolean query(String packageName) {
		SQLiteDatabase database = openHelper.getWritableDatabase();
		String selection = AppLockConstants.COLUMN_PACKAGE_NAME + " = ?";
		String[] selectionArgs = new String[] { packageName };
		Cursor cursor = database.query(AppLockConstants.TABLE_NAME,
				new String[] { AppLockConstants.COLUMN_PACKAGE_NAME },
				selection, selectionArgs, null, null, null);
		boolean result = false;
		while (cursor != null && cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		database.close();
		return result;
	}

	public List<String> queryAll() {
		SQLiteDatabase database = openHelper.getWritableDatabase();
		Cursor cursor = database.query(AppLockConstants.TABLE_NAME, new String[] { AppLockConstants.COLUMN_PACKAGE_NAME }, null,
				null, null, null, null);
		List<String> datas = new ArrayList<String>();
		while (cursor != null && cursor.moveToNext()) {
			datas.add(cursor.getString(0));
		}
		cursor.close();
		database.close();
		return datas;
	}
}
