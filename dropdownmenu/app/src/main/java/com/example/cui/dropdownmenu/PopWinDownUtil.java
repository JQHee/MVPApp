package com.example.cui.dropdownmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by cuijunling on 2016/11/1.
 */
public class PopWinDownUtil {
    private Context context;
    private View contentView;
    private View relayView;
    private PopupWindow popupWindow;

    public PopWinDownUtil(Context context, View contentView, View relayView){
        this.context = context;
        this.contentView = contentView;
        this.relayView = relayView;
        init();
    }
    @SuppressLint("WrongConstant")
    public void init(){
        //内容，高度，宽度
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationTopFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOutsideTouchable(true);
        //关闭事件
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(onDismissLisener != null){
                    onDismissLisener.onDismiss();
                }
            }
        });
    }
    public void show(){

        /*
         * 此方法针对7.0部分机型PopupWindow弹出位置不正确的解决方法
         */
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                int[] location = new int[2];
                relayView.getLocationOnScreen(location);
                int tempHeight = popupWindow.getHeight();
                if (tempHeight == WindowManager.LayoutParams.MATCH_PARENT || context.getResources().getDisplayMetrics().heightPixels <=
                        tempHeight) {
                    // - relayView.getHeight()
                    popupWindow.setHeight(context.getResources().getDisplayMetrics().heightPixels - location[1]);
                }
                popupWindow.showAtLocation(relayView, Gravity.NO_GRAVITY, location[0], location[1] + relayView.getHeight());
                popupWindow.update();
            } else {
                if (popupWindow != null) {
                    popupWindow.showAsDropDown(relayView, 0, 0);
                    popupWindow.update();
                }
            }
        }

    }



    public void hide(){
        if(popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    private OnDismissLisener onDismissLisener;
    public void setOnDismissListener(OnDismissLisener onDismissLisener){
        this.onDismissLisener = onDismissLisener;
    }
    public interface OnDismissLisener{
        void onDismiss();
    }
    public boolean isShowing(){
        return popupWindow.isShowing();
    }
}
