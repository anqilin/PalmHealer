package com.moxi.palmhealer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.LogUtils;


public class DeviceSureFragment extends BaseFragment implements View.OnClickListener{

    private final static String TAG = "--DeviceSureFragment---";
    public DeviceSureFragment deviceSureFragment;
    public ConnectFragment connectFragment;
    public TextView device_add_tx;
    private TextView main_bar_tx;
    private ImageView left_back;
    private  String DeviceName;
    private  TextView sure;
    private  TextView go_back;
    Bundle bundle = new Bundle();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_sure, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DeviceName = getArguments().getString("deviceName_EN");
        getArguments().clear();
        initView();
    }

    private void initView() {
        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("我的设备");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
        sure=(TextView) getView().findViewById(R.id.device_sure_con);
        go_back=(TextView) getView().findViewById(R.id.device_not_con);
        sure.setOnClickListener(this);
        go_back.setOnClickListener(this);

        Fragment devicefragment = getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment");
        if (devicefragment == null) {
            connectFragment = new ConnectFragment();
        } else {
            connectFragment = (ConnectFragment) devicefragment;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device_sure_con:

                /*FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.control_maincontent, deviceSureFragment, "DeviceSureFragment");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();*/
                if (getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment") == null) {
                    bundle.putString("deviceName_EN", DeviceName);
                    connectFragment.setArguments(bundle);
                    LogUtils.debug("----执行了setArguments device");
                } else {
                    LogUtils.debug("----没执行setArguments，直接赋值 device");
                    getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment")
                            .getArguments().putString("deviceName_EN", DeviceName);
                }
                AppConfig.connctDevice=DeviceName;
                LogUtils.debug(TAG, "-----setArguments 的值为" + DeviceName);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.control_maincontent, connectFragment, "ConnectFragment");
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.device_not_con:
                getActivity().finish();
                break;
            case R.id.left_back:
                getActivity().finish();
                break;
            default:
                break;
        }
    }




}
