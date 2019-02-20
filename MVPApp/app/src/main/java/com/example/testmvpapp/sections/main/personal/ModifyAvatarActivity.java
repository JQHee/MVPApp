package com.example.testmvpapp.sections.main.personal;

import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.util.popupwindow.CustomPopupWindow;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改用户头像
 */
public class ModifyAvatarActivity extends SimpleActivity {

    @BindView(R.id.iv_header)
    AppCompatImageView mHeaderImageView;

    private  CustomPopupWindow mCustomPopupWindow;

    @OnClick(R.id.iv_header)
    void onClickHeaderIV() {
        mCustomPopupWindow = new CustomPopupWindow.Builder()
                .setContext(this) //设置 context
                .setContentView(R.layout.popup_window_select_photo) // 设置布局文件
                .setwidth(LinearLayout.LayoutParams.MATCH_PARENT) // 设置宽度，由于我已经在布局写好，这里就用 wrap_content就好了
                .setheight(LinearLayout.LayoutParams.WRAP_CONTENT) // 设置高度
                .setFouse(true)  // 设置popupwindow 是否可以获取焦点
                .setOutSideCancel(true) // 设置点击外部取消
                .setAnimationStyle(R.style.pop_anim) // 设置popupwindow动画
                // .setBackGroudAlpha(mActivity,0.7f) // 是否设置背景色，原理为调节 alpha
                .builder() //
                .showAtLocation(R.layout.activity_modify_avatar, Gravity.BOTTOM,0,0); // 设置popupwindow居中显示
    }

    @Override
    protected Object getLayout() {
        return R.layout.activity_modify_avatar;
    }

    @Override
    protected void initView() {
        addToolBar(R.mipmap.menu_back);
        setTitle("修改头像");
    }
}
