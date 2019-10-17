package com.okwx.xiao.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.okwx.xiao.dao.UserDao;
import com.okwx.xiao.entity.User;
import com.okwx.xiao.okwx.ApiUtil;
import com.okwx.xiao.okwx.MainActivity;
import com.okwx.xiao.okwx.NotiService;
import com.okwx.xiao.okwx.R;
import com.okwx.xiao.util.http.Format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 守护进程 （前台通知）
 */
public class WatchService extends Service {

    private final String CHANNEL_ID = "TEST_SERVICE_ID";

    private final String CHANNEL_NAME = "渠道一";

    private final String contentSub = "纵驰粉丝统计系统";

    private final String contentTitle = "纵驰粉丝统计系统";

    private final String contentText = "监听微信粉丝添加记录";

    private static String TAG = "WatchService";

    public static final int NOTICE_ID = 100;

    Notification notification;

    Notification.Builder builder;

    public static Integer TIME = 2000;

    //检查进程是否被杀
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!isServiceRunning(WatchService.this,"com.okwx.xiao.okwx.NotiService")){
                zhuxiao();
                handler.removeCallbacks(runnable);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                    NotificationManager mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    mManager.cancel(NOTICE_ID);
                }
                android.os.Process.killProcess(android.os.Process.myPid());
            }else{
                handler.postDelayed(this, TIME);
            }
        }
    };

    public void zhuxiao(){
        List <User> list = UserDao.getInstance().query();
        User user = list.get(0);

        String userNoText = user.getWxNo();

        String cellphoneText = user.getCellphone();

        String url = ApiUtil.LoginOut+"?userNo="+userNoText+"&cellphone="+cellphoneText;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){//回调的方法执行在子线程。
                    MainActivity.instance.msg("注销成功");
                }
            }
        });
    }

    @Override
    public void onCreate() {
        Log.i(TAG,"you");
        handler.postDelayed(runnable, TIME);
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            chan.enableLights(true);
            chan.setLightColor(Color.RED);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            builder = new Notification.Builder(this, CHANNEL_ID);
            notification = builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(contentText)
                    .setSubText(contentSub)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(contentTitle)
                    .build();
        } else {
            Intent i = new Intent();
            PendingIntent p = PendingIntent.getActivity(this, 1, i, 0);
            notification = new Notification.Builder(getApplicationContext())
                    .setContentInfo(contentSub)
                    .setSubText(contentSub)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(p)
                    .build();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
        // 如果Service被杀死，干掉通知
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            NotificationManager mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            mManager.cancel(NOTICE_ID);
        }
        Log.i(TAG,"DaemonService---->onDestroy，前台service被杀死");
        // 重启自己
        Intent i = new Intent(getApplicationContext(),WatchService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("通知","守护进程");
            startForegroundService(i);
        } else {
            startService(i);
        }
    }

    /**
     * 判断Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service 类全名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(200);
        if (serviceInfoList.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : serviceInfoList) {
            System.out.println(info.service.getClassName());
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getAllProcess(String cmd) {
        List<String> orgProcList = new ArrayList<String>();
        List<String> errMsg = new ArrayList<String>();
        Process proc = null;
        BufferedReader bufferedReader = null;
        BufferedReader errReader = null;
        try {
            proc = Runtime.getRuntime().exec(cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(
                    proc.getInputStream())); // 将捕获内容转换为BufferedReader
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                orgProcList.add(str);
            }
            errReader = new BufferedReader(new InputStreamReader(
                    proc.getErrorStream()));
            str = null;
            while ((str = errReader.readLine()) != null) {
                errMsg.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (proc != null) {
                    proc.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (errReader != null) {
                try {
                    errReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (orgProcList.size() != 0)
            return orgProcList;
        return errMsg;
    }
}
