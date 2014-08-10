package com.tien.ao.volley.imagecache;

import java.util.concurrent.BlockingQueue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Process;

import com.tien.ao.AOApplication;
import com.tien.ao.R;
import com.tien.ao.utils.ImageUtil;
import com.tien.ao.volley.toolbox.ImageLoader.ImageCache;

public class BitmapDispatcher extends Thread {
	/** The queue of requests to service. */
	private final BlockingQueue<Request> mQueue;
	/** The cache to write to. */
	private final ImageCache mCache;
	/** For posting responses and errors. */
	private final ResponseDelivery mDelivery;
	/** Used for telling us to die. */
	private volatile boolean mQuit = false;

	/**
	 * Creates a new network dispatcher thread. You must call {@link #start()}
	 * in order to begin processing.
	 * 
	 * @param queue
	 *            Queue of incoming requests for triage
	 * @param network
	 *            Network interface to use for performing requests
	 * @param cache
	 *            Cache interface to use for writing responses to cache
	 * @param delivery
	 *            Delivery interface to use for posting responses
	 */
	public BitmapDispatcher(BlockingQueue<Request> queue,
			ImageCache cache, ResponseDelivery delivery) {
		mQueue = queue;
		mCache = cache;
		mDelivery = delivery;
	}

	/**
	 * Forces this dispatcher to quit immediately. If any requests are still in
	 * the queue, they are not guaranteed to be processed.
	 */
	public void quit() {
		mQuit = true;
		interrupt();
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		Request request;
		while (true) {
			try {
				// Take a request from the queue.
				request = mQueue.take();
			} catch (InterruptedException e) {
				// We may have been interrupted because it was time to quit.
				if (mQuit) {
					return;
				}
				continue;
			}

			Response<?> response = null;
			//Bitmap bitmap = mCache.getBitmap(request.createKey());
			//if (bitmap == null) {
				Drawable drawable = AOApplication.getInstance().getResources().getDrawable(request.getKey());
				if(drawable != null){
					Bitmap bitmap = ImageUtil.drawableToBitmap(drawable);
					response = new Response(bitmap);

					mDelivery.postResponse(request, response);
				}
//				Bitmap bmp = BitmapFactory.decodeResource(AOApplication.getInstance().getResources(), request.getKey());
//				response = new Response(bmp);
//				mDelivery.postResponse(request, response);
//			}
		}
	}

}