package com.tien.ao.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tien.ao.AOApplication;

/**
 * @Description:
 * @author:wangtf
 * @see:
 * @since:
 * @copyright © baidu.com
 * @Date:2014-4-24
 */
public class NetworkUtil {

	public static Map<String, String> initRequestParams() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("strIMEI", AppUtils.getMyUUID(AOApplication.getInstance().getApplicationContext()));
		params.put("mod", "default");
		params.put("ctrl", "secret");

		return params;
	}

	public static boolean isNetworkConnected() {
		Context context = AOApplication.getInstance().getApplicationContext();
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

}
