package com.example.testmvpapp.sections.main.personal;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.testmvpapp.R;
import com.example.testmvpapp.app.MyApplication;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.util.base.CrashHandler;
import com.example.testmvpapp.util.base.DensityUtils;
import com.example.testmvpapp.util.base.ToastUtils;
import com.example.testmvpapp.util.popupwindow.CustomPopupWindow;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改用户头像
 */
public class ModifyAvatarActivity extends SimpleActivity {

    private static final  int SCAN_OPEN_PHONE = 0;
    private static final  int PHONE_CAMERA = 1;
    private static final  int PHONE_CROP = 2;

    @BindView(R.id.iv_header)
    AppCompatImageView mHeaderImageView;



    private  CustomPopupWindow mCustomPopupWindow;
    private Uri mCutUri;

    @OnClick(R.id.iv_header)
    void onClickHeaderIV() {

        // 动态申请权限 （内部已经有判断判断）
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .compose(this.bindToLifecycle())
                .subscribe(granted -> {
                    if (granted) {
                        showSelectPicPopup();
                    } else {
                        ToastUtils.showToast("请开启相机和读取权限");
                    }
                });

    }

    @Override
    protected Object getLayout() {
        return R.layout.activity_modify_avatar;
    }

    @Override
    protected void init() {
        addToolBar(R.mipmap.menu_back);
        setTitle("修改头像");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case SCAN_OPEN_PHONE: //从相册图片后返回的uri
                    //启动裁剪
                    startActivityForResult(CutForPhoto(data.getData()),PHONE_CROP);
                    break;
                case PHONE_CAMERA: //相机返回的 uri
                    //启动裁剪
                    String path = getContext().getExternalCacheDir().getPath();
                    String name = "output.png";
                    startActivityForResult(CutForCamera(path,name),PHONE_CROP);
                    break;
                case PHONE_CROP:
                    try {
                        //获取裁剪后的图片，并显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContext().getContentResolver().openInputStream(mCutUri));
                        mHeaderImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 弹窗选择框
     */
    private void showSelectPicPopup() {

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
                .showAtLocation(R.layout.activity_modify_avatar, Gravity.BOTTOM,0,0) // 设置popupwindow居中显示
                .setOnClickListener(R.id.pop_pic, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCustomPopupWindow.onDismiss();
                        mCustomPopupWindow.dismiss();
                        photoAlbum();
                    }
                })
                .setOnClickListener(R.id.pop_camera, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCustomPopupWindow.onDismiss();
                        mCustomPopupWindow.dismiss();
                        cameraPic();

                    }
                })
                .setOnClickListener(R.id.pop_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCustomPopupWindow.onDismiss();
                        mCustomPopupWindow.dismiss();
                    }
                });

        /* 添加监听方法也可以用下面这种
        mCustomPopupWindow.setOnClickListener(R.id.pop_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */
    }

    /**
     * 打开相册
     */
    private void cameraPic() {
        //创建一个file，用来存储拍照后的照片
        File outputfile = new File(getContext().getExternalCacheDir(),"output.png");
        try {
            if (outputfile.exists()){
                outputfile.delete();//删除
            }
            outputfile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri imageuri ;
        if (Build.VERSION.SDK_INT >= 24){
            imageuri = FileProvider.getUriForFile(getContext(),
                    "com.rachel.studyapp.fileprovider", //可以是任意字符串
                    outputfile);
        }else{
            imageuri = Uri.fromFile(outputfile);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
        startActivityForResult(intent,PHONE_CAMERA);
    }

    /**
     * 打开相册
     */
    private void photoAlbum() {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image*//**//*");
            startActivityForResult(intent,ToolUtils.SCAN_OPEN_PHONE);
        */
        // 由于模拟器图库的刷新问题，采用如下打开方式，实际开发请采用上面这种
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SCAN_OPEN_PHONE);
    }

    /**
     * 图片裁剪
     * @param uri
     * @return
     */
    @NonNull
    private Intent CutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    "cutcamera.png"); //随便命名一个
            if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Log.d(TAG, "CutForPhoto: "+cutfile);
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;
            Log.d(TAG, "mCameraUri: "+mCutUri);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", DensityUtils.dp2px(getContext(),200)); //200dp
            intent.putExtra("outputY",DensityUtils.dp2px(getContext(),200));
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拍照之后，启动裁剪
     * @param camerapath 路径
     * @param imgname img 的名字
     * @return
     */
    @NonNull
    private Intent CutForCamera(String camerapath,String imgname) {
        try {

            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    "cutcamera.png"); //随便命名一个
            if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = null; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Intent intent = new Intent("com.android.camera.action.CROP");
            //拍照留下的图片
            File camerafile = new File(camerapath,imgname);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri = FileProvider.getUriForFile(getContext(),
                        "com.rachel.studyapp.fileprovider",
                        camerafile);
            } else {
                imageUri = Uri.fromFile(camerafile);
            }
            outputUri = Uri.fromFile(cutfile);
            //把这个 uri 提供出去，就可以解析成 bitmap了
            mCutUri = outputUri;
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", DensityUtils.dp2px(getContext(),200));
            intent.putExtra("outputY",DensityUtils.dp2px(getContext(),200));
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
