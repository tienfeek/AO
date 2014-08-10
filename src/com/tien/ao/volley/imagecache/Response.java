package com.tien.ao.volley.imagecache;

import com.tien.ao.volley.VolleyError;

public class Response<T>{

	 /** Callback interface for delivering parsed responses. */
    public interface Listener<T> {
        /** Called when a response is received. */
        public void onResponse(T response);
    }

    /** Callback interface for delivering error responses. */
    public interface ErrorListener {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        public void onErrorResponse(VolleyError error);
    }
    
    public final T result;
    
    
    public Response(T result) {
        this.result = result;
    }

}
