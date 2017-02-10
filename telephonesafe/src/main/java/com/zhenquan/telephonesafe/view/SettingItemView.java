package com.zhenquan.telephonesafe.view;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.utils.ContentValue;
import com.zhenquan.telephonesafe.utils.SpUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private ImageView iv_settingitem;
	private boolean flag = false;
	public SettingItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("Recycle")
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.setting_item, null);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
		String title = typedArray.getString(R.styleable.SettingItemView_title);
		TextView tv_settingitem = (TextView) view.findViewById(R.id.tv_settingitem);
		tv_settingitem.setText(title);
		int int1 = typedArray.getInt(R.styleable.SettingItemView_bg_type, -1);
		if (int1==-1) {
			throw new RuntimeException("要使用这个控件一定要加上bg_type属性");
		}
		switch (int1) {
		case 0:
			this.setBackgroundResource(R.drawable.selector_setting_firstitem);
			break;
		case 1:
			this.setBackgroundResource(R.drawable.selector_setting_middleitem);
			break;
		case 2:
			this.setBackgroundResource(R.drawable.selector_setting_lastitem);
			break;

		default:
			break;
		}
		this.addView(view);
	
		iv_settingitem = (ImageView) view.findViewById(R.id.iv_settingitem);
		boolean enable = typedArray.getBoolean(R.styleable.SettingItemView_enable, true);
		if (enable) {
			
			iv_settingitem.setVisibility(View.VISIBLE);
		}else {
			
			iv_settingitem.setVisibility(View.INVISIBLE);
		}
	}
	public  void setToggle(boolean flag){
		if (flag) {
			iv_settingitem.setImageResource(R.drawable.on);
		}else{
			iv_settingitem.setImageResource(R.drawable.off);
			
		}
		this.flag  = flag;
		
	};
	public boolean isToggle(){
		return flag;
	}
	public void toggle(){
		setToggle(!flag);
	}
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

}
