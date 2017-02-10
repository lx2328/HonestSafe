package com.zhenquan.telephonesafe.service;

import com.zhenquan.telephonesafe.engine.ProgressManagerProvider;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoCleanService extends Service {
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			//清理后台进程
			ProgressManagerProvider.cleanAllProcess(getApplicationContext());
		}
	};
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("自动清理服务开启了");
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
		
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("自动清理服务关闭了");
		unregisterReceiver(receiver);
	}
}
