package com.zhenquan.telephonesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
	private static final String AUTO_UPDATA = "config";

	public static void putBoolean(Context context,String key,Boolean value){
		SharedPreferences sp = context.getSharedPreferences(AUTO_UPDATA, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	public static boolean gettBoolean(Context context,String key,Boolean defValue){
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences(AUTO_UPDATA, context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	public static void putInt(Context context,String key,int value){
		SharedPreferences sp = context.getSharedPreferences(AUTO_UPDATA, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	public static int gettInt(Context context,String key,int defValue){
		SharedPreferences sp = context.getSharedPreferences(AUTO_UPDATA, Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
}
