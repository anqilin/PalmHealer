package com.moxi.palmhealer.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.activity.RemoteActivity;
import com.moxi.palmhealer.db.DBHelper;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.CommonUtil;
import com.moxi.palmhealer.utils.LogUtils;
import com.moxi.palmhealer.utils.SharedPrefsUtil;
import com.moxi.palmhealer.widget.EasySwitchButton;
import com.moxi.palmhealer.widget.VerticalSeekBar;

import java.util.ArrayList;

/**
 * Created by yinlu on 2016/4/21.
 */
public class ControlorFragment extends DataFragment implements TransfertoControler, View.OnTouchListener {
    private final static String TAG = "---ControlorFragment---";
    String device_Name;
    String device_Name_EN;
    String device_Mac;
    TextView time_float_mark;
    TextView temp_float_mark;
    VerticalSeekBar bar_time;
    VerticalSeekBar bar_temp;
    boolean isReceiveData = true;
    float bar_time_height_percent;
    ImageView controler_add_button_time;
    ImageView controler_decrease_button_time;
    ImageView controler_add_button_temp;
    ImageView controler_decrease_button_temp;
    //数据交互
    int control_set_time;
    int control_set_tempe;
    public int control_get_tempe;
    public int control_get_time;
    public int control_hongguang;
    public EasySwitchButton button_hongguang;
    public EasySwitchButton button_power;

