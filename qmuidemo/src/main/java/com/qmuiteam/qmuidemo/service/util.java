package com.qmuiteam.qmuidemo.service;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by andy on 2017/9/1.
 */

public class util {
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
        if (serviceList.size() == 0) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}
