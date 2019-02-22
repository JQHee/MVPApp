package com.example.testmvpapp.sections.common.activities;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.component.events.MessageEvent;
import com.example.testmvpapp.sections.adapter.ShowImageAdapter;
import com.example.testmvpapp.util.bus.LiveBus;
import com.example.testmvpapp.util.log.LatteLogger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 本地图片查看器
 */

public class ShowImageActivity extends SimpleActivity {

    private ViewPager viewPager;  //声明viewpage
    private List<View> listViews = null;  //用于获取图片资源
    private int index = 0;   //获取当前点击的图片位置
    private ShowImageAdapter adapter;   //ViewPager的适配器
    private ArrayList<Bitmap> bmp = null;
    private int position;

    @Override
    protected void onResume() {
        super.onResume();
        registerEventBus(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }


    @Override
    protected Object getLayout() {
        return R.layout.show_image_layout;
    }
    @Override
    protected void initView() {
        // WARNING: - 必须设置在setcontentView之前
        // requestWindowFeature(Window.FEATURE_NO_TITLE);  //去除标题栏
        isHiddenToolbar(true);
        viewPager = (ViewPager) findViewById(R.id.show_view_pager);  //绑定viewpager的id
        initData();
    }

    private void initData() {
        listViews = new ArrayList<View>();   //初始化list
        position = getIntent().getIntExtra("id",0);
    }

    // 方法名字随便写
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(MessageEvent em) {
        LatteLogger.d(em.obj);
        bmp = em.obj;
        // int byteCount = bmp.get(0).getByteCount();
        inint();   //初始化
    }

    private void inint() {

        if (bmp != null && bmp.size() > 0){
            for (int i = 0; i < bmp.size(); i++) {  //for循环将试图添加到list中
                View view = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
//                ImageView iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
//                iv.setImageBitmap(bmp.get(i));   //设置当前点击的图片

                SubsamplingScaleImageView iv = (SubsamplingScaleImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
                iv.setImage(ImageSource.bitmap(bmp.get(i)));
                listViews.add(view);
                /**
                 * 图片的长按监听
                 * */
                iv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //弹出提示，提示内容为当前的图片位置
                        Toast.makeText(ShowImageActivity.this, "这是第" + (index + 1) + "图片", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
            adapter = new ShowImageAdapter(listViews);
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(new PageChangeListener()); //设置viewpager的改变监听
            //截取intent获取传递的值
            viewPager.setCurrentItem(position);    //viewpager显示指定的位置
        }
    }



    /**
     * pager的滑动监听
     * */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            index = arg0;
        }
    }

}
