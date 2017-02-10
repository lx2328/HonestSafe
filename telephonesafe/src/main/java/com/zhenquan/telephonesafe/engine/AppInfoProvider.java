package com.zhenquan.telephonesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.zhenquan.telephonesafe.bean.AppInfoBean;

public class AppInfoProvider {
	public static List<AppInfoBean> getAllAppInfos(Context context){
		PackageManager packageManager = context.getPackageManager();
		List<AppInfoBean> datas = new ArrayList<AppInfoBean>();
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		for (PackageInfo packageInfo : installedPackages) {
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			Drawable icon = applicationInfo.loadIcon(packageManager);
			String label = applicationInfo.loadLabel(packageManager).toString();
			
			AppInfoBean appInfoBean = new AppInfoBean();
			appInfoBean.appIcon = icon;
			appInfoBean.appName = label;
			appInfoBean.appPackageName = applicationInfo.packageName;
			appInfoBean.appDir = applicationInfo.sourceDir;
			datas.add(appInfoBean);
		}
		return datas;
	}
}
