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

/**
 * Created by yinlu on 2016/6/9.
 */
public class DeviceAddFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = "--DeviceAddFragment---";
    public MyDeviceFragment myDeviceFragment;
    public TextView device_add_tx;
    private TextView main_bar_tx;
    private ImageView left_back;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_add_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("遥控器");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
        device_add_tx = (TextView) getView().findViewById(R.id.device_add_tx);
        device_add_tx.setOnClickListener(this);
        Fragment devicefragment = getActivity().getSupportFragmentManager().findFragmentByTag("MyDeviceFragment");
        if (devicefragment == null) {
            myDeviceFragment = new MyDeviceFragment();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device_add_tx:
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.control_maincontent, myDeviceFragment, "MyDeviceFragment");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.left_back:
                getActivity().finish();
                break;
            default:
                break;
        }
    }


}
