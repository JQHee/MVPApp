package com.example.testmvpapp.util.base;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.testmvpapp.R;

/**
 * Created by long on 2016/6/6.
 * 避免同样的信息多次触发重复弹出的问题
 */
public class ToastUtils {

    private static Context sContext;
    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    private ToastUtils() {
        throw new RuntimeException("ToastUtils cannot be initialized!");
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static void showToast(String s) {
        if (toast == null) {
            // String m_ToastStr = "<font color='#ffffff'>"+s+"</font>";
            // toast = Toast.makeText(sContext, Html.fromHtml(m_ToastStr), Toast.LENGTH_SHORT);
            toast = Toast.makeText(sContext, s, Toast.LENGTH_SHORT);

            // 设置背景颜色
            View view = toast.getView();
            // view.setBackgroundResource(android.R.color.holo_green_light);
            view.setBackgroundResource(R.drawable.round_corner_toast);
            toast.setView(view);

            // 位置
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
            oneTime = twoTime;
        }
    }

    public static void showToast(int resId) {
        showToast(sContext.getString(resId));
    }
}
