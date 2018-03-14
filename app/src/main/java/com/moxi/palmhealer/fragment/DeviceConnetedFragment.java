package com.moxi.palmhealer.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.activity.RemoteActivity;
import com.moxi.palmhealer.beans.Device;
import com.moxi.palmhealer.db.DBHelper;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.CommonUtil;
import com.moxi.palmhealer.utils.LogUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yinlu on 2016/5/6.
 */
public class DeviceConnetedFragment extends DataFragment implements View.OnClickListener {
    private final static String TAG = "--DeviceConnetedFragment---";
    Device mydevice = null;
    String deviceName_EN;
    TextView connect_device_tx;
    MyDeviceFragment deviceFragment;
    ControlorFragment controlorFragment;
    private TextView main_bar_tx;
    ImageView left_back;
    ImageView mydevice_conn;
    EditText mydeviceconn_deviceName;
    private DBHelper dbHelper = null;// 数据库对象
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mydeviceconn_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        deviceName_EN = getArguments().getString("deviceName_EN");
        mDeviceAddress = getArguments().getString("mDeviceAddress");

        getArguments().clear();

        if (deviceName_EN.length() > 0 && deviceName_EN != null) {
            try {
                mydevice = CommonUtil.getInstance().deSerialization(getObject(deviceName_EN, "bluetoothDevice"));
                mydevice.device_mac = mDeviceAddress;
                Log.i(TAG, "反序列化得到的device =" + mydevice.toString());
                //将可以连接的设备保存到数据库中
                ContentValues contentValues = new ContentValues();
                DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(currentTime);

                ContentValues values = new ContentValues();
                values.put("device_mac", mydevice.device_mac);
                values.put("device_name", "");
                values.put("device_name_EN", deviceName_EN);
                values.put("latest", dateString);
                if (deviceName_EN.equals("moxibustion")) {
                    values.put("deviceName_CN", "便携式艾灸仪");
                    values.put("device_Pic", "portable_moxibustion");
                }
                dbHelper.insert(values);

                LogUtils.debug(TAG, "插入数据库成功了");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        mydeviceconn_deviceName = (EditText) getView().findViewById(R.id.mydeviceconn_deviceName);

        connect_device_tx = (TextView) getView().findViewById(R.id.connect_device_tx);
        mydevice_conn=(ImageView) getView().findViewById(R.id.mydevice_conn);
        if (AppConfig.connctDevice.equals("moxibustion")==false){
            mydevice_conn.setBackgroundResource(R.mipmap.lishiaddname);
        }
        connect_device_tx.setOnClickListener(this);

        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("配对成功");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
        Fragment device = getActivity().getSupportFragmentManager().findFragmentByTag("MyDeviceFragment");
        if (device == null) {
            deviceFragment = new MyDeviceFragment();
        } else {
            deviceFragment = (MyDeviceFragment) device;
        }

        Fragment controltarget = getActivity().getSupportFragmentManager().findFragmentByTag("ControlorFragment");
        if (controltarget == null) {
            controlorFragment = new ControlorFragment();
        } else {
            controlorFragment = (ControlorFragment) controltarget;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_device_tx:
                dbHelper = new DBHelper(getActivity().getApplicationContext());
                String deviceName = mydeviceconn_deviceName.getText().toString();
                LogUtils.debug(TAG, "你设置的名称为=" + deviceName);
                ContentValues values = new ContentValues();
                values.put("device_name", deviceName);
                dbHelper.update(values, mDeviceAddress);

                Intent controlintent = new Intent(getActivity(), RemoteActivity.class);
                startActivity(controlintent);
                getActivity().finish();
                break;
            case R.id.left_back:
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    LogUtils.debug(TAG, "执行了回调方法");
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
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
}
