package com.zhenquan.telephonesafe.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocationDao {
	public static String queryLocation(Context context, String number) {
		File file = new File(context.getFilesDir(), "address.db");
		String locationString = "未知";
		SQLiteDatabase database = SQLiteDatabase.openDatabase(
				file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		String sql = "select cardtype from  info where mobileprefix = ?";
		String regexString = "^1[3-8]\\d{9}$";
		if (number.matches(regexString)) {
			String substring = number.substring(0, 7);
			Cursor cursor = database.rawQuery(sql, new String[] { substring });
			if (cursor != null) {
				if (cursor.moveToNext()) {
					locationString = cursor.getString(0);
				}
			}
			cursor.close();
		} else {
			switch (number.length()) {
			case 3:
				locationString = "紧急号码";
				break;
			case 4:
				locationString = "模拟器号码";
				break;
			case 5:
				locationString = "服务号码";
				break;
			case 7:
			case 8:
				locationString = "本地座机";
			default:
				break;
			}
		}
		return locationString;
	}
}
