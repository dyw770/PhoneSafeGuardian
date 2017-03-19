package cn.dyw.phonesafeguardian.splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.TimerTask;

import cn.dyw.phonesafeguardian.R;
import cn.dyw.phonesafeguardian.splash.utils.VersionUpdate;

public class SplashActivity extends AppCompatActivity {
    private TextView textView;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.d("timer", "定时器启动");
                updateVersion();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        textView = (TextView) findViewById(R.id.version);

        /**
         * 开启定时器----等待3秒后执行更新
         */
        new Thread(new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3 * 1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateVersion(){
        VersionUpdate localVersion = new VersionUpdate(SplashActivity.this);
        String version = localVersion.getLocalVersion();
        if(!version.equals("err")){
            textView.setText("版本号：" + version);
        }
        try {
            localVersion.getHttpVerson(version);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}