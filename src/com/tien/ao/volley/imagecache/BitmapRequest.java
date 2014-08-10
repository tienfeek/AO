package com.tien.ao.volley.imagecache;

import com.tien.ao.utils.XLog;
import com.tien.ao.volley.imagecache.Response.Listener;

import android.graphics.Bitmap;

public class BitmapRequest extends Request<Bitmap> {

	public BitmapRequest(int key, Listener listener) {
		super(key, listener);
	}

	@Override
	protected void deliverResponse(Bitmap response) {
		mListener.onResponse(response);
	}

}
