/*
 *    Copyright [2019] [chengjie.jlu@qq.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.testmvpapp.util.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

    private static final String EMPTY_STRING = "";
    private static final String TAG = "EncryptUtil";

    static String aesEncrypt(String content, String key) {
        if (!TextUtils.isEmpty(content)) {
            try {
                @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
                byte[] bytes = new byte[0];
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    bytes = content.getBytes(StandardCharsets.UTF_8);
                } else {
                    bytes = content.getBytes(Charset.forName("UTF-8"));
                }
                byte[] result = cipher.doFinal(bytes);
                return Base64.getEncoder().encodeToString(result);
            } catch (Exception e) {
                // Log.e(TAG, "aes encrypt exception.", e);
            }
        }
        return EMPTY_STRING;
    }

    public static String aesDecrypt(String content, String key) {
        if (!TextUtils.isEmpty(content)) {
            try {
                // Log.d(TAG, "start to decrypt %s with key %s", content, key);
                @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
                byte[] bytes = Base64.getDecoder().decode(content);
                byte[] result = cipher.doFinal(bytes);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return new String(result, StandardCharsets.UTF_8);
                } else {
                    return  new String(result, Charset.forName("UTF-8"));
                }
            } catch (Exception e) {
                Log.e(TAG, "aes decrypt exception. key: " + key, e);
            }
        }
        return EMPTY_STRING;
    }
}
