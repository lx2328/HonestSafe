package com.zhenquan.telephonesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.service.DogWatchService;
import com.zhenquan.telephonesafe.service.LocationService;
import com.zhenquan.telephonesafe.utils.CommonNumberManager;
import com.zhenquan.telephonesafe.utils.ServiceStateUtil;
import com.zhenquan.telephonesafe.view.SettingItemView;

public class CoomanToolActivity extends Activity implements OnClickListener {

	private SettingItemView siv_watchdog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cooman_tool);
		
		initUI();
	}

	private void initUI() {
		SettingItemView siv_locationItemView = (SettingItemView) findViewById(R.id.siv_location);
		SettingItemView siv_normalNumber = (SettingItemView) findViewById(R.id.siv_normalNumber);
		SettingItemView siv_appLock = (SettingItemView) findViewById(R.id.siv_appLock);
		siv_watchdog = (SettingItemView) findViewById(R.id.siv_watchdog);
		siv_locationItemView.setOnClickListener(this);
		siv_normalNumber.setOnClickListener(this);
		siv_appLock.setOnClickListener(this);
		siv_watchdog.setOnClickListener(this);
		
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (ServiceStateUtil.isServiceRunning(getApplicationContext(), DogWatchService.class)) {
			siv_watchdog.setToggle(true);
		}else {
			siv_watchdog.setToggle(false);
		}

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.siv_location:
			startActivity(new Intent(getApplicationContext(), LocationActivity.class));
			break;
		case R.id.siv_normalNumber:
			startActivity(new Intent(getApplicationContext(), NormalNumActivty.class));
			break;
		case R.id.siv_appLock:
			startActivity(new Intent(getApplicationContext(), AppLockActivty.class));
			break;
		case R.id.siv_watchdog:
			siv_watchdog.toggle();
			boolean toggle = siv_watchdog.isToggle();
			Intent intent = new Intent(getApplicationContext(), DogWatchService.class);
			if (toggle) {
				startService(intent);
			}else {
				stopService(intent);
			}
			break;
		default:
			break;
		}
	}
}
