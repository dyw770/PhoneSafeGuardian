package cn.dyw.phonesafeguardian;

import android.app.Application;
import android.content.Context;

/**
 * Created by dyw on 2017/3/13.
 * 该类为自定义application类
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
