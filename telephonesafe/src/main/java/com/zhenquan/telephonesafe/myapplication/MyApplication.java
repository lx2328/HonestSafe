package com.zhenquan.telephonesafe.myapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("tag", "MyApplication is running ");
		Thread.setDefaultUncaughtExceptionHandler(new MyUnHandler());
	}
	private class MyUnHandler implements UncaughtExceptionHandler{

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			//3.下一次打开应用，后台做一个上传文件给服务器
			
			//1.收集错误
			File file = new File(Environment.getExternalStorageDirectory(), "error.log");
			PrintStream err;
			try {
				err = new PrintStream(file);
				ex.printStackTrace(err);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//1.杀死自己的进程
			Process.killProcess(Process.myPid());
			
		}
		
	}
}
