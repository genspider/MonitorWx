package com.okwx.xiao.okwx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.okwx.xiao.dao.UserDao;
import com.okwx.xiao.entity.User;
import com.okwx.xiao.util.http.Format;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends  AppCompatActivity{

    public static MainActivity instance = null;

    BroadcastReceiver receiver;

    private AlertDialog mAlertDialog;

    public static AndroidOInstallPermissionListener sListener;

    private static String TAG = "MainActivity";

    //当前版本
    public static final String CurrentVersion = "3.0";

    //检查版本更新
    private void checkVersion(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(ApiUtil.UpdateApp)
                        .build();

                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result="";
                        if (response.body()!=null) {
                            result=response.body().string();
                        }else {
                            //返回数据错误
                            return;
                        }
                        emitter.onNext(result);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())// 将被观察者切换到子线程
                .observeOn(AndroidSchedulers.mainThread())// 将观察者切换到主线程
                .subscribe(new Observer<String>() {
                    private Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }
                    @Override
                    public void onNext(String result) {
                        if (result.isEmpty()){
                            return;
                        }
                        Format format = JSON.parseObject(result,Format.class);
                        if(format.getCode() == 0) {
                            List list = format.getData();
                            Log.i(TAG,"list" + list);
                            Map map = (Map) list.get(0);
                            final String verison = (String) map.get("version");
                            final String downUrl = (String) map.get("apk_url");
                            Log.i(TAG,downUrl);
                            if (!verison.equals(CurrentVersion))
                            {
                                Log.i(TAG,"可以进行版本更新");
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("版本更新");
                                builder.setMessage("有新版本可以更新，是否进行更新?");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startInstallPermissionSettingActivity(downUrl);
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (sListener != null) {
                                            sListener.permissionFail();
                                        }
                                        mAlertDialog.dismiss();
                                        //finish();
                                    }
                                });
                                mAlertDialog = builder.create();
                                mAlertDialog.show();

                            }else{
                                Toast.makeText(MainActivity.this,"当前已是最新版本", Toast.LENGTH_LONG).show();
                            }
                        }
                        mDisposable.dispose();
                    }
                    @Override
                    public void onError(Throwable e) {
                        checkVersion();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * 忽略电池优化
     */
    public void ignoreBatteryOptimization(Activity activity) {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
        if(!hasIgnored) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:"+activity.getPackageName()));
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        Log.i(TAG,"开启服务");
        ignoreBatteryOptimization(MainActivity.this);
        autoLogin();
        validateInstall();
        checkVersion();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG,"有通知");
                String msg = intent.getExtras().getString("msg", "无通知");
                Log.i(TAG,msg);
                if (NotifiUtils.kill_noti.equals(msg)){
                    toggleNotificationListenerService();
                    msg="notification service restart";
                    Log.i(TAG,msg);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotifiUtils.SETTING_CHANGED);
        registerReceiver(receiver, intentFilter);
    }

    //检查安装权限
    public void validateInstall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "2");
            boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                Toast.makeText(MainActivity.this,"请点击允许安装应用", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        }
    }

    //监听通知栏权限
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 授权成功
            if (sListener != null) {
                sListener.permissionSuccess();
            }
        } else {
            // 授权失败
            if (sListener != null) {
                sListener.permissionFail();
            }
        }
    }

    //登录
    public void sendMessage( View view){

        //实例化布局
        View loginView = LayoutInflater.from(this).inflate(R.layout.login,null);

        //创建对话框
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("请输入微信账号");//设置标题
        dialog.setView(loginView);//添加布局
        final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = preference.getBoolean("isRemember", false);
        final CheckBox remember = loginView.findViewById(R.id.remember);
        final EditText zcNo = loginView.findViewById(R.id.zcNo);
        if (isRemember) {
            zcNo.setText(preference.getString("zcNo", ""));
            remember.setChecked(true);
        }
        if(remember.isChecked()){
            remember.setChecked(true);
        }else{
            remember.setChecked(false);
        }

        //设置按键
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String message = zcNo.getText().toString();

                String url = ApiUtil.Login+"?UserNo="+message;
                //第一步获取okHttpClient对象
                OkHttpClient client = new OkHttpClient.Builder()
                        .build();
                Log.i("result", url);
                //第二步构建Request对象
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                //第三步构建Call对象
                Call call = client.newCall(request);
                //第四步:异步get请求
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("onFailure", e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String res = response.body().string();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Format format = JSON.parseObject(res,Format.class);
                                if(format.getCode() == 0){
                                    List list = format.getData();
                                    Map map = (Map) list.get(0);

                                    String userNo = (String) map.get("user_no");
                                    String cellphone = (String) map.get("cellphone");

                                    TextView userNoText = (TextView) findViewById ( R . id .user_no);
                                    TextView cellphoneText = (TextView) findViewById ( R . id .cellphone);
                                    TextView fansSum = (TextView) findViewById ( R . id .fans_sum);

                                    userNoText.setText(userNo);
                                    cellphoneText.setText(cellphone);
                                    fansSum.setText("0");
                                    int count = (int) UserDao.getInstance().getCount();

                                    if(count > 0){
                                        UserDao.getInstance().clearAll();
                                    }
                                    User user = new User(userNo,cellphone,0);
                                    UserDao.getInstance().insert(user);

                                    Toast.makeText(MainActivity.this, format.getMsg(), Toast.LENGTH_LONG).show();
                                    Button one = (Button) findViewById ( R . id . one );
                                    one.setEnabled(false);
                                    Button two = (Button) findViewById ( R . id . two );
                                    two.setEnabled(true);

                                    SharedPreferences.Editor editor = preference.edit();
                                    if (remember.isChecked()) {
                                        editor.putBoolean("isRemember", true);
                                        editor.putString("zcNo", userNo);
                                    } else {//清空数据
                                        editor.clear();
                                    }
                                    editor.apply();
                                }else{
                                    Toast.makeText(MainActivity.this, format.getMsg(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        dialog.show();
    }

    //复制
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void copyMsg(View view){
        EditText fansSum = (EditText) findViewById ( R . id .fans_sum);
        String fansSumVal = fansSum . getText (). toString ();
        EditText userNo = (EditText) findViewById ( R . id .user_no);
        String userNoVal = userNo . getText (). toString ();
        EditText cellphone = (EditText) findViewById ( R . id .cellphone);
        String cellphoneVal = cellphone . getText (). toString ();

        StringBuilder copyStr = new StringBuilder();
        copyStr.append("粉丝数量为");
        copyStr.append(fansSumVal);
        copyStr.append("，");
        copyStr.append("微信号为:");
        copyStr.append(userNoVal);
        copyStr.append("，");
        copyStr.append("手机号码为:");
        copyStr.append(cellphoneVal);
        String copyTest = copyStr.toString();

        ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("copy", copyTest);
        mClipboardManager.setPrimaryClip(clipData);

        AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("复制成功" )
                .setMessage("你可以将复制的信息发给组长")
                .setPositiveButton("确定" ,  null )
                .show();

    }

    //开启下载
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(String downUrl) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "5");
            boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                Toast.makeText(MainActivity.this,"请点击允许安装应用", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
                return;
            }
        }
        DownLoadApk.download(MainActivity.this,downUrl,"版本更新","zc");
    }

    //开始统计
    public void statistics(View view){
        gather();
    }

    //自动登录
    public void gather(){

        if (!isNotificationListenerEnabled(this)){
            Toast.makeText(MainActivity.this, "请别忘记授权！", Toast.LENGTH_LONG).show();
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    Log.i(TAG, "6");
                    intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                }
                else {
                    Log.i(TAG, "6");
                    intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                }
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "手机的系统不支持此功能", Toast.LENGTH_SHORT)
                        .show();
            }
            return;
        }
        TextView userNoText = (TextView) findViewById ( R . id .user_no);

        String url = ApiUtil.Login2+"?userNo="+userNoText.getText().toString();
        //第一步获取okHttpClient对象
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Log.i("result", url);
        //第二步构建Request对象
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //第三步构建Call对象
        Call call = client.newCall(request);
        //第四步:异步get请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Format format = JSON.parseObject(res,Format.class);
                        if(format.getCode() == 0){

                            Toast.makeText(MainActivity.this, "开始收集好友申请", Toast.LENGTH_LONG).show();

                            Button one = (Button) findViewById ( R . id . two );
                            one.setEnabled(false);
                            Button two = (Button) findViewById ( R . id . three );
                            two.setEnabled(true);
                        }else{
                            Toast.makeText(MainActivity.this, format.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    //监听 进程重启
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, NotiService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, NotiService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    //设置监听权限
    public void setAuth(View view){
        checkVersion();
    }

    //弹框
    public  void msg(String a){
        Looper.prepare();
        Toast.makeText(MainActivity.this, a, Toast.LENGTH_SHORT)
                .show();
        Looper.loop();
    }

    //检测通知监听服务是否被授权
    public boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packageNames.contains(context.getPackageName())){
            return true;
        }
        return false;
    }

    //页面恢复
    @Override
    protected void onResume() {
        super.onResume();
        TextView tv_s = (TextView) findViewById ( R . id .tv_s);
        if (isNotificationListenerEnabled(this)){
            tv_s.setText("好友申请权限已经开启");
            toggleNotificationListenerService();
        }else{
            tv_s.setText("权限没开启");
        }
    }

    //退出回调
    @Override
    protected void onPause() {
        if(UserDao.getInstance().getCount()> 0){
            TextView fansSum = (TextView) findViewById ( R . id .fans_sum);
            String fansSumVal = fansSum.getText().toString();
            if( "".equals(fansSumVal) ){
                fansSumVal = "0";
            }
            Integer fansSumVal2 = Integer.parseInt(fansSumVal);
            UserDao.getInstance().updateFansum(fansSumVal2);
        }
        super.onPause();
    }

    //注销
    public void loginOut(View view){
        zhuxiao();
    }

    //注销
    public void zhuxiao(){

        TextView userNoText = (TextView) findViewById ( R . id .user_no);

        if("".equals(userNoText.getText().toString())){
            return;
        }
        TextView cellphoneText = (TextView) findViewById ( R . id .cellphone);

        String url = ApiUtil.LoginOut+"?userNo="+userNoText.getText().toString()+"&cellphone="+cellphoneText.getText().toString();
        //第一步获取okHttpClient对象
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Log.i("result", url);
        //第二步构建Request对象
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //第三步构建Call对象
        Call call = client.newCall(request);
        //第四步:异步get请求
        call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("onFailure", e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    Log.i("result", res);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Format format = JSON.parseObject(res,Format.class);
                            if(format.getCode() == 0){
                                Button one = (Button) findViewById ( R . id . one );
                                one.setEnabled(true);
                                Button two = (Button) findViewById ( R . id . two );
                                two.setEnabled(false);
                                Button three = (Button) findViewById ( R . id . three );
                                three.setEnabled(false);
                                Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
            });
    }

    //自动登录
    public void autoLogin(){
        TextView userNo = (TextView) findViewById ( R . id .user_no);
        if(UserDao.getInstance().getCount() > 0 ){
            List <User> list = UserDao.getInstance().query();
            User user = list.get(0);
            String fansSumVal = String.valueOf(user.getFansSum());
            String wxNoVal = user.getWxNo();
            String cellphoneVal = user.getCellphone();

            TextView fansSum = (TextView) findViewById ( R . id .fans_sum);
            fansSum.setText(fansSumVal);
            userNo.setText(wxNoVal);
            TextView cellphone = (TextView) findViewById ( R . id .cellphone);
            cellphone.setText(cellphoneVal);

            Toast.makeText(MainActivity.this, "自动登录成功", Toast.LENGTH_LONG).show();
            Button one = (Button) findViewById ( R . id . one );
            one.setEnabled(false);
            Button two = (Button) findViewById ( R . id . two );
            two.setEnabled(true);
            gather();
        }
    }

    //自定义退出
    @Override
    public void onBackPressed() {}

    //退出
    @Override
    protected void onDestroy() {
        if(mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        sListener = null;
        unregisterReceiver(receiver);
        Log.i(TAG,"监听程序被退出");
        super.onDestroy();
    }
}

