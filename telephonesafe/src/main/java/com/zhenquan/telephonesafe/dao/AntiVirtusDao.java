package com.zhenquan.telephonesafe.dao;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class AntiVirtusDao {
	public static boolean queryAntivirtus(Context context,String appMd5){
		File file = new File(context.getFilesDir(), "antivirus.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		String sql = "select md5 from datable where md5 = ?";
		String[] selectionArgs = new String[]{appMd5};
		Cursor cursor = database.rawQuery(sql, selectionArgs);
		boolean result = false;
		if (cursor !=null && cursor.moveToNext()) {
			result = true;
		}
		database.close();
		cursor.close();
		return result;
	}
}
