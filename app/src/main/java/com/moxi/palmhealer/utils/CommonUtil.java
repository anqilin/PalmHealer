package com.moxi.palmhealer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.moxi.palmhealer.beans.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yinlu on 2016/4/16.
 */
public class CommonUtil {
    long startTime = 0l;
    long endTime = 0l;

    private static CommonUtil commonUtil;

    public static CommonUtil getInstance() {
        if (commonUtil == null) {
            commonUtil = new CommonUtil();
        }
        return commonUtil;
    }

    //截取数字
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public int getChineseNum(String str) {
        char[] t1 = null;
        t1 = str.toCharArray();
        int t0 = t1.length;
        int count = 0;
        for (int i = 0; i < t0; i++) {
            if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                count++;
            }
        }
        return count;
    }

    /**
     * 序列化对象
     *
     * @param device
     * @return
     * @throws IOException
     */
    public String serialize(Device device) throws IOException {

        startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(device);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.i("serial", "serialize str =" + serStr);
        endTime = System.currentTimeMillis();
        Log.i("serial", "序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Device deSerialization(String str) throws IOException,
            ClassNotFoundException {
        startTime = System.currentTimeMillis();
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Device device = (Device) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        endTime = System.currentTimeMillis();
        Log.i("serial", "反序列化耗时为:" + (endTime - startTime));
        return device;
    }


    public void saveDeviceList(Context context, String key,
                               List<Device> datas) {
        JSONArray mJsonArray = new JSONArray();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> itemMap = new HashMap<String, String>();
            itemMap.put("device_mac", datas.get(i).device_mac);
            itemMap.put("device_Name", datas.get(i).device_Name);
            itemMap.put("deviceName_EN", datas.get(i).deviceName_EN);
            itemMap.put("device_Pic", datas.get(i).device_Pic);
            itemMap.put("deviceName_CN", datas.get(i).deviceName_CN);


            System.out.println("序列化本地的map=" + itemMap);
            Iterator<Map.Entry<String, String>> iterator = itemMap.entrySet()
                    .iterator();

            JSONObject object = new JSONObject();

            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                try {
                    object.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {

                }
            }
            mJsonArray.put(object);
        }

        SharedPreferences sp = context.getSharedPreferences("deviceList",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, mJsonArray.toString());
        editor.commit();
    }

    public List<Device> getDeviceList(Context context, String key) {
        List<Device> deviceList = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences("deviceList",
                Context.MODE_PRIVATE);
        String result = sp.getString(key, "");
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObject = array.getJSONObject(i);
                Device device = new Device();
                JSONArray names = itemObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        device.deviceName_EN = itemObject.getString("deviceName_EN");
                        device.deviceName_CN = itemObject.getString("deviceName_CN");
                        device.device_Pic = itemObject.getString("device_Pic");

                    }
                }
                deviceList.add(device);
                LogUtils.debug("反序列化出的list对象=" + deviceList);
            }
        } catch (JSONException e) {

        }

        return deviceList;
    }


}
