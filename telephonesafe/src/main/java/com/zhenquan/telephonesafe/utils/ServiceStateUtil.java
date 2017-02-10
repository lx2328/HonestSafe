package com.zhenquan.telephonesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;

import com.zhenquan.telephonesafe.service.LocationService;

public class ServiceStateUtil {

	public static boolean isServiceRunning(Context applicationContext,
			Class<? extends Service> clazz) {
		
		ActivityManager activityManager = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			ComponentName service = runningServiceInfo.service;
			if (service.getClassName().equals(clazz.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/*public static boolean isServiceRunning2(Context context, Class<? extends Service> clazz){
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			ComponentName service = runningServiceInfo.service;
			if (service.getClassName().equals(clazz.getName())) {
				return true;
			}
		}
		return false;
	}*/

}
