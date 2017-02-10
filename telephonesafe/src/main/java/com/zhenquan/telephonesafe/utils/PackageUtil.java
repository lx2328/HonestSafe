package com.zhenquan.telephonesafe.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtil {
	public static String getVersionName(Context context,String pacageName){
		PackageManager manager = context.getPackageManager();
		String versionName = "";
		try {
			PackageInfo info = manager.getPackageInfo(pacageName, 0);
			versionName = info.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	public static int getVersionCode(Context context,String pacageName){
		PackageManager manager = context.getPackageManager();
		int versionCode = 1;
		try {
			PackageInfo info = manager.getPackageInfo(pacageName, 0);
			versionCode = info.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
