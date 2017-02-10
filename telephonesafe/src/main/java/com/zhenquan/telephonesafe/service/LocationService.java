package com.zhenquan.telephonesafe.service;

import com.zhenquan.telephonesafe.dao.LocationDao;
import com.zhenquan.telephonesafe.view.LocationToast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class LocationService extends Service {

	PhoneStateListener listener = new PhoneStateListener(){
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String queryLocation = LocationDao.queryLocation(getApplicationContext(), incomingNumber);
				//响铃的时候我们要让其显示吐丝
				locationToast.getToast(queryLocation);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				//去除土司显示
				locationToast.removeToast();
				break;

			default:
				break;
			}
		};
		
	};
	private LocationToast locationToast;
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			String location = LocationDao.queryLocation(context, number);
			locationToast.getToast(location);
		}
	};
	private TelephonyManager telephonyManager;

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("open");
		locationToast = new LocationToast(getApplicationContext());
		
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver , filter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("destroy");
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

}
