package com.qmuiteam.qmuidemo.service;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.MotionEvent;

import com.qmuiteam.qmuidemo.model.MessageEvent;
import com.qmuiteam.qmuidemo.model.NotificaionBean;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by andy on 2017/9/1.
 */

public class NotificationMonitorService extends NotificationListenerService {
    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // TODO Auto-generated method stub
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);
        Log.i("XSL_Test", "Notification posted " + notificationTitle + " & " + notificationText + "&" + notificationPkg);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.NEW_NOTIFICATION,new NotificaionBean(notificationTitle,notificationText)));

    }

    // 在删除消息时触发
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);
        Log.i("XSL_Test", "Notification removed " + notificationTitle + " & " + notificationText);
    }
}
