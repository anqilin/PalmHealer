package com.moxi.palmhealer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.db.DBHelper;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.LogUtils;

/**
 * Created by anqilin on 17/8/5.
 */

public class DeviceConnetedSureFragment  extends DataFragment implements View.OnClickListener {
    private final static String TAG = "---DeviceConnetedSureFragment---";
    String device_Name;
    String device_Name_EN;
    String device_Mac;
    TextView Connect_Sure;
    TextView Connect_Not;
    private TextView main_bar_tx;
    private ImageView left_back;
    boolean isReceiveData = true;
    //数据交互
    public DeviceConnetedFragment deviceConnetedFragment;
    Bundle bundle = new Bundle();
    private DBHelper dbHelper = null;// 数据库对象
    private Handler myhandler=new Handler();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_con_sure, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deviceName_EN = getArguments().getString("deviceName_EN");
        mDeviceAddress = getArguments().getString("mDeviceAddress");
        getArguments().clear();
    }

    private void initData() {
       // mDeviceAddress = device_Mac;
       // deviceName_EN = device_Name_EN;
        mybindService();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConfig.CONTROLER_DISCONNECTED);
        getActivity().registerReceiver(controlFragmentReceiver, intentFilter);
        Fragment addfragment = getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedFragment");
        if (addfragment == null) {
            deviceConnetedFragment = new DeviceConnetedFragment();
        } else {
            deviceConnetedFragment = (DeviceConnetedFragment) addfragment;
        }
    }

    public final BroadcastReceiver controlFragmentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (AppConfig.CONTROLER_DISCONNECTED.equals(action)) {
                disConnectService();
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        initView();
        initData();
        myConnetService();
        myhandler.postDelayed(runnable,200);

    }

    private void initView() {
        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("是否连接此设备");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
        Connect_Sure=(TextView)getView().findViewById(R.id.mydevice_sure_connn);
        Connect_Not=(TextView)getView().findViewById(R.id.mydevice_not_connn);
        Connect_Sure.setOnClickListener(this);
        Connect_Not.setOnClickListener(this);
       /* dbHelper = new DBHelper(getActivity().getApplicationContext());
        Cursor cur = dbHelper.queryRecently();
        while (cur.moveToNext()) {
            LogUtils.debug(TAG, "一共有多少行数据=" + cur.getCount());
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                int device_mac_int = cur.getColumnIndex("device_mac");
                int device_name_int = cur.getColumnIndex("device_name");
                int device_name_EN_int = cur.getColumnIndex("device_name_EN");
                device_Mac = cur.getString(device_mac_int);
                device_Name = cur.getString(device_name_int);
                device_Name_EN = cur.getString(device_name_EN_int);
                LogUtils.debug(TAG, "查询出来的device为=" + device_Name + device_Mac);
            }
        }
        if (cur != null) {
            cur.close();
            cur = null;
        }*/

    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            LogUtils.debug(TAG, "-------button hongguang=" + "true");
            controlled(true);
            myhandler.postDelayed(runnable,1000);
        }
    };



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.debug(TAG, "-----------onDestroyView---------");
        LogUtils.debug(TAG, "------mBluetoothLeService  onDestroyView" + mBluetoothLeService);
        myhandler.removeCallbacks(runnable);
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
        getActivity().unregisterReceiver(mGattUpdateReceiver);
//        disConnectService();
        getActivity().unbindService(mServiceConnection);
        getActivity().unregisterReceiver(controlFragmentReceiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mydevice_sure_connn:
                if (getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedFragment") == null) {
                    bundle.putString("deviceName_EN", deviceName_EN);
                    bundle.putString("mDeviceAddress", mDeviceAddress);
                    deviceConnetedFragment.setArguments(bundle);
                    LogUtils.debug("----执行了setArguments device");
                } else {
                    getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedFragment")
                            .getArguments().putString("deviceName_EN", deviceName_EN);
                    getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedFragment")
                            .getArguments().putString("mDeviceAddress", mDeviceAddress);
                }


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.control_maincontent, deviceConnetedFragment, "DeviceConnetedFragment");
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.left_back:
                getActivity().finish();
                break;
            case  R.id.mydevice_not_connn:
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
