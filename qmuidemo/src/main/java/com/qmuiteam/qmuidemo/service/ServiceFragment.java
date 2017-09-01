package com.qmuiteam.qmuidemo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.qmuiteam.qmuidemo.R;
import com.qmuiteam.qmuidemo.base.BaseFragment;
import com.qmuiteam.qmuidemo.lib.Group;
import com.qmuiteam.qmuidemo.lib.annotation.Widget;

/**
 * Created by andy on 2017/9/1.
 */

@Widget(group = Group.Lab, name = "測試通知拦截", iconRes = R.mipmap.icon_grid_device_helper)
public class ServiceFragment extends BaseFragment {
    private PushMessCache pushIns;
    private Intent upservice;
    private LinearLayout rootLayout;
    private Button accesscBt;
    private Button accesscStartNo;
    private Button ClearViews;

    @Override
    protected View onCreateView() {
        instance = this;
        View view = View.inflate(getContext(), R.layout.activity_service, null);
        pushIns = new PushMessCache();
        upservice = new Intent(getContext(), NeNotificationService.class);
        rootLayout = (LinearLayout) view.findViewById(R.id.root_layout);
        accesscBt = (Button) view.findViewById(R.id.buttonAssesc);
        accesscBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, 0);

            }
        });
        accesscStartNo = (Button) view.findViewById(R.id.buttonStartNofi);
        accesscStartNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //registerBroadcast();
                updateServiceStatus(true);
            }
        });

        ClearViews = (Button) view.findViewById(R.id.buttonClearView);
        ClearViews.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rootLayout.removeAllViews();
                NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            }
        });

        updateServiceStatus(true);
        return view;
    }

    private void updateServiceStatus(boolean start) {
        boolean bRunning = util.isServiceRunning(getContext(), "com.nis.bcreceiver.NeNotificationService");

        if (start && !bRunning) {
            getContext().startService(upservice);
        } else if (!start && bRunning) {
            getContext().stopService(upservice);
        }
        bRunning = util.isServiceRunning(getContext(), "com.qmuiteam.qmuidemo.service.NeNotificationService");

        AppLog.i("updateServiceStatus ctrl[ " + start + "] result running:" + bRunning);

    }

    private NotifyDataReceiver receiver = null;

    private void registerBroadcast() {
        receiver = new NotifyDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(".NeNotificationService");
        Intent it = getContext().registerReceiver(receiver, filter);

        AppLog.i("Broadcast registered.........:" + it);
    }


    private void EnumGroupViews(View v1, PushMessCache.MessageData data) {
        if (v1 instanceof ViewGroup) {
            //Log.i(TAG, "FrameLayout in");
            ViewGroup lav = (ViewGroup) v1;
            int lcCnt = lav.getChildCount();
            for (int i = 0; i < lcCnt; i++) {
                View c1 = lav.getChildAt(i);
                if (c1 instanceof ViewGroup)
                    EnumGroupViews(c1, data);
                else if (c1 instanceof TextView) {
                    TextView txt = (TextView) c1;
                    String str = txt.getText().toString().trim();
                    if (str.length() > 0) {
                        pushIns.addMess(txt.getId(), data, str);
                    }

                    AppLog.i("TextView id:" + txt.getId() + ".text:" + str);
                } else {
                    AppLog.w("2 other layout:" + c1.toString());

                }
            }
        } else {
            AppLog.w("1 other layout:" + v1.toString());
        }
    }

    private void addToUi(RemoteViews remoteView, String packName) {
        //rootLayout.addView(remoteView);
        try {
            View v1 = remoteView.apply(getContext(), rootLayout);
            //AppLog.i("remoteview:" + v1.toString());
            PushMessCache.MessageData data = pushIns.new MessageData();

            data.packageName = packName;
            EnumGroupViews(v1, data);
            if (rootLayout.getChildCount() > 100) {
                AppLog.i("remove 50 views in child!");
                rootLayout.removeViews(0, 50);

            }

            rootLayout.addView(v1);
            data.isChina = PackName.isChina(packName);
            pushIns.sendMess(getContext(), data);
        } catch (Exception e) {
            AppLog.e("addToUi excep" + e.toString());
        }

    }

    static ServiceFragment instance;

    public static void notifyReceive(String packageName, Notification notification) {
        PendingIntent nit = notification.contentIntent;

        AppLog.i("onReceive packageName: " + packageName);

        if (notification != null) {

            RemoteViews remoteV = notification.contentView;

            if (remoteV == null) {
                AppLog.e("remoteView is: null");
            } else {
                if (instance != null)
                    instance.addToUi(remoteV, packageName);
            }
        }
    }


    public class NotifyDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //AppLog.i("Receiver got msg in onReceive()...");

            Parcelable notifyParcelable = intent.getParcelableExtra("NotifyData");
            String packageName = intent.getStringExtra("packageName");
            AppLog.i("onReceive packageName: " + packageName);
            if (notifyParcelable != null) {

                Notification notification = (Notification) notifyParcelable;
                //Log.i("tickerText: " + notification.tickerText);

                RemoteViews remoteV = notification.contentView;
                PendingIntent nit = notification.contentIntent;


                if (remoteV == null) {
                    AppLog.e("remoteView is: null");
                } else {
                    //showNotify("remoteView is: not null" );

                    addToUi(remoteV, packageName);
                }
            }

        }
    }

}
