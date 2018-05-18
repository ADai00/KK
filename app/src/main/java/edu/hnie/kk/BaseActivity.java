package edu.hnie.kk;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

//    protected NimUserInfo userInfo;

    protected void actionStart(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }

    /**
     * 得到当前登入账号
     *
     * @return
     */
    protected String getAccount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
        String account = preferences.getString("account", null);
        return account;
    }

    protected List<String> getAccounts() {
        List<String> accounts = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
        int accountSize = preferences.getInt("accountSize", 0);
        if (accountSize > 0) {
            for (int i = 0; i < accountSize; i++) {
                String account = preferences.getString("account_" + i, null);
                if (account != null) {
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }

    /**
     * 得到当前登入用户信息
     */
    protected NimUserInfo getCurrentUser() {
        NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(getAccount());
        return user;
    }

    protected String getSex(GenderEnum genderEnum) {
        if (genderEnum == GenderEnum.UNKNOWN) {
            return "未知";
        } else if (genderEnum == GenderEnum.FEMALE) {
            return "女";
        } else if (genderEnum == GenderEnum.MALE) {
            return "男";
        }
        return "未知";
    }


    /**
     * 修改信息
     */
    protected void editInfo(Map<UserInfoFieldEnum, Object> fields) {
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {

                    }
                });
    }

    /**
     * 打开相册
     */
    protected void openAlbum(int requestCode) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    /**
     * 处理所选图片 sdk19以上处理方法
     *
     * @param data
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径
            imagePath = uri.getPath();
        }
        //显示图片
        return imagePath;
    }

    /**
     * 处理所选图片 sdk19以下处理方法
     *
     * @param data
     */
    protected String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        return getImagePath(uri, null);
    }

    protected String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    protected void displayImage(String imagePath, ImageView imageView) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断版本号
     */
    protected String sdkVersion(Intent data) {
        String imagePath = null;
        //判断手机版本号
        if (Build.VERSION.SDK_INT >= 19) {
            //4.4及以上系统使用这个方法处理图片
            imagePath = handleImageOnKitKat(data);
        } else {
            //4.4及以下系统使用这个方法处理图片
            imagePath = handleImageBeforeKitKat(data);
        }
        return imagePath;
    }


    /**
     * ========================分割线===========================
     */

    /**
     * 调用相机拍照
     */
    protected Uri photoShoot(int requestCode) {
        Uri imageUri = null;
        String fileName = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
        try {
            File imageFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+"/Pictures", fileName);
            if (imageFile.exists()) {
                imageFile.delete();
            }
            imageFile.createNewFile();
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(this, "edu.hnie.kk.fileprovider", imageFile);
            } else {
                imageUri = Uri.fromFile(imageFile);
            }
            //启动相机程序
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, requestCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUri;
    }


    protected void displayImage(Uri coverImgUri, ImageView imageView) {
        try {
            //将照片显示出来
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(coverImgUri));
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
