package com.zhenquan.telephonesafe.view;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.utils.ContentValue;
import com.zhenquan.telephonesafe.utils.SpUtils;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

public class LocationToast implements OnTouchListener {

	private Context mContext;
	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	private View view;
	private int startX;
	private int startY;

	public LocationToast(Context context) {
		this.mContext = context;
		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		params = mParams;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		params.setTitle("Toast");
	}

	public void getToast(String location) {
		view = View.inflate(mContext, R.layout.view_location_toast, null);
		TextView textView = (TextView) view.findViewById(R.id.tv_location_view);
		textView.setText(location);
		textView.setTextColor(Color.RED);

		int gettInt = SpUtils.gettInt(mContext, ContentValue.KEY_LOCATION_ID,
				R.drawable.shape_location_nomal);
		view.setBackgroundResource(gettInt);

		view.setOnTouchListener(this);
		windowManager.addView(view, params);
	}

	public void removeToast() {
		if (view != null) {
			if (view.getParent() != null) {
				windowManager.removeView(view);
			}
			view = null;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 获取实时位置，并更新
			int nowX = (int) event.getRawX();
			int nowY = (int) event.getRawY();

			int diffX = nowX - startX;
			int diffY = nowY - startY;

			params.x += diffX;
			params.y += diffY;

			windowManager.updateViewLayout(view, params);
			startX = nowX;
			startY = nowY;
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}
		return true;
	}
}
