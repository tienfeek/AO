package com.tien.ao.volley.imagecache;

import com.tien.ao.volley.RequestQueue;
import com.tien.ao.volley.imagecache.Response.Listener;



public abstract class Request<T> implements Comparable<Request<T>>  {
	
	
	private int key ;
	private Integer mSequence;
	
	protected final Listener<T> mListener;
	
	
	public Request(int key, Response.Listener listener) {
		this.key = key;
		this.mListener = listener;
	}
	
	/**
     * Priority values.  Requests will be processed from higher priorities to
     * lower priorities, in FIFO order.
     */
    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    /**
     * Returns the {@link Priority} of this request; {@link Priority#NORMAL} by default.
     */
    public Priority getPriority() {
        return Priority.NORMAL;
    }
    
    /**
     * Sets the sequence number of this request.  Used by {@link RequestQueue}.
     */
    public final void setSequence(int sequence) {
        mSequence = sequence;
    }

    /**
     * Returns the sequence number of this request.
     */
    public final int getSequence() {
        if (mSequence == null) {
            throw new IllegalStateException("getSequence called before setSequence");
        }
        return mSequence;
    }
    
    public int getKey(){
    	return key;
    } 
    
    public String createKey(){
		return "system_bitmap"+key;
	}


	@Override
	public int compareTo(Request<T> other) {
	
	 //Priority left = this.getPriority();
     //Priority right = other.getPriority();

     // High-priority requests are "lesser" so they are sorted to the front.
     // Equal priorities are sorted by sequence number to provide FIFO ordering.
//     return left == right ?
//             this.mSequence - other.mSequence :
//             right.ordinal() - left.ordinal();
     return 0;
	}
	
	
    abstract protected void deliverResponse(T response) ;

}
