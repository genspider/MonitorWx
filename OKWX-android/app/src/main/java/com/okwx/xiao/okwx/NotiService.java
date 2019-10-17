package com.okwx.xiao.okwx;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.okwx.xiao.dao.DBDao;
import com.okwx.xiao.dao.UserDao;
import com.okwx.xiao.entity.Key;
import com.okwx.xiao.entity.User;
import com.okwx.xiao.service.WatchService;
import com.okwx.xiao.util.http.Format;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 监听通知栏
 */
public class NotiService extends NotificationListenerService {
    private final static int GRAY_SERVICE_ID = 1001;
    private static final String TAG = "[NotiService]";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        sendMsg("监控服务开启");
    }

    @Override
    public void onDestroy() {
        sendMsg(NotifiUtils.kill_noti);
        super.onDestroy();
    }

    @Override
    public void onListenerConnected() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onListenerConnected();
        }

        Log.i("yeqinfu", "onListenerConnected");
        //发送广播，已经连接上了
        Intent intent = new Intent(NotifiUtils.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        sendBroadcast(intent);
        startWatchService();
    }

    //守护进程
    public void startWatchService(){
        Intent i = new Intent(this, WatchService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("通知","守护进程");
            startForegroundService(i);
        } else {
            startService(i);
        }
    }

    /***
     * 通知栏新增或者更新 都会触发该回调方法
     * @param sbn
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        System.out.println("有消息了");
        super.onNotificationPosted(sbn);
        String key = sbn.getKey();

        String packName = sbn.getPackageName();
        String content = "";
        String notificationTitle = "";
        Notification notification = sbn.getNotification();

        Bundle bundle = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bundle = notification.extras;
            if (bundle.get("android.text") != null) {
                content = bundle.get("android.text").toString();
                notificationTitle = bundle.getString(Notification.EXTRA_TITLE);
            }
        }

        System.out.println(sbn.getId());
        System.out.println(sbn.getNotification());
        System.out.println(sbn.getPackageName());
        System.out.println(sbn.getPostTime());
        System.out.println(sbn.getTag());
        System.out.println(sbn.getUserId());
        System.out.println(sbn.isOngoing());
        System.out.println(sbn.isClearable());
        System.out.println(DBDao.getInstance().query());
        if ((
                "com.tencent.mm".equalsIgnoreCase(packName) ||
                        "pkgcom.tencent.mm".equalsIgnoreCase(packName))
                && "请求添加你为朋友".equals(content)
                ) {

            key = md5(key + notificationTitle);

            long count = DBDao.getInstance().getCount(key);

            if (count == 0) {
                setFansSum(notificationTitle);
                Key keys = new Key(key);
                DBDao.getInstance().insert(keys);
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            onListenerConnected();
        }
    }

    //记录粉丝数
    public void setFansSum(String notificationTitle){
        Log.i(TAG,"添加粉丝中...");
        //启动前台Service

        if(UserDao.getInstance().getCount() < 1){
            return;
        }

        List<User> list = UserDao.getInstance().query();

        User user = list.get(0);

        String userNoText = user.getWxNo();

        if(userNoText.equals("")){
            return;
        }

        String url = new StringBuilder().append(ApiUtil.AddFirend).append("/OkUser/addFirend?UserNo=").append(userNoText).append("&fansName=").append(notificationTitle).toString();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                showDialog();
            }
        });
    }

    private void sendMsg(String s) {
        Intent intent = new Intent(NotifiUtils.SETTING_CHANGED);
        intent.putExtra("msg",s);
        try {
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        Handler handlerThree=new Handler(Looper.getMainLooper());
        handlerThree.post(new Runnable(){
            public void run(){
                Toast.makeText(getApplicationContext() ,"有新的粉丝添加",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d("yeqinfu", "===========onNotificationRemoved=========");
    }

    @NonNull
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
