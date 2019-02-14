package com.example.testmvpapp.ui.proportion;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.testmvpapp.R;

/**
 * 作者：Created by hjq on 2016-07-18
 * 邮箱：
 * 描述：自定义宽高比例的view
 */
public class RatioLayout extends FrameLayout {
    // 宽和高的比例
    private float ratio = 0.0f;
    public RatioLayout(Context context) {
        this(context, null);
    }
    public RatioLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //从xml中获取比例值（需在values下的attrs下声明）
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        ratio = a.getFloat(R.styleable.RatioLayout_ratio, 0.0f);
        a.recycle();
    }
    /**
     * 可代码设置，或者布局中设置
     <span style="white-space:pre">	</span> * 设置宽高比例
     * @param f
     */
    public void setRatio(float f) {
        ratio = f;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        //MeasureSpec.EXACTLY精确模式
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(),
                    MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
            width = (int) (height * ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(),
                    MeasureSpec.EXACTLY);
        }else {
            new RuntimeException("设置比例值，宽高需要一个是精确模式");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

/*
<com.ontim.googleplay.view.RatioLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:ratio="2.43"
        >
<ImageView
            android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:scaleType="fitCenter"
                    android:src="@mipmap/ic_launcher"/>
</com.ontim.googleplay.view.RatioLayout>
*/
