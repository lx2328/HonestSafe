package com.zhenquan.telephonesafe.adapter;

import com.zhenquan.telephonesafe.utils.CommonNumberManager;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CommonNumAdapter extends BaseExpandableListAdapter{
	
	private Context mContext;

	public CommonNumAdapter(Context context) {
		super();
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String[] childText = CommonNumberManager.getChildText(mContext, groupPosition, childPosition);
		TextView textView = new TextView(mContext);
		textView.setPadding(5, 5, 5, 5);
		textView.setTextColor(Color.BLACK);
		textView.setText(childText[0]+"\n"+childText[1]);
		return textView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int childCount = CommonNumberManager.getChildCount(mContext, groupPosition);
		return childCount;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		int parentCount = CommonNumberManager.getParentCount(mContext);
		return parentCount;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String parentText = CommonNumberManager.getParentText(mContext, groupPosition);
		TextView textView = new TextView(mContext);
		textView.setText(parentText);
		textView.setTextSize(18);
		textView.setTextColor(Color.BLACK);
		textView.setBackgroundColor(color.darker_gray);
		textView.setPadding(5, 5, 5, 5);
		return textView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	

}
