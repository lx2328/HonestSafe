package com.zhenquan.telephonesafe.service;

import java.util.List;

import com.zhenquan.telephonesafe.activity.PasswordActivity;
import com.zhenquan.telephonesafe.dao.AppLockDao;
import com.zhenquan.telephonesafe.utils.ContentValue;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

public class DogWatchService extends Service {

	private ActivityManager activityManager ;
	 private AppLockDao appLockDao ;
	 private String extra;
	 private boolean isOpenDog  = true;
	 BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				extra = intent.getStringExtra(ContentValue.KEY_DOGPACKAGE);
				
			}
		};
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("电子狗服务打开了");
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		appLockDao =  new AppLockDao(getApplicationContext());
		// 开启电子狗，获取当前最新的任务栈对象
		startWatchDog();
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(ContentValue.ACTION_SKIP);
		registerReceiver(receiver, filter);
	}

	private void startWatchDog() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isOpenDog) {
					List<RunningTaskInfo> runningTasks = activityManager
							.getRunningTasks(1);
					RunningTaskInfo runningTaskInfo = runningTasks.get(0);
					ComponentName activity = runningTaskInfo.topActivity;
					String packageName = activity.getPackageName();
					
					if (TextUtils.equals(packageName, extra)) {
						continue;
					}
					
					if (appLockDao.query(packageName)) {
						System.out.println("发现需要拦截的应用");
						//弹出密码验证页面
						Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra(ContentValue.KEY_DOGPACKAGE, packageName);
						startActivity(intent);
					}
					SystemClock.sleep(10);
				}

			}
		}).start();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("电子狗服务关闭了");
		unregisterReceiver(receiver);
		isOpenDog = false;
	}
}
