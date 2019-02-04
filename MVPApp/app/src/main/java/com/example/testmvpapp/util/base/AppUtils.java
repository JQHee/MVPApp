package com.example.testmvpapp.util.base;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 跟App相关的辅助类
 * 
 * 
 * 
 */
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本等级]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * [获取手机IMEI串号]
	 * 
	 * @param context
	 * @return 当前手机IMEI串号
	 */
	public static String getImei(Context context) {
		TelephonyManager tmManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tmManager.getDeviceId();
		if (StringUtils.isBlank(imei)) {
			imei = tmManager.getSimSerialNumber();
		}
		if (StringUtils.isBlank(imei)) {
			imei = Build.SERIAL;
		}
		return imei;
	}

	/**
	 * 比较SDK的版本号
	 *
	 * @return
	 */
	public static boolean sdkLessThan19() {
		return Build.VERSION.SDK_INT < 19;
	}

	/**
	 * 获取 当前手机 的手机号码
	 *
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getLine1Number();
	}

	/**
	 * 判断是否安装目标应用
	 *
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	@SuppressLint("SdCardPath")
	public static boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	/**
	 * 发送短信、打电话
	 * "sms:" "tel:"
	 */
	public static void doAction(Context mContext, String action, String phone) {
		Uri uri = Uri.parse(action + phone);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		mContext.startActivity(intent);
	}

	/**
	 * 判断服务是否在后台运行
	 *
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean isServiceRun(Context context, String className) {
		boolean isRun = false;
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = manager.getRunningServices(30);
		if (!(list.size() > 0)) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).service.getClassName().equals(className)) {
				isRun = true;
				break;
			}
		}
		return isRun;
	}

	/**
	 * 打开百度地图
	 */
	@SuppressWarnings("deprecation")
	public static void jumpToBaiduMap(Context mContext, String startLL,
			String startAdd, String endLL, String endAdd, String city) {
		try {

			if (AppUtils.isInstallByread("com.baidu.BaiduMap")) {
				// 调用百度地图应用
				Intent intent = Intent
						.getIntent("intent://map/direction?origin=latlng:"
								+ startLL
								+ "|name:"
								+ startAdd
								+ "&destination=latlng:"
								+ endLL
								+ "|name:"
								+ endAdd
								+ "&mode=driving"
								+ "&region="
								+ city
								+ "&src=nncb@vcb.com|com.cb.cbcrm#"
								+ "Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
				mContext.startActivity(intent); // 启动调用
			} else if (AppUtils.isInstallByread("com.autonavi.minimap")) {
				// 调用高德地图应用
				String[] sll = startLL.split(",");
				String[] ell = endLL.split(",");
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("androidamap://route?sourceApplication="
								+ "cbcrm&slat=" + sll[0] + "&slon=" + sll[1]
								+ "&sname=" + startAdd + "&dlat=" + ell[0]
								+ "&dlon=" + ell[1] + "&dname=" + endAdd
								+ "&dev=0&m=0&t=1"));
				intent.setPackage("com.autonavi.minimap");
				mContext.startActivity(intent);
			} else {
				// ===============================百度网页地图============================
				 Uri uri =
				 Uri.parse("http://api.map.baidu.com/direction?origin=latlng:"
				 + startLL
				 + "|name:"
				 + startAdd
				 + "&destination=latlng:"
				 + endLL
				 + "|name:"
				 + endAdd
				 + "&mode=driving"
				 + "&region="
				 + city
				 + "&output=html&src=nncb@vcb.com|com.cb.tunke#");
				 Intent it = new Intent(Intent.ACTION_VIEW, uri);
				 mContext.startActivity(it);

				// =====================高德地图==========================================
				// Uri uri = Uri.parse("http://m.amap.com/?from=" +
				// startLL +
				// "("+startAdd+")&to=" +
				// endLL +
				// "("+endAdd+")");
				// Intent it = new Intent(Intent.ACTION_VIEW, uri);
				// mContext.startActivity(it);
				// ==========================腾讯地图先屏蔽=====================================
				// Uri uri =
				// Uri.parse("https://apis.map.qq.com/uri/v1/routeplan?type=drive&from="
				// +
				// startAdd+
				// "&fromcoord=" +
				// startLL +
				// "&to=" +
				// endAdd +
				// "&tocoord=" +
				// endLL +
				// "&policy=1&referer=cbcrm");
				// Intent it = new Intent(Intent.ACTION_VIEW, uri);
				// mContext.startActivity(it);

			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 格式化单位
	 *
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			// return size + "Byte";
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	/**
	 * 获取当前栈顶Activity名称
	 * @param context
	 * @return
	 */
	public static String getTopActivity(Context context){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		return info.topActivity.getShortClassName(); //类名
	}

	/**
	 * 判断当前应用是否在前台
	 * true在
	 */
	public static boolean appInFrontDesk(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;

	}

}
