package com.zhenquan.telephonesafe.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
	public static Response getResponse(String path) {
		// 01. 定义okhttp
		OkHttpClient okHttpClient_get = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).build();
		//OkHttpClient okHttpClient_get = new OkHttpClient();
		//OkHttpClient okHttpClient_get = new OkHttpClient().Builder().conn...
		// 02.请求体
		Request request = new Request.Builder().get()// get请求方式
				.url(path)// 网址
				.build();
		// 03.执行okhttp
		Response response = null;
		try {
			response = okHttpClient_get.newCall(request).execute();
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
}
