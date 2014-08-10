package com.tien.ao.volley.imagecache;

import java.util.concurrent.PriorityBlockingQueue;

import android.os.Handler;
import android.os.Looper;

import com.tien.ao.volley.toolbox.ImageLoader.ImageCache;

@SuppressWarnings("rawtypes")
public class BitmapQuene {

	private final PriorityBlockingQueue<Request> mCacheQueue = new PriorityBlockingQueue<Request>();

	private BitmapDispatcher[] mDispatchers;
	private ResponseDelivery mDelivery;
	private ImageCache mCache;

	public BitmapQuene(ImageCache cache, int threadPoolSize) {
		this.mCache = cache;
		this.mDispatchers = new BitmapDispatcher[threadPoolSize];
		this.mDelivery = new ExecutorDelivery(new Handler(
				Looper.getMainLooper()));
	}

	public void start() {
		stop();

		for (int i = 0; i < mDispatchers.length; i++) {
			BitmapDispatcher networkDispatcher = new BitmapDispatcher(
					mCacheQueue, mCache, mDelivery);
			mDispatchers[i] = networkDispatcher;
			networkDispatcher.start();
		}
	}

	/**
	 * Stops the cache and network dispatchers.
	 */
	public void stop() {
		for (int i = 0; i < mDispatchers.length; i++) {
			if (mDispatchers[i] != null) {
				mDispatchers[i].quit();
			}
		}
	}

	public Request add(Request request){
		 mCacheQueue.add(request);
		 
		 return request;
	}
}
