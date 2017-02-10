package com.zhenquan.telephonesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.adapter.HomeAdapter;
import com.zhenquan.telephonesafe.bean.HomeBean;
import com.zhenquan.telephonesafe.service.RocketService;

public class HomeActivity extends Activity {

	private ImageView iv_home_logo;
	private GridView home_gv;
	
		private final static String[] TITLES = new String[] { 
			 "常用工具","进程管理", "手机杀毒","功能设置"};
			
	private final static String[] DESCS = new String[] { 
			 "工具大全" ,"管理运行进程", "病毒无处藏身","管理您的软件"};

	private final static int[] ICONS = new int[] {  R.drawable.cygj, R.drawable.jcgl,R.drawable.sjsd ,R.drawable.rjgj};
	private Button btn_stopRocket;
	private Button btn_startRocket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		initUI();
		
		initDate();
		
	}

	

	private void initDate() {
		List<HomeBean> data = new ArrayList<HomeBean>();
		 ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo, "rotationY", 0,45,180,360);
		oa.setDuration(3000);
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		oa.setRepeatMode(ObjectAnimator.REVERSE);
		 oa.start();
		 	for (int i = 0; i < TITLES.length; i++) {
				HomeBean homeBean = new HomeBean();
				homeBean.desc = DESCS[i];
				homeBean.title = TITLES[i];
				homeBean.imageId = ICONS[i];
				data.add(homeBean);
			}
		 home_gv.setAdapter(new HomeAdapter(this, data));
		 home_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(HomeActivity.this, CoomanToolActivity.class));
					break;
				case 1:
					//startActivity(new Intent(HomeActivity.this, ProcessManagerActivity.class));
					startActivity(new Intent(HomeActivity.this, ProcessManagerActivity2.class));
					break;
				case 2:
					startActivity(new Intent(getApplicationContext(), AntvirtusActivity.class));
					break;
				case 3:
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;
					
				default:
					break;
				}
				
			}
		});
	}
	
	private void initUI() {
		iv_home_logo = (ImageView) findViewById(R.id.iv_home_logo);
		home_gv = (GridView) findViewById(R.id.home_gv);
	}
}
