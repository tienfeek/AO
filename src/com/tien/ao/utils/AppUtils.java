package com.tien.ao.utils;

import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.TypedValue;


public class AppUtils {

	public static String getMyUUID(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId;

	}
	
	
	public static void startAppByPackageName(Context context, String packageName) throws Exception {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}
	
	public static boolean isAppOnForeground(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null) return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}
	
	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
		if (runningTaskInfos != null){
			return (runningTaskInfos.get(0).topActivity).getClassName();
		}
		return null;
	}
	
	public static int getPixels(int dpValue,Context context) {
		Resources r = context.getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpValue, r.getDisplayMetrics());
		return px;
	}
}
