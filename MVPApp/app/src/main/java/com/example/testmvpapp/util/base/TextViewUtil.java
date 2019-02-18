package com.example.testmvpapp.util.base;

import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class TextViewUtil {

    /** * @param tv
     * 主要判断显示的TV 内容只为一行则居中显示，内容超过两行 则左对齐
     */
    public static void setTextViewGravity(final TextView tv) {
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if(tv.getLineCount() == 1) {
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setGravity(Gravity.LEFT | Gravity.CENTER);
                }
                return true;
            }
        });
    }
}

