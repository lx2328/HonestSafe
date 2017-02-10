package com.zhenquan.telephonesafe.adapter;

import java.util.List;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.bean.HomeBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {

	private Context mContext;
	private List mList;

	public HomeAdapter(Context context, List list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		class ViewHolder {
			ImageView icon;
			TextView title;
			TextView desc;
		}

		ViewHolder holder = null;
		if (convertView == null) {
			// 初始化view
			convertView = View.inflate(parent.getContext(),
					R.layout.home_grid_item, null);
			// 初始化控件
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.iv_griditem);
			holder.title = (TextView) convertView
					.findViewById(R.id.tv_gridhome_title);
			holder.desc = (TextView) convertView
					.findViewById(R.id.tv_gridhome_desc);
			// 保存ViewHolder
			convertView.setTag(holder);
		} else {
			// 从复用View里获取ViewHolder
			holder = (ViewHolder) convertView.getTag();
		}

		// 绑定数据
		HomeBean homeBean = (HomeBean) mList.get(position);
		holder.desc.setText(homeBean.desc);
		holder.title.setText(homeBean.title);
		holder.icon.setImageResource(homeBean.imageId);
		return convertView;
	}

}
