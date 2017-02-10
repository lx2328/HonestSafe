package com.zhenquan.telephonesafe.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.activity.AntvirtusActivity;
import com.zhenquan.telephonesafe.bean.AntivirusBean;

public class AntvirtusAdapter extends BaseAdapter{
	
	private List<AntivirusBean> mDatas;
	private Context mContext;
	private AntivirusBean mAntivirus;

	public AntvirtusAdapter(Context context, List<AntivirusBean> datas) {
		super();
		this.mContext = context;
		this.mDatas = datas;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public AntivirusBean getItem(int position) {
		return mDatas.get(position);
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
			TextView isSafe;
			ImageView isDelete;
		} 
		
			ViewHolder holder = null;
		if (convertView == null) {
			//初始化view
			convertView = View.inflate(mContext, R.layout.lv_item_virtus,
					null);
			//初始化控件
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.iv_virtus);
			holder.name = (TextView) convertView.findViewById(R.id.tv_virtus_appName);
			holder.isSafe = (TextView) convertView.findViewById(R.id.tv_virtus_issafe);
			holder.isDelete = (ImageView) convertView.findViewById(R.id.iv_virtus_delete);
			//保存ViewHolder
			convertView.setTag(holder);
		} else {
			//从复用View里获取ViewHolder
			holder = (ViewHolder) convertView.getTag();
		}
		
		//绑定数据
		 final AntivirusBean antivirusBean = getItem(position);
		holder.icon.setImageDrawable(antivirusBean.appIcon);
		holder.name.setText(antivirusBean.appName);
		if (antivirusBean.isAntiVirus) {
			holder.isSafe.setText("病毒");
			holder.isSafe.setTextColor(Color.RED);
			holder.isDelete.setVisibility(View.VISIBLE);
		}else {
			holder.isSafe.setText("安全");
			holder.isSafe.setTextColor(Color.BLACK);
			holder.isDelete.setVisibility(View.INVISIBLE);
		}
		
		holder.isDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					//隐式意图开启删除应用的界面
				//{act=android.intent.action.DELETE dat=package:com.zhenquan.telephonesafe 
				//得到程序的索引
				
				mAntivirus = antivirusBean;
				Intent intent = new Intent();
				intent.setAction("android.intent.action.DELETE");
				intent.setData(Uri.parse("package:"+antivirusBean.appPackageName));
				((AntvirtusActivity)mContext).startActivityForResult(intent, 100);
				
			}
		});
		return convertView; 
	}

	public AntivirusBean getAntvirtus() {
		// TODO Auto-generated method stub
		return mAntivirus;
	}

}
