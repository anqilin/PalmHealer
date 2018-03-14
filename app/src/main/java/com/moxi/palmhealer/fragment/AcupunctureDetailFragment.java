package com.moxi.palmhealer.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.activity.ControlActivity;
import com.moxi.palmhealer.activity.RemoteActivity;
import com.moxi.palmhealer.beans.BodyPoint;
import com.moxi.palmhealer.db.DBHelper;
import com.moxi.palmhealer.utils.LogUtils;
import com.moxi.palmhealer.utils.XmlParser;
import com.zhy.autolayout.AutoFrameLayout;


/**
 * Created by yinlu on 2016/7/22.
 */
public class AcupunctureDetailFragment extends BaseFragment {
    private final static String TAG = "---AcupunctureDetailFragment---";

    private String pointName;
    AutoFrameLayout point_rela;
    TextView point_loaction;
    TextView point_features;
    String point_location_str;
    String point_features_str;
    String point_pic;
    BodyPoint bodyPoint;
    private TextView main_bar_tx;
    private ImageView left_back;
    public TextView point_start;
    private DBHelper dbHelper = null;// 数据库对象
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.point_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pointName = getArguments().getString("pointName");
        LogUtils.debug(TAG, "pointName=" + pointName);
        getArguments().clear();
        initView();
        initData();
    }

    private void initData() {
        if (XmlParser.pointMap.isEmpty()) {
            try {
                XmlParser.getInstance().parsePoint(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        bodyPoint = XmlParser.pointMap.get(pointName);
        LogUtils.debug(TAG, "bodypoint=" + bodyPoint);
        point_location_str = bodyPoint.getLocation();
        point_features_str = bodyPoint.getFeatures();
        point_pic = bodyPoint.getPicName();

        Context ctx = getActivity().getBaseContext();
        int resId = getResources().getIdentifier(point_pic, "mipmap", ctx.getPackageName());

        point_rela.setBackgroundResource(resId);
        point_loaction.setText(point_location_str);
        point_features.setText(point_features_str);
    }

    private void initView() {
        point_start = (TextView) getView().findViewById(R.id.point_start);
        point_rela = (AutoFrameLayout) getView().findViewById(R.id.point_rela);
        point_loaction = (TextView) getView().findViewById(R.id.point_loaction);
        point_features = (TextView) getView().findViewById(R.id.point_features);
        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("穴位功效");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        point_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断之前是否已经连接了遥控器，如果没有连接的话，就进入到连接界面
                dbHelper = new DBHelper(getActivity().getApplicationContext());
                Cursor cur = dbHelper.queryRecently();
                if (cur.getCount() > 0) {
                    Intent controlintent = new Intent(getActivity(), RemoteActivity.class);
                    startActivity(controlintent);
                } else {
                    Intent controlintent = new Intent(getActivity(), ControlActivity.class);
                    startActivity(controlintent);
                }

                if (dbHelper != null) {
                    dbHelper.close();
                    dbHelper = null;
                }
            }
        });
    }
}
