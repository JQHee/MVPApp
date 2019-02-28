package com.example.testmvpapp.sections.launcher;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.ui.guidepage.GuidePage;

import java.util.Arrays;

import butterknife.BindView;

public class GuidePageActivity extends SimpleActivity {

    @BindView(R.id.guidePage)
    GuidePage guidePage;
    private Integer[] images = {
            R.mipmap.launcher_01,
            R.mipmap.launcher_02,
            R.mipmap.launcher_03,
            R.mipmap.launcher_04,
            R.mipmap.launcher_05
    };

    @Override
    protected Object getLayout() {
        return R.layout.activity_guide_page;
    }

    @Override
    protected void init() {

        //设置图片集合,圆点颜色大小
        guidePage.setLocalImageResList(Arrays.asList(images)).setOvalIndicator(Color.parseColor
                ("#00FF00"), Color.parseColor("#FFFFFF"), 10);

        //设置进入按钮（文字，颜色，大小，背景）点击事件
        guidePage.setOnEntryClickListener("立即体验", Color.parseColor("#000000"), 10, R.drawable
                .shape_radius_yellow_bg, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GuidePageActivity.this, "进入主界面", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
