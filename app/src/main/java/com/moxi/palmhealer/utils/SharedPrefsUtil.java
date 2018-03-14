package com.moxi.palmhealer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by anqilin on 17/8/24.
 */

public class SharedPrefsUtil {
    /**
     * 向SharedPreferences中写入int类型数据
     *
     * @param context 上下文环境
     * @param name 对应的xml文件名称
     * @param key 键
     * @param value 值
     */
    public static void putIntValue(Context context, String name, String key,
                                int value) {
        Editor sp = getEditor(context, name);
        sp.putInt(key, value);
        sp.commit();
    }

    /**
     * 从SharedPreferences中读取int类型的数据
     *
     * @param context 上下文环境
     * @param name 对应的xml文件名称
     * @param key 键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static int getIntValue(Context context, String name, String key,
                               int defValue) {
        SharedPreferences sp = getSharedPreferences(context, name);
        int value = sp.getInt(key, defValue);
        return value;
    }



    private static Editor getEditor(Context context, String name) {
        return getSharedPreferences(context, name).edit();
    }

    //获取SharedPreferences实例
    private static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
