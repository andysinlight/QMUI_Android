package com.qmuiteam.qmuidemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hss01248.notifyutil.NotifyUtil;
import com.qmuiteam.qmuidemo.R;

/**
 * Created by andy on 2017/9/1.
 */

public class SimulateNoticeService extends Service {
    private String mStrCount;
    private String mStrDelay;
    private String mTitle;
    private String mName;
    private int mId = 100;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        getInfo();
        final Integer number = Integer.valueOf(!TextUtils.isEmpty(mStrCount) ? mStrCount : "0");
        final Integer delay = Integer.valueOf(!TextUtils.isEmpty(mStrDelay) ? mStrDelay : "0");

        new Thread(new Runnable() {
            @Override
            public void run() {
                int n = number;
                int temp = delay;
                while (n > 0) {
                    n--;
                    NotifyUtil.buildSimple(mId++, R.drawable.qmui_icon_notify_info, mTitle, mName, null).show();
                    try {
                        Thread.sleep(temp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf();
            }
        }).start();
    }

    private void getInfo() {
        mStrCount = SPUtils.getInstance().getString("count", "");
        mStrDelay = SPUtils.getInstance().getString("delay", "");
        mTitle = SPUtils.getInstance().getString("title", "");
        mName = SPUtils.getInstance().getString("name", "");
    }
}
