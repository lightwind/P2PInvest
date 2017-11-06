package com.lightwind.p2pinvest.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.common.BaseActivity;
import com.lightwind.p2pinvest.utils.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity {

    private static final int PICTURE = 1;
    private static final int CAMERA = 2;
    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.iv_user_icon)
    ImageView mIvUserIcon;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initTitle() {
        mIvTitleBack.setVisibility(View.VISIBLE);
        mTvTitle.setText("用户信息");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.iv_title_back)
    public void onClickBack(View view) {
        // 销毁当前页面
        this.removeCurrentActivity();
        //
    }

    @OnClick({R.id.tv_change_icon, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change_icon:
                String[] items = new String[]{"图库", "相机"};
                new AlertDialog.Builder(this)
                        .setTitle("选择来源")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:// 图库
                                        // 启动其他应用的Activity -- 隐式意图
                                        Intent picture = new Intent(Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(picture, PICTURE);
                                        break;
                                    case 1:// 相机
                                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(camera, CAMERA);
                                        break;
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
                break;
            case R.id.btn_exit:
                // 将保存在sp中的数据清除
                SharedPreferences preferences = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                preferences.edit().clear().apply();// 清除以后，文件还存在，只是里面的数据清除了
                // 将本地保存的图片的file删除
                File filesDir;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// SD卡是否挂载
                    // 路径：storage/sdcard/Android/data/包名/files
                    filesDir = this.getExternalFilesDir("");
                } else {// 手机内部存储
                    // 路径：data/data/包名/files
                    filesDir = this.getFilesDir();
                }
                File file = new File(filesDir, "icon.png");
                if (file.exists()) {
                    file.delete();
                }
                // 销毁所有的Activity
                this.removeAll();
                // 重新进入MainActivity
                goToActivity(MainActivity.class, null);
                break;
        }
    }

    // 重新启动新的Activity以后的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) {// 相机
            // 获取到Bitmap对象
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            // 对获取到的Bitmap对象进行压缩和圆形处理
            bitmap = BitmapUtils.zoom(bitmap, mIvUserIcon.getWidth(), mIvUserIcon.getHeight());
            bitmap = BitmapUtils.circleBitmap(bitmap);
            // 加载显示
            mIvUserIcon.setImageBitmap(bitmap);
            // 上传到服务器（省略）
            // 保存到本地
            saveImage(bitmap);
        } else if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {// 图库
            Uri selectedImage = data.getData();
            // android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,
            // 所以要保证无论是哪个系统版本都能正确获取到图片资源的话就需要针对各种情况进行一个处理了
            // 这里返回的uri情况就有点多了
            // 在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....
            // 在4.4.2返回的是content://com.android.providers.media.documents/document/image

            String pathResult = getPath(selectedImage);
            // 存储--->内存
            Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
            Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, mIvUserIcon.getWidth(), mIvUserIcon.getHeight());
            // bitmap圆形裁剪
            Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);

            // 加载显示
            mIvUserIcon.setImageBitmap(circleImage);
            // 上传到服务器（省略）

            // 保存到本地
            saveImage(circleImage);
        }
    }

    /**
     * 将Bitmap保存到本地
     * 数据的存储。（5种）
     * Bitmap：内存层面的图片对象
     * <p>
     * 存储 ---> 内存：
     * BitmapFactory.decodeFile(String filePath);
     * BitmapFactory.decodeStream(InputStream in);
     * 内存 ---> 存储：
     * bitmap.compress(Bitmap.CompressFormat.PNG,100,OutputStream os);
     */
    private void saveImage(Bitmap bitmap) {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// SD卡是否挂载
            // 路径：storage/sdcard/Android/data/包名/files
            filesDir = this.getExternalFilesDir("");
        } else {// 手机内部存储
            // 路径：data/data/包名/files
            filesDir = this.getFilesDir();
        }

        FileOutputStream fos = null;
        try {
            File file = new File(filesDir, "icon.png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据系统相册选择的文件获取路径
     */
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        // 高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }


        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * uri路径查询字段
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
