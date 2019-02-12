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

import android.content.Intent;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRunner {

    private static final String TAG = "TaskRunner";

    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();

    private static ThreadPoolExecutor executor;

    static {
        /*
         * Create a thread pool. Core thread count is set equal to maximum
         * thread count, and allowCoreThreadTimeOut is set to true, which
         * ensures that we can have all THREAD_POOL_COUNT threads created to
         * execute our jobs instead of queuing jobs before all of the threads
         * are created and no idle threads will live forever.
         */
        executor = new ThreadPoolExecutor(
                CORE_COUNT, // core pool threads count
                CORE_COUNT, // maximum pool threads count
                1, // idle thread can live at most 1 second if not reused
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        executor.allowCoreThreadTimeOut(true); // allow core threads to be recycled if they're idle
    }

    private TaskRunner() {
        throw new AssertionError("No com.jlu.chengjie.zhifou.util.TaskRunner instances for you!");
    }

    public static void execute(Runnable runnable) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            Log.e(TAG, "execute runnable exception: ", e);
        }
    }

}

/*
private void loginBackground() {
    User user = SPUtil.getUser();
    if (user == null) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        return;
    }
    TaskRunner.execute(() -> {
        final long st = System.currentTimeMillis();
        final Response<String> response = OkHttpHelper.generateToken(user);
        runOnUiThread(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            try {
                // waiting for 3 seconds, including login time.
                long sleep = 3000 - System.currentTimeMillis() + st;
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
                if (response != null && response.getStatus() == 200) {
                    String json = EncryptUtil.aesDecrypt(response.getObject(), getString(R.string.aes_key));
                    User u = new Gson().fromJson(json, User.class);
                    ZLog.d(TAG, "user json: " + json);
                    intent = new Intent(this, MainActivity.class);
                    ZLog.d(TAG, "login success, start MainActivity.");
                    HeartBeat.start(u);
                    SPUtil.putUser(u);
                    finish();
                } else {
                    ZLog.w(TAG, "login background failed.");
                    ToastUtil.show("登陆失败");
                }
                startActivity(intent);
            } catch (Exception e) {
                ZLog.w(TAG, "login success but get user info failed.", e);
                ToastUtil.show(R.string.error_occurred);
            }
            startActivity(intent);
            finish();
        });
    });
}
*/
