package com.zhenquan.telephonesafe.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.UserDataHandler;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.R.layout;
import com.zhenquan.telephonesafe.bean.ProcessBean;
import com.zhenquan.telephonesafe.engine.ProgressManagerProvider;
import com.zhenquan.telephonesafe.view.ProgressManagerView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProcessManagerActivity extends Activity {
	
	private List<ProcessBean> datas;
	private ProgressManagerView mPmv;
	private ProgressManagerView mPmv_memory;
	private ListView listView;
	private ProgressBar progressBar;
	private LinearLayout ll_loading;
	private List<ProcessBean> mUserData;
	private List<ProcessBean> mSysData;
	private TextView tv_progress_lvitem;
	private View ll_head;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);

		initUI();
		
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		initPmvDate();
		initProgressBar();
		
		
	}

	private void initProgressBar() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SystemClock.sleep(2000);
				
				mUserData = new ArrayList<ProcessBean>();
				mSysData = new ArrayList<ProcessBean>();
				List<ProcessBean> runningProgressInfo = ProgressManagerProvider.getRunningProgressInfo(getApplicationContext());
				datas = new ArrayList<ProcessBean>();
				for (ProcessBean processBean : runningProgressInfo) {
					if (processBean.isSystem) {
						mSysData.add(processBean);
					}else {
						mUserData.add(processBean);
					}
				}
				
				//datas.addAll(runningProgressInfo);
				datas.addAll(mUserData);
				datas.addAll(mSysData);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ll_head.setVisibility(View.VISIBLE);
						ll_loading.setVisibility(View.INVISIBLE);
						MyProcessAdapter adapter = new MyProcessAdapter();
						listView.setAdapter(adapter);
						listView.setOnScrollListener(new OnScrollListener() {
							
							@Override
							public void onScrollStateChanged(AbsListView view, int scrollState) {
								
							}
							
							@Override
							public void onScroll(AbsListView view, int firstVisibleItem,
									int visibleItemCount, int totalItemCount) {
								//System.out.println("wo bei gundong le ");
								if (firstVisibleItem>mUserData.size()+1) {
									tv_progress_lvitem.setText("系统程序("+mSysData.size()+")个");
								}else {
									tv_progress_lvitem.setText("用户程序("+mUserData.size()+")个");
								}
							}
						});
					}
				});
			}
		}).start();

	}

	/**初始化进程管理以及内存条目的数据
	 * 
	 */
	private void initPmvDate() {
		int runningProgress = ProgressManagerProvider
				.getRunningProgress(getApplicationContext());
		int totalProgress = ProgressManagerProvider
				.getTotalProgress(getApplicationContext());

		long availMemory = ProgressManagerProvider
				.getAvailMemory(getApplicationContext());
		long totalMemory = 0;
		try {
			totalMemory = ProgressManagerProvider
					.getTotalMemory(getApplicationContext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPmv_memory.setLeftText("占用内存："
				+ Formatter
						.formatFileSize(getApplicationContext(), (totalMemory - availMemory)));
		mPmv_memory.setRightText("可用内存："
				+ Formatter.formatFileSize(getApplicationContext(),
						availMemory));

		mPmv.setLeftText("正在运行(" + runningProgress + ")个");
		mPmv.setRightText("总进程(" + totalProgress + ")个");
		mPmv_memory.setTitle("内存：    ");
		mPmv.setpbProgress((runningProgress * 100) / totalProgress);
		mPmv_memory.setpbProgress((int) (((totalMemory - availMemory) * 100) / totalMemory));
	}

	private void initUI() {
		mPmv = (ProgressManagerView) findViewById(R.id.pmv_progress);
		mPmv_memory = (ProgressManagerView) findViewById(R.id.pmv_memory);
		listView = (ListView) findViewById(R.id.lv_process1);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		progressBar = (ProgressBar) findViewById(R.id.pb_process_manager);
		tv_progress_lvitem = (TextView) findViewById(R.id.tv_progress_lvitem);
			ll_head = findViewById(R.id.ll_head);	
	}

	
	/**内部类              ListView的适配器
	 * @author lizhenquan
	 *
	 */
	class MyProcessAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mUserData.size()+mSysData.size()+2;
		}

		@Override
		public ProcessBean getItem(int position) {
			if (position==0) {
				return null;
			}
			if (position == mUserData.size()+1) {
				return null;
			}
			if (position < mUserData.size()+1) {
				return mUserData.get(position-1);
			}else{
				return mSysData.get(position-(mUserData.size()+2));
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if (position==0 || position == (mUserData.size()+1)) {
				return 0;
			}else {
				return 1;
			}
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			class ViewHolder {
				public ImageView appIcon;
				public TextView appName;
				public TextView appMemory;
				public CheckBox isSystem;
			} 
			int itemViewType = getItemViewType(position);
			switch (itemViewType) {
			case 0:
				if (convertView == null) {
					convertView = View.inflate(ProcessManagerActivity.this, R.layout.listitem_progress_title, null);
				}
				TextView title = (TextView) convertView.findViewById(R.id.tv_progress_lvitem);
				if (position == 0) {
					title.setText("用户程序("+mUserData.size()+")个");
				}else{
					title.setText("系统程序("+mSysData.size()+")个");
				}
				break;
			case 1:
				ViewHolder holder = null;
				if (convertView == null) {
					//初始化view
					convertView = View.inflate(ProcessManagerActivity.this, R.layout.listview_process_item,
							null);
					//初始化控件
					holder = new ViewHolder();
					holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					holder.appName = (TextView) convertView.findViewById(R.id.tv_package);
					holder.appMemory = (TextView) convertView.findViewById(R.id.tv_memory);
					holder.isSystem = (CheckBox) convertView.findViewById(R.id.cb_process);
					//保存ViewHolder
					convertView.setTag(holder);
				} else {
					//从复用View里获取ViewHolder
					holder = (ViewHolder) convertView.getTag();
				}
				
				//绑定数据
				ProcessBean processBean = getItem(position);
				holder.appIcon.setImageDrawable(processBean.appIcon);
				holder.appName.setText(processBean.appName);
				holder.appMemory.setText(processBean.appMemory+"");
				break;
				
			default:
				break;
				
			}
			return convertView; 
		}
		
	}
}
