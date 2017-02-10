package com.zhenquan.telephonesafe.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.format.Formatter;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.bean.ProcessBean;

public class ProgressManagerProvider {
	
	public static void cleanProcess(Context context ,String packageName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses(packageName);
	}
	/**获取总共的进程数
	 * @param context
	 * @return
	 */
	public static int getTotalProgress(Context context) {

		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPackages = packageManager
				.getInstalledPackages(PackageManager.GET_RECEIVERS
						| PackageManager.GET_ACTIVITIES
						| PackageManager.GET_PROVIDERS
						| PackageManager.GET_SERVICES);
		Set<String> set = new HashSet<String>();
		for (PackageInfo packageInfo : installedPackages) {
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			String processName = applicationInfo.processName;
			set.add(processName);

			ActivityInfo[] activities = packageInfo.activities;
			if (activities != null) {
				for (ActivityInfo activityInfo : activities) {
					set.add(activityInfo.processName);
				}
			}

			ServiceInfo[] services = packageInfo.services;
			if (services != null) {
				for (ServiceInfo serviceInfo : services) {
					set.add(serviceInfo.processName);
				}
			}

			ActivityInfo[] receivers = packageInfo.receivers;
			if (receivers != null) {
				for (ActivityInfo activityInfo : receivers) {
					set.add(activityInfo.processName);
				}
			}

			ProviderInfo[] providers = packageInfo.providers;
			if (providers != null) {
				for (ProviderInfo providerInfo : providers) {
					set.add(providerInfo.processName);
				}
			}
		}

		return set.size();

	}

	/**获取正在运行的进程数
	 * @param context
	 * @return
	 */
	public static int getRunningProgress(Context context) {

		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = manager
				.getRunningAppProcesses();
		return runningAppProcesses.size();
	}
	
	/**获取正在运行的进程信息
	 * @param context
	 * @return
	 */
	public static List<ProcessBean> getRunningProgressInfo(Context context) {
		PackageManager packageManager = context.getPackageManager();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();
		List<ProcessBean> datas = new ArrayList<ProcessBean>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			String processName = runningAppProcessInfo.processName;
			
			ProcessBean processBean = new ProcessBean();
			Drawable appIcon = null;
			String appName= null;
			boolean isSystem = false;
			try {
				PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
				appIcon = applicationInfo.loadIcon(packageManager);
				appName = applicationInfo.loadLabel(packageManager).toString();
				//获取每个应用的进程类型
				int flags = applicationInfo.flags;
				if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
					isSystem = true;
				}else {
					isSystem = false;
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				appIcon = context.getResources().getDrawable(R.drawable.ic_launcher);
				appName = processName;
				
				isSystem = true;
			}
			
			//使用任务管理器类，获取每个进程所占的内存数
			android.os.Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int []{runningAppProcessInfo.pid});
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			//一个应用执行所占的内存数：c进程（硬件）+虚拟机+应用程
			int totalPss = memoryInfo.getTotalPss()*1024;//获取当前进程所占用的内存
			
			
			processBean.appIcon = appIcon;
			processBean.appName = appName;
			processBean.appMemory = totalPss;
			processBean.isSystem = isSystem;
			processBean.appPackageName = processName;
			
			datas.add(processBean);
		}
		return datas;
	}
	
	/**获取设备当前可用内存
	 * @param context
	 * @return
	 */
	public static long getAvailMemory(Context context){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memmoInfo = new MemoryInfo();
		manager.getMemoryInfo(memmoInfo);//赋值函数
		long availMem = memmoInfo.availMem;
		return availMem;
	}
	
	/**获取设备的总内存
	 * @param context
	 * @return
	 * @throws IOException 
	 */
	public static long getTotalMemory(Context context) throws IOException{
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memmoInfo = new MemoryInfo();
		manager.getMemoryInfo(memmoInfo);//赋值函数
		long totalMem = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			totalMem = memmoInfo.totalMem;
		}else {
			File file = new File("proc/meminfo");
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String readLine = br.readLine();
				readLine= readLine.replace("MemTotal:", "");
				readLine = readLine.trim();
				int valueOf = Integer.valueOf(readLine);
				valueOf = valueOf*1024;
				totalMem = valueOf;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return totalMem;
	}
	/**删除所有正在运行进程
	 * @param context
	 */
	public static void cleanAllProcess(Context context) {
		List<ProcessBean> runningProgressInfo = getRunningProgressInfo(context);
		for (ProcessBean processBean : runningProgressInfo) {
			cleanProcess(context, processBean.appPackageName);
		}
	}
}
