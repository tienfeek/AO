package com.tien.ao.utils;


import com.tien.ao.AOApplication;

import android.content.Context;
import android.widget.Toast;


/**
 * @Description:
 * @author:wangtf
 * @see:   
 * @since:      
 * @copyright © baidu.com
 * @Date:2014-4-15
 */
public class ToastUtil {

	public static void shortToast(int resId){
		Context context = AOApplication.getInstance().getApplicationContext();
		Toast.makeText(context, context.getText(resId), Toast.LENGTH_SHORT).show();
	}
	
	public static void shortToast(String toast){
		Context context = AOApplication.getInstance().getApplicationContext();
		Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
	}
	
	public static void longToast(int resId){
		Context context = AOApplication.getInstance().getApplicationContext();
		Toast.makeText(context, context.getText(resId), Toast.LENGTH_LONG).show();
	}
	
	public static void longToast(String toast){
		Context context = AOApplication.getInstance().getApplicationContext();
		Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
	}
	
	public static void specifyTimeToast(int resId, int time){
		Context context = AOApplication.getInstance().getApplicationContext();
		Toast.makeText(context, context.getText(resId), time).show();
	}
	
	public static void specifyTimeToast(String toast, int time){
		Context context = AOApplication.getInstance().getApplicationContext();
		Toast.makeText(context, toast, time).show();
	}
}
