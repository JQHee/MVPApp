package com.example.testmvpapp.sections.common.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.sections.common.fragments.BigImageFragment;
import com.example.testmvpapp.sections.common.listener.PermissionListener;
import com.example.testmvpapp.util.base.ToastUtils;
import com.example.testmvpapp.util.file.FileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author ChayChan
 * @description: 图片浏览器 的activity
 * @date 2017/8/23  11:02
 */

public class ImageViewPagerActivity extends SimpleActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = ImageViewPagerActivity.class.getSimpleName();
    public static final String IMG_URLS = "mImageUrls";
    public static final String POSITION = "position";

    @BindView(R.id.vp_pics)
    ViewPager mVpPics;

    @BindView(R.id.tv_indicator)
    TextView mTvIndicator;

    @BindView(R.id.tv_save)
    TextView mTvSave;

    private List<String> mImageUrls = new ArrayList<String>();
    private List<BigImageFragment> mFragments = new ArrayList<BigImageFragment>();
    private int mCurrentPosition;
    private Map<Integer,Boolean> mDownloadingFlagMap = new HashMap<>();//用于保存对应位置图片是否在下载的标识

    public void initData() {
        Intent intent = getIntent();
        mImageUrls = intent.getStringArrayListExtra(IMG_URLS);
        int position = intent.getIntExtra(POSITION, 0);
        mCurrentPosition = position;

        for (int i=0; i<mImageUrls.size(); i++) {
            String url = mImageUrls.get(i);
            BigImageFragment imageFragment = new BigImageFragment();

            Bundle bundle = new Bundle();
            bundle.putString(BigImageFragment.IMG_URL, url);
            imageFragment.setArguments(bundle);

            mFragments.add(imageFragment);//添加到fragment集合中
            mDownloadingFlagMap.put(i,false);//初始化map，一开始全部的值都为false
        }

        // mVpPics.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mVpPics.setAdapter(new ImageViewPagerAdapter(mFragments,getSupportFragmentManager()));
        mVpPics.addOnPageChangeListener(this);


        mVpPics.setCurrentItem(mCurrentPosition);// 设置当前所在的位置
        setIndicator(mCurrentPosition);//设置当前位置指示
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        ;//页面变化时，设置当前的指示
        setIndicator(mCurrentPosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setIndicator(int position) {
        mTvIndicator.setText(position + 1 + "/" + mImageUrls.size());//设置当前的指示
    }


    @OnClick(R.id.tv_save)
    public void onViewClicked() {

        // 动态申请权限 （内部已经有判断判断）
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(this.bindToLifecycle())
                .subscribe(granted -> {
                    if (granted) {
                        //保存图片
                        downloadImg();
                    } else {
                        ToastUtils.showToast("请开启相机和读取权限");
                    }
                });
        /*
        requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                //保存图片
                downloadImg();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {

            }
        });
        */
    }

    private void downloadImg() {
        String imgUrl = mImageUrls.get(mCurrentPosition);
        Boolean isDownlading = mDownloadingFlagMap.get(mCurrentPosition);
        if (!isDownlading){
            //如果不是正在下载，则开始下载
            mDownloadingFlagMap.put(mCurrentPosition,true);//更改标识为下载中
            new DownloadImgTask(mCurrentPosition).execute(imgUrl);
        }
    }

    @Override
    protected Object getLayout() {
        return R.layout.activity_image_view_pager;
    }

    @Override
    protected void init() {
        isHiddenToolbar(true);
        initData();
    }

    class DownloadImgTask extends AsyncTask<String,Integer,Void>{

     private int mPosition;

     public  DownloadImgTask(int position){
         mPosition = position;
     }

     @Override
     protected Void doInBackground(String... params) {
         String imgUrl = params[0];
         File file = null;
         try {
             FutureTarget<File> future = Glide
                     .with(ImageViewPagerActivity.this)
                     .load(imgUrl)
                     .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
             file = future.get();

             String filePath = file.getAbsolutePath();

             String destFileName = System.currentTimeMillis() + FileUtils.getImageFileExt(filePath);
             File destFile = new File(FileUtils.getDir(""), destFileName);

             FileUtils.copy(file, destFile);// 保存图片

             // 最后通知图库更新
             sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                     Uri.fromFile(new File(destFile.getPath()))));
         } catch (Exception e) {
             Log.e(TAG, e.getMessage());
         }
         return null;
     }

     @Override
     protected void onPostExecute(Void aVoid) {
         mDownloadingFlagMap.put(mPosition,false);//下载完成后更改对应的flag
         // UIUtils.showToast("保存成功，图片所在文件夹:SD卡根路径/TouTiao");
     }

     @Override
     protected void onProgressUpdate(Integer... values) {
         Log.i(TAG,"progress: " + values[0]);
     }
 }

class ImageViewPagerAdapter extends FragmentPagerAdapter {

    private List<BigImageFragment> mFragments = new ArrayList<>();
    public ImageViewPagerAdapter(List<BigImageFragment> fragmentList, FragmentManager fm) {
        super(fm);
        if (fragmentList != null){
            mFragments = fragmentList;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
}
