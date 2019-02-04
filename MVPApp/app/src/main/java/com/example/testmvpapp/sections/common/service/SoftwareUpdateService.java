package com.example.testmvpapp.sections.common.service;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;


import com.example.testmvpapp.R;
import com.example.testmvpapp.app.Constant;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.sections.common.entity.CheckUpdateEntity;
import com.example.testmvpapp.util.base.AppUtils;

import java.io.File;


/**
 * 软件更新类 创建时间：2015-05-13
 * 
 * @author pjw
 * 
 */
@SuppressLint("NewApi")
public class SoftwareUpdateService extends Service {
	private DownloadManager mDownloadManager;// 软件更新下载器
	private String savePath;// APK存储目录
	private String saveName;// APK存储名称
	private CheckUpdateEntity entity;
	private MyReceiver mCompleteReceiver;// 最新APK下载完成广播接收器
	private boolean isDownload = false;// 是否在下载
	private long downloadId;// 当前下载Id
	private DownloadsChangeObserver mDownloadObserver;
	private Uri downloadUri = Uri.parse("content://downloads/my_downloads");

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "正在下载中，请稍候！", Toast.LENGTH_LONG).show();
		mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		init();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		start(intent);
	}

	/**
	 * 启动
	 */
	private void start(final Intent intent) {
		if(intent != null){
			entity = (CheckUpdateEntity) intent.getSerializableExtra(Constant.SERIALIZABLE);
			if (entity != null) {
				if (!isDownload) {
					downloadApk(entity);
				} else {
					Toast.makeText(this, "已经在下载正好房" + entity.getData().getVersion(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				isDownload = false;
				Toast.makeText(this, "下载失败，请重新再试！", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		mDownloadObserver = new DownloadsChangeObserver(downloadUri);
		getContentResolver().registerContentObserver(downloadUri, true,
				mDownloadObserver);

		// 创建下载管理器下载完成监听广播
		mCompleteReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(
				DownloadManager.ACTION_NOTIFICATION_CLICKED);
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(mCompleteReceiver, filter);
	}

	/**
	 * 使用系统下载起 下载Apk
	 */
	private void downloadApk(CheckUpdateEntity entity) {
		isDownload = true;
		/*
		String downUrl = Constant.DOWN_UPDATE_URL
				+ "?softType=Android&versionCode="
				+ AppUtils.getVersionCode(this);
		*/

		/* 下载的url */
        final  String downUrl = "";

		DownloadManager.Request download = new DownloadManager.Request(
				Uri.parse(downUrl));
		// 移动数据和wifi网络都可以下载
		download.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);
		// 漫游也可以下载
		download.setAllowedOverRoaming(true);
		// 通知栏提示标题
		download.setTitle(AppUtils.getAppName(this) + entity.getData().getVersion());
		// 通知栏提示的介绍
		download.setDescription(getString(R.string.in_downloading_cb_tunke));
		// 下载完成在通知栏中提示(下载完成关闭通知栏)
		// download.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
		download.setVisibleInDownloadsUi(true);
		download.allowScanningByMediaScanner();
		download.setMimeType("application/vnd.android.package-archive");

		// 设置下载的APK存储路径
		savePath = MyApplication.APP_ROOT_DIRECTORY + File.separator+"download";
		saveName = AppUtils.getAppName(this) +"v"+ entity.getData().getVersion() + ".apk";

		File file = new File(MyApplication.APPPATH+"download");
		if (!file.exists()&&!file.isDirectory()) {// 判断该SD目录是否存在
			file.mkdirs();// 不存在就创建
		}
		download.setDestinationInExternalPublicDir(savePath, saveName);
		downloadId = mDownloadManager.enqueue(download);
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
				// 通知栏被点击
				// notificationClicked();
			} else if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				// 下载完成
				downloadComplete(intent);
			}

		}
	};

	/**
	 * 通知栏被点击
	 */
	private void notificationClicked() {
		// 销毁广播
		unregister();
		mDownloadManager.remove(downloadId);
		// 停止服务
		stopService(new Intent(SoftwareUpdateService.this,
				SoftwareUpdateService.class));
	}

	/**
	 * 下载完成
	 */
	private void downloadComplete(Intent intent) {
		// 销毁广播
		unregister();
		Toast.makeText(this, "最新正好房" + entity.getData().getVersion() + "版本已经下载完毕",
				Toast.LENGTH_LONG).show();
		//Uri downloadFileUri =  mDownloadManager.getUriForDownloadedFile(downloadId);
		File apkFile = queryDownloadedApk(this);
		/** 跳转到安装APK操作 **/
		Intent intent2 = new Intent();
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent2.setAction(Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent2.setDataAndType(Uri.fromFile(apkFile), type);
		startActivity(intent2);

		// 停止服务
		stopService(new Intent(SoftwareUpdateService.this,
				SoftwareUpdateService.class));
	}

	//通过downLoadId查询下载的apk，解决6.0以后安装的问题
	public File queryDownloadedApk(Context context) {
		File targetApkFile = null;
		DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		if (downloadId != -1) {
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(downloadId);
			query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
			Cursor cur = downloader.query(query);
			if (cur != null) {
				if (cur.moveToFirst()) {
					String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
					if (!TextUtils.isEmpty(uriString)) {
						targetApkFile = new File(Uri.parse(uriString).getPath());
					}
				}
				cur.close();
			}
		}
		return targetApkFile;
	}

	/**
	 * 销毁广播监听
	 */
	private void unregister() {
		isDownload = false;
		getContentResolver().unregisterContentObserver(mDownloadObserver);
		if (mCompleteReceiver != null) {
			unregisterReceiver(mCompleteReceiver);
			mCompleteReceiver = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregister();
	}

	private class DownloadsChangeObserver extends ContentObserver {
		public DownloadsChangeObserver(Uri uri) {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
			queryDownloadStatus();
		}
	}

	private void queryDownloadStatus() {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(downloadId);
		Cursor c = mDownloadManager.query(query);
		if (c != null && c.moveToFirst()) {
			int status = c.getInt(c
					.getColumnIndex(DownloadManager.COLUMN_STATUS));

			int titleIdx = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
			int fileSizeIdx = c
					.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
			int bytesDLIdx = c
					.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
			String title = c.getString(titleIdx);
			int fileSize = c.getInt(fileSizeIdx);
			int bytesDL = c.getInt(bytesDLIdx);

			StringBuilder sb = new StringBuilder();
			sb.append(title).append("\n");
			sb.append("Downloaded ").append(bytesDL).append(" / ")
					.append(fileSize);
			switch (status) {
			case DownloadManager.STATUS_PAUSED:
				downloadTermination();
			case DownloadManager.STATUS_PENDING:
			case DownloadManager.STATUS_RUNNING:
				// 正在下载，不做任何事情
				break;
			case DownloadManager.STATUS_SUCCESSFUL:
				// 完成
				break;
			case DownloadManager.STATUS_FAILED:
				// 清除已下载的内容，重新下载
				downloadTermination();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 下载终止
	 */
	private void downloadTermination() {
		Toast.makeText(this, "下载已中断！", Toast.LENGTH_LONG).show();
		mDownloadManager.remove(downloadId);// 删除任务
		unregister();
		// 停止服务
		stopService(new Intent(SoftwareUpdateService.this,
				SoftwareUpdateService.class));
	}
}
