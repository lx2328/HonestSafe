package com.zhenquan.telephonesafe.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.bean.ProcessBean;
import com.zhenquan.telephonesafe.engine.ProgressManagerProvider;
import com.zhenquan.telephonesafe.service.AutoCleanService;
import com.zhenquan.telephonesafe.utils.ContentValue;
import com.zhenquan.telephonesafe.utils.ServiceStateUtil;
import com.zhenquan.telephonesafe.utils.SpUtils;
import com.zhenquan.telephonesafe.view.ProgressManagerView;
import com.zhenquan.telephonesafe.view.SettingItemView;

public class ProcessManagerActivity2 extends Activity implements
		OnClickListener {

	private List<ProcessBean> datas;
	private ProgressManagerView mPmv;
	private ProgressManagerView mPmv_memory;
	private StickyListHeadersListView listView;
	private ProgressBar progressBar;
	private LinearLayout ll_loading;
	private List<ProcessBean> mUserData;
	private List<ProcessBean> mSysData;
	private TextView tv_progress_lvitem;
	private View ll_head;
	private Button btn_checkAll;
	private Button btn_checkReverse;
	private MyProcessAdapter adapter;
	private ImageView iv_clean;
	private int runningProgress;
	private ImageView iv_process_arrow1;
	private ImageView iv_process_arrow2;
	private SettingItemView siv_displaySysprocess;
	private SettingItemView siv_autoclean;
	private boolean isSystemShow;
	private List<ProcessBean> data2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);

		initUI();

		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_checkAll:
			if (isSystemShow) {
				for (ProcessBean processBean : datas) {
					if (TextUtils.equals(processBean.appPackageName,
							getPackageName())) {
						continue;
					}
					processBean.isChecked = true;
				}
				adapter.notifyDataSetChanged();
			} else {
				for (ProcessBean processBean : mUserData) {
					if (TextUtils.equals(processBean.appPackageName,
							getPackageName())) {
						continue;
					}
					processBean.isChecked = true;
				}
				adapter.notifyDataSetChanged();
			}

			break;
		case R.id.btn_checkReverse:
			List<ProcessBean> data = new ArrayList<ProcessBean>();
			if (isSystemShow) {
				data = datas;
			} else {
				data = mUserData;
			}
			for (ProcessBean processBean : data) {
				if (TextUtils.equals(processBean.appPackageName,
						getPackageName())) {
					continue;
				}
				processBean.isChecked = !processBean.isChecked;
			}
			adapter.notifyDataSetChanged();
			break;
		case R.id.iv_clean: // 清理
			data2 = new ArrayList<ProcessBean>();
			if (isSystemShow) {
				data2 = datas;
			} else {
				data2 = mUserData;
			}

			ListIterator<ProcessBean> listIterator = data2.listIterator();
			while (listIterator.hasNext()) {
				ProcessBean processBean = listIterator.next();
				if (processBean.isChecked) {
					ProgressManagerProvider
							.cleanProcess(getApplicationContext(),
									processBean.appPackageName);
					
					listIterator.remove();

					if (isSystemShow) {
						if (processBean.isSystem) {
							mSysData.remove(processBean);
						} else {
							mUserData.remove(processBean);
						}
					} else {
						datas.remove(processBean);
					}
					

				}
			}

			adapter.notifyDataSetChanged();
			runningProgress = datas.size();
			initProcess();
			// 重新计算内存占用
			long appMemory = 0;
			for (ProcessBean processBean : datas) {
				appMemory += processBean.appMemory;
			}
			initCleanMemory(appMemory);
			
		
			break;
		case R.id.siv_autoclean: // 锁屏自动清理
			siv_autoclean.toggle();
			// 因为这是一个服务所以我们不再使用sp记录状态而是使用 根据服务是否正在运行而显示状态
			if (ServiceStateUtil.isServiceRunning(getApplicationContext(),
					AutoCleanService.class)) {
				stopService(new Intent(ProcessManagerActivity2.this,
						AutoCleanService.class));
			} else {
				startService(new Intent(ProcessManagerActivity2.this,
						AutoCleanService.class));
			}

			break;
		case R.id.siv_displaySysprocess: // 显示系统进程
			siv_displaySysprocess.toggle();
			SpUtils.putBoolean(getApplicationContext(),
					ContentValue.DISPLAY_SYS_PROCESS,
					siv_displaySysprocess.isToggle());
			isSystemShow = siv_displaySysprocess.isToggle();
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	// 根据服务的状态回显
	@Override
	protected void onResume() {
		super.onResume();
		if (ServiceStateUtil.isServiceRunning(getApplicationContext(),
				AutoCleanService.class)) {
			siv_autoclean.setToggle(true);
		} else {
			siv_autoclean.setToggle(false);
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		runningProgress = ProgressManagerProvider
				.getRunningProgress(getApplicationContext());
		initProcess();
		initMemeory();
		initProgressBar();
	}

	private void initProgressBar() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SystemClock.sleep(2000);

				mUserData = new ArrayList<ProcessBean>();
				mSysData = new ArrayList<ProcessBean>();
				List<ProcessBean> runningProgressInfo = ProgressManagerProvider
						.getRunningProgressInfo(getApplicationContext());
				datas = new ArrayList<ProcessBean>();
				for (ProcessBean processBean : runningProgressInfo) {
					if (processBean.isSystem) {
						mSysData.add(processBean);
					} else {
						mUserData.add(processBean);
					}
				}

				// datas.addAll(runningProgressInfo);
				datas.addAll(mUserData);
				datas.addAll(mSysData);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						adapter = new MyProcessAdapter();
						listView.setAdapter(adapter);
					}
				});
			}
		}).start();

	}

	private void initProcess() {

		int totalProgress = ProgressManagerProvider
				.getTotalProgress(getApplicationContext());

		mPmv.setLeftText("正在运行(" + runningProgress + ")个");
		mPmv.setRightText("总进程(" + totalProgress + ")个");
		mPmv.setTitle("进程数：    ");
		mPmv.setpbProgress((runningProgress * 100) / totalProgress);
	};

	/**
	 * 初始化进程管理以及内存条目的数据
	 * 
	 * @param appMemory
	 * 
	 */
	private void initMemeory() {
		mPmv_memory.setTitle("内存：");
		long availMemory = ProgressManagerProvider
				.getAvailMemory(getApplicationContext());
		long totalMemory;
		try {
			totalMemory = ProgressManagerProvider
					.getTotalMemory(getApplicationContext());
			long usedMemory = totalMemory - availMemory;
			mPmv_memory.setLeftText("占用内存："
					+ Formatter.formatFileSize(getApplicationContext(),
							usedMemory));
			mPmv_memory.setRightText("可用内存："
					+ Formatter.formatFileSize(getApplicationContext(),
							availMemory));

			mPmv_memory
					.setpbProgress((int) (usedMemory * 100 / totalMemory + 0.5f));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initCleanMemory(long usedMemory) {
		mPmv_memory.setTitle("内存：");
		long totalMemory;
		try {
			totalMemory = ProgressManagerProvider
					.getTotalMemory(getApplicationContext());
			long availMemory = totalMemory - usedMemory;
			mPmv_memory.setLeftText("占用内存："
					+ Formatter.formatFileSize(getApplicationContext(),
							usedMemory));
			mPmv_memory.setRightText("可用内存："
					+ Formatter.formatFileSize(getApplicationContext(),
							availMemory));

			mPmv_memory
					.setpbProgress((int) (usedMemory * 100 / totalMemory + 0.5f));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initUI() {
		mPmv = (ProgressManagerView) findViewById(R.id.pmv_progress);
		mPmv_memory = (ProgressManagerView) findViewById(R.id.pmv_memory);
		listView = (StickyListHeadersListView) findViewById(R.id.lv_process1);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		progressBar = (ProgressBar) findViewById(R.id.pb_process_manager);
		tv_progress_lvitem = (TextView) findViewById(R.id.tv_progress_lvitem);
		ll_head = findViewById(R.id.ll_head);

		btn_checkAll = (Button) findViewById(R.id.btn_checkAll);
		btn_checkReverse = (Button) findViewById(R.id.btn_checkReverse);
		btn_checkAll.setOnClickListener(this);
		btn_checkReverse.setOnClickListener(this);

		iv_clean = (ImageView) findViewById(R.id.iv_clean);
		iv_clean.setOnClickListener(this);

		iv_process_arrow1 = (ImageView) findViewById(R.id.iv_process_arrow1);
		iv_process_arrow2 = (ImageView) findViewById(R.id.iv_process_arrow2);
		// 开启箭头动画
		startAlpaAnima();
		
		
		SlidingDrawer sd_process = (SlidingDrawer) findViewById(R.id.sd_process);
		sd_process.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				stopAlphaAnima();

			}
		});
		sd_process.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				startAlpaAnima();
			}

		});

		siv_displaySysprocess = (SettingItemView) findViewById(R.id.siv_displaySysprocess);
		siv_autoclean = (SettingItemView) findViewById(R.id.siv_autoclean);
		siv_displaySysprocess.setOnClickListener(this);
		siv_autoclean.setOnClickListener(this);

		isSystemShow = SpUtils.gettBoolean(ProcessManagerActivity2.this,
				ContentValue.DISPLAY_SYS_PROCESS, true);
		siv_displaySysprocess.setToggle(isSystemShow);

	}

	protected void stopAlphaAnima() {
		iv_process_arrow1.clearAnimation();
		iv_process_arrow2.clearAnimation();
		iv_process_arrow1.setBackgroundResource(R.drawable.drawer_arrow_down);
		iv_process_arrow2.setBackgroundResource(R.drawable.drawer_arrow_down);
	}

	private void startAlpaAnima() {
		iv_process_arrow1.setBackgroundResource(R.drawable.drawer_arrow_up);
		iv_process_arrow2.setBackgroundResource(R.drawable.drawer_arrow_up);
		// 用动态代码实现动画效果
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(500);
		aa.setRepeatCount(Animation.INFINITE);
		aa.setRepeatMode(Animation.REVERSE);
		aa.start();
		/*
		 * AlphaAnimation aa2 = new AlphaAnimation(1.0f, 0.2f);
		 * aa2.setDuration(500); aa2.setRepeatCount(Animation.INFINITE);
		 * aa2.setRepeatMode(Animation.REVERSE); aa2.start();
		 */
		// 用xml文件实现动画效果
		Animation aa2 = AnimationUtils.loadAnimation(
				ProcessManagerActivity2.this, R.anim.alpha_arrow2);
		iv_process_arrow1.setAnimation(aa);
		iv_process_arrow2.setAnimation(aa2);
	}

	/**
	 * 内部类 ListView的适配器
	 * 
	 * @author lizhenquan
	 * 
	 */
	class MyProcessAdapter extends BaseAdapter implements
			StickyListHeadersAdapter {

		private CheckBox cb;

		@Override
		public int getCount() {
			if (isSystemShow) {
				return datas.size();
			} else {
				return mUserData.size();
			}

		}

		@Override
		public ProcessBean getItem(int position) {
			if (isSystemShow) {
				return datas.get(position);
			} else {
				return mUserData.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			class ViewHolder {
				public ImageView appIcon;
				public TextView appName;
				public TextView appMemory;
				public CheckBox isChecked;
			}
			int itemViewType = getItemViewType(position);
			ViewHolder holder = null;
			if (convertView == null) {
				// 初始化view
				convertView = View.inflate(ProcessManagerActivity2.this,
						R.layout.listview_process_item, null);
				// 初始化控件
				holder = new ViewHolder();
				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.appName = (TextView) convertView
						.findViewById(R.id.tv_package);
				holder.appMemory = (TextView) convertView
						.findViewById(R.id.tv_memory);
				cb = (CheckBox) convertView.findViewById(R.id.cb_process);

				holder.isChecked = cb;
				// 保存ViewHolder
				convertView.setTag(holder);
			} else {
				// 从复用View里获取ViewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			// 绑定数据
			final ProcessBean processBean = getItem(position);
			holder.appIcon.setImageDrawable(processBean.appIcon);
			holder.appName.setText(processBean.appName);
			String fileSize = Formatter.formatFileSize(
					ProcessManagerActivity2.this, processBean.appMemory);
			holder.appMemory.setText(fileSize);
			holder.isChecked
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							processBean.isChecked = isChecked;
						}
					});
			holder.isChecked.setChecked(processBean.isChecked);
			// 判断当前条目的应用是否是本应用，如果是则隐藏checkbox
			if (TextUtils.equals(processBean.appPackageName, getPackageName())) {
				holder.isChecked.setVisibility(View.INVISIBLE);
			} else {
				holder.isChecked.setVisibility(View.VISIBLE);
			}
			
			return convertView;
		}

		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			int headerId = (int) getHeaderId(position);
			if (convertView == null) {
				convertView = new TextView(getApplicationContext());
			}
			TextView tv = (TextView) convertView;
			tv.setTextColor(Color.BLACK);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackgroundColor(Color.parseColor("#9c9c9c"));
			tv.setTextSize(18);

			switch (headerId) {
			case 0:
				tv.setText("系统进程(" + mSysData.size() + ")个");
				break;
			case 1:
				tv.setText("用户进程(" + mUserData.size() + ")个");
				break;

			default:
				break;
			}
			return tv;
		}

		@Override
		public long getHeaderId(int position) {
			ProcessBean processBean = getItem(position);
			if (processBean.isSystem) {
				return 0;
			} else {
				return 1;
			}
		}
	}

}
