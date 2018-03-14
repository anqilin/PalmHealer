/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moxi.palmhealer.service;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();

    public static String GATT_UUID = "00001801-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String SERVIECE_NOTIFY_DATA = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static String SERVIECE_WRITE_DATA = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static String CHARACTER_NOTIFY_DATA = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static String CHARACTER_WRITE_DATA = "0000fff2-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services
        attributes.put(GATT_UUID, "GATT");
        attributes.put(SERVIECE_NOTIFY_DATA, "moxibustion_notify_service");
        attributes.put(SERVIECE_WRITE_DATA, "moxibustion_write_service");
        // Sample Characteristics.
        attributes.put(CHARACTER_NOTIFY_DATA, "moxibustion_notify_character");
        attributes.put(CHARACTER_WRITE_DATA, "moxibustion_write_character");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
