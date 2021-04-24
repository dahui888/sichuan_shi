package com.base.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;

/**
 * 类名：权限基础类--所有的权限判断（或者授权）
 * 创建人：ZXB
 * 创建时间：2017/7/27
 * 类说明：
 */
public class XPermissionUtil {

    /**
     * 初始化获取权限
     * @param context
     * @param permissions
     * @return
     */
    public static final boolean initPermission(Context context, @NonNull String[] permissions) {
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //判断是否拥有权限
            boolean isPer = false;
            for (int i = 0; i < permissions.length; i++) {
                int checkCallPhonePermission1 = context.checkSelfPermission(permissions[i]);
                if (checkCallPhonePermission1 != PackageManager.PERMISSION_GRANTED) {
                    //弹出对话框接收权限
                    if (context instanceof Activity) {
                        ((Activity) context).requestPermissions(permissions, 60);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证是否有权限
     *
     * @param context
     * @param permissions 权限列表
     * @param perName     权限名称
     * @return
     */
    public static final boolean isPermission(final Context context, @NonNull String[] permissions, @NonNull String perName) {
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //判断是否拥有权限
            boolean isPer = false;
            for (int i = 0; i < permissions.length; i++) {
                int checkCallPhonePermission1 = context.checkSelfPermission(permissions[i]);
                if (checkCallPhonePermission1 != PackageManager.PERMISSION_GRANTED) {
                    //弹出对话框接收权限
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setTitle("缺少" + perName);
                    builder.setMessage("是否去授予" + perName);
                    builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                            context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return false;
                }
            }
        }
        return true;
    }
}
