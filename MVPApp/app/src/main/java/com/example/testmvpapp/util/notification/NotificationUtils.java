package com.example.testmvpapp.util.notification;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author HJQ
 * @fileName NotificationUtils
 * @createDate 2019/2/19 11:38
 * @desc 通知工具 适配 8.0 增加渠道的问题
 */
public class NotificationUtils {

    /**
     * 聊天消息渠道
     */
    public static final String  CHANNEL_ID_CHAT = "chat";
    /**
     * 咨询消息渠道
     */
    public static final String  CHANNEL_ID_NEWS = "news";
    /**
     * 报警消息渠道
     */
    public static final String  CHANNEL_ID_WARNING = "warning";

    /**
     * chat通知id
     */
    public static final int NOTIFICATION_ID_CHAT = 1;
    /**
     * 报警通知id
     */
    public static final int NOTIFICATION_ID_WARNING = 2;
    /**
     * 咨询通知id
     */
    public static final int NOTIFICATION_ID_NEWS = 3;

    /**
     * 初始化通知渠道
     */
    public static void initNotification(Application application){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 聊天消息
            createNotificationChannel(application,CHANNEL_ID_CHAT,"聊天消息通知", NotificationManager.IMPORTANCE_HIGH);
            createNotificationChannel(application,CHANNEL_ID_NEWS,"系统消息通知",NotificationManager.IMPORTANCE_HIGH);
            createNotificationChannel(application,CHANNEL_ID_WARNING,"报警消息通知",NotificationManager.IMPORTANCE_HIGH);
        }
    }

    /**
     * 创建通知渠道
     * @param application
     * @param channelId
     * @param channelName
     * @param importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Application application,String channelId, String channelName, int importance){
        NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
        NotificationManager notificationManager = (NotificationManager) application.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

}
/* 显示系统通用通知
    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification notification = new NotificationCompat.Builder(this, "chat")
            .setContentTitle("收到一条聊天消息")
            .setContentText("今天中午吃什么？")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.icon)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
            .setAutoCancel(true)
            .build();
        // 1 是通知id
        manager.notify(1, notification);
*/
