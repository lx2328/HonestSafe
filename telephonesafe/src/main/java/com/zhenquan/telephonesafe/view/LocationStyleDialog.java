package com.zhenquan.telephonesafe.view;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.adapter.DialogAdapter;
import com.zhenquan.telephonesafe.utils.ContentValue;
import com.zhenquan.telephonesafe.utils.SpUtils;

import android.app.Dialog;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LocationStyleDialog extends Dialog {
	private String[] mTitles = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
	private int[] mColors = new int[] { R.drawable.shape_location_nomal,
			R.drawable.shape_location_orange, R.drawable.shape_location_blue,
			R.drawable.shape_location_gery, R.drawable.shape_location_green};
	private Context mContext;
	private DialogAdapter dialogAdapter;

	public LocationStyleDialog(Context context) {
		super(context,R.style.style_location_style);
		this.mContext = context;
		//对话框加载之前，先让其窗口布局在下面
		Window window = getWindow();
		LayoutParams attributes = window.getAttributes();
		attributes.gravity=Gravity.BOTTOM;
		window.setAttributes(attributes);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_location);
		initUI();
	}

	private void initUI() {
		TextView textView = (TextView) findViewById(R.id.tv_location_dialog);
		ListView listView = (ListView) findViewById(R.id.lv_location_dialog);
		dialogAdapter = new DialogAdapter(mContext, mTitles, mColors);
		listView.setAdapter(dialogAdapter);
		//
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SpUtils.putInt(mContext, ContentValue.KEY_LOCATION_ID, mColors[position]);
				dialogAdapter.notifyDataSetChanged();
			}
		});
	}
}
