package edu.hnie.kk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.utils.DateUtils;

public class UserInfoActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;
    private ImageView commonInfoBg;
    private CircleImageView commonInfoHeaderIcon;
    private TextView commonInfoNickname;
    private TextView commonInfoSign;
    private LinearLayout commonInfoLayout;
    private TextView commonInfoAccount;
    private TextView commonInfoSex;
    private TextView commonInfoAge;
//    private TextView commonInfoAddress;
    private RelativeLayout commonInfoBtn;
    private TextView commonInfoBtnText;

    private Uri coverImgUri;
    private static final int COVER_PHOTO = 1;
    private static final int COVER_ALBUM = 2;
    private static final int HEADER_PHOTO = 3;
    private static final int HEADER_ALBUM = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        NimUserInfo user = getCurrentUser();

        //初始化title
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(UserInfoActivity.this, MainActivity.class);
                finish();
            }
        });
        commonBackTitle.setText("我的资料");

        //初始化content
        commonInfoBg = findViewById(R.id.common_info_bg);
        commonInfoHeaderIcon = findViewById(R.id.common_info_head_icon);
        commonInfoNickname = findViewById(R.id.common_info_nickname);
        commonInfoSign = findViewById(R.id.common_info_sign);
        commonInfoLayout = findViewById(R.id.common_info_layout);
        commonInfoAccount = findViewById(R.id.common_info_account);
        commonInfoSex = findViewById(R.id.common_info_sex);
        commonInfoAge = findViewById(R.id.common_info_age);
//        commonInfoAddress = findViewById(R.id.common_info_address);
        commonInfoBtn = findViewById(R.id.common_info_btn);
        commonInfoBtnText = findViewById(R.id.common_info_btn_text);

        commonInfoBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow(v, COVER_PHOTO, COVER_ALBUM, "更换封面");
            }
        });
        commonInfoHeaderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow(v, HEADER_PHOTO, HEADER_ALBUM, "更换头像");
            }
        });
        commonInfoNickname.setText(user.getName());
        commonInfoSign.setText(user.getSignature());
        commonInfoSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, EditSignActivity.class);
                intent.putExtra("my_info", true);
                startActivity(intent);
                finish();
            }
        });
        commonInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(UserInfoActivity.this, EditInfoActivity.class);
                finish();
            }
        });
        commonInfoAccount.setText(user.getAccount());
        GenderEnum genderEnum = user.getGenderEnum();
        String sex = getSex(genderEnum);
        commonInfoSex.setText(sex);
        if (TextUtils.isEmpty(user.getBirthday())) {
            commonInfoAge.setText("0");
        } else {
            commonInfoAge.setText(String.valueOf(DateUtils.getAge(user.getBirthday())));
        }
//        commonInfoAddress.setText(user.getExtension());


        commonInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(UserInfoActivity.this, EditInfoActivity.class);
                finish();
            }
        });
        commonInfoBtnText.setText("编辑资料");
    }

    /**
     * @param v
     * @param requestCode1 调用相机的requestCode
     * @param requestCode2 调用相册的requestCode
     * @param title        弹窗标题
     */
    private void initPopupWindow(View v, final int requestCode1, final int requestCode2, String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.my_info_pop_item, null, false);
        TextView popItemReplace = view.findViewById(R.id.pop_item_replace);
        TextView popItemPhoneAlbum = view.findViewById(R.id.pop_item_phone_album);
        TextView popItemPhotoShoot = view.findViewById(R.id.pop_item_photo_shoot);
        TextView popItemCancel = view.findViewById(R.id.pop_item_cancel);

        //初始化popupWindow
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));    //要为popWindow设置一个背景才有效

//        popupWindow.showAsDropDown(v,0, Gravity.BOTTOM);
        popupWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        //当popupWindow弹出时使活动背景变色
        final Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        window.setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = 1f;
                window.setAttributes(lp);
            }
        });

        popItemReplace.setText(title);
        //给组件设置监听事件
        popItemPhoneAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有权限
                if (ContextCompat.checkSelfPermission(UserInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode2);
                } else {
                    openAlbum(requestCode2);
                }
                popupWindow.dismiss();
            }
        });
        popItemPhotoShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coverImgUri = photoShoot(requestCode1);
                popupWindow.dismiss();
            }
        });
        popItemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case COVER_ALBUM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(COVER_ALBUM);
                } else {
                    Toast.makeText(this, "你没有赋予访问权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case HEADER_ALBUM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(HEADER_ALBUM);
                } else {
                    Toast.makeText(this, "你没有赋予访问权限", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case COVER_PHOTO:
                if (resultCode == RESULT_OK) {
                    displayImage(coverImgUri, commonInfoBg);
                }
                break;
            case COVER_ALBUM:
                if (resultCode == RESULT_OK) {
                    String imagePath = sdkVersion(data);
                    displayImage(imagePath, commonInfoBg);
                }
                break;
            case HEADER_PHOTO:
                if (resultCode == RESULT_OK) {
                    displayImage(coverImgUri, commonInfoHeaderIcon);
                }
                break;
            case HEADER_ALBUM:
                if (resultCode == RESULT_OK) {
                    String imagePath = sdkVersion(data);
                    displayImage(imagePath, commonInfoHeaderIcon);
                }
                break;
            default:
                break;
        }
    }
}
