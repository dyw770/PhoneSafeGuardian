package cn.dyw.phonesafeguardian.splash.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.storage.StorageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.dyw.phonesafeguardian.R;
import cn.dyw.phonesafeguardian.home.HomeActivity;
import cn.dyw.phonesafeguardian.splash.entity.VersionEntity;
import okhttp3.Call;

import static android.content.ContentValues.TAG;

/**
 * 该类用来更新版本
 * Created by dyw on 2017/3/13.
 */

public class VersionUpdate {

    private ProgressDialog mDialog;

    private Context context;

    public VersionUpdate(Context context) {
        this.context = context;
    }

    /**
     * 获取当前本地版本号，并在欢迎界面进行显示
     */
    public String getLocalVersion(){
        PackageManager manger = context.getPackageManager();
        try {
            Log.d("PackageManager", context.getPackageName());
            PackageInfo info = manger.getPackageInfo(context.getPackageName(), 0);
            Log.d("getLocalVersion", info.versionName);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("getLocalVersion", "获取本地版本号失败");
            return "err";
        }
    }

    /**
     * 获取网路上的版本号
     * @return
     */
    public void getHttpVerson(final String localVersion) throws IOException {
        OkHttpUtils
                .get()
                .url("http://59.110.153.233:8080/PhoneSafe/VersionServlet")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("getHttpVerson", e.getMessage());
                        StackTraceElement[] el = e.getStackTrace();
                        for(StackTraceElement e1 : el){
                            Log.d(TAG, "onError: " + e1.toString());
                        }
                        e.printStackTrace();
                        Toast.makeText(context, "网络出了一点问题", Toast.LENGTH_SHORT).show();
                        enterHome();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("getHttpVerson", response);
                        Gson gson = new Gson();
                        VersionEntity sion = gson.fromJson(response, VersionEntity.class);
                        Log.d("localVersion", sion.getCode().equals(localVersion)+"");
                        if(!sion.getCode().equals(localVersion)){
                            //版本号不一致，执行弹出对话框，提醒用户更新
                            showUpdateDialog(sion, context);
                        } else {
                            //TODO 版本号一致直接进入HomeActivity
                            enterHome();
                        }
                    }
                });
    }

    /**
     * 升级逻辑
     * @param versionEntity
     * @param context
     */

    public void showUpdateDialog(VersionEntity versionEntity, final Context context){
        new AlertDialog.Builder(context)
                .setTitle("检查到新版本：" + versionEntity.getCode())
                .setMessage(versionEntity.getDes())
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //显示一个有进度条的对话框，提示用户在下载新版的安装包
                        mDialog = new ProgressDialog(context);
                        mDialog.setMessage("准备下载....");
                        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mDialog.setCancelable(false);
                        mDialog.show();
                        downloadApk(context);
                    }
                })
                .setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        enterHome();
                    }
                })
                .show();
    }

    private void downloadApk(final Context context){
        Log.d("downloadapk","downloadapk");
        String[] extSdcardPath = getExtSDCardPath();
        for (int i = 0; i < extSdcardPath.length; i ++ ){
            Log.d("logapk", extSdcardPath[i]);
        }
        Log.d("downloadApkPath", extSdcardPath[0]);

        OkHttpUtils
                .get()
                .url("http://59.110.153.233:8080/PhoneSafe/PhoneSafeApk.apk")
                .build()
                .execute(new FileCallBack(extSdcardPath[0], "file.exe") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("getHttpVerson", e.getMessage());
                        StackTraceElement[] el = e.getStackTrace();
                        for(StackTraceElement e1 : el){
                            Log.d(TAG, "onError: " + e1.toString());
                        }
                        e.printStackTrace();
                        Toast.makeText(context, "网络出了一点问题", Toast.LENGTH_SHORT).show();
                        enterHome();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.d("downloadapk","下载成功");
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        mDialog.setMessage("正在下载");
                        Log.d("inProgress", progress + ":" + total + ":" + id);
                        mDialog.setProgress((int)(progress * 100));
                    }
                });
    }

    private void enterHome(){
        Intent homeIntent = new Intent(context, HomeActivity.class);
        context.startActivity(homeIntent);
        ((Activity)context).finish();
    }

    /**
     * 获取外置SD卡路径
     *
     */
    public String[] getExtSDCardPath() {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context
                .STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            return (String[])invoke;
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
