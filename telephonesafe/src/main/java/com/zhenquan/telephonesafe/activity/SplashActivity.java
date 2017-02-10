package com.zhenquan.telephonesafe.activity;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.bean.UpDate;
import com.zhenquan.telephonesafe.utils.ContentValue;
import com.zhenquan.telephonesafe.utils.GzipUtils;
import com.zhenquan.telephonesafe.utils.HttpUtils;
import com.zhenquan.telephonesafe.utils.PackageUtil;
import com.zhenquan.telephonesafe.utils.SpUtils;

public class SplashActivity extends Activity {
	private TextView tv_versionName;
	private String LocalVersionName;
	private int localVersionCode;
	private UpDate upDate;
	private File mInstallFile;
	private ProgressDialog progressDialog;
	private File file;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
		};
	};


		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		initUI();
		
		unZip();
		getDbFromAssets("antivirus.db");
		getDbFromAssets("commonnum.db");
		initDate();
	}

	private void initDate() {
		LocalVersionName = PackageUtil.getVersionName(this,
				"com.zhenquan.telephonesafe");
		tv_versionName.setText(LocalVersionName);
		localVersionCode = PackageUtil.getVersionCode(this,
				"com.zhenquan.telephonesafe");
		boolean b = SpUtils.gettBoolean(getApplicationContext(), ContentValue.SETTING_UPDATE, true);
		if (b) {
			//自动更新
			autoUpdate();
		}else{
			//在splash界面中将压缩包解压到data/data/包名/files/xxx.db
			unZip();
			getDbFromAssets("antivirus.db");
			getDbFromAssets("commonnum.db");
			// 如果没有更新则延迟两秒进入主界面
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					enterHome();
					finish();
				}
			}, 2000);
		}
			

	}

	private void getDbFromAssets(String string){
		file = new File(getFilesDir(), string);
		if (file.exists()) {
			return;
		}
		AssetManager assets = getAssets();
		InputStream inputStream = null ;
		FileOutputStream fos = null ;
		try {
			inputStream = assets.open(string);
			
			 fos = new FileOutputStream(file);
			int len;
			byte[] buffer = new byte[1024];
			while ((len = inputStream.read(buffer))!=-1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			closeIO(inputStream,fos);
		}
	}
	
	private void unZip() {
		File file = new File(getFilesDir(), "address.db");
		if (file.exists()) {
			return;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				AssetManager am =getAssets();
				InputStream inputStream;
				try {
					inputStream = am.open("address.zip");
					File targetFile = new File(getFilesDir(), "address.db");
					GzipUtils.unZipByStream(inputStream, targetFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	private void autoUpdate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String path = "http://10.0.2.2:8080/update.json";
				Response response = null;
				try {
					response = HttpUtils.getResponse(path);
					String json = response.body().string();
					try {
						JSONObject jsonObject = new JSONObject(json);
						int serverVersionCode = jsonObject
								.getInt("versionCode");
						String serverVersionName = jsonObject
								.getString("versionName");
						String serverVersionDes = jsonObject
								.getString("versionDes");
						String serverDownloadUrl = jsonObject
								.getString("downloadUrl");
						upDate = new UpDate();
						upDate.downloadUrl = serverDownloadUrl;
						upDate.versionCode = serverVersionCode;
						upDate.versionDes = serverVersionDes;
						upDate.versionName = serverVersionName;

						if (serverVersionCode > localVersionCode) {
							showDialog();
						} else {
							// 如果没有更新则延迟两秒进入主界面
							mHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									enterHome();
									finish();
								}
							}, 2000);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						enterHome();
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
					enterHome();
					finish();
				}

			}
		}).start();
	}

	/**
	 * 弹出更新提示对话框
	 */
	protected void showDialog() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				AlertDialog.Builder builder = new Builder(SplashActivity.this);
				builder.setTitle("亲，现在有更新哟~");
				builder.setMessage(upDate.versionDes);
				builder.setPositiveButton("立即更新", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						progressDialog = new ProgressDialog(SplashActivity.this);
						progressDialog.setCancelable(false);
						progressDialog.setCanceledOnTouchOutside(false);
						progressDialog
								.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						progressDialog.show();
						// 下载
						new Thread(new Runnable() {
							@Override
							public void run() {
								downloadAPK();
								// 弹出下载成功的提示
								mHandler.sendEmptyMessage(1);
								// 进入的安装界面
								installAPK();
							}
						}).start();

					}

				});
				builder.setNegativeButton("下次再说", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 进入主界面
						enterHome();
						finish();
					}

				});
				AlertDialog dialog = builder.create();
				dialog.setCanceledOnTouchOutside(false);
				builder.show();
			}
		});

	}

	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	private void installAPK() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setDataAndType(Uri.fromFile(mInstallFile),
				"application/vnd.android.package-archive");
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Log.d("tag", "启动安装界面成功");
			} else {
				Log.d("tag", "安装失败");
				enterHome();
				finish();
			}
			break;

		default:
			break;
		}
	}

	private void downloadAPK() {
		Response response = HttpUtils.getResponse(upDate.downloadUrl);
		InputStream inputStream = response.body().byteStream();

		long contentLength = response.body().contentLength();

		progressDialog.setMax((int) contentLength);

		mInstallFile = new File(Environment.getExternalStorageDirectory(),
				"telephonesafe.apk");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(mInstallFile);
			int len;
			int progress = 0;
			byte[] b = new byte[1024];
			while ((len = inputStream.read(b)) != -1) {
				fos.write(b, 0, len);
				progress += len;
				SystemClock.sleep(5);
				progressDialog.setProgress(progress);
			}
			fos.flush();
			progressDialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
			enterHome();
			finish();
		} finally {
			// 可变参数关流
			closeIO(inputStream, fos);
		}

	}

	/**
	 * 关闭流的操作
	 * 
	 */
	private void closeIO(Closeable... io){
		if (io != null) {
			for (Closeable closeable : io) {
				if (closeable != null) {
					try {
						closeable.close();
					} catch (IOException e) {
						e.printStackTrace();
						enterHome();
					}
				}
			}
		}
	}
	
	private void initUI() {
		tv_versionName = (TextView) findViewById(R.id.tv_versionName);
	}
}
