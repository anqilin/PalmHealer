package com.moxi.palmhealer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.beans.BodyPoint;
import com.moxi.palmhealer.utils.XmlParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinlu on 2016/6/29. 暂时不用
 */
public class AcupunctureFragment extends BaseFragment {
    public List<Fragment> pointFragmentList = new ArrayList<Fragment>();
    public ViewPager viewPager;
    public FragmentStatePagerAdapter mfragmentAdapter;
    List<BodyPoint> pointList = new ArrayList<>();
    View view;
    private TextView main_bar_tx;
    private ImageView left_back;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.point_detail, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (XmlParser.pointList.size() == 0 || XmlParser.pointList == null) {
            try {
                XmlParser.getInstance().parsePoint(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("穴位功效");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        pointList = XmlParser.pointList;
        viewPager = (ViewPager) getView().findViewById(R.id.point_viewpager);
        for (int i = 0; i < pointList.size(); i++) {
            PointFragment pointFg = (PointFragment) PointFragment.newInstance(pointList.get(i));
            pointFragmentList.add(pointFg);

        }

        mfragmentAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int arg0) {

                return pointFragmentList.get(arg0);
            }

            @Override
            public int getCount() {

                return pointFragmentList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup arg0, int arg1) {

                return super.instantiateItem(arg0, arg1);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                super.destroyItem(container, position, object);
            }
        };
        viewPager.setAdapter(mfragmentAdapter);
        viewPager.addOnPageChangeListener(new ContentChangeListener());
        viewPager.setCurrentItem(0);

    }

    private class ContentChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // Nothing to do
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // Nothing to do
        }

        @Override
        public void onPageSelected(int position) {
        }

    }

}
