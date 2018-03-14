package com.moxi.palmhealer.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.activity.ControlActivity;
import com.moxi.palmhealer.activity.RemoteActivity;
import com.moxi.palmhealer.beans.Disease;
import com.moxi.palmhealer.db.DBHelper;
import com.moxi.palmhealer.utils.LogUtils;
import com.moxi.palmhealer.utils.XmlParser;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinlu on 2016/6/29.
 */
public class DiseaseDetailFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = ConnectFragment.class.getSimpleName();
    public static String diseaseName;
    public List<String> picNameList;
    public List<Integer> picNameIds;
    public Disease disease;
    public List<String[]> relelist;
    private ViewPager viewPager;
    public List<Disease.PicRele> picReleList;
    public String[] rele;
    private List<ImageView> imageViewContainer = null;
    public TextView disease_care_tx;
    public TextView disease_control_start;
    public TextView disease_control_start_other;
    private TextView main_bar_tx;
    private ImageView left_back;
    AutoLinearLayout disease_detail_layout;
    public ViewGroup detail_other;
    private DBHelper dbHelper = null;// 数据库对象
    ImageView disease_detail_right;
    ImageView disease_detail_left;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.disease_detail_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        diseaseName = getArguments().getString("diseaseName");
        getArguments().clear();

        if (diseaseName != null && diseaseName.length() > 0 && diseaseName.equals("其他")) {
            disease_detail_layout = (AutoLinearLayout) getView().findViewById(R.id.disease_detail_layout);
            disease_detail_layout.removeAllViews();
            LinearLayout.LayoutParams prams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            detail_other = (ViewGroup) View.inflate(getActivity(), R.layout.disease_detail_other, null);
            disease_detail_layout.addView(detail_other, prams);

            main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
            main_bar_tx.setText("其他常见病");
            left_back = (ImageView) getView().findViewById(R.id.left_back);
            left_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            });
            disease_control_start_other = (TextView) getView().findViewById(R.id.disease_control_start_other);
            disease_control_start_other.setOnClickListener(this);
        } else {
            if (diseaseName != null && diseaseName.length() > 0)
                initData();
            initView();
        }

    }

    private void initView() {
        disease_detail_right = (ImageView) getView().findViewById(R.id.disease_detail_right);
        disease_detail_left = (ImageView) getView().findViewById(R.id.disease_detail_left);
        disease_detail_left.setOnClickListener(this);
        disease_detail_right.setOnClickListener(this);

        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText(diseaseName);
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });


        viewPager = (ViewPager) getView().findViewById(R.id.view_pager_pic);
        //填充imageview
        for (int i = 0; i < picReleList.size(); i++) {
            String picName = picReleList.get(i).picName;
            rele = picReleList.get(i).rele.split("\\|");

            picNameList.add(picName);

            picNameIds.add(getResources().getIdentifier(picName, "mipmap", getActivity().getPackageName()));
            relelist.add(rele);
        }

        LogUtils.debug("picNameList", "-----数据测试-----picNameList=" + picNameList);
        imageViewContainer = new ArrayList<>();
        int[] imageIDs = new int[picNameIds.size()];
        for (int i = 0; i < picNameIds.size(); i++) {
            imageIDs[i] = picNameIds.get(i);
        }

        if (picNameIds.size() > 1) {
            disease_detail_right.setVisibility(View.VISIBLE);
        }

        ImageView imageView = null;
        for (int id : imageIDs) {
            imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(id);
            imageViewContainer.add(imageView);

        }

        //注意事项
        disease_care_tx = (TextView) getView().findViewById(R.id.disease_care_tx);
        disease_care_tx.setText(disease.care);
        /*启动*/
        disease_control_start = (TextView) getView().findViewById(R.id.disease_control_start);
        disease_control_start.setOnClickListener(this);


        viewPager.setAdapter(new ContentAdapter());
        viewPager.addOnPageChangeListener(new ContentChangeListener());
        viewPager.setCurrentItem(0);
    }

    private void initData() {
        if (XmlParser.diseaseMap.isEmpty()) {
            try {
                XmlParser.getInstance().parseDisease(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        picReleList = new ArrayList<>();
        picNameList = new ArrayList<>();
        picNameIds = new ArrayList<>();
        relelist = new ArrayList<>();
        LogUtils.debug("-----数据测试---diseaseName=" + diseaseName);
        disease = XmlParser.diseaseMap.get(diseaseName);
        LogUtils.debug("-----数据测试---disease=" + disease);
        picReleList.clear();
        picNameList.clear();
        picNameIds.clear();
        picReleList = disease.list;
        LogUtils.debug("-----数据测试---picReleList=" + picReleList);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.disease_control_start:
            case R.id.disease_control_start_other:
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
                break;
            case R.id.disease_detail_left:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                break;

            case R.id.disease_detail_right:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

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

    private class ContentAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewContainer.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(imageViewContainer.get(position));
            return imageViewContainer.get(position);
        }

        @Override
        public int getCount() {
            return imageViewContainer.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private class ContentChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            LogUtils.debug(TAG, "position=" + position);
            LogUtils.debug(TAG, "size=" + picNameIds.size());
            position = position + 1;
            if (position == picNameIds.size()) {
                disease_detail_right.setVisibility(View.GONE);
                disease_detail_left.setVisibility(View.VISIBLE);
            } else if (position > 1 && position < picNameIds.size()) {
                disease_detail_right.setVisibility(View.VISIBLE);
                disease_detail_left.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                disease_detail_right.setVisibility(View.VISIBLE);
                disease_detail_left.setVisibility(View.GONE);
            }

        }

    }

}
