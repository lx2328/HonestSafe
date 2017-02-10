package com.zhenquan.telephonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.zhenquan.telephonesafe.R;

public class SmokeActivity extends Activity {

	private ImageView iv_bottom;
	private ImageView iv_top;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smoke);
		initView();
	}

	private void initView() {
		iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
		iv_top = (ImageView) findViewById(R.id.iv_top);
		
		//延迟200ms进行烟的透明度动画
		final AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setAnimationListener(new AnimationListener() {
			//当动画开始 前
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			//当动画重复时
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			//当动画结束后
			@Override
			public void onAnimationEnd(Animation animation) {
				finish();
			}
		});
		aa.setDuration(600);
		
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				iv_bottom.setVisibility(View.VISIBLE);
				iv_top.setVisibility(View.VISIBLE);
				iv_bottom.startAnimation(aa);
				iv_top.startAnimation(aa);
				
			}
		}, 200);
		
		
	}
}
