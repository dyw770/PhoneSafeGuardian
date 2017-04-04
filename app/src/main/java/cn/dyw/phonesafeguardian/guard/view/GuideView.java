package cn.dyw.phonesafeguardian.guard.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import cn.dyw.phonesafeguardian.R;

/**
 * Created by dyw on 2017/3/28.
 */

public class GuideView {

    private Context content;

    public GuideView(Context content) {
        this.content = content;
    }

    public View getOneView() {
        View oneView = LayoutInflater.from(content).inflate(R.layout.guide_one, null);
        return oneView;
    }

    public View getTwoView() {
        View twoView = LayoutInflater.from(content).inflate(R.layout.guide_two, null);
        return twoView;
    }
    public View getThreeView() {
        View threeView = LayoutInflater.from(content).inflate(R.layout.guide_three, null);
        return threeView;
    }
    public View getFourView() {
        View fourView = LayoutInflater.from(content).inflate(R.layout.guide_four, null);
        return fourView;
    }
}
