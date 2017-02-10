package com.zhenquan.telephonesafe.view;

import com.zhenquan.telephonesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressManagerView extends LinearLayout{

	private TextView tv_left;
	private TextView tv_right;
	private ProgressBar pb_progress;
	private TextView tv_title_progress;

	public ProgressManagerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ProgressManagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(getContext(), R.layout.view_progress_items, this);
		pb_progress = (ProgressBar) view.findViewById(R.id.pb_progress);
		tv_title_progress = (TextView) view.findViewById(R.id.tv_title_progress);
		tv_left = (TextView) view.findViewById(R.id.tv_left);
		tv_right = (TextView) view.findViewById(R.id.tv_right);
		pb_progress.setMax(100);
	}

	public ProgressManagerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void setLeftText(String text){
		//"正在运行("+runningProgress+")个"
		tv_left.setText(text);
	}
	public void setRightText(String text){
		tv_right.setText(text);
	}
	public void setpbProgress(int num){
		pb_progress.setProgress((int) (num+0.5f));
	}
	public void setTitle(String text){
		tv_title_progress.setText(text);
	}
	
}
