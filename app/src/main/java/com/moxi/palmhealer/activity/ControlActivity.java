package com.moxi.palmhealer.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.beans.Device;
import com.moxi.palmhealer.fragment.ControlorFragment;
import com.moxi.palmhealer.fragment.DeviceAddFragment;
import com.moxi.palmhealer.fragment.MyDeviceFragment;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.CommonUtil;
import com.moxi.palmhealer.utils.LogUtils;

import java.io.IOException;

/**
 * Created by yinlu on 2016/6/8.
 */
public class ControlActivity extends AppCompatActivity {
    private final static String TAG = "--ControlActivity---";
    public DeviceAddFragment deviceAddFragment;
    public MyDeviceFragment myDeviceFragment;
    FrameLayout control_maincontent;
    ControlorFragment controlorFragment;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private long clickTime = 0; //记录第一次点击的时间

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_main_layout);
        initView();
        initData();
    }

    private void initData() {

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
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

        //实例化 device
        Device device = new Device();
        device.deviceName_EN = "moxibustion";
        device.deviceName_CN = "便携式艾灸仪";
        device.device_Pic = "portable_moxibustion";
        //保存到SP中
        Device device2 = new Device();
        device2.deviceName_EN = AppConfig.lishidevice;
        device2.deviceName_CN = "立式艾灸仪";
        device2.device_Pic = "portable_moxibustion";
        try {
            saveObject(CommonUtil.getInstance().serialize(device), "bluetoothDevice");
            saveObject2(CommonUtil.getInstance().serialize(device2), "bluetoothDevice");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void saveObject(String strObject, String preferName) {
        SharedPreferences sp = getSharedPreferences(preferName, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("moxibustion", strObject);
        edit.commit();
    }
    void saveObject2(String strObject, String preferName) {
        SharedPreferences sp = getSharedPreferences(preferName, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(AppConfig.lishidevice, strObject);
        edit.commit();
    }

    private void initView() {

        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

//        DBHelper dbHelper = new DBHelper(getApplicationContext());
//        Cursor cur = dbHelper.queryRecently();
//
//
//        if (cur.getCount() > 0) {
//            Fragment controltarget = getSupportFragmentManager().findFragmentByTag("ControlorFragment");
//            if (controltarget == null) {
//                controlorFragment = new ControlorFragment();
//            } else {
//                controlorFragment = (ControlorFragment) controltarget;
//            }
//            Intent controlintent = new Intent(this, RemoteActivity.class);
//            startActivity(controlintent);
////            FragmentTransaction controlorFragmenttransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
////                    R.anim.right_in, R.anim.left_out);
////            controlorFragmenttransaction.replace(R.id.control_maincontent, controlorFragment, "ControlorFragment");
////            controlorFragmenttransaction.commit();
//        } else {
//
//        }
        control_maincontent = (FrameLayout) findViewById(R.id.control_maincontent);
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        LogUtils.debug(TAG, "---key的值" + key);

        if (key != null && key.equals("from_remote")) {
            LogUtils.debug(TAG, "进入了from_remote");
            Fragment devicefragment = getSupportFragmentManager().findFragmentByTag("MyDeviceFragment");
            if (devicefragment == null) {
                myDeviceFragment = new MyDeviceFragment();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.right_in, R.anim.left_out);
            transaction.replace(R.id.control_maincontent, myDeviceFragment, "MyDeviceFragment");
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        } else {
            LogUtils.debug(TAG, "没有进入from_remote");
            Fragment healconfragment = getSupportFragmentManager().findFragmentByTag("DeviceAddFragment");
            if (healconfragment == null) {
                deviceAddFragment = new DeviceAddFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.control_maincontent, deviceAddFragment, "DeviceAddFragment").commit();
        }


    }

    @Override
    protected void onDestroy() {
        LogUtils.debug(TAG, "------调用了onDestroy");
//        if (DataFragment.isConnect){
//            LogUtils.debug(TAG, "-----ondestroy="+DataFragment.mServiceConnection);
//            unbindService(DataFragment.mServiceConnection);
//            DataFragment.disConnectService();
//        }
        super.onDestroy();

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
////            moveTaskToBack(true);//true对任何Activity都适用
//            setContentView(R.layout.layout_forever);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public void onBackPressed() {
//        Fragment dsfrag = getSupportFragmentManager().findFragmentByTag("DisConnectFragment");
//        Fragment crfrag = getSupportFragmentManager().findFragmentByTag("ControlorFragment");
//        if ((dsfrag != null && dsfrag.isVisible()) ||
//                (crfrag != null && crfrag.isVisible())) {
//            finish();
//        }
//        super.onBackPressed();
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Fragment dsfrag = getSupportFragmentManager().findFragmentByTag("DisConnectFragment");
        Fragment crfrag = getSupportFragmentManager().findFragmentByTag("ControlorFragment");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((dsfrag != null && dsfrag.isVisible()) ||
                    (crfrag != null && crfrag.isVisible())) {
                finish();
                exit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次后退键退出遥控器页面",
                    Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            LogUtils.debug("-----exit application");
            this.finish();
            System.exit(0);
        }
    }
}
