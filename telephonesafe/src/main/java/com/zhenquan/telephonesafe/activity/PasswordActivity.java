package com.zhenquan.telephonesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Contacts;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.R.id;
import com.zhenquan.telephonesafe.R.layout;
import com.zhenquan.telephonesafe.utils.ContentValue;

public class PasswordActivity extends Activity {

	private ImageView iv;
	private TextView tv;
	private EditText et;
	private Button btn;
	private String packageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);

		initUI();
		initData();
	}

	private void initUI() {
		iv = (ImageView) findViewById(R.id.iv);
		tv = (TextView) findViewById(R.id.tv);
		et = (EditText) findViewById(R.id.et);
		btn = (Button) findViewById(R.id.btn);

		 
	}

	private void initData() {
		packageName = getIntent().getStringExtra(
				ContentValue.KEY_DOGPACKAGE);
		PackageManager packageManager = getPackageManager();
		try {
			ApplicationInfo applicationInfo = packageManager
					.getApplicationInfo(packageName, 0);
			Drawable drawable = applicationInfo.loadIcon(packageManager);
			CharSequence label = applicationInfo.loadLabel(packageManager);
			tv.setText(label);
			iv.setImageDrawable(drawable);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.equals("123", et.getText().toString().trim())) {
					// 进入应用。  发送隐式广播进入程序
					Intent intent = new Intent();
					intent.putExtra(ContentValue.KEY_DOGPACKAGE, packageName);
					intent.setAction(ContentValue.ACTION_SKIP);
					sendBroadcast(intent);
					finish();
				} else if (TextUtils.isEmpty(tv.getText().toString().trim())) {
					Toast.makeText(PasswordActivity.this, "密码不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(PasswordActivity.this, "密码错误！请重新输入",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	//点击返回键出现HOME键的功能（回到桌面）
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		//{act=android.intent.action.MAIN cat=[android.intent.category.HOME] 
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		startActivity(intent);
	}
	//如果设置启动模式为singleInstance，那么当他重新打开时，他的intent就不是最新的数据
	//需要通过onNewIntent传递最新的intent
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
		initData();
		
	}
}
