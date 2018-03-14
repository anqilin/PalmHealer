package com.moxi.palmhealer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.palmhealer.R;


/**
 * Created by yinlu on 2016/5/6.
 */
public class DisConnectFragment extends BaseFragment implements View.OnClickListener {
    private MyDeviceFragment deviceFragment;
    TextView disconnect_reconnect_tx;
    private TextView main_bar_tx;
    private ImageView left_back;
    MyDeviceFragment myDeviceFragment;
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mydevicedisconnect_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        Fragment device = getActivity().getSupportFragmentManager().findFragmentByTag("MyDeviceFragment");
        if (device == null) {
            deviceFragment = new MyDeviceFragment();
        } else {
            deviceFragment = (MyDeviceFragment) device;
        }
    }

    private void initView() {
        disconnect_reconnect_tx = (TextView) getView().findViewById(R.id.disconnect_reconnect_tx);
        disconnect_reconnect_tx.setOnClickListener(this);

        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("设备配对");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        Fragment healconfragment = getActivity().getSupportFragmentManager().findFragmentByTag("MyDeviceFragment");
        if (healconfragment == null) {
            myDeviceFragment = new MyDeviceFragment();
        } else {
            myDeviceFragment = (MyDeviceFragment) healconfragment;
        }
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.disconnect_reconnect_tx:
                /*FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.control_maincontent, deviceFragment, "MyDeviceFragment");
                transaction.addToBackStack(null);
                transaction.commit();*/
                getActivity().finish();
                break;
        }
    }

}

