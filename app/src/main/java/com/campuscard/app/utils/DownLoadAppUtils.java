package com.campuscard.app.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;


import com.campuscard.app.ui.entity.VersionEntity;

import java.io.File;

/**
 * App版本下载
 */
public class DownLoadAppUtils {

    private static DownCompleteReceiver downCompleteReceiver;

    private static void gotoUpdate(Context context, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType("application/vnd.android.package-archive"); //修改
        File downloadFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "ykt.apk"); //修改
        request.setDestinationUri(Uri.fromFile(downloadFile)); //修改
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载新版本");
        request.setVisibleInDownloadsUi(true);
        long downloadId = downloadManager.enqueue(request);
        downCompleteReceiver = new DownCompleteReceiver(downloadId, downloadFile);
        context.registerReceiver(downCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public static void createDialogUpdate(final Context context, final VersionEntity versionEntity) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("版本更新")
                .setMessage(versionEntity.getUpdateContent())
                .setCancelable(false).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(versionEntity.getVersionUrl())) {
                    gotoUpdate(context, versionEntity.getVersionUrl());
                }
                dialog.dismiss();
            }
        });
        alert.create().show();

    }

    private static class DownCompleteReceiver extends BroadcastReceiver {
        private long enqueueId;
        private File downloadFile;

        public DownCompleteReceiver(long enqueueId, File file) {
            this.enqueueId = enqueueId;
            this.downloadFile = file;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
            long id = intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (enqueueId != id) {
                return;
            }
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(enqueueId);
            Cursor c = dm.query(query);
            if (c != null && c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                // 下载失败也会返回这个广播，所以要判断下是否真的下载成功
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        Uri uri = FileProvider.getUriForFile(context, "com.campuscard.app" + ".fileprovider", downloadFile);
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        installIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        context.startActivity(intent);
                    } else {
                        // 获取下载好的 apk 路径
                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                        // 提示用户安装
                        installAPP(Uri.parse("file://" + uriString), context);
                    }
                }
                c.close();
            }
        }
    }

    private static void installAPP(Uri data, Context context) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(data, "application/vnd.android.package-archive");
        // FLAG_ACTIVITY_NEW_TASK 可以保证安装成功时可以正常打开 app
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }

    public static void unregisterReceiver(Context context) {
        if (downCompleteReceiver != null)
            context.unregisterReceiver(downCompleteReceiver);
    }
}
