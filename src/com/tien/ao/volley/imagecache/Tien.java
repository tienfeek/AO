package com.tien.ao.volley.imagecache;

import android.content.Context;

import com.tien.ao.volley.toolbox.ImageLoader.ImageCache;

public class Tien {
	
	private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 3;
	
	public static BitmapQuene newBitmapQuene(Context context, ImageCache cache){
		BitmapQuene quene = new BitmapQuene(cache, DEFAULT_NETWORK_THREAD_POOL_SIZE);
		quene.start();
		
		return quene;
	}

}
