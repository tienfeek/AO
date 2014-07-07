package com.tien.ao;

import com.tien.ao.utils.XLog;
import com.tien.ao.volley.RequestQueue;
import com.tien.ao.volley.toolbox.Volley;

import android.app.Application;
import android.os.Handler;

public class AOApplication extends Application {
	
	private static AOApplication instance;
	private static Handler handler;
	private static RequestQueue requestQueue;
	
	public void onCreate() {
		super.onCreate();
		
		XLog.setLogLevel(1);
		
		handler = new Handler();
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		
	}
	
	public static Handler getHandler(){
		return handler;
	}
	
	public static RequestQueue getRequestQueue(){
		return requestQueue;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	

	public AOApplication() {
		instance = this;
	}

	public static AOApplication getInstance() {
		return instance;
	}


}
