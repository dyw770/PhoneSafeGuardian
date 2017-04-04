package cn.dyw.phonesafeguardian.guard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.dyw.phonesafeguardian.R;
import cn.dyw.phonesafeguardian.guard.adapter.ViewPagerAdapter;
import cn.dyw.phonesafeguardian.guard.view.GuideView;

public class GuardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<View> views;
    private LinearLayout ll;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard);
        initData();
        viewPager.setAdapter(new ViewPagerAdapter(GuardActivity.this,views));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ll.getChildAt(position).setEnabled(true);
                ll.getChildAt(count).setEnabled(false);
                count = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        viewPager = (ViewPager) findViewById(R.id.vp);
        views = new ArrayList<>();
        GuideView guideView = new GuideView(GuardActivity.this);
        views.add(guideView.getOneView());
        views.add(guideView.getTwoView());
        views.add(guideView.getThreeView());
        views.add(guideView.getFourView());
        ll = (LinearLayout) findViewById(R.id.ll);

        for(int i = 0; i < views.size(); i ++ ) {
            ll.getChildAt(i).setEnabled(false);
        }
        ll.getChildAt(0).setEnabled(true);
    }
}
