package com.moxi.palmhealer.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;
import com.moxi.palmhealer.R;

import com.moxi.palmhealer.beans.Device;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.CommonUtil;
import com.moxi.palmhealer.utils.LogUtils;
import com.moxi.palmhealer.utils.SharedPrefsUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yinlu on 2016/4/24.
 */
public class MyDeviceFragment extends BaseFragment {
    private final static String TAG = "--MyDeviceFragment---";
    public static final HashMap<String, String> devicenameMap = new HashMap<>();
    private ArrayList<Device> mLeDevices;
    ListView mydevicelist_ls;

    private TextView main_bar_tx;
    private ImageView left_back;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 1;

    public DeviceAddFragment deviceAddFragment;
    public DeviceSureFragment deviceSureFragment;
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mydevicelist_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("我的设备");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        Fragment healconfragment = getActivity().getSupportFragmentManager().findFragmentByTag("DeviceAddFragment");
        if (healconfragment == null) {
            deviceAddFragment = new DeviceAddFragment();
        } else {
            deviceAddFragment = (DeviceAddFragment) healconfragment;
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
       /* Fragment devicefragment = getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment");
        if (devicefragment == null) {
            connectFragment = new ConnectFragment();
        } else {
            connectFragment = (ConnectFragment) devicefragment;
        }*/
        Fragment devicefragment = getActivity().getSupportFragmentManager().findFragmentByTag("DeviceSureFragment");
        if (devicefragment == null) {
            deviceSureFragment = new DeviceSureFragment();
        }else {
            deviceSureFragment=(DeviceSureFragment)devicefragment;
        }


        mydevicelist_ls = (ListView) getView().findViewById(R.id.mydevicelist_ls);
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        mLeDevices = (ArrayList<Device>) CommonUtil.getInstance().getDeviceList(getActivity(), "connectDevice");
        mydevicelist_ls.setAdapter(mLeDeviceListAdapter);
        mydevicelist_ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Device device = mLeDeviceListAdapter.getDevice(position);
                if(position==0){
                    SharedPrefsUtil.putIntValue(getActivity().getApplicationContext(),"temp_set","temp",70);
                }else if(position==1){
                    SharedPrefsUtil.putIntValue(getActivity().getApplicationContext(),"temp_set","temp",90);
                }
                if (device == null) return;

              /*  if (getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment") == null) {
                    bundle.putString("deviceName_EN", device.getDeviceName_EN());
                    connectFragment.setArguments(bundle);
                    LogUtils.debug("----执行了setArguments device");
                } else {
                    LogUtils.debug("----没执行setArguments，直接赋值 device");
                    getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment")
                            .getArguments().putString("device", device.getDeviceName_EN());
                }
                AppConfig.connctDevice=device.getDeviceName_EN();
                LogUtils.debug(TAG, "-----setArguments 的值为" + device.getDeviceName_EN());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.control_maincontent, connectFragment, "ConnectFragment");
                transaction.addToBackStack(null);
                transaction.commit();*/

                if (getActivity().getSupportFragmentManager().findFragmentByTag("DeviceSureFragment") == null) {
                    bundle.putString("deviceName_EN", device.getDeviceName_EN());
                    deviceSureFragment.setArguments(bundle);
                    LogUtils.debug("----执行了setArguments device");
                } else {
                    LogUtils.debug("----没执行setArguments，直接赋值 device");
                    getActivity().getSupportFragmentManager().findFragmentByTag("DeviceSureFragment")
                            .getArguments().putString("deviceName_EN", device.getDeviceName_EN());
                }
                AppConfig.connctDevice=device.getDeviceName_EN();
                LogUtils.debug(TAG, "-----setArguments 的值为" + device.getDeviceName_EN());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.control_maincontent, deviceSureFragment, "DeviceSureFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        initData();
    }

    private void initData() {


        devicenameMap.put("moxibustion", "随身灸");
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
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

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLeDeviceListAdapter.clear();
        mLeDevices.clear();
//        mLeScanCallback = null;
    }


    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {


        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<>();
        }


        public Device getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
//                view = View.inflate(getActivity(), R.layout.listitem_device, null);
                //这种写法高度设置有效
                view = LayoutInflater.from(getActivity()).inflate(R.layout.listitem_device, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.devicePic = (ImageView) view.findViewById(R.id.listitem_pic);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.listitem_name);
                viewHolder.listitem_next = (ImageView) view.findViewById(R.id.listitem_next);
                view.setTag(viewHolder);
                AutoUtils.autoSize(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Device device = mLeDevices.get(i);
            viewHolder.deviceName.setText(device.getDeviceName_CN());
            int resId = getResources().getIdentifier(device.getDevice_Pic(), "mipmap" , getActivity().getPackageName());
            viewHolder.devicePic.setImageResource(resId);


            return view;
        }
    }


    static class ViewHolder {
        ImageView devicePic;
        TextView deviceName;
        ImageView listitem_next;
    }

}
