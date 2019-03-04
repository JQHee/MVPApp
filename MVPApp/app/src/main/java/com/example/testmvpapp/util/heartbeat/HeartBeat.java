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

package com.example.testmvpapp.util.heartbeat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/* 检查登录状态  token是否过期 (登录成功后HeartBeat.start())
public class HeartBeat implements Runnable {

    private static final String TAG = "HeartBeat";

    private static final HeartBeat HEART_BEAT = new HeartBeat();

    private static final Queue<Response<List<Message>>> RESPONSE_QUEUE = new LinkedList<>();

    private final static Type TYPE = new TypeToken<Response<List<Message>>>() {
    }.getType();

    private static volatile User USER = null;

    private final Runnable authenticateFailed = () -> {
        SPUtil.clear();
        Activity activity = ZApplication.getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("警告");
        builder.setMessage("您的账号在其它设备登录，或者您的密码已被修改，如果这不是您自己的操作，" +
                "您的密码可能已经被盗，请找回密码后重新登录。");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", (dialog, which) -> {
            activity.startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    };

    private final static CountDownTimer COUNT_DOWN_TIMER =
            new CountDownTimer(24 * 60 * 60 * 1000, 20 * 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    TaskRunner.execute(HEART_BEAT);
                }

                @Override
                public void onFinish() {
                    COUNT_DOWN_TIMER.start();
                }
            };

    private HeartBeat() {

    }

    @Override
    public void run() {
        Response<List<Message>> response = OkHttpHelper.post("/api/heartbeat", TYPE, USER);
        if (response != null && response.getStatus() == 200) {
            RESPONSE_QUEUE.offer(response);
            ZLog.d(TAG, "receive message: " + response.getObject().toString());
        } else {
            if (response != null) {
                ZLog.w(TAG, "heartbeat failed, " + response.getMessage());
                if (response.getStatus() == 401) {
                    COUNT_DOWN_TIMER.cancel();
                    Looper.prepare();
                    new Handler().post(authenticateFailed);
                    Looper.loop();
                }
            } else {
                ZLog.d(TAG, "heartbeat failed, can not connected to the server.");
            }
        }
    }

    public synchronized static void start(User user) {
        if (user != null) {
            USER = user;
            COUNT_DOWN_TIMER.start();
        }
    }

    public synchronized static Response<List<Message>> top() {
        return RESPONSE_QUEUE.peek();
    }

    public synchronized static List<Response<List<Message>>> all() {
        List<Response<List<Message>>> responses = new ArrayList<>(RESPONSE_QUEUE);
        RESPONSE_QUEUE.clear();
        return responses;
    }
}
*/
