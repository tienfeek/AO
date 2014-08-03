package com.tien.ao.utils;

import android.text.format.DateFormat;

public class StringUtil {
	
	public static CharSequence formatTime(String pattern, long time){
		
		if(time - System.currentTimeMillis() > 24*60*60*1000){
			return "一天前";
		}else if(time - System.currentTimeMillis() > 7*24*60*60*1000){
			return "一周前";
		}
		
		CharSequence formatTime = DateFormat.format(pattern, time);
		
		return formatTime;
	}
	
	public static CharSequence formatTime(long time){
		
		time = System.currentTimeMillis()/1000 - time;
		if(time < 2*60){
			return "1分钟前";
		}else if(time > 2*60 && time <= 3*60){
			return "2分钟前";
		}else if(time > 3*60 && time <= 4*60){
			return "3分钟前";
		}else if(time > 4*60 && time <= 5*60){
			return "4分钟前";
		}else if(time > 5*60 && time <= 10*60){
			return "5分钟前";
		}else if(time > 10*60 && time <= 30*60){
			return "10分钟前";
		}else if(time > 30*60 && time <= 60*60) {
			return "30分钟前";
		}else if(time > 60*60 && time <= 12*60*60){
			return "1小时前";
		}else if(time > 12*60*60 && time <= 24*60*60){
			return "12小时前";
		}else if(time > 24*60*60 && time <= 2*24*60*60){
			return "1天前";
		}else if(time > 2*24*60*60 && time <= 3*24*60*60){
			return "2天前";
		}else if(time > 3*24*60*60 && time <= 3*24*60*60){
			return "3天前";
		}else if(time > 4*24*60*60 && time <= 5*24*60*60){
			return "4天前";
		}else if(time > 5*24*60*60 && time <= 10*24*60*60){
			return "5天前";
		}else if(time > 10*24*60*60 && time <= 15*24*60*60){
			return "10天前";
		}else if(time > 15*24*60*60 && time <= 30*24*60*60){
			return "半个月前";
		}else if(time > 30*24*60*60 && time <= 2*30*24*60*60){
			return "1个月前";
		}else if(time > 2*30*24*60*60 && time <= 3*30*24*60*60){
			return "2个月前";
		}else if(time > 3*30*24*60*60 && time <= 6*30*24*60*60){
			return "3个月前";
		}else if(time > 6*30*24*60*60 && time <= 12*30*24*60*60){
			return "半年前";
		}else if(time > 12*30*24*60*60){
			return "史前一万年前";
		}
		return "史前一万年前";
	}

}
