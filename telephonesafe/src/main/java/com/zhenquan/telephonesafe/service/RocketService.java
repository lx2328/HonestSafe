package com.zhenquan.telephonesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zhenquan.telephonesafe.view.RocketToast;

public class RocketService extends Service {

	private RocketToast rocketToast;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("火箭服务打开了");
		rocketToast = new RocketToast(this);
		
		rocketToast.showRocketToast();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("火箭服务关闭了");
		rocketToast.hideRocketToast();
	}

}
