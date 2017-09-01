package com.qmuiteam.qmuidemo.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Intent;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by andy on 2017/9/1.
 */

public class NeNotificationService extends AccessibilityService {
    private static String qqpimsecure = "com.tencent.qqpimsecure";
    //public static String TAG = "NeNotificationService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            if (event.getPackageName().equals(qqpimsecure)) {

            } else {
                Parcelable data = event.getParcelableData();
                if (data instanceof Notification) {
                    Notification notification = (Notification) data;
                    Intent intent = new Intent();
                    intent.putExtra("NotifyData", notification);
                    intent.putExtra("packageName", event.getPackageName());
                    intent.setAction(".NeNotificationService");
                    ServiceFragment.notifyReceive((String) event.getPackageName(), notification);
                }
            }
        }

    }


    @Override
    protected void onServiceConnected() {
        AppLog.d("onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED |
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED |
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        AppLog.d("onInterrupt");
    }
}
