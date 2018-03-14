package com.moxi.palmhealer.fragment;


import java.util.ArrayList;

/**
 * Created by yinlu on 2016/5/14.
 */
public interface TransfertoControler {
    //从datafragment 向遥控器发送数据
    public void transferData(ArrayList<Integer> data);

    //从datafragment 发送是否开关button数据
    public void closeButton(boolean isLedOpen, boolean isBlueToothOpen);
}
