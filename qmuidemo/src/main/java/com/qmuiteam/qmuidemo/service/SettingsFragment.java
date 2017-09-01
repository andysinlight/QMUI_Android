package com.qmuiteam.qmuidemo.service;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmuidemo.QDDataManager;
import com.qmuiteam.qmuidemo.R;
import com.qmuiteam.qmuidemo.base.BaseFragment;
import com.qmuiteam.qmuidemo.lib.Group;
import com.qmuiteam.qmuidemo.lib.annotation.Widget;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by andy on 2017/9/1.
 */

@Widget(group = Group.Lab, name = "測試通知設置", iconRes = R.mipmap.icon_grid_device_helper)
public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_notice_number)
    EditText mEtNoticeNumber;
    @BindView(R.id.et_time)
    EditText mEtTime;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    @BindView(R.id.btn_open)
    Button mBtnStart;
    @BindView(R.id.btn_append_apps)
    Button mBtnAppendApps;
    private int mId = 100;
    private String mStrCount;
    private String mStrDelay;
    private String mTitle;
    private String mName;

    @Override
    protected View onCreateView() {
        View view = View.inflate(getContext(), R.layout.fragment_notification_setting, null);
        ButterKnife.bind(this, view);
        initTopBar();
        mBtnSend.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mBtnAppendApps.setOnClickListener(this);
        retore();
        return view;
    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(QDDataManager.getInstance().getName(this.getClass()));

        // 动态修改效果按钮
        mTopBar.addRightTextButton("保存", 123)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        save();
                    }
                });
    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_send:
                startSend();
                break;
            case R.id.btn_open:
                if (!isEnabled()) {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
                Toast toast = Toast.makeText(getContext().getApplicationContext(), isEnabled() ? "监控器开关已打开" : "监控器开关已关闭", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }

    }

    private void startSend() {
        if (util.isServiceRunning(getContext(), "com.qmuiteam.qmuidemo.service.SimulateNoticeService"))
            return;
        save();
        Intent intent = new Intent(getContext(), SimulateNoticeService.class);
        getContext().startService(intent);
    }

    private void getInfo() {
        mStrCount = mEtNoticeNumber.getText().toString();
        mStrDelay = mEtTime.getText().toString();
        mTitle = mEtTitle.getText().toString();
        mName = mEtName.getText().toString();
    }

    private void retore() {
        mStrCount = SPUtils.getInstance().getString("count", "");
        mStrDelay = SPUtils.getInstance().getString("delay", "");
        mTitle = SPUtils.getInstance().getString("title", "");
        mName = SPUtils.getInstance().getString("name", "");
        mEtNoticeNumber.setText(mStrCount);
        mEtTime.setText(mStrDelay);
        mEtTitle.setText(mTitle);
        mEtName.setText(mName);
    }

    private void save() {
        getInfo();
        SPUtils.getInstance().put("count", mStrCount);
        SPUtils.getInstance().put("delay", mStrDelay);
        SPUtils.getInstance().put("title", mTitle);
        SPUtils.getInstance().put("name", mName);
    }


    // 判断是否打开了通知监听权限
    private boolean isEnabled() {
        String pkgName = getContext().getPackageName();
        final String flat = Settings.Secure.getString(getContext().getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
