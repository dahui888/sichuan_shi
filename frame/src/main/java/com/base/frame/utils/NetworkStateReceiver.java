package com.base.frame.utils;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.base.frame.R;

/**
 * 全局网络检测
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private Dialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            // 手机网络连接成功
        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            dialog = showNetWorkDialog(context);
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
            // 手机没有任何的网络
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    /**
     * 判断网络--对话框
     *
     * @param context
     * @return
     */
    public static Dialog showNetWorkDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.DialogStyle);
        View netView = LayoutInflater.from(context).inflate(R.layout.dialog_net, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(netView);
        dialog.setCanceledOnTouchOutside(true);

        Button btCancel = (Button) netView.findViewById(R.id.bt_cancel);
        Button btSure = (Button) netView.findViewById(R.id.bt_sure);
        //取消
        btCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确定
        btSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        return dialog;
    }


}