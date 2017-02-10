package com.zhenquan.telephonesafe.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.activity.SmokeActivity;

public class RocketToast implements OnTouchListener {
	private View mView;
	private WindowManager mWM;
	private WindowManager.LayoutParams mParams;
	private WindowManager.LayoutParams mTipViewParams;
	private Context mContext;
	private int startX;
	private int startY;
	private ImageView tipView;
	private int [] rocketLocation;
	private int [] tipLocation;
	private boolean shouldSend = false;//用来判断当前是否满足发射状态

	public RocketToast(Context context) {
		this.mContext = context;
		mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// LayoutParams : 布局参数
		// 在xml中部分属性是以layout_开头的，当前控件在该属性不能自己做主，需要与父控件进行“商量”才能显示效果
		// 在xml中部分属性是不以layout_开头的，当前控件在该属性自己能够做主，不需要与父控件进行“商量”才能显示效果
		// 在xml中部分属性是不以layout_开头的,能通过控件的set方法来进行设置
		// 在xml中部分属性是以layout_开头的,只能通过布局参数LayoutParams来进行设置
		// 在使用布局参数时，必须使用当前控件的父控件的类型的布局参数！！！不同的容器各自的部分属性是不同，因此，布局参数不能混用

//		LinearLayout.LayoutParams layoutParams = new LayoutParams(6, 6);
//		 layoutParams.leftMargin//等价于xml中layout_marginleft;
//		 layoutParams.setMargins(left, top, right, bottom)
//		TextView tv = new TextView(mContext);
//		 tv.setText(text);
//		 tv.setTextSize(size)
//		 tv.setTextColor(color)
//		tv.setLayoutParams(layoutParams);

		mParams = new WindowManager.LayoutParams();
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.format = PixelFormat.TRANSLUCENT;// 设置窗口以像素级别来显示
		mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;// 设置当前的窗口的类型
		mParams.setTitle("Toast");
		// 设置窗口的一些标记
		mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		//让火箭设置在左上角
		mParams.gravity = Gravity.LEFT|Gravity.TOP;
		
		
		//创建 提示框的布局参数
		mTipViewParams = new WindowManager.LayoutParams();
		mTipViewParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mTipViewParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mTipViewParams.format = PixelFormat.TRANSLUCENT;// 设置窗口以像素级别来显示
		mTipViewParams.type = WindowManager.LayoutParams.TYPE_TOAST;// 设置当前的窗口的类型
		mTipViewParams.setTitle("Toast");
			// 设置窗口的一些标记
		mTipViewParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
			| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		//让提示框设置在底部并水平居中
		mTipViewParams.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
		
		//创建记录火箭与提示框的数组对象
		rocketLocation = new int[2];
		
		tipLocation = new int [2];
	}

	// 显示toast
	// 仿照toast在界面上显示对应内容
	public void showRocketToast() {
		// WindowManager：窗口管理者 ，用来添加、删除、修改窗口操作
		// window:窗口，是android中最顶层元素,就是一个看不见的框，将view添加到window中进行显示（android所有的界面都是基于窗口显示的，activity，dialog，toast）

		mView = View.inflate(mContext, R.layout.view_rocket, null);// 在窗口中需要显示的视图对象

		// 通过帧布局设置火箭喷火
		ImageView iv = (ImageView) mView.findViewById(R.id.iv);
		iv.setBackgroundResource(R.anim.rocket);
		AnimationDrawable background = (AnimationDrawable) iv.getBackground();
		background.start();

		// 给显示到屏幕上的view设置触摸监听
		mView.setOnTouchListener(this);

		mWM.addView(mView, mParams);
	}

	// 隐藏toast
	public void hideRocketToast() {
		if (mView != null) {
			// note: checking parent() just to make sure the view has
			// been added... i have seen cases where we get here when
			// the view isn't yet added, so let's try not to crash.
			if (mView.getParent() != null) {
				mWM.removeView(mView);
			}

			mView = null;
		}
	}

