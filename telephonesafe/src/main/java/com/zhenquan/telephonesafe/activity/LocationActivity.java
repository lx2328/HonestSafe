package com.zhenquan.telephonesafe.activity;

import java.io.File;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.R.layout;
import com.zhenquan.telephonesafe.dao.LocationDao;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LocationActivity extends Activity implements OnClickListener {

	private EditText et_location_number;
	private String number;
	private TextView tv_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		initUI();
		
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		et_location_number.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String queryLocation = LocationDao.queryLocation(getApplicationContext(), s.toString());
				tv_location.setText(queryLocation);
			}
		});
	}

	private void initUI() {
		// TODO Auto-generated method stub
		Button btn_selector = (Button) findViewById(R.id.btn_selector);
		et_location_number = (EditText) findViewById(R.id.et_location_number);
		tv_location = (TextView) findViewById(R.id.tv_location);
		btn_selector.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_selector:
			System.out.println("btn_selector pressed");
			number = et_location_number.getText().toString().trim();
			if (TextUtils.isEmpty(number)) {
				//来个抖动的动画  
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		        findViewById(R.id.et_location_number).startAnimation(shake);
			}
			
			//匹配数据库中的条目
			String queryLocation = LocationDao.queryLocation(getApplicationContext(), number);
			tv_location.setText(queryLocation);
			break;

		default:
			break;
		}
	}

}
