package com.zhenquan.telephonesafe.adapter;

import java.util.List;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.utils.ContentValue;
import com.zhenquan.telephonesafe.utils.SpUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogAdapter extends BaseAdapter{
	
	private Context mContext;
	private String[] mTitle;
	private int[] mColors;

	public DialogAdapter(Context context,String[] title,int[] colors) {
		super();
		this.mContext = context;
		this.mTitle = title;
		this.mColors = colors;
		// TODO Auto-generated constructor stub
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		View view = null;
		if (convertView==null) {
			 view= View.inflate(mContext, R.layout.listview_dialog_location, null);
			viewHolder = new ViewHolder();
			
			TextView tv_location_item = (TextView) view.findViewById(R.id.tv_location_item);
			ImageView iv_color = (ImageView) view.findViewById(R.id.iv_color);
			ImageView iv_selected = (ImageView) view.findViewById(R.id.iv_selected);
			
			viewHolder.iv_color = iv_color;
			viewHolder.iv_selected = iv_selected;
			viewHolder.tv_location_item = tv_location_item;
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_location_item.setText(mTitle[position]);
		viewHolder.iv_color.setImageResource(mColors[position]);
		
		int gettInt = SpUtils.gettInt(mContext, ContentValue.KEY_LOCATION_ID, R.drawable.shape_location_nomal);
		if (gettInt == mColors[position]) {
			viewHolder.iv_selected.setVisibility(View.VISIBLE);
		}else {
			viewHolder.iv_selected.setVisibility(View.INVISIBLE);
		}
		
		return view;
	}
	class ViewHolder{
		TextView tv_location_item;
		ImageView iv_color;
		ImageView iv_selected;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTitle.length;
	}
}


