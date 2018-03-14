package com.moxi.palmhealer.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.beans.Device;
import com.moxi.palmhealer.fragment.ConsultFragment;
import com.moxi.palmhealer.fragment.FirstPageFragment;
import com.moxi.palmhealer.fragment.MySettingFragment;
import com.moxi.palmhealer.fragment.ShopFragment;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    public FirstPageFragment firstPageFragment;
    public ShopFragment shopFragment;
    public ConsultFragment consultFragment;
    public MySettingFragment mySettingFragment;
    private ArrayList<Fragment> fragments;
    private RadioGroup group;

    private List<Device> deviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addDevive();


        group = (RadioGroup) findViewById(R.id.mRadioGroup);
        group.setOnCheckedChangeListener(this);
        fragments = new ArrayList<>();
        fragments.add(firstPageFragment);
        fragments.add(shopFragment);
        fragments.add(consultFragment);
        fragments.add(mySettingFragment);
        //设置进入首页
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mMainContent, fragments.get(0));
        transaction.commit();
    }

    //保存可以连接的设备
    private void addDevive() {
        deviceList= new ArrayList<>();
        Device device = new Device();
        device.deviceName_EN = "moxibustion";
        device.deviceName_CN = "便携式艾灸仪";
        device.device_Pic = "mydevice_bianxie";

        Device device2 = new Device();
        device2.deviceName_EN = AppConfig.lishidevice;
        device2.deviceName_CN = "立式艾灸仪";
        device2.device_Pic = "mydevice_lishi";

        deviceList.add(device);
        deviceList.add(device2);
        CommonUtil.getInstance().saveDeviceList(this, "connectDevice", deviceList);
    }

    private void initView() {
        firstPageFragment = new FirstPageFragment();
        shopFragment = new ShopFragment();
        consultFragment = new ConsultFragment();
        mySettingFragment = new MySettingFragment();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment;


        switch (checkedId) {
            case R.id.firstpage:
                fragment = fragments.get(0);
                transaction.replace(R.id.mMainContent, fragment);
                transaction.commit();
                break;
            case R.id.shop:
                fragment = fragments.get(1);
                transaction.replace(R.id.mMainContent, fragment);
                transaction.commit();
                break;
            case R.id.consult:
                fragment = fragments.get(2);
                transaction.replace(R.id.mMainContent, fragment);
                transaction.commit();
                break;
            case R.id.mysetting:
                fragment = fragments.get(3);
                transaction.replace(R.id.mMainContent, fragment);
                transaction.commit();
                break;

            default:
                break;
        }
    }
}