    String now_time;
    int nowtime;
    int newtime;
    String now_temp;
    int nowtemp;
    int newtemp;
    private DBHelper dbHelper = null;// 数据库对象
    private Handler myhandler=new Handler();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        transfertoControler = this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.controler_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initData() {
        mDeviceAddress = device_Mac;
        deviceName_EN = device_Name_EN;
        mybindService();
        myhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (IsConnected==true) {

                    ((RemoteActivity) getActivity()).IsConnect();
                }
            }
        },2000);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConfig.CONTROLER_DISCONNECTED);
        getActivity().registerReceiver(controlFragmentReceiver, intentFilter);
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

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            LogUtils.debug(TAG, "-------button hongguang=" + "true");
            controlled(true);
            //myhandler.postDelayed(runnable,1000);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initData();
        myhandler.postDelayed(runnable,1000);
        myConnetService();
    }

    private void initView() {
        controler_add_button_time = (ImageView) getView().findViewById(R.id.controler_add_button_time);
        controler_decrease_button_time = (ImageView) getView().findViewById(R.id.controler_decrease_button_time);
        controler_add_button_temp = (ImageView) getView().findViewById(R.id.controler_add_button_temp);
        controler_decrease_button_temp = (ImageView) getView().findViewById(R.id.controler_decrease_button_temp);
        controler_add_button_time.setOnTouchListener(this);
        controler_decrease_button_time.setOnTouchListener(this);
        controler_add_button_temp.setOnTouchListener(this);
        controler_decrease_button_temp.setOnTouchListener(this);


        button_hongguang = (EasySwitchButton) getView().findViewById(R.id.button_hongguang);
        button_power = (EasySwitchButton) getView().findViewById(R.id.button_power);
        button_hongguang.setOnCheckChangedListener(new MyEasyOnOpenedListener());
        button_power.setOnCheckChangedListener(new MyEasyOnOpenedListener());


        time_float_mark = (TextView) getView().findViewById(R.id.time_float_mark);
        temp_float_mark = (TextView) getView().findViewById(R.id.temp_float_mark);
        bar_time = (VerticalSeekBar) getView().findViewById(R.id.controler_bar_time);
        ViewTreeObserver vto = bar_time.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bar_time.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = bar_time.getHeight();
                bar_time_height_percent = (float) (height * 0.84) / 100;
                int weight = bar_time.getWidth();
                LogUtils.debug(TAG, "高度为=" + height + ",宽度为=" + weight + ",bar_time_height_percent=" + bar_time_height_percent);

            }
        });


        bar_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.debug(TAG, "当前进度" + progress + "%");
                LogUtils.debug(TAG, "当前margin" + (int) (bar_time_height_percent * progress));
                //使用属性动画移动位置
                float curTranslationY = time_float_mark.getTranslationY();
                ObjectAnimator.ofFloat(time_float_mark, "translationY", curTranslationY, -(int) (bar_time_height_percent * progress)).setDuration(1).start();

                control_set_time = 60 * progress / 100;
                time_float_mark.setText(control_set_time + "min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isReceiveData = false;
                LogUtils.debug(TAG, "开始拖动");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                controltime(control_set_time);
                isReceiveData = true;
                LogUtils.debug(TAG, "拖动停止");
            }
        });
        // 温度部分动画
        bar_temp = (VerticalSeekBar) getView().findViewById(R.id.controler_bar_temp);
        bar_temp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.debug(TAG, "当前进度" + progress + "%");
                LogUtils.debug(TAG, "当前margin" + (int) (bar_time_height_percent * progress));

                //使用属性动画移动位置
                float curTranslationY = temp_float_mark.getTranslationY();
                ObjectAnimator.ofFloat(temp_float_mark, "translationY", curTranslationY, -(int) (bar_time_height_percent * progress)).setDuration(1).start();
                Context context=getActivity().getApplicationContext();
                int temp= SharedPrefsUtil.getIntValue(context,"temp_set","temp",70);
                control_set_tempe = 100 + temp * progress / 100;
                temp_float_mark.setText(control_set_tempe + "℃");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isReceiveData = false;
                LogUtils.debug(TAG, "开始拖动");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                controltemp(control_set_tempe);
                isReceiveData = true;
                LogUtils.debug(TAG, "拖动停止");
            }
        });


        dbHelper = new DBHelper(getActivity().getApplicationContext());
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
        }

    }

    /**
     * EasySwitchButton 的点击事件
     */
    private class MyEasyOnOpenedListener implements
            EasySwitchButton.OnOpenedListener {
        @Override
        public void onChecked(View v, boolean isOpened) {
            switch (v.getId()) {
                case R.id.button_hongguang:
                    LogUtils.debug(TAG, "-------button hongguang=" + isOpened);
                    controlled(isOpened);
                    break;

                case R.id.button_power:
                    LogUtils.debug(TAG, "-------button hongguang=" + isOpened);
                    controlpower(isOpened);
                    break;

            }
        }
    }


    @Override
    public void transferData(ArrayList<Integer> data) {
        if (isReceiveData) {
            LogUtils.debug(TAG, "--------control get data = " + data);
            control_get_tempe = data.get(3);
            control_get_time = data.get(4);
            control_hongguang = data.get(1);

            //暂时注释掉
//            if (control_hongguang == 1) {
//                button_hongguang.setStatus(true);
//            } else {
//
//                button_hongguang.setStatus(false);
//            }
//            button_power.setStatus(true);
            // 接受时间设置和动画
            if (control_get_time == 0)
                isReceiveData = false;
            int time_progress = 100 * control_get_time / 60;
            bar_time.setProgressAndThumb(time_progress);
//            bar_time.setProgress(time_progress);
            //使用属性动画移动位置
            float curTranslationY = time_float_mark.getTranslationY();
            ObjectAnimator.ofFloat(time_float_mark, "translationY", curTranslationY, -(int) (bar_time_height_percent * time_progress)).setDuration(1).start();
            time_float_mark.setText(control_get_time + "min");
            //接受温度设置和动画
            int temp_progress = 100 * (control_get_tempe - 100) / 70;

            //使用属性动画移动位置
            if (control_get_tempe > 100) {
                bar_temp.setProgressAndThumb(temp_progress);
                float curTranslationY_temp = temp_float_mark.getTranslationY();
                ObjectAnimator.ofFloat(temp_float_mark, "translationY", curTranslationY_temp, -(int) (bar_time_height_percent * temp_progress)).setDuration(1).start();
            } else {
                temp_progress = 0;
                bar_temp.setProgressAndThumb(temp_progress);
                float curTranslationY_temp = temp_float_mark.getTranslationY();
                ObjectAnimator.ofFloat(temp_float_mark, "translationY", curTranslationY_temp, -(int) (bar_time_height_percent * temp_progress)).setDuration(1).start();

            }
            temp_float_mark.setText(control_get_tempe + "℃");

        }

    }

    @Override
    public void closeButton(boolean isLedOpen, boolean isPowerOpen) {
        button_hongguang.setStatus(isLedOpen);
        button_power.setStatus(isPowerOpen);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.debug(TAG, "-----------onDestroyView---------");
        LogUtils.debug(TAG, "------mBluetoothLeService  onDestroyView" + mBluetoothLeService);
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
    public boolean onTouch(View v, MotionEvent event) {
        now_time = CommonUtil.getInstance().getNumbers(time_float_mark.getText().toString());
        now_temp = CommonUtil.getInstance().getNumbers(temp_float_mark.getText().toString());
        switch (v.getId()) {
            case R.id.controler_add_button_time:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isReceiveData = false;
                        LogUtils.debug(TAG, "--controler_add_button_time--ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        isReceiveData = true;
                        LogUtils.debug(TAG, "--controler_add_button_time--ACTION_UP");

                        nowtime = Integer.parseInt(now_time);
                        newtime = nowtime + 1;
                        if (newtime < 60)
                            controltime(newtime);
                        break;
                }
                break;
            case R.id.controler_decrease_button_time:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isReceiveData = false;
                        LogUtils.debug(TAG, "--controler_decrease_button_time--ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        isReceiveData = true;
                        LogUtils.debug(TAG, "--controler_decrease_button_time--ACTION_UP");

                        nowtime = Integer.parseInt(now_time);
                        newtime = nowtime - 1;
                        if (newtime > 0)
                            controltime(newtime);
                        break;
                }
                break;
            case R.id.controler_add_button_temp:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isReceiveData = false;
                        LogUtils.debug(TAG, "--controler_add_button_temp--ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        isReceiveData = true;
                        LogUtils.debug(TAG, "--controler_add_button_temp--ACTION_UP");

                        nowtemp = Integer.parseInt(now_temp);
                        newtemp = nowtemp + 1;
                        if (newtemp < 170)
                            controltemp(newtemp);
                        break;
                }
                break;
            case R.id.controler_decrease_button_temp:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isReceiveData = false;
                        LogUtils.debug(TAG, "--controler_decrease_button_temp--ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        isReceiveData = true;
                        LogUtils.debug(TAG, "--controler_decrease_button_temp--ACTION_UP");

                        nowtemp = Integer.parseInt(now_temp);
                        newtemp = nowtemp - 1;
                        if (newtemp > 100)
                            controltemp(newtemp);
                        break;
                }
                break;
        }
        return true;
    }
}
