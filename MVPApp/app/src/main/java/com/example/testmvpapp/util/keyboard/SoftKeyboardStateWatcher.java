package com.example.testmvpapp.util.keyboard;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * ================================
 * 作   者:  Administrator
 * 邮   箱:   13405995634@163.com
 * 创建时间:    2017/6/16 15:26
 * 版   本: 1.0
 * 描   述:   监听软键盘状态的工具类
 *  http://blog.csdn.net/u011370871/article/details/51024723
 *  http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
 *
 *  使用方法：
 *  final SoftKeyboardStateWatcher softKeyboardStateWatcher = new SoftKeyboardStateWatcher(findViewById(R.id.activity_main_layout);
 *
 * Add listener
 * softKeyboardStateWatcher.addSoftKeyboardStateListener(...);
 *
 * then just handle callbacks
 *
 * =================================
 */

public class SoftKeyboardStateWatcher implements ViewTreeObserver.OnGlobalLayoutListener{
    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened(int keyboardHeightInPx);
        void onSoftKeyboardClosed();
    }

    private final List<SoftKeyboardStateListener> listeners =new LinkedList<SoftKeyboardStateListener>();
    private final View activityRootView;
    private int       lastSoftKeyboardHeightInPx;
    private boolean   isSoftKeyboardOpened;

    public SoftKeyboardStateWatcher(View activityRootView) {
        this(activityRootView,false);
    }

    public SoftKeyboardStateWatcher(View activityRootView, boolean isSoftKeyboardOpened) {
        this.activityRootView    = activityRootView;
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
    //视图发生改变
    @Override
    public void onGlobalLayout() {
        final Rect r =new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r);

        final int heightDiff =activityRootView.getRootView().getHeight() - (r.bottom - r.top);
        if (!isSoftKeyboardOpened && heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
            isSoftKeyboardOpened =true;
            notifyOnSoftKeyboardOpened(heightDiff);
        } else if (isSoftKeyboardOpened && heightDiff < 100) {
            isSoftKeyboardOpened =false;
            notifyOnSoftKeyboardClosed();
        }
    }

    public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
    }

    public boolean isSoftKeyboardOpened() {
        return isSoftKeyboardOpened;
    }

    /**
     * Default value is zero {@code 0}.
     *
     *@return last saved keyboard height inpx
     */
    public int getLastSoftKeyboardHeightInPx() {
        return lastSoftKeyboardHeightInPx;
    }

    public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.add(listener);
    }

    public void removeSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.remove(listener);
    }

    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx;

        for (SoftKeyboardStateListener listener :listeners) {
            if (listener !=null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    /**
     * 通知软件键盘关闭
     */
    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener listener :listeners) {
            if (listener !=null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }
}
