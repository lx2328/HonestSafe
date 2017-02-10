package com.zhenquan.telephonesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.adapter.AppLockAdapter;
import com.zhenquan.telephonesafe.bean.AppInfoBean;
import com.zhenquan.telephonesafe.dao.AppLockDao;
import com.zhenquan.telephonesafe.engine.AppInfoProvider;

public class AppLockActivty extends Activity implements OnClickListener {

	private Button btn_unlock;
	private Button btn_lock;
	private ListView lv_applock;
	private ListView lv_appunlock;
	private AppLockDao appLockDao;
	private List<AppInfoBean> unLockDatas = new ArrayList<AppInfoBean>();// 未加锁的应用集合
	private List<AppInfoBean> lockedDatas = new ArrayList<AppInfoBean>();// 已加锁的应用集合
	private TextView applock_tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock_activty);

		initUI();

		initData();

	}

	private void initData() { 
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<AppInfoBean> allAppInfos = AppInfoProvider
						.getAllAppInfos(getApplicationContext());
				List<String> queryAll = appLockDao.queryAll();
				for (AppInfoBean appInfoBean : allAppInfos) {
					if (queryAll.contains(appInfoBean.appPackageName)) {
						lockedDatas.add(appInfoBean);
					} else {
						unLockDatas.add(appInfoBean);
					}
					 //添加一个静态数据
					/*if (appInfoBean.appPackageName
							.equals("com.android.browser")) {
						lockedDatas.add(appInfoBean);					
						}*/
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// 区分完数据，设置标题内容
						applock_tv_title.setText("未加锁(" + unLockDatas.size()
								+ ")个");
						AppLockAdapter unLockAdapter = new AppLockAdapter(
								AppLockActivty.this, unLockDatas,lockedDatas, false);
						AppLockAdapter lockAdapter = new AppLockAdapter(
								AppLockActivty.this, lockedDatas,unLockDatas, true);
						lv_appunlock.setAdapter(unLockAdapter);
						lv_applock.setAdapter(lockAdapter);
					}
				});
			}
		}).start();
	}

	private void initUI() {

		lv_appunlock = (ListView) findViewById(R.id.lv_appunlock);
		lv_applock = (ListView) findViewById(R.id.lv_applock);

		btn_unlock = (Button) findViewById(R.id.btn_unlock);
		btn_lock = (Button) findViewById(R.id.btn_lock);
		btn_lock.setOnClickListener(this);
		btn_unlock.setOnClickListener(this);
		
		appLockDao = new AppLockDao(AppLockActivty.this);
		
		applock_tv_title = (TextView) findViewById(R.id.applock_tv_title);
		
	}

	@Override
	public void onClick(View v) {  //加锁按钮
		switch (v.getId()) {
		case R.id.btn_lock:
			// 改变背景
			btn_lock.setBackgroundResource(R.drawable.applock_tableft_pressed_shape);
			btn_unlock
					.setBackgroundResource(R.drawable.applock_tabright_normal_shape);
			btn_lock.setTextColor(Color.WHITE);
			btn_unlock.setTextColor(Color.parseColor("#5caad7"));
			lv_applock.setVisibility(View.VISIBLE);
			lv_appunlock.setVisibility(View.GONE);
			
			applock_tv_title.setText("已加锁(" + lockedDatas.size() + ")个");
			break;
		case R.id.btn_unlock:  //未加锁按钮
			btn_unlock
					.setBackgroundResource(R.drawable.applock_tabright_pressed_shape);
			btn_lock.setBackgroundResource(R.drawable.applock_tabright_normal_shape);
			btn_lock.setTextColor(Color.parseColor("#5caad7"));
			btn_unlock.setTextColor(Color.WHITE);
			lv_applock.setVisibility(View.GONE);
			lv_appunlock.setVisibility(View.VISIBLE);
			
			applock_tv_title.setText("未加锁(" + unLockDatas.size() + ")个");
			break;

		default:
			break;
		}

	}
	//对标题进行动态修改
		public void updateTitle(boolean isLocked){
			if (isLocked) {
				applock_tv_title.setText("已加锁(" + lockedDatas.size() + ")个");
			}else{
				applock_tv_title.setText("未加锁(" + unLockDatas.size() + ")个");
			}
		}
}
