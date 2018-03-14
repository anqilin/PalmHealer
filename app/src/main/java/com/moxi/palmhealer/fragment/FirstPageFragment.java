package com.moxi.palmhealer.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.activity.ControlActivity;
import com.moxi.palmhealer.activity.HealthActivity;
import com.moxi.palmhealer.activity.RemoteActivity;
import com.moxi.palmhealer.db.DBHelper;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by yinlu on 2016/6/4.
 */
public class FirstPageFragment extends BaseFragment implements View.OnClickListener {
    private BGABanner mAccordionBanner;
    private AutoLinearLayout yangshengsuo;
    private AutoLinearLayout yaokongqi;
    private DBHelper dbHelper = null;// 数据库对象

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_page_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        mAccordionBanner = (BGABanner) getView().findViewById(R.id.banner_main_accordion);
        // 用Java代码方式设置切换动画
//        mAccordionBanner.setTransitionEffect(BGABanner.TransitionEffect.Rotate);
        // banner.setPageTransformer(new RotatePageTransformer());
        // 设置page切换时长
        mAccordionBanner.setPageChangeDuration(1000);
        List<View> views = new ArrayList<>();
        views.add(getPageView(R.mipmap.guide_1));
        views.add(getPageView(R.mipmap.guide_2));
        views.add(getPageView(R.mipmap.guide_3));

        mAccordionBanner.setViews(views);
        // banner.setCurrentItem(1);


        yangshengsuo = (AutoLinearLayout) getView().findViewById(R.id.yangshengsuo);
        yaokongqi = (AutoLinearLayout) getView().findViewById(R.id.yaokongqi);

        yangshengsuo.setOnClickListener(this);
        yaokongqi.setOnClickListener(this);
    }

    private View getPageView(@DrawableRes int resid) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yangshengsuo:
                Intent intent = new Intent(getActivity(), HealthActivity.class);
                startActivity(intent);
                break;
            case R.id.yaokongqi:
                dbHelper = new DBHelper(getActivity().getApplicationContext());
                Cursor cur = dbHelper.queryRecently();
                if (cur.getCount() > 0) {
                    Intent controlintent = new Intent(getActivity(), RemoteActivity.class);
                    startActivity(controlintent);
                } else {
                    Intent controlintent = new Intent(getActivity(), ControlActivity.class);
                    startActivity(controlintent);
                }

                if (cur != null) {
                    cur.close();
                    cur = null;
                }
                if (dbHelper != null) {
                    dbHelper.close();
                    dbHelper = null;
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }
}
