package com.okwx.xiao.okwx;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;
import com.okwx.xiao.okwx.BuildConfig;
import java.io.File;
import java.util.List;

public class ApkInstallReceiver extends BroadcastReceiver {

    private static final String FILE_PROVIDER =  BuildConfig.APPLICATION_ID+".provider";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            long downloadApkId =intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            installApk(context, downloadApkId);
            checkPermission(context,downloadApkId);
        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context, long downloadApkId) {
        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Uri downloadFileUri = null;
        Intent install= new Intent(Intent.ACTION_VIEW);
        long downId =sp.getLong(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        if(downloadApkId == downId){
            DownloadManager downManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // 6.0以下
                downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                // 6.0 - 7.0
                File apkFile = queryDownloadedApk(context, downloadApkId);
                downloadFileUri = Uri.fromFile(apkFile);
            }else { // Android 7.0 以上
                //downloadFileUri = FileProvider.getUriForFile(context,"com.okwx.xiao.okwx.Fileprovider", file);

                /*downloadFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",
                        new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "zc.apk"));*/
                String packageName = context.getApplicationContext().getPackageName();
                String authority =  new StringBuilder(packageName).append(".provider").toString();

                downloadFileUri = FileProvider.getUriForFile(
                        context,
                        authority,
                        new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "zc.apk"));
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            //Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                List<ResolveInfo> resolveLists = context.getPackageManager().queryIntentActivities(install, PackageManager.MATCH_DEFAULT_ONLY);
                // 然后全部授权
                for (ResolveInfo resolveInfo : resolveLists){
                    String packageName = resolveInfo.activityInfo.packageName;
                    context.grantUriPermission(packageName, downloadFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                context.startActivity(install);

            }else{
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static File queryDownloadedApk(Context context, long downloadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    private void checkPermission(final Context context, final long downloadApkId){
        boolean haveInstallPermission;
        // 兼容Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                //没有权限,弹窗，并去设置页面授权
                final AndroidOInstallPermissionListener listener = new AndroidOInstallPermissionListener() {
                    @Override
                    public void permissionSuccess() {
                        installApk(context, downloadApkId);
                    }

                    @Override
                    public void permissionFail() {
                        //ToastUtils.shortToast(context, "授权失败，无法安装应用");
                    }
                };
                MainActivity.sListener = listener;
                Intent intent1 = new Intent(context, MainActivity.class);
                context.startActivity(intent1);
            } else {
                installApk(context, downloadApkId);
            }
        }else {
            installApk(context, downloadApkId);
        }
    }


}