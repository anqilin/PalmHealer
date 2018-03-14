package com.moxi.palmhealer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.utils.LogUtils;
import com.moxi.palmhealer.utils.XmlParser;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yinlu on 2016/6/29.
 */
public class DiseaseFragment extends BaseFragment {
    DiseaseDetailFragment diseaseDetailFragment;
    ListView disease_list;
    List<String> diseases;
    MydeseaseListAdapter mydeseaseListAdapter;
    Context context;
    Bundle bundle = new Bundle();
    private TextView main_bar_tx;
    private ImageView left_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.disease_item, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (XmlParser.diseaseMap.isEmpty()) {
            try {
                XmlParser.getInstance().parseDisease(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Fragment target = getActivity().getSupportFragmentManager().findFragmentByTag("DiseaseDetailFragment");
        if (target == null) {
            diseaseDetailFragment = new DiseaseDetailFragment();
        } else {
            diseaseDetailFragment = (DiseaseDetailFragment) target;
        }
        initView();
        initData();
    }

    private void initData() {
        context = getActivity();
        String[] diseaArr = {"发烧", "失眠", "高血压", "颈椎病", "关节炎", "糖尿病", "女性病", "男性病", "肠胃炎", "手脚冷", "其他"};
        diseases = Arrays.asList(diseaArr);
        mydeseaseListAdapter = new MydeseaseListAdapter();
        disease_list.setAdapter(mydeseaseListAdapter);


        disease_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity().getSupportFragmentManager().findFragmentByTag("DiseaseDetailFragment") == null) {
                    bundle.putString("diseaseName", diseases.get(position));
                    diseaseDetailFragment.setArguments(bundle);
                    LogUtils.debug("----执行了setArguments");
                } else {
                    getActivity().getSupportFragmentManager().findFragmentByTag("DiseaseDetailFragment")
                            .getArguments().putString("diseaseName", diseases.get(position));
                }


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.health_maincontent, diseaseDetailFragment, "DiseaseDetailFragment");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


    }

    private void initView() {
        disease_list = (ListView) getView().findViewById(R.id.disease_list);
        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("常见病");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }


    class MydeseaseListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return diseases.size();
        }

        @Override
        public Object getItem(int position) {
            return diseases.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
//                view = View.inflate(context, R.layout.disease_list_item, null);
                //这种写法高度设置有效
                view = LayoutInflater.from(getActivity()).inflate(R.layout.disease_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.disease_item_num_tx = (TextView) view.findViewById(R.id.disease_item_num_tx);
                viewHolder.disease_item_tx = (TextView) view.findViewById(R.id.disease_item_tx);
                view.setTag(viewHolder);
                AutoUtils.autoSize(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }


            viewHolder.disease_item_num_tx.setText(String.valueOf(position + 1));
            viewHolder.disease_item_tx.setText(diseases.get(position));
            return view;
        }

    }

    static class ViewHolder {
        TextView disease_item_num_tx;
        TextView disease_item_tx;
    }


}
