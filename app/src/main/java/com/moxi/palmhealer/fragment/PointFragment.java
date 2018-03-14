package com.moxi.palmhealer.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.activity.ControlActivity;
import com.moxi.palmhealer.activity.RemoteActivity;
import com.moxi.palmhealer.beans.BodyPoint;
import com.moxi.palmhealer.db.DBHelper;
import com.zhy.autolayout.AutoFrameLayout;

/**
 * Created by yinlu on 2016/4/16.
 */
public class PointFragment extends Fragment {

    public AutoFrameLayout point_rela;
    public TextView point_loaction;
    public TextView point_features;
    public TextView point_start;
    ControlorFragment controlorFragment;
    private DBHelper dbHelper = null;// 数据库对象
    View pointView;

    public static Fragment newInstance(BodyPoint point) {
        PointFragment fragment = new PointFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("point", point);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        pointView = inflater.inflate(R.layout.point_layout, container, false);


        return pointView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        point_rela = (AutoFrameLayout) pointView.findViewById(R.id.point_rela);
        point_loaction = (TextView) pointView.findViewById(R.id.point_loaction);
        point_features = (TextView) pointView.findViewById(R.id.point_features);
        point_start = (TextView) pointView.findViewById(R.id.point_start);

        BodyPoint bodyPoint = (BodyPoint) getArguments().getSerializable("point");
        int resId = getResources().getIdentifier(bodyPoint.picName, "mipmap", this.getActivity().getPackageName());
        point_rela.setBackgroundResource(resId);
        point_loaction.setText(bodyPoint.location);
        point_features.setText(bodyPoint.features);
        Fragment controltarget = getFragmentManager().findFragmentByTag("ControlorFragment");
        if (controltarget == null)
            controlorFragment = new ControlorFragment();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }

    }
}
