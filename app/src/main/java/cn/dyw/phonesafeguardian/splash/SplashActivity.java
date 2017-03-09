package cn.dyw.phonesafeguardian.splash;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import cn.dyw.phonesafeguardian.R;

public class SplashActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        textView = (TextView) findViewById(R.id.version);

        /**
         * 获取当前本地版本号，并在欢迎界面进行显示
         */
        PackageManager manger = SplashActivity.this.getPackageManager();
        try {
            PackageInfo info = manger.getPackageInfo(SplashActivity.this.getPackageName(), 0);
            Log.d("testttt", info.versionName);
            textView.setText("版本号：" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("testttt", "45555");
        }
    }
}