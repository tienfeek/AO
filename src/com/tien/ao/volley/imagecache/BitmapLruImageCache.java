/**
 * 
 */
package com.tien.ao.volley.imagecache;


import android.graphics.Bitmap;
import android.util.Log;

import com.tien.ao.volley.toolbox.ImageLoader.ImageCache;

/**
 * Basic LRU Memory cache.
 * 
 * @author Trey Robinson
 *
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache{

    private final String TAG = this.getClass().getSimpleName();

    public BitmapLruImageCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        Log.v(TAG, "Retrieved item from Mem Cache");
        return get(url);
    }
 
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        //XLog.i("wanges", "url:"+url+" "+bitmap);
        if(url == null || bitmap == null) return;
        put(url, bitmap);
    }
}
