package com.moxi.palmhealer.fragment;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.moxi.palmhealer.service.BluetoothLeService;
import com.moxi.palmhealer.service.SampleGattAttributes;
import com.moxi.palmhealer.utils.CommonUtil;
import com.moxi.palmhealer.utils.LogUtils;
import com.moxi.palmhealer.beans.Device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by yinlu on 2016/4/28.
 */
public class DataFragment extends BaseFragment {

    private final static String TAG = "DataFragment---蓝牙模块";
    Boolean SENTLOG = true;
    public static BluetoothDevice device;

    public static String deviceName_EN;

    public static String mDeviceAddress;
    public static BluetoothLeService mBluetoothLeService;
    public static boolean mConnected = false;
    public static BluetoothGattCharacteristic mCommandCharacteristic;
    public static BluetoothGattCharacteristic mNotifyCharacteristic;
    public ConnectService connectService;
    public static TransfertoControler transfertoControler;

    public static int read_power_state;
    public static int read_led_state;
    public static int read_current_temperature;
    public static int read_given_temp;
    public static int read_minutes;
    public static int read_given_minutes;
    public static boolean IsConnected=false;

    //把通过广播得到数据，放在list中
    public static ArrayList<Integer> transferData = new ArrayList<>();
    // Code to manage Service lifecycle.
    public final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LogUtils.debug(TAG, "开始绑定service onServiceConnected" + "---name=" + deviceName_EN + "--address=" + mDeviceAddress);

            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.i(TAG, "Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up initialization.
           IsConnected= mBluetoothLeService.connect(mDeviceAddress);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.debug(TAG, "--------onServiceDisconnected service无法绑定了");
            mBluetoothLeService = null;
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.debug(TAG, "---------开始执行onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.debug(TAG, "---------开始执行onCreate");
    }

    public void mybindService() {
        LogUtils.debug(TAG, "---------开始执行onCreate---bindservice");
        Intent gattServiceIntent = new Intent(getActivity(), BluetoothLeService.class);
//        getActivity().startService(gattServiceIntent);
        getActivity().bindService(gattServiceIntent, mServiceConnection, getActivity().BIND_AUTO_CREATE);
        getActivity().startService(gattServiceIntent);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.debug(TAG, "---------开始执行onActivityCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.debug(TAG, "---------开始执行onResume");

    }

    public void myConnetService() {
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        LogUtils.debug(TAG, "------mBluetoothLeService  myConnetService" + mBluetoothLeService);
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            LogUtils.debug(TAG, "Connect request result=" + result);
        }
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                LogUtils.debug(TAG, "--mGattUpdateReceiver  ACTION_GATT_CONNECTED");
                mConnected = true;

                LogUtils.debug(TAG, "--------ACTION_GATT_CONNECTED devicename" + deviceName_EN);
                Fragment dcfrag = getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment");
                if ((dcfrag != null && dcfrag.isVisible())) {

                    //暂时写死在这可以连接的BLE设备 name is moxibusition

                    if (connectService != null) {
                        if (deviceName_EN == null || !deviceFilter(deviceName_EN)) {
                            connectService.switchFragment(false, deviceName_EN);
                        } else {
                            connectService.switchFragment(true, deviceName_EN);
                        }
                    }

                }

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                LogUtils.debug(TAG, "--mGattUpdateReceiver  ACTION_GATT_DISCONNECTED");
                mConnected = false;
                Fragment dcfrag = getActivity().getSupportFragmentManager().findFragmentByTag("ConnectFragment");
                if ((dcfrag != null && dcfrag.isVisible()))
                    connectService.switchFragment(false, deviceName_EN);

                Fragment contfrag = getActivity().getSupportFragmentManager().findFragmentByTag("ControlorFragment");
                if ((contfrag != null && contfrag.isVisible())) {
                    transfertoControler.closeButton(false, false);
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                LogUtils.debug(TAG, "--mGattUpdateReceiver  ACTION_GATT_SERVICES_DISCOVERED");

                initMoxibustionService(
                        mBluetoothLeService.getSupportedGatteService(
                                SampleGattAttributes.SERVIECE_NOTIFY_DATA));

                initMoxibustionService(
                        mBluetoothLeService.getSupportedGatteService(
                                SampleGattAttributes.SERVIECE_WRITE_DATA));


                // Show all the supported services and characteristics on the user interface.
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                LogUtils.debug(TAG, "--mGattUpdateReceiver  ACTION_DATA_AVAILABLE");
                parsedata(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
            }


        }
    };
    //anqilin 2017-7-9
    private boolean deviceFilter(String mdevicename) {
       Device mydevice = null;
        try {
            mydevice = CommonUtil.getInstance().deSerialization(getObject(mdevicename, "bluetoothDevice"));
            Log.i(TAG, "反序列化得到的device =" + mydevice.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (mydevice == null) ? false : true;

    }

    public String getObject(String key, String preferName) {
        SharedPreferences sp = getActivity().getSharedPreferences(preferName, 0);
        return sp.getString(key, null);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void initMoxibustionService(BluetoothGattService gattService) {
        String uuid = "";
        if (gattService == null) {
            LogUtils.debug(TAG, "gattService is null");
            return;
        }
        List<BluetoothGattCharacteristic> gattCharacteristics = new ArrayList<>();
        gattCharacteristics.clear();
        gattCharacteristics = gattService.getCharacteristics();
        LogUtils.debug(TAG, "---gattCharacteristics=" + gattCharacteristics);
        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            uuid = gattCharacteristic.getUuid().toString();
            LogUtils.debug(TAG, "-------uuid=" + uuid);
            if (SampleGattAttributes.CHARACTER_NOTIFY_DATA.substring(0, 8).equals(uuid.substring(0, 8))) {
                mNotifyCharacteristic = gattCharacteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        mNotifyCharacteristic, true);
                LogUtils.debug(TAG, "NOTIFY_DATA");
                LogUtils.debug(TAG, "getProperties()=" + mNotifyCharacteristic.getProperties());
            } else if (SampleGattAttributes.CHARACTER_WRITE_DATA.substring(0, 8).equals(uuid.subSequence(0, 8))) {
//                mCommandCharacteristic = gattCharacteristic;

                //写数据的服务和characteristic
                mCommandCharacteristic = mBluetoothLeService.getSupportedGatteService(SampleGattAttributes.SERVIECE_WRITE_DATA)
                        .getCharacteristic(UUID.fromString(SampleGattAttributes.CHARACTER_WRITE_DATA));


                LogUtils.debug(TAG, "WRITE_CMD");
                LogUtils.debug(TAG, "getProperties()=" + mCommandCharacteristic.getProperties());
                mCommandCharacteristic.setWriteType(
                        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                LogUtils.debug(TAG, "getProperties()=" + mCommandCharacteristic.getProperties());
            }
        }
    }


    private void parsedata(byte[] data) {
        int type = data[0];
        switch (type) {
            case 1:/* 0x01 Header Flag for memory status */
                if (transferData.size() > 0)
                    transferData.clear();
//                LogUtils.debug(TAG, "开始解析数据了 --------- parsedata");
                read_power_state = data[3];
                read_led_state = data[4];
//                LogUtils.debug(TAG, "开始解析data[5]="+byteToInt(data[5]));
                read_current_temperature = data[5] & 0xFF;
                read_current_temperature = read_current_temperature - 20; // unsigned data
                read_given_temp = data[6] & 0xFF; // unsigned data
                read_given_temp = read_given_temp - 20; // unsigned data
                read_minutes = data[7];
                read_given_minutes = data[8];

                transferData.add(read_power_state);
                transferData.add(read_led_state);
                transferData.add(read_current_temperature);
                transferData.add(read_given_temp);
                transferData.add(read_minutes);
                transferData.add(read_given_minutes);

                LogUtils.debug(TAG, "-----------transferData = " + transferData);
//                LogUtils.debug(TAG, "------------transfertoControler = "+transfertoControler);
                Fragment contfrag = getActivity().getSupportFragmentManager().findFragmentByTag("ControlorFragment");
                if ((contfrag != null && contfrag.isVisible())) {
                    transfertoControler.transferData(transferData);
                }


//                LogUtils.debug(TAG, "解析得到的数据为--------power_state="+read_power_state+",led_state="+read_led_state+
//                        ",current_temperature="+read_current_temperature+",given_temp="+read_given_temp+",minutes="+
//                        read_minutes+",given_minutes="+read_given_minutes);
                break;
        }
    }

    public static int byteToInt(byte b) {
        return b & 0xff;
    }


    public void controlled(boolean isOpen) {
        if (mCommandCharacteristic == null) return;
        if (mBluetoothLeService == null) return;
        byte[] setDataAfter;
        if (isOpen) {
            byte[] setDataBefore = {0x01, 0x20, 0x01, 0x03};
            byte[] trans = inttobyte(integrityCheck(setDataBefore));
            byte[] transV = Arrays.copyOfRange(trans, 2, 4);
            byte[] setData = byteMerger(setDataBefore, reverse(transV));
            byte[] dd = {0x17};
            setDataAfter = byteMerger(setData, dd);

        } else {
            byte[] setDataBefore = {0x01, 0x20, 0x01, 0x00};
            byte[] trans = inttobyte(integrityCheck(setDataBefore));
            byte[] transV = Arrays.copyOfRange(trans, 2, 4);
            byte[] setData = byteMerger(setDataBefore, reverse(transV));
            byte[] dd = {0x17};
            setDataAfter = byteMerger(setData, dd);
        }
        LogUtils.debug(TAG, "---setDataAfter[4]=" + setDataAfter[4] + ",setDataAfter[5]=" + setDataAfter[5]);
        LogUtils.debug(TAG, "---------------mCommandCharacteristic=" + mCommandCharacteristic);
        printDataHex(setDataAfter);
        mCommandCharacteristic.setValue(setDataAfter);
        mBluetoothLeService.writeCharacteristic(mCommandCharacteristic);

    }

    // 01 10 01 01 92 07 17
    public void controlpower(boolean isOpen) {
        if (mCommandCharacteristic == null) return;
        if (mBluetoothLeService == null) return;
        byte[] setDataAfter;
        if (isOpen) {
            byte[] setDataBefore = {0x01, 0x10, 0x01, 0x01};
            byte[] trans = inttobyte(integrityCheck(setDataBefore));
            byte[] transV = Arrays.copyOfRange(trans, 2, 4);
            byte[] setData = byteMerger(setDataBefore, reverse(transV));
            byte[] dd = {0x17};
            setDataAfter = byteMerger(setData, dd);
            LogUtils.debug(TAG, "---setDataAfter[4]=" + setDataAfter[4] + ",setDataAfter[5]=" + setDataAfter[5]);
        } else {
            byte[] setDataBefore = {0x01, 0x10, 0x01, 0x00};
            byte[] trans = inttobyte(integrityCheck(setDataBefore));
            byte[] transV = Arrays.copyOfRange(trans, 2, 4);
            byte[] setData = byteMerger(setDataBefore, reverse(transV));
            byte[] dd = {0x17};
            setDataAfter = byteMerger(setData, dd);
            printDataHex(setDataAfter);
        }

        mCommandCharacteristic.setValue(setDataAfter);
        mBluetoothLeService.writeCharacteristic(mCommandCharacteristic);

    }

    public void controltemp(int temp) {
        temp = temp + 20;
        if (mCommandCharacteristic == null) return;
        if (mBluetoothLeService == null) return;
        byte[] setDataAfter;
        byte[] b = Arrays.copyOfRange(inttobyte(temp), 3, 4);
        byte[] setDataBefore = {0x01, 0x30, 0x01};
        setDataBefore = byteMerger(setDataBefore, b);
        byte[] trans = inttobyte(integrityCheck(setDataBefore));
        byte[] transV = Arrays.copyOfRange(trans, 2, 4);
        byte[] setData = byteMerger(setDataBefore, reverse(transV));
        byte[] dd = {0x17};
        setDataAfter = byteMerger(setData, dd);

        printDataHex(setDataAfter);
        mCommandCharacteristic.setValue(setDataAfter);
        mBluetoothLeService.writeCharacteristic(mCommandCharacteristic);

    }

    public void controltime(int minutes) {
        if (mCommandCharacteristic == null) return;
        if (mBluetoothLeService == null) return;
        byte[] setDataAfter;
        byte[] b = Arrays.copyOfRange(inttobyte(minutes), 3, 4);
        byte[] setDataBefore = {0x01, 0x40, 0x01};
        setDataBefore = byteMerger(setDataBefore, b);
        byte[] trans = inttobyte(integrityCheck(setDataBefore));
        byte[] transV = Arrays.copyOfRange(trans, 2, 4);
        byte[] setData = byteMerger(setDataBefore, reverse(transV));
        byte[] dd = {0x17};
        setDataAfter = byteMerger(setData, dd);

        printDataHex(setDataAfter);
        mCommandCharacteristic.setValue(setDataAfter);
        mBluetoothLeService.writeCharacteristic(mCommandCharacteristic);

    }


    public void printDataHex(byte[] data) {
        if (SENTLOG) {
            StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data)
                stringBuilder.append(String.format("0x%02X ", byteChar));
            String log = stringBuilder.toString();
            LogUtils.debug(TAG, "---发送到蓝牙的字节数组为=" + log);
        }
    }

    //数组倒序
    public byte[] reverse(byte[] rt) {
        for (int i = 0; i < rt.length / 2; i++) {
            byte temp = rt[i];
            rt[i] = rt[rt.length - 1 - i];
            rt[rt.length - 1 - i] = temp;
        }
        return rt;
    }

    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }


    //crc java
    public int integrityCheck(byte[] bytes) {
        int wCrc = 0xffff;
        for (byte srcData : bytes) {
            int data = byteToInt(srcData);
            for (int j = 0; j < 8; j++) {
                if ((((wCrc & 0x8000) >> 8) ^ ((data << j) & 0x80)) != 0) {
                    wCrc = (wCrc << 1) ^ 0x1021;
                } else {
                    wCrc = wCrc << 1;
                }
            }
        }

        wCrc = (wCrc << 8) | (wCrc >> 8 & 0xff);
        return wCrc & 0xffff;
    }


    // int to byte
    public static byte[] inttobyte(int value) {
        byte b0 = (byte) ((value >> 24) & 0xFF);
        byte b1 = (byte) ((value >> 16) & 0xFF);
        byte b2 = (byte) ((value >> 8) & 0xFF);
        byte b3 = (byte) (value & 0xFF);
        byte[] bytes = {b0, b1, b2, b3};
        return bytes;
    }

    public static void disConnectService() {
        LogUtils.debug(TAG, "---------setOnClickListener---disConnectService=" + mBluetoothLeService);
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.close();
        }
        LogUtils.debug(TAG, "-----------onDestroyView---------");
        LogUtils.debug(TAG, "------mBluetoothLeService  onDestroyView" + mBluetoothLeService);
    }


    @Override
    public void onDestroy() {
        LogUtils.debug(TAG, "------------onDestroy----------");
        LogUtils.debug(TAG, "------------mServiceConnection=" + mServiceConnection);
        super.onDestroy();

    }

}
