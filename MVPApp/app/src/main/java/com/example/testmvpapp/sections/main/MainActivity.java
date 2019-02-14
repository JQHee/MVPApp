package com.example.testmvpapp.sections.main;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baidu.location.BDLocation;
import com.example.testmvpapp.Model.UpdateInfo;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.component.jpush.NotificationsUtils;
import com.example.testmvpapp.component.net.ConstantService;
import com.example.testmvpapp.component.net.DefaultObserver;
import com.example.testmvpapp.component.net.RxRestCreator;
import com.example.testmvpapp.component.net.RxRestService;
import com.example.testmvpapp.component.net.download.DownloadFileAsync;
import com.example.testmvpapp.component.net.listener.ProgressResponseListener;
import com.example.testmvpapp.sections.main.discover.DiscoverFragment;
import com.example.testmvpapp.sections.main.index.IndexFragment;
import com.example.testmvpapp.sections.main.personal.PersonalFragment;
import com.example.testmvpapp.sections.main.sort.SortFragment;
import com.example.testmvpapp.ui.bottom.BottomBarAdapter;
import com.example.testmvpapp.ui.bottom.BottomBarLayout;
import com.example.testmvpapp.ui.bottom.BottomBarViewPager;
import com.example.testmvpapp.util.base.ToastUtils;
import com.example.testmvpapp.util.json.JsonUtils;
import com.example.testmvpapp.util.location.BdLocationUtil;
import com.example.testmvpapp.util.log.LatteLogger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class MainActivity extends SimpleActivity {

    protected final String TAG = this.getClass().getSimpleName();
    private static final int BAIDU_ACCESS_COARSE_LOCATION = 100;
    private static final int REQUEST_CODE_APP_INSTALL = 1001;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private BottomBarViewPager mVpContent = null;
    private BottomBarLayout mBottomBarLayout = null;
    private List<Fragment> mFragmentList = new ArrayList<>();

    /* 下载进度 */
    private ProgressDialog mProgressDialog = null;
    /* 子线程用来写文件 */
    private Thread mThread;

    @Override
    protected Object getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        mVpContent = (BottomBarViewPager) findViewById(R.id.vp_content);
        mBottomBarLayout = (BottomBarLayout) findViewById(R.id.bbl);
        initData();
        initListener();
        checkNotificationPermissions();
        updateApk();
    }

    private void initData() {

        IndexFragment homeFragment = new IndexFragment();
        /*
        Bundle bundle1 = new Bundle();
        bundle1.putString(TabFragment.CONTENT,"第一个页面");
        homeFragment.setArguments(bundle1);
        */
        mFragmentList.add(homeFragment);

        SortFragment sortFragment = new SortFragment();
        mFragmentList.add(sortFragment);

        DiscoverFragment discoverFragment = new DiscoverFragment();
        mFragmentList.add(discoverFragment);

        PersonalFragment personalFragment = new PersonalFragment();
        mFragmentList.add(personalFragment);
    }

    private void initListener() {

        final BottomBarAdapter bottomBarAdapter = new BottomBarAdapter(getSupportFragmentManager(), mFragmentList);
        mVpContent.setAdapter(bottomBarAdapter);
        mBottomBarLayout.setViewPager(mVpContent);
        mBottomBarLayout.setCurrentItem(1);

        /*
        *  设置未读消息数
        */
        /*
        mBottomBarLayout.setUnread(0,20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1,101);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3,"NEW");//设置第四个页签显示NEW提示文字
        */
    }

    /**
     * 动态请求权限，安卓手机版本在5.0以上时需要
     */
    private void myPermissionRequest() {
        // BAIDU_ACCESS_COARSE_LOCATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否拥有权限，申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, BAIDU_ACCESS_COARSE_LOCATION);
            }
            else {
                // 已拥有权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                myLocation();
            }
        }else {
            // 安卓手机版本在5.0时，配置清单中已申明权限，作相应处理，此处正对sdk版本低于23的手机
            myLocation();
        }
    }

    /**
     * 百度地图定位的请求方法   拿到 国 省 市  区
     */
    private void myLocation() {
        BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
            @Override
            public void myLocation(BDLocation location) {
                if (location == null) {
                    return;
                }
                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    String mCounty = location.getCountry();        //获取国家
                    String mProvince = location.getProvince();     //获取省份
                    String mCity = location.getCity();             //获取城市
                    String mDistrict = location.getDistrict();     //获取区
                    Log.i("==requestLocation===", "myLocation: "+mCounty+"="+mProvince+"="+mCity+"="+mDistrict);
                }
            }
        });
    }

    /**
     * 权限请求的返回结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_ACCESS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 第一次获取到权限，请求定位
                    myLocation();
                }
                else {
                    // 没有获取到权限，做特殊处理
                    Log.i("=========", "请求权限失败");
                }
                break;

            default:
                break;
        }
    }


    /**
     * 高德地图定位
     */
    public void startLocaion(){

        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation !=null ) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    Log.i(TAG,"当前定位结果来源-----"+amapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.i(TAG,"纬度 ----------------"+amapLocation.getLatitude());//获取纬度
                    Log.i(TAG,"经度-----------------"+amapLocation.getLongitude());//获取经度
                    Log.i(TAG,"精度信息-------------"+amapLocation.getAccuracy());//获取精度信息
                    Log.i(TAG,"地址-----------------"+amapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i(TAG,"国家信息-------------"+amapLocation.getCountry());//国家信息
                    Log.i(TAG,"省信息---------------"+amapLocation.getProvince());//省信息
                    Log.i(TAG,"城市信息-------------"+amapLocation.getCity());//城市信息
                    Log.i(TAG,"城区信息-------------"+amapLocation.getDistrict());//城区信息
                    Log.i(TAG,"街道信息-------------"+amapLocation.getStreet());//街道信息
                    Log.i(TAG,"街道门牌号信息-------"+amapLocation.getStreetNum());//街道门牌号信息
                    Log.i(TAG,"城市编码-------------"+amapLocation.getCityCode());//城市编码
                    Log.i(TAG,"地区编码-------------"+amapLocation.getAdCode());//地区编码
                    Log.i(TAG,"当前定位点的信息-----"+amapLocation.getAoiName());//获取当前定位点的AOI信息
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    /**
     * 检查通知权限是否已经开启
     */
    private void checkNotificationPermissions() {
        if (NotificationsUtils.isNotificationEnabled(this)) {
            Log.e(TAG, "onCreate: 通知权限 已开启");
            setBasicSetup(1);
            setBasicSetup(4);
        } else {
            Log.e(TAG, "onCreate: 通知权限 未开启");
            //提示用户去设置，跳转到应用信息界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
    }

    /**
     * 1-2-3-4
     * 增、删、改、查
     */
    public void setBasicSetup(int type) {
        if (type == 1) {
            //设置别名（新的调用会覆盖之前的设置）
            JPushInterface.setAlias(this, 0, "0x123");
            //设置标签（同上）
            JPushInterface.setTags(this, 0, setUserTags());
        } else if (type == 2) {
            //删除别名
            JPushInterface.deleteAlias(this, 0);
            //删除指定标签
            JPushInterface.deleteTags(this, 0, setUserTags());
            //删除所有标签
            JPushInterface.cleanTags(this, 0);
        } else if (type == 3) {
            //增加tag用户量(一般都是登录成功设置userid为目标，在别处新增加比较少见)
            JPushInterface.addTags(this, 0, setUserTags());
        } else if (type == 4) {
            //查询所有标签
            JPushInterface.getAllTags(this, 0);
            //查询别名
            JPushInterface.getAlias(this, 0);
            //查询指定tag与当前用户绑定的状态（MyJPushMessageReceiver获取）
            JPushInterface.checkTagBindState(this, 0, "0x123");
            //获取注册id
            JPushInterface.getRegistrationID(this);
        }
    }

    /**
     * 标签用户
     */
    private static Set<String> setUserTags() {
        //添加3个标签用户（获取登录userid较为常见）
        Set<String> tags = new HashSet<>();
        tags.add("0x123");
        tags.add("0x124");
        tags.add("0x125");
        return tags;
    }

    /* 1.apk更新 */
    private void updateApk() {
        // 6.0以上的申请动态权限 (文件写入权限)
        if (Build.VERSION.SDK_INT >= 23) {
            final RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            getApkInfo();
                        } else {
                            ToastUtils.showToast("暂无权限");
                        }
                    });
            return;
        }
        getApkInfo();
    }

    /* 2.获取线上apk信息 */
    private void getApkInfo() {

        ProgressResponseListener listener = new ProgressResponseListener() {
            @Override
            public void onStartDownload(long length) {

            }

            @Override
            public void onProgress(int progress) {
                LatteLogger.d("下载进度" + String.valueOf(progress) + "总长：");

            }

            @Override
            public void onDownloadFinish(File file) {

            }

            @Override
            public void onFailed(String message) {

            }
        };
        RxRestCreator.createResponseService(RxRestService.class, listener)
                .post(ConstantService.UPDATE, new WeakHashMap<String, Object>())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<String>(){
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d(response);
                        JSONObject fastJson = JSON.parseObject(response);
                        UpdateInfo updateInfo = JsonUtils.parseObject(fastJson.getString("data"), UpdateInfo.class);
                        showUpdate(updateInfo);
                    }
                });



        // 获取app更新信息

        /*
        RxRestClient.builder()
                .url(ConstantService.UPDATE)
                .build()
                .post()
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<String>(){
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d(response);
                        JSONObject fastJson = JSON.parseObject(response);
                        UpdateInfo updateInfo = JsonUtils.parseObject(fastJson.getString("data"), UpdateInfo.class);
                        showUpdate(updateInfo);
                    }
                });
                */

    }

    /**
     * 3.显示版本更新新
     */
    public void showUpdate(final UpdateInfo updateInfo) {
        long now_version = 0;
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(),0);
            now_version = packageInfo.versionCode;//获取原版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(String.valueOf(now_version).equals(updateInfo.getVersion())){
            Toast.makeText(this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            Log.d("版本号是", "onResponse: "+ now_version);
        } else {
            LatteLogger.d("版本号是", "onResponse: "+ now_version + " 服务器版本" + updateInfo.getVersion());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle("请升级APP至版本" + updateInfo.getVersion());
            builder.setMessage(updateInfo.getDesc());
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("MainActivity",String.valueOf(Environment.MEDIA_MOUNTED));
                    downFile(updateInfo.getUpadeUrl());
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }
    }

    /**
     * 4.下载apk文件
     */
    private void downFile(String url) {
        mProgressDialog = new ProgressDialog(this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("正在下载");
        mProgressDialog.setMessage("请稍候...");
        mProgressDialog.setProgress(0);
        mProgressDialog.show();
        File file = new File(getApkPath(),"ZhouzhiHouse.apk"); //获取文件路径
        // download("http://gdown.baidu.com/data/wisegame/43b4382f3c757ebe/weixin_1400.apk", file);
        download(ConstantService.BASE_URL + url, file);

    }

    // 4.文件路径
    private String getApkPath() {
        String directoryPath="";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
            directoryPath = getExternalFilesDir("apk").getAbsolutePath();
        }else{//没外部存储就使用内部存储
            directoryPath = getFilesDir()+File.separator+"apk";
        }
        File file = new File(directoryPath);
        Log.e("测试路径",directoryPath);
        if(!file.exists()){//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }

    /**
     * 开始下载
     * @param url
     * @param file
     */
    public void download(@NonNull String url, final File file) {

        ProgressResponseListener downloadListener = new ProgressResponseListener() {
            @Override
            public void onStartDownload(long length) {
                setMax(length);
            }

            @Override
            public void onProgress(int progress) {
                downLoading(progress);
            }

            @Override
            public void onDownloadFinish(File file) {
                downSuccess();
            }

            @Override
            public void onFailed(String message) {

            }
        };

        // SaveFileTask 以后再用DownloadListener
        RxRestCreator.createResponseService(RxRestService.class, downloadListener)
                .download(url)
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                        if (file.exists()) {
                            file.delete();
                        }

                        // 下载完成 task cancle
                        final DownloadFileAsync task = new DownloadFileAsync(downloadListener, file);
                        // task.execute(url);
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                        // 这里一定要判断，否则文件下载不全
                        if (task.isCancelled()) {
                            // 下载完成
                        }

                        //下载文件放在子线程
                        /*
                        mThread = new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                //保存到本地
                                writeFileToDisk(responseBody, file, downloadListener);
                            }
                        };
                        mThread.start();
                        */

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    // 将下载的文件写入本地存储
    private void writeFileToDisk(ResponseBody response, File file, ProgressResponseListener downloadListener) {
        long currentLength = 0;
        OutputStream os = null;

        InputStream is = response.byteStream(); //获取下载输入流
        long totalLength = response.contentLength();

        try {
            os = new FileOutputStream(file); //输出流
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
                Log.e(TAG, "当前进度: " + currentLength);
                //计算当前下载百分比，并经由回调传出
                downloadListener.onProgress((int) (100 * currentLength / totalLength));
                //当百分比为100时下载结束，调用结束回调，并传出下载后的本地路径
                if ((int) (100 * currentLength / totalLength) == 100) {
                    downloadListener.onDownloadFinish(file);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close(); //关闭输入流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /* 进度条更新 */
    public void setMax(final long total) {
        mProgressDialog.setMax((int) total);
    }

    public void downLoading(final int i) {
        mProgressDialog.setProgress(i);
    }

    /**
     * 下载成功
     */
    public void downSuccess() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("下载完成");
        builder.setMessage("是否安装");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android N的权限问题
                    // 兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity();
                            return;
                        }
                    }
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//授权读权限
                    Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "com.example.testmvpapp.fileprovider", new File(getApkPath(), "ZhouzhiHouse.apk"));//注意修改
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(getApkPath(), "ZhouzhiHouse.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
        // startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_APP_INSTALL) {
                downSuccess();
            }
        }
    }
}
