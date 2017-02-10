package com.zhenquan.telephonesafe.activity;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.service.LocationService;
import com.zhenquan.telephonesafe.utils.ContentValue;
import com.zhenquan.telephonesafe.utils.ServiceStateUtil;
import com.zhenquan.telephonesafe.utils.SpUtils;
import com.zhenquan.telephonesafe.view.LocationStyleDialog;
import com.zhenquan.telephonesafe.view.SettingItemView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class SettingActivity extends Activity implements OnClickListener {
	
	private SettingItemView siv_autoUpdate;
	private SettingItemView siv_localstyle;
	private SettingItemView siv_localstyle_set;
	private boolean flag = false;
	private ImageView iv_settingitem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		
		
		
		initUI();
		boolean b = SpUtils.gettBoolean(getApplicationContext(), ContentValue.SETTING_UPDATE, true);
		siv_autoUpdate.setToggle(b);                   
	}

	private void initUI() {
		siv_autoUpdate = (SettingItemView) findViewById(R.id.siv_autoUpdate);
		siv_localstyle = (SettingItemView) findViewById(R.id.siv_localstyle);
		siv_localstyle_set = (SettingItemView) findViewById(R.id.siv_localstyle_set);
		siv_autoUpdate.setOnClickListener(this);
		siv_localstyle.setOnClickListener(this);
		siv_localstyle_set.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//当界面重新显示时，根据location的服务的状态，动态回显，设置按钮的开关状态
				if (ServiceStateUtil.isServiceRunning(getApplicationContext(), LocationService.class)) {
					siv_localstyle.setToggle(true);
				}else{
					siv_localstyle.setToggle(false);
				}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.siv_autoUpdate:
			siv_autoUpdate.toggle();
			SpUtils.putBoolean(getApplicationContext(), ContentValue.SETTING_UPDATE, siv_autoUpdate.isToggle());
			break;
		case R.id.siv_localstyle:
			siv_localstyle.toggle();
			boolean toggle = siv_localstyle.isToggle();
			Intent intent = new Intent(getApplicationContext(),LocationService.class);
			if (toggle) {
				startService(intent);
			}else {
				stopService(intent);
			}
			break;
		case R.id.siv_localstyle_set:
			LocationStyleDialog lsd = new LocationStyleDialog(this);
			lsd.show();
			break;
		default:
			break;
		}
		
	}
}
