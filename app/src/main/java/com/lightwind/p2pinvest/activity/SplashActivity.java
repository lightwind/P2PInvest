package com.lightwind.p2pinvest.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.bean.UpdateInfo;
import com.lightwind.p2pinvest.common.ActivityManager;
import com.lightwind.p2pinvest.common.AppNetConfig;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    private static final int TO_MAIN = 1;
    private static final int DOWNLOAD_VERSION_SUCCESS = 2;
    private static final int DOWNLOAD_APK_FILE = 3;
    private static final int DOWNLOAD_APK_SUCCESS = 4;
    @BindView(R.id.rl_splash)
    RelativeLayout mRlSplash;
    @BindView(R.id.tv_splash_version)
    TextView mTvSplashVersion;

    private ProgressDialog mDialog;
    private File mApkFile;// 保存下载的APK的文件夹
    private long mStartTime;
    private UpdateInfo mUpdateInfo;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TO_MAIN:
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case DOWNLOAD_VERSION_SUCCESS:
                    // 获取当前应用的版本信息
                    String version = getVersion();

                    mTvSplashVersion.setText(version);

                    // 对比服务器的
                    if (version.equals(mUpdateInfo.version)) {
                        UIUtils.toast("当前应用已经是最新版本！", false);
                        toMain();
                    } else {
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("下载最新版本")
                                .setMessage(mUpdateInfo.desc)
                                .setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 下载服务器保存的应用数据
                                        downloadApk();
                                    }
                                })
                                .setNegativeButton("暂不下载", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        toMain();
                                    }
                                })
                                .show();
                    }
                    break;
                case DOWNLOAD_APK_FILE:
                    UIUtils.toast("联网下载数据失败！", false);
                    toMain();
                    break;
                case DOWNLOAD_APK_SUCCESS:
                    UIUtils.toast("联网成功，下载数据成功！", false);
                    mDialog.dismiss();
                    installApk();// 安装应用
                    finish();
                    break;
            }
            return false;
        }
    });

    /**
     * 安装应用
     */
    private void installApk() {
        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
        intent.setData(Uri.parse("file:" + mApkFile.getAbsolutePath()));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置该页面全屏显示，要放在加载布局的前面
        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        // 将当前的activity添加到ActivityManager中
        ActivityManager.getInstance().add(this);
        // 启动动画
        setAnimation();

        // 联网更新应用
        updateApkFile();
    }

    /**
     * 下载服务器保存的应用数据
     */
    private void downloadApk() {
        // 初始化ProgressDialog
        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.show();
        // 初始化数据要保存的位置
        File fileDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fileDir = this.getExternalFilesDir("");

        } else {
            fileDir = this.getFilesDir();
        }
        mApkFile = new File(fileDir, "update.apk");

        // 启动分线程联网下载数据
        new Thread() {
            @Override
            public void run() {
                String path = mUpdateInfo.apkUrl;
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    if (connection.getResponseCode() == 200) {
                        mDialog.setMax(connection.getContentLength());// 设置ProgressDialog的最大值
                        inputStream = connection.getInputStream();
                        outputStream = new FileOutputStream(mApkFile);

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = inputStream.read(buffer)) != -1) {
                            // 更新ProgressDialog的进度
                            mDialog.incrementProgressBy(len);

                            outputStream.write(buffer, 0, len);

                            SystemClock.sleep(1);
                        }
                        mHandler.sendEmptyMessage(DOWNLOAD_APK_SUCCESS);
                    } else {
                        mHandler.sendEmptyMessage(DOWNLOAD_APK_FILE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    private void updateApkFile() {

        // 获取系统当前时间
        mStartTime = System.currentTimeMillis();

        // 1、判断是否可以联网
        boolean isConnect = isConnect();
        if (!isConnect) {
            UIUtils.toast("当前没有移动数据网络！", false);
            toMain();
        } else {
            // 联网获取服务器最新版本数据
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(AppNetConfig.UPDATE, new AsyncHttpResponseHandler() {
                @Override
                public void onFailure(Throwable error, String content) {
                    UIUtils.toast("联网请求数据失败", false);
                    toMain();
                }

                @Override
                public void onSuccess(String content) {
                    // 解析JSON数据
                    mUpdateInfo = JSON.parseObject(content, UpdateInfo.class);
                    mHandler.sendEmptyMessage(DOWNLOAD_VERSION_SUCCESS);
                }
            });
        }
    }

    /**
     * 获取当前应用的版本信息
     */
    private String getVersion() {
        String version = "未知版本";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();  找不到就返回“未知版本”。
        }
        return version;
    }

    private void toMain() {
        long currentTime = System.currentTimeMillis();
        long delayTime = 3000 - (currentTime - mStartTime);
        if (delayTime < 0) {
            delayTime = 0;
        }
        mHandler.sendEmptyMessageDelayed(TO_MAIN, delayTime);
    }

    private boolean isConnect() {
        boolean isConnect = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                isConnect = info.isConnected();
            }
        }
        return isConnect;
    }

    /**
     * 启动动画
     */
    private void setAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);// 0：完全透明  1：完全不透明
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());// 设置动画的变化率

        // 方式一：
//        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            /**
//             * 动画结束时：调用如下方法
//             */
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

        // 方式二：使用Handler
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
                // 结束activity的显示，并从栈空间中移除
                ActivityManager.getInstance().remove(SplashActivity.this);
            }
        }, 3000);

        //启动动画
        mRlSplash.startAnimation(alphaAnimation);
    }

}
