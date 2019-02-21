package com.example.testmvpapp.sections.main.personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.testmvpapp.R;
import com.example.testmvpapp.sections.adapter.UploadPicGridAdapter;
import com.example.testmvpapp.sections.common.activities.ShowImageActivity;
import com.example.testmvpapp.util.base.DensityUtil;
import com.example.testmvpapp.util.bus.LiveBus;
import com.example.testmvpapp.util.takephoto.BitmapUtils;
import com.example.testmvpapp.util.takephoto.ImageFactory;
import com.example.testmvpapp.util.takephoto.TakePhotoUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 添加图片页面
 */
public class AddPhotosActivity extends AppCompatActivity {

    private Uri origalUri;
    private File file;
    private File newFile;
    private Bitmap zoomImageBitmap;
    private GridView mGvPhoto;
    private UploadPicGridAdapter mAdapter;
    private int width;
    private ArrayList<Bitmap> bmp = new ArrayList<>();
    private ArrayList<Bitmap> origalBmp = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);
        init();
        viewBindEvents();
    }

    @Override
    public void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
        Uri uri;
        if (pResultCode == Activity.RESULT_OK) {
            switch (pRequestCode) {
                // 从相册取
                case TakePhotoUtils.CHOOSE_PICTURE:
                    origalUri = pData.getData();
                    file = BitmapUtils.getFileFromMediaUri(AddPhotosActivity.this, origalUri);
                    // 压缩图片
                    newFile = file;
                            // CompressHelper.getDefault(getApplicationContext()).compressToFile(file);
                    Bitmap photoBmp = null;
                    try {
//                        photoBmp = BitmapUtils.getBitmapFormUri(UserFeedbackActivity.this, Uri.fromFile(file));
                        photoBmp = BitmapFactory.decodeFile(newFile.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int degree = BitmapUtils.getBitmapDegree(newFile.getAbsolutePath());

                    /**
                     * 把图片旋转为正的方向
                     */
                    Bitmap newbitmap = BitmapUtils.rotateBitmapByDegree(photoBmp, degree);

                    origalBmp.add(newbitmap);
                    zoomImage(newbitmap,width,width);
//                    TakePhotoUtils.getInstance().cropImageUri(context, origalUri, width, width, TakePhotoUtils.CROP_BIG_PICTURE);
                    break;

                case TakePhotoUtils.CROP_BIG_PICTURE:
                    // 剪大图用uri
                    if (TakePhotoUtils.getInstance().mImageFile != null) {
                        Bitmap bitmap = TakePhotoUtils.getInstance().decodeUriAsBitmap(this, Uri.fromFile
                                (TakePhotoUtils.getInstance().mImageFile));

                        Bitmap image = ImageFactory.ratio(bitmap, width, width);
                        if (image != null) {
                            // spath:生成图片取个名字和路径包含类型
                            String fileName = "image" + System.currentTimeMillis()
                                    + ".png";
                            String outPath = AddPhotosActivity.this.getFilesDir().getAbsolutePath() + fileName;
                            try {
                                ImageFactory.compressAndGenImage(image, outPath, 140);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            File file = new File(outPath);
                            bmp.add(image);

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    break;

            }
        }
        super.onActivityResult(pRequestCode, pResultCode, pData);
    }


    /**
     * 缩放图片
     * @param bitmap
     * @param width
     * @param height
     */
    private void zoomImage(Bitmap bitmap,int width,int height){
        zoomImageBitmap = BitmapUtils.zoomBitmap(bitmap, width, height);
        bmp.add(zoomImageBitmap);
        mAdapter.notifyDataSetChanged();
    }


    /**
     *  让图片按照屏幕3等分
     */
    public void init() {
        if (bmp == null) {
            bmp = new ArrayList<>();
        }
        // Drawable drawable = getResources().getDrawable(R.drawable.ic);
        bmp.add(BitmapFactory.decodeResource(getResources(),R.drawable.icon_add_photo));
        //初始化控件
        mGvPhoto = (GridView) findViewById(R.id.gv_photo);
        //设置gridview分割线为透明
        mGvPhoto.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //初始化适配器
        mAdapter = new UploadPicGridAdapter(this,bmp);
        //绑定图片数据
        mGvPhoto.setAdapter(mAdapter);
        int screenWidth = DensityUtil.getScreenWidth(this);
        width = screenWidth / 3;

        // 设置列数
        int colnum =  (int) (((getResources().getDisplayMetrics().widthPixels  )) / 200 );
        mGvPhoto.setNumColumns(colnum);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (zoomImageBitmap != null && !zoomImageBitmap.isRecycled()){
            zoomImageBitmap.recycle();
        }
    }

    /**
     * 绑定点击事件
     */
    private void viewBindEvents() {

        //绑定点击事件
        mGvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //判断点击的是否是图片
                if (arg2 == bmp.size()) {
                    //显示选择提示窗
                    TakePhotoUtils.getInstance().pickPhoto(AddPhotosActivity.this);
                } else {
                    //进入图片预览页面
                    LiveBus.getDefault().postEvent("SHOW_IMGS", origalBmp);
                    Intent intent = new Intent(AddPhotosActivity.this, ShowImageActivity.class);
                    intent.putExtra("id", arg2);   //将当前点击的位置传递过去
                    startActivity(intent);     //启动Activity
                }
            }
        });

        // 删除图片
        mAdapter.setOnDelItemPhotoClickListener(new UploadPicGridAdapter.OnDelItemPhotoClickListener() {
            @Override
            public void onDelItemPhotoClick(int position) {
                if (bmp != null && bmp.size() > 0){
                    bmp.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

                if (origalBmp != null && origalBmp.size() > 0){
                    origalBmp.remove(position);
                }

            }
        });
    }
}
