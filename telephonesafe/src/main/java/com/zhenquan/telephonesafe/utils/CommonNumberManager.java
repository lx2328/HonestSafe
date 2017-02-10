package com.zhenquan.telephonesafe.utils;

import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberManager {
	
	

	public static int getParentCount(Context context){
		int count = 0;
		File file = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		Cursor cursor = database.rawQuery("select count(*) from classlist", null);
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		database.close();
		return count;
	};
	
	public static String getParentText(Context context,int position){
		//select name from classlist where idx = 1
		String parentText = null;
		File file = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		Cursor cursor = database.rawQuery("select name from classlist where idx = ?", new String[]{String.valueOf(position+1)});
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				parentText	 = cursor.getString(0);
			}
		}
		cursor.close();
		database.close();
		return parentText;
	};
	
	public static int getChildCount(Context context,int position){
		//select count(*) from table1
		int count = 0;
		File file = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		Cursor cursor = database.rawQuery("select count(*) from table"+(String.valueOf(position+1)),null);
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		database.close();
		return count;
	};
	
	public static String[] getChildText(Context context,int groupPosition,int childPosition){
		//select name,number from table1 where _id = 1
		String[] parentText = null;
		File file = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		Cursor cursor = database.rawQuery("select name,number from table"+(String.valueOf(groupPosition+1))+" where _id = ?", new String[]{String.valueOf(childPosition+1)});
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				parentText = new String[2];
			parentText[0]= cursor.getString(0);
			parentText[1]= cursor.getString(1);
			}
		}
		cursor.close();
		database.close();
		return parentText;
	};
}	
