package com.tien.ao.notification;

/**
 * 
 * @Description:
 * @author:wangtf
 * @see:   
 * @since:      
 * @copyright © baidu.com
 * @Date:2014-4-24
 */
public class NotificationItem {
	
	public static final int TYPE_SEND_SERCET_SUCCESS = 100;										//发送秘密成功


	private int type;
	private Object message;
	
	public NotificationItem() {}
	
	public NotificationItem(int type, Object message) {
		this.type = type;
		this.message = message;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	
	
}