	// 当手指触摸设置了触摸监听的view之后，该方法就会被调用
	/**
	 * v 被触摸的view event 触摸事件
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// System.out.println("onTouch");
		// 触摸事件：一个按下0，n个移动2，一个抬起1
		int action = event.getAction();
//		System.out.println("action:" + action);
		switch (action) {
		case MotionEvent.ACTION_DOWN:// 手指按下
			// 1.记录起始点
			startX = (int) event.getRawX();// 触摸其实是以像素点为单位，不会有小数点，只有整数
			startY = (int) event.getRawY();

			// 显示出提示框
			showTipToast();

			break;
		case MotionEvent.ACTION_MOVE:// 手指移动
			// 2.记录移动后的结束点
			int endX = (int) event.getRawX();
			int endY = (int) event.getRawY();

			// 3.计算出间距
			int diffX = endX - startX;
			int diffY = endY - startY;

			// 4.修改显示mView的窗口的位置
			// 将间距设置给mparams
			mParams.x = (int) (mParams.x + diffX);
			mParams.y += diffY;

			mWM.updateViewLayout(mView, mParams);

			// 5.重新初始化起始点
			startX = endX;
			startY = endY;

			// 设置提示框进行闪烁动画
			startTipAnimation();
			// 实时获取火箭与提示框的位置关系，并进行业务操作
			//获取火箭的位置
			
			mView.getLocationOnScreen(rocketLocation);//赋值函数
			int rocketX = rocketLocation[0];
			int rocketY = rocketLocation[1];
			//获取提示框的位置
			
			tipView.getLocationOnScreen(tipLocation);
			int tipX = tipLocation[0];
			int tipY = tipLocation[1];
			shouldSend = false;
			
			//判断火箭是否进入了提示框范围,
			//如果火箭的左边界大于提示框的左边界，火箭的右边界小于提示框的右边，在x轴才满足条件
			if (rocketY > (tipY - mView.getHeight()/2)&&rocketX > tipX&&(rocketX+mView.getWidth())<(tipX+tipView.getWidth())) {
				System.out.println("xy轴满足条件了");
				//满足条件时，停止闪烁动画，改变背景
				stopTipAnimation();
				
				shouldSend = true;
			}
			

			break;
		case MotionEvent.ACTION_UP:// 手指抬起
			// 提示框隐藏
			hideTipToast();
			// 如果火箭在提示框范围内进行发射操作
			if (shouldSend) {
				sendRocket();
				
				//冒烟动画
				Intent intent = new Intent(mContext,SmokeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
			
			break;

		default:
			break;
		}

		return true;// 由我们自己来处理触摸事件，false，我们不处理由系统来处理
	}

	//发射火箭
	private void sendRocket() {
		//值动画：集没有动，又没有画，只做数据的模拟
		ValueAnimator va = ValueAnimator.ofInt(mParams.y,0);
		va.setDuration(800);
		va.addUpdateListener(new AnimatorUpdateListener() {
			//将当前正在模拟的值，返回回来
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer animatedValue = (Integer) animation.getAnimatedValue();
//				System.out.println(animatedValue);
				//将对应的值获取到并设置给布局参数，动态修改火箭窗口的位置
				mParams.y = animatedValue;
				mWM.updateViewLayout(mView, mParams);
			}
		});
		
		va.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				//动画开始前设置火箭居中
				int center =  mContext.getResources().getDisplayMetrics().widthPixels/2 - mView.getWidth()/2;//获取屏幕的宽度
				mParams.x = center;
				mWM.updateViewLayout(mView, mParams);
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				//动画结束后，设置火箭归位
				mParams.x = 0;
				mWM.updateViewLayout(mView, mParams);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				
			}
		});
		
		
		va.start();
	}

	private void stopTipAnimation() {
		tipView.setBackgroundResource(R.anim.tipview);
		AnimationDrawable background = (AnimationDrawable) tipView.getBackground();
		background.stop();
		tipView.setBackgroundResource(R.drawable.desktop_bg_tips_3);
	}

	//设置提示框进行闪烁
	private void startTipAnimation() {
		tipView.setBackgroundResource(R.anim.tipview);
		AnimationDrawable background = (AnimationDrawable) tipView.getBackground();
		background.start();
	}

	private void hideTipToast() {
		if (tipView != null) {
			// note: checking parent() just to make sure the view has
			// been added... i have seen cases where we get here when
			// the view isn't yet added, so let's try not to crash.
			if (tipView.getParent() != null) {
				mWM.removeView(tipView);
			}

			tipView = null;
		}
	}

	// 显示提示框
	private void showTipToast() {
		tipView = new ImageView(mContext);
		tipView.setBackgroundResource(R.drawable.desktop_bg_tips_1);

		mWM.addView(tipView, mTipViewParams);
	}

}
