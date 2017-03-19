package cn.dyw.phonesafeguardian.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import cn.dyw.phonesafeguardian.R;
import cn.dyw.phonesafeguardian.home.adapter.HomeAdapter;
import cn.dyw.phonesafeguardian.home.utils.MD5Utils;
import cn.dyw.phonesafeguardian.home.widget.InterPasswordDialog;
import cn.dyw.phonesafeguardian.home.widget.SetUpPasswordDialog;

public class HomeActivity extends AppCompatActivity {
    //记录按退出按钮的事件
    private long exitTime = 0;
    //主页的GridView
    private GridView gv_home;
    //记录存储面的sp文件
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));

        //获取SharedPreferences实例
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        //设值 gv_home的单击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // parent代表gridView,view代表每个条目的view对象,postion代表每个条目的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0: // 点击手机防盗
                        if (isSetUpPassword()) {
                            // 弹出输入密码对话框
                            showInsterPwdDialog();
                        } else {
                            // 弹出设置密码对话框
                            showSetUpPswdDialog();
                        }
                        break;
//                    case 1: // 点击通讯卫士
//                        startActivity(SecurityPhoneActivity.class);
//                        break;
//                    case 2: // 软件管家
//                        startActivity(AppManagerActivity.class);
//                        break;
//                    case 3:// 手机杀毒
//                        startActivity(VirusScanActivity.class);
//                        break;
//                    case 4:// 缓存清理
//                        startActivity(CacheClearListActivity.class);
//                        break;
//                    case 5:// 进程管理
//                        startActivity(ProcessManagerActivity.class);
//                        break;
//                    case 6: // 流量统计
//                        startActivity(TrafficMonitoringActivity.class);
//                        break;
//                    case 7: // 高级工具
//                        startActivity(AdvancedToolsActivity.class);
//                        break;
//                    case 8: // 设置中心
//                        startActivity(SettingsActivity.class);
//                        break;
                }
            }
        });
    }

    /**
     * 设置密码框显示
     */
    private void showSetUpPswdDialog() {
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(
                HomeActivity.this);
        setUpPasswordDialog
                .setCallBack(new SetUpPasswordDialog.MyCallBack() {

                    @Override
                    public void ok() {
                        String firstPwsd = setUpPasswordDialog.mFirstPWDET
                                .getText().toString().trim();
                        String affirmPwsd = setUpPasswordDialog.mAffirmET
                                .getText().toString().trim();
                        if (!TextUtils.isEmpty(firstPwsd)
                                && !TextUtils.isEmpty(affirmPwsd)) {
                            if (firstPwsd.equals(affirmPwsd)) {
                                // 两次密码一致,存储密码
                                saveSetUpPwd(affirmPwsd);
                                setUpPasswordDialog.dismiss();
                                // 显示输入密码对话框
                                showInsterPwdDialog();
                            } else {
                                Toast.makeText(HomeActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void cancle() {
                        setUpPasswordDialog.dismiss();
                    }
                });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }

    private void showInsterPwdDialog(){
        final InterPasswordDialog dialog = new InterPasswordDialog(HomeActivity.this);
        dialog.setCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if (!TextUtils.isEmpty(dialog.getPassword())){
                    if(isPwd(dialog.getPassword())){
                        dialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 判断用户是否设置过密码
     * @return true:设置过 false:没有设置过
     */
    private boolean isSetUpPassword(){
        String pwd = sharedPreferences.getString("pwd", "");
        return !TextUtils.isEmpty(pwd);
    }

    /**
     * 保存密码
     * @param pwd
     */
    private void saveSetUpPwd(String pwd){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pwd", MD5Utils.encode(pwd));
        editor.commit();
    }

    /**
     * 判断密码是否正确
     * @param pwd
     * @return
     */
    private boolean isPwd(String pwd){
        String readPwd = sharedPreferences.getString("pwd","");
        if(readPwd.equals(MD5Utils.encode(pwd))) return true;
        else return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
