package com.zhenquan.telephonesafe.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppUnLockOpenHelper extends SQLiteOpenHelper {

	public AppUnLockOpenHelper(Context context) {
		super(context, AppLockConstants.DB_NAME, null, AppLockConstants.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(AppLockConstants.DB_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
