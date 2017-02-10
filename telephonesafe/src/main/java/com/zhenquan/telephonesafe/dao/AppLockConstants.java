package com.zhenquan.telephonesafe.dao;

public interface AppLockConstants {
	public String DB_NAME = "applock.db";
	public int VERSION = 1;
	public String TABLE_NAME = "applock";
	public String COLUMN_PACKAGE_NAME = "package_name";
	public String DB_SQL = "create table " + TABLE_NAME
			+ " (_id integer primary key autoincrement," + COLUMN_PACKAGE_NAME
			+ " varchar (200))";
}
