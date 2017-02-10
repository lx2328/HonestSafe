package com.zhenquan.telephonesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.adapter.AntvirtusAdapter;
import com.zhenquan.telephonesafe.bean.AntivirusBean;
import com.zhenquan.telephonesafe.bean.AppInfoBean;
import com.zhenquan.telephonesafe.dao.AntiVirtusDao;
import com.zhenquan.telephonesafe.engine.AppInfoProvider;
import com.zhenquan.telephonesafe.utils.MD5Utils;

public class AntvirtusActivity extends Activity {
	private List<AntivirusBean> datas = new ArrayList<AntivirusBean>();;
	private ListView lv_shadu;
	private ArcProgress arc_progress;
	private TextView tv_antivirus_scan;
	private TextView tv_antivirus_result;
	private LinearLayout ll_antivirtus_result;
	private LinearLayout ll_antivirtus_scan;
	private LinearLayout ll_opendoor_anim;
	private ImageView iv_animaLeft;
	private ImageView iv_animaRight;
	private Button btn_antivirus_result;
	private String part;
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			part = intent.getData().getSchemeSpecificPart();
			
		}
	};
	private AntvirtusAdapter adapter;
	private List<AppInfoBean> allAppInfos;
	private int max;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println(resultCode);
		if(requestCode==100){
			
			if(adapter!=null){
				PackageManager manager = getPackageManager();
//				initData();
				
				//第一种方式  获取到包名 然后单独判断这个是否已经被删除了  datas.remove()  notifydaatee
				/*try {
					manager.getApplicationInfo(adapter.getAntvirtus().appPackageName, PackageManager.GET_ACTIVITIES);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
					datas.remove(adapter.getAntvirtus());
					adapter.notifyDataSetChanged();
				}*/
				
				//第二种方式通过广播接受者
				if (TextUtils.equals(part, adapter.getAntvirtus().appPackageName)) {
					datas.remove(adapter.getAntvirtus());
					adapter.notifyDataSetChanged();
				}
				
//				String packageName = manager.getInstallerPackageName(adapter.getAntvirtus().appPackageName);
//				if(TextUtils.isEmpty(packageName)) {
//					datas.remove(adapter.getAntvirtus());
//					adapter.notifyDataSetChanged();
//				}
				
				//获取到包名 然后单独判断这个是否已经被删除了  datas.remove()  notifydaatee
//				adapter.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antvirtus);

		initUI();
		initData();
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver , filter);
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	private void initData() {
		AntivirusTask antivirusTask = new AntivirusTask();
		antivirusTask.execute();

	}

	private void initUI() {
		lv_shadu = (ListView) findViewById(R.id.lv_shadu);
		arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
		
		//扫描页面
		tv_antivirus_scan = (TextView) findViewById(R.id.tv_antivirus_scan);
		ll_antivirtus_scan = (LinearLayout) findViewById(R.id.ll_antivirtus_scan);
		
		//扫描结果页面
		tv_antivirus_result = (TextView) findViewById(R.id.tv_antivirus_result);
		ll_antivirtus_result = (LinearLayout) findViewById(R.id.ll_antivirtus_result);
		btn_antivirus_result = (Button) findViewById(R.id.btn_antivirus_result);
		//开门动画
		ll_opendoor_anim = (LinearLayout) findViewById(R.id.ll_opendoor_anim);
		iv_animaLeft = (ImageView) findViewById(R.id.iv_animaLeft);
		iv_animaRight = (ImageView) findViewById(R.id.iv_animaRight);
	}

	class AntivirusTask extends AsyncTask<Void, AntivirusBean, Void> {

		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (adapter==null) {
				adapter = new AntvirtusAdapter(AntvirtusActivity.this, datas);
				lv_shadu.setAdapter(adapter);
			}
			datas.clear();
			
		}

		@Override
		protected Void doInBackground(Void... params) {

			allAppInfos = AppInfoProvider
					.getAllAppInfos(getApplicationContext());
			max = allAppInfos.size();
			for (AppInfoBean appInfoBean : allAppInfos) {
				String appDir = appInfoBean.appDir;
				String fileMd5 = MD5Utils.getFileMd5(appDir);

				boolean queryAntivirtus = AntiVirtusDao.queryAntivirtus(
						getApplicationContext(), fileMd5);
				AntivirusBean antivirusBean = new AntivirusBean();
				antivirusBean.appIcon = appInfoBean.appIcon;
				antivirusBean.appName = appInfoBean.appName;
				antivirusBean.isAntiVirus = queryAntivirtus;
				antivirusBean.appPackageName = appInfoBean.appPackageName;
				publishProgress(antivirusBean);
				SystemClock.sleep(50);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(AntivirusBean... values) {
			super.onProgressUpdate(values);
			AntivirusBean antivirusBean = values[0];
			if (antivirusBean.isAntiVirus) {
				datas.add(0, antivirusBean);
			} else {
				datas.add(antivirusBean);
			}

			adapter.notifyDataSetChanged();

			lv_shadu.smoothScrollToPosition(datas.size() - 1);

			arc_progress.setProgress((int) (datas.size() * 100f / max + 0.5f));
			tv_antivirus_scan.setText(antivirusBean.appName);

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// 子线程执行完毕后（即扫描完毕），显示结果
			AntivirusBean antivirusBean = datas.get(0);
			if (antivirusBean.isAntiVirus) {
				tv_antivirus_result.setText("您的手机很危险，请及时清理病毒！");
			} else {
				tv_antivirus_result.setText("您的手机很安全，请继续保持！");
			}
			ll_antivirtus_result.setVisibility(View.VISIBLE);
			
			
			lv_shadu.smoothScrollToPosition(0);
			
			//开门动画
			ll_opendoor_anim.setVisibility(View.VISIBLE);
			ll_antivirtus_scan.setDrawingCacheEnabled(true);
			Bitmap drawingCache = ll_antivirtus_scan.getDrawingCache();
			//iv_animaLeft.setImageBitmap(drawingCache);
			Bitmap leftDrawingCache = getLeftDrawingCache(drawingCache);
			Bitmap rightDrawingCache = getRightDrawingCache(drawingCache);
			iv_animaLeft.setImageBitmap(leftDrawingCache);
			iv_animaRight.setImageBitmap(rightDrawingCache);
			
			//执行开门动画之前先让我们的扫描页面隐藏
			ll_antivirtus_scan.setVisibility(View.INVISIBLE);
			
			openDoorAnima();
			
			btn_antivirus_result.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//关门动画
					stopDoorAnima();
					
				}

				
			});
		}
		
		/**
		 * 关门动画集合
		 */
		private void stopDoorAnima() {
			ObjectAnimator oa1 = ObjectAnimator.ofFloat(iv_animaLeft, "translationX", -iv_animaLeft.getWidth(),0);	
			ObjectAnimator oa2 = ObjectAnimator.ofFloat(iv_animaLeft, "alpha", 0.0f,1.0f);
			ObjectAnimator oa3 = ObjectAnimator.ofFloat(iv_animaRight, "translationX",iv_animaRight.getWidth(),0);	
			ObjectAnimator oa4 = ObjectAnimator.ofFloat(iv_animaRight, "alpha", 0.0f,1.0f);
			ObjectAnimator oa5 = ObjectAnimator.ofFloat(ll_antivirtus_result, "alpha", 1.0f,0.0f);
			AnimatorSet animationSet = new AnimatorSet();
			animationSet.setDuration(1000);
			animationSet.playTogether(oa1,oa2,oa3,oa4,oa5);
			animationSet.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					//隐藏掉动画页面
					ll_opendoor_anim.setVisibility(View.INVISIBLE);
					//显示出扫描页面
					ll_antivirtus_scan.setVisibility(View.VISIBLE);
					//清空数据
					datas.clear();
					//重新进入扫描
					initData();
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub
					
				}
			});
			animationSet.start();
		}
		
	
		/**
		 * 开门动画集合
		 */
		private void openDoorAnima() {
//			iv_animaLeft.setTranslationX(translationX)
//			iv_animaLeft.setAlpha(alpha)
			ObjectAnimator oa1 = ObjectAnimator.ofFloat(iv_animaLeft, "translationX", 0,-iv_animaLeft.getWidth());	
			ObjectAnimator oa2 = ObjectAnimator.ofFloat(iv_animaLeft, "alpha", 1.0f,0.0f);
			ObjectAnimator oa3 = ObjectAnimator.ofFloat(iv_animaRight, "translationX",0,iv_animaRight.getWidth());	
			ObjectAnimator oa4 = ObjectAnimator.ofFloat(iv_animaRight, "alpha", 1.0f,0.0f);
			ObjectAnimator oa5 = ObjectAnimator.ofFloat(ll_antivirtus_result, "alpha", 0.0f,1.0f);
			AnimatorSet animationSet = new AnimatorSet();
			animationSet.setDuration(1500);
			animationSet.playTogether(oa1,oa2,oa3,oa4,oa5);
			animationSet.start();
		}

		/**获取右半边图片，一个宽度为原图一半，高度相同的图片对象
		 * @param drawingCache
		 * @return
		 */
		private Bitmap getRightDrawingCache(Bitmap drawingCache) {
			int width = drawingCache.getWidth()/2;
			int height = drawingCache.getHeight();
			Config config = drawingCache.getConfig();
			Bitmap createBitmap = Bitmap.createBitmap(width, height, config);
			Canvas canvas = new Canvas(createBitmap);
			Matrix matrix = new Matrix();
			matrix.postTranslate(-width, 0);
			canvas.drawBitmap(drawingCache, matrix, null);
			return createBitmap;
		}
		
		/**
		 * 获取左半边图片，一个宽度为原图一半，高度相同的图片对象
		 * @param drawingCache 
		 * @return 
		 */
		private Bitmap getLeftDrawingCache(Bitmap drawingCache) {
			int width = drawingCache.getWidth()/2;
			int height = drawingCache.getHeight();
			Config config = drawingCache.getConfig();
			Bitmap createBitmap = Bitmap.createBitmap(width, height, config);
			Canvas canvas = new Canvas(createBitmap);
			Matrix matrix = new Matrix();
			canvas.drawBitmap(drawingCache, matrix, null);
			return createBitmap;
		}
		
		

	}

}
