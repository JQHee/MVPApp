package com.example.testmvpapp.util.recycler;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * RecyclerView 快速返回顶部
 */
public class FastScrollLinearLayoutManager extends LinearLayoutManager {

    public FastScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public FastScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return FastScrollLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            //控制速度。
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return super.calculateSpeedPerPixel(displayMetrics);
            }
            @Override
            protected int calculateTimeForScrolling(int dx) {
                if (dx > 3000) {
                    dx = 3000;
                }

                int time = super.calculateTimeForScrolling(dx);
                return time;
            }
        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

}

/*
// 滑动监听(是否显示返回顶部按钮) 返回顶部：mRecyclerView.smoothScrollToPosition(0);
private class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        // 当不滚动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 判断是否滚动超过一屏
            if (firstVisibleItemPosition == 0) {
                mImageViewRebackTop.setVisibility(View.INVISIBLE);
            } else {
                mImageViewRebackTop.setVisibility(View.VISIBLE);
            }

        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
            mImageViewRebackTop.setVisibility(View.INVISIBLE);
        }
    }
}
*/