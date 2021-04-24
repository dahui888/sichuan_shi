package com.campuscard.app.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.legacy.app.ActivityCompat;

import com.base.frame.utils.XPermissionUtil;

import static android.content.pm.PackageManager.SIGNATURE_MATCH;

/**
 * 类名：拨打电话操作类
 * 创建人：ZXB
 * 创建时间：2018/1/10
 * 类说明：1.调用拨号界面 2.调用拨号功能
 */
public class TelUtils {


    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    public static void callPhoneDialog(Context context, final String phone) {
        //高版本授权
        if (!XPermissionUtil.initPermission(context, new String[]{Manifest.permission.CALL_PHONE})) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == SIGNATURE_MATCH) {
                XPermissionUtil.isPermission(context, new String[]{Manifest.permission.CALL_PHONE}, "拨打电话权限");
            }
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 调用拨号功能
     * @param phone 电话号码
     */
    public static void callPhone(Context context, final String phone) {
        //高版本授权
        if (!XPermissionUtil.initPermission(context, new String[]{Manifest.permission.CALL_PHONE})) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == SIGNATURE_MATCH) {
                XPermissionUtil.isPermission(context, new String[]{Manifest.permission.CALL_PHONE}, "拨打电话权限");
            }
            return;
        }
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        context.startActivity(intent);
    }




}