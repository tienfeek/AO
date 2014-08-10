package com.tien.ao.volley.imagecache;

import com.tien.ao.volley.VolleyError;

public interface ResponseDelivery {
	
	public void postResponse(Request<?> request, Response<?> response);
	
	public void postResponse(Request<?> request, Response<?> resposne, Runnable runnable);
	
	public void postError(Request<?> request, VolleyError error);

}
