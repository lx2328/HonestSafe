package com.zhenquan.telephonesafe.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.activity.AppLockActivty;
import com.zhenquan.telephonesafe.bean.AppInfoBean;
import com.zhenquan.telephonesafe.dao.AppLockDao;

public class AppLockAdapter extends BaseAdapter {

	private Context mContext;
	private List<AppInfoBean> mData;
	private boolean mIsLocked;
	private AppLockDao appLockDao;
	private List<AppInfoBean> mOtherData;

	public AppLockAdapter(Context context, List<AppInfoBean> data,
			List<AppInfoBean> otherData, boolean isLocked) {
		super();
		this.mContext = context;
		this.mData = data;
		this.mIsLocked = isLocked;
		this.mOtherData = otherData;
		appLockDao = new AppLockDao(mContext);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public AppInfoBean getItem(int position) {

		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		class ViewHolder {
			ImageView icon;
			TextView name;
			ImageView lock;
		}

		ViewHolder holder = null;
		if (convertView == null) {
			// 初始化view
			convertView = View.inflate(mContext, R.layout.lvitem_lock, null);
			// 初始化控件
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.iv_lockicon);
			holder.name = (TextView) convertView.findViewById(R.id.tv_lock);
			holder.lock = (ImageView) convertView.findViewById(R.id.iv_lock);
			// 保存ViewHolder
			convertView.setTag(holder);
		} else {
			// 从复用View里获取ViewHolder
			holder = (ViewHolder) convertView.getTag();
		}

		// 绑定数据
		final AppInfoBean appInfoBean = getItem(position);
		holder.icon.setImageDrawable(appInfoBean.appIcon);
		holder.name.setText(appInfoBean.appName);

		if (mIsLocked) {
			holder.lock
					.setBackgroundResource(R.drawable.list_button_unlock_default);
		} else {
			holder.lock
					.setBackgroundResource(R.drawable.list_button_lock_default);
		}
		final View itemView = convertView;
		if (mIsLocked) {
			holder.lock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 1.数据库删除，如果删除成功，才将当前的数据从列表中删除并刷新2.更新标题的数量
					if (appLockDao.delete(appInfoBean.appPackageName)) {
						Animation unlockAnimation = AnimationUtils
								.loadAnimation(mContext,
										R.anim.anim_applock_unlock);
						unlockAnimation
								.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationRepeat(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationEnd(
											Animation animation) {
										mData.remove(appInfoBean);
										notifyDataSetChanged();

										// 将上下文强转为applockactivity对象并调用它的方法即可
										((AppLockActivty) mContext)
												.updateTitle(mIsLocked);

										// 还需要将当前被删除的对象添加到另一个集合（已加锁的集合）并展示(listview如果从gone变为可见，内部，就根据当前的集合动态重新显示（适配器刷新了）)
										mOtherData.add(appInfoBean);
									}
								});
						itemView.startAnimation(unlockAnimation);

					}

				}
			});

		} else {
			// 未加锁适配器，加锁操作，数据库插入
			holder.lock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 1.数据库插入，如果插入成功，才将当前的数据从列表中删除并刷新2.更新标题的数量
					if (appLockDao.insert(appInfoBean.appPackageName)) {
						// 从左往右进行平移动画
						Animation lockAnimation = AnimationUtils.loadAnimation(
								mContext, R.anim.anim_applock_lock);
						lockAnimation
								.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationRepeat(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationEnd(
											Animation animation) {
										// 当动画执行完毕后，才删除数据并刷新
										mData.remove(appInfoBean);

										notifyDataSetChanged();

										// 将上下文强转为applockactivity对象并调用它的方法即可
										((AppLockActivty) mContext)
												.updateTitle(mIsLocked);

										// 还需要将当前被删除的对象添加到另一个集合（已加锁的集合）并展示(listview如果从gone变为可见，内部，就根据当前的集合动态重新显示（适配器刷新了）)
										mOtherData.add(appInfoBean);
									}
								});
						itemView.startAnimation(lockAnimation);

					}

				}
			});
		}
		return convertView;
	}

}
