package com.moxi.palmhealer.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.LogUtils;

import java.util.ArrayList;


/**
 * Created by yinlu on 2016/4/28.
 */
public class ConnectFragment extends DataFragment implements ConnectService, BluetoothAdapter.LeScanCallback {
    private final static String TAG = ConnectFragment.class.getSimpleName();
    FrameLayout connect_cicle;
    ImageView connect_cicle_iv;
    ImageView connect_device_iv;
    public DisConnectFragment disconnectFragment;
    public DeviceConnetedFragment deviceConnetedFragment;
    public  DeviceConnetedSureFragment deviceConnetedSureFragment;
    public MyDeviceFragment myDeviceFragment;
    Bundle bundle = new Bundle();
    private TextView main_bar_tx;
    private ImageView left_back;

    private boolean mScanning;
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 20 seconds.
    private static final long SCAN_PERIOD = 20000;
    private ArrayList<BluetoothDevice> mLeDevices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mydeviceconnect_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (AppConfig.connctDevice.length() == 0)
            AppConfig.connctDevice = getArguments().getString("deviceName_EN");
        LogUtils.debug(TAG, "---------getArguments().getParcelable(\"device\")--=" + AppConfig.connctDevice);
        getArguments().clear();
        mybindService();
        initView();
        initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectService = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        myConnetService();
    }



    private void initData() {
//        mDeviceAddress = device.getAddress();
//        deviceName_EN = device.getName();
        mHandler = new Handler();
        mLeDevices = new ArrayList<>();
        LogUtils.debug(TAG, "-------mDeviceAddress=" + mDeviceAddress + ",-----mDeviceName=" + deviceName_EN);

        Fragment disfragment = getActivity().getSupportFragmentManager().findFragmentByTag("DisConnectFragment");
        if (disfragment == null) {
            disconnectFragment = new DisConnectFragment();
        } else {
            disconnectFragment = (DisConnectFragment) disfragment;
        }
      /*  Fragment addfragment = getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedFragment");
        if (addfragment == null) {
            deviceConnetedFragment = new DeviceConnetedFragment();
        } else {
            deviceConnetedFragment = (DeviceConnetedFragment) addfragment;
        }*/
         Fragment addfragment = getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedSureFragment");
        if (addfragment == null) {
            deviceConnetedSureFragment = new DeviceConnetedSureFragment();
        } else {
            deviceConnetedSureFragment = (DeviceConnetedSureFragment) addfragment;
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        scanLeDevice(true);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment dcfrag = getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment");
                if (dcfrag != null && dcfrag.isVisible()) {
                    LogUtils.debug(TAG, "没有扫描到，15秒后显示未连接");
                    switchFragment(false, "");
                }
            }
        }, 15000);
    }

    public void scanLeDevice(final boolean enable) {
        LogUtils.debug(TAG, "-----------开始扫描蓝牙=" + enable);
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScanOuter();
                }
            }, SCAN_PERIOD);
            LogUtils.debug("----------startLeScan--");
            mScanning = true;
            mBluetoothAdapter.startLeScan(this);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(this);
        }
    }

    private void stopScanOuter() {
        mScanning = false;
        mBluetoothAdapter.stopLeScan(this);
    }

    private void initView() {
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
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    LogUtils.debug(TAG, "执行了回调方法");
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        connect_cicle = (FrameLayout) getView().findViewById(R.id.connect_cicle);
        connect_cicle_iv = (ImageView) getView().findViewById(R.id.connect_cicle_iv);
        connect_device_iv=(ImageView)getView().findViewById(R.id.connect_device_iv);
        if (AppConfig.connctDevice.equals("moxibustion")==false){
            connect_device_iv.setBackgroundResource(R.mipmap.lishisearch);
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(connect_cicle_iv, "rotation", 0f, 360f);
        animator.setDuration(2000);//动画时间
        animator.setInterpolator(new LinearInterpolator());//动画插值
        animator.setRepeatCount(-1);//设置动画重复次数
        animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
        animator.setStartDelay(100);//动画延时执行
        animator.start();//启动动画
    }

    @Override
    public void switchFragment(boolean connect, String deviceName_EN) {

        LogUtils.debug(TAG, "----switchFragment " + connect + ",deviceName_EN = " + deviceName_EN);
        if (connect) {

            //序列化保存可以连接的device信息，BLE设备的mac地址


            /*if (getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedFragment") == null) {
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
            transaction.commit();*/
            if (getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedSureFragment") == null) {
                bundle.putString("deviceName_EN", deviceName_EN);
                bundle.putString("mDeviceAddress", mDeviceAddress);
                deviceConnetedSureFragment.setArguments(bundle);
                LogUtils.debug("----执行了setArguments device");
            } else {
                getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedSureFragment")
                        .getArguments().putString("deviceName_EN", deviceName_EN);
                getActivity().getSupportFragmentManager().findFragmentByTag("DeviceConnetedSureFragment")
                        .getArguments().putString("mDeviceAddress", mDeviceAddress);
            }


            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.right_in, R.anim.left_out);
            transaction.replace(R.id.control_maincontent, deviceConnetedSureFragment, "DeviceConnetedSureFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.right_in, R.anim.left_out);
            transaction.replace(R.id.control_maincontent, disconnectFragment, "DisConnectFragment");
            transaction.commit();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.debug(TAG, "-----------onDestroyView---------");
        LogUtils.debug(TAG, "------mBluetoothLeService  onDestroyView" + mBluetoothLeService);

        scanLeDevice(false);
        mLeDevices.clear();

        getActivity().unregisterReceiver(mGattUpdateReceiver);
//        disConnectService();
        getActivity().unbindService(mServiceConnection);
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        Fragment dcfrag = getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment");


        if (dcfrag != null && dcfrag.isVisible()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.debug(TAG, "---------获得设备" + device);
                    addDevice(device);
                }
            });
        }
    }

    public void addDevice(BluetoothDevice device) {
        if (!mLeDevices.contains(device)) {
            LogUtils.debug("----before-mLeDevices=" + mLeDevices);
            LogUtils.debug("----before-connect_device=" + AppConfig.connctDevice);
            LogUtils.debug("----before-connect_device.getName()=" + device.getName());
            boolean Islishi=false;
            if(AppConfig.connctDevice.equals(AppConfig.lishidevice)&&device.getName().contains("AJY")){
                Islishi=true;
            }

            if ((device.getName() != null && device.getName().equals(AppConfig.connctDevice))||Islishi) {
            //if (device.getName() != null ) {
                LogUtils.debug(TAG, "connect_device=" + AppConfig.connctDevice);
                mLeDevices.add(device);
                mDeviceAddress = device.getAddress();
                deviceName_EN = device.getName();
                if(deviceName_EN.contains("AJY")){
                    deviceName_EN="moxibusion2";
                }

                myConnetService();
            }
            LogUtils.debug("---after--mLeDevices=" + mLeDevices);
        }

    }
}
