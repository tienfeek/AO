package com.tien.ao;

import android.app.Application;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;

import com.tien.ao.utils.XLog;
import com.tien.ao.volley.RequestQueue;
import com.tien.ao.volley.imagecache.ImageCacheManager;
import com.tien.ao.volley.imagecache.ImageCacheManager.CacheType;
import com.tien.ao.volley.toolbox.Volley;

public class AOApplication extends Application {
	
	private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
    private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
    private static int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided
	
	private static AOApplication instance;
	private static Handler handler;
	private static RequestQueue requestQueue;
	
	public void onCreate() {
		super.onCreate();
		
		XLog.setLogLevel(1);
		
		handler = new Handler();
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		
		createImageCache();
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

	
	private void createImageCache(){
        ImageCacheManager.getInstance().init(this,
                this.getPackageCodePath()
                , DISK_IMAGECACHE_SIZE
                , DISK_IMAGECACHE_COMPRESS_FORMAT
                , DISK_IMAGECACHE_QUALITY
                , CacheType.DISK);
    }

}
