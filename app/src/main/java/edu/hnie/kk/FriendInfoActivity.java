package edu.hnie.kk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

import edu.hnie.kk.utils.DateUtils;

public class FriendInfoActivity extends BaseActivity {
    private TextView commonBackMoreBack;
    private TextView commonBackMoreTitle;
    private TextView commonBackMoreMore;

    private TextView commonInfoNickname;
    private TextView commonInfoSign;
    private LinearLayout commonInfoLayout;
    private TextView commonInfoAccount;
    private TextView commonInfoSex;
    private TextView commonInfoAge;
    private RelativeLayout commonInfoBtn;
    private TextView commonInfoBtnText;
    private String account;
    private NimUserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        //初始化title
        Intent intent = getIntent();
        commonBackMoreBack = findViewById(R.id.common_back_more_back);
        commonBackMoreTitle = findViewById(R.id.common_back_more_title);
        commonBackMoreMore = findViewById(R.id.common_back_more_more);
        commonBackMoreBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(FriendInfoActivity.this, MainActivity.class);
                finish();
            }
        });
        commonBackMoreTitle.setText("个人资料");
        commonBackMoreMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FriendInfoActivity.this, FriendMoreActivity.class);
                intent1.putExtra("account", account);
                startActivity(intent1);
                finish();
            }
        });


        account = getIntent().getStringExtra("account");

        //初始化content
        commonInfoNickname = findViewById(R.id.common_info_nickname);
        commonInfoSign = findViewById(R.id.common_info_sign);
        commonInfoLayout = findViewById(R.id.common_info_layout);
        commonInfoAccount = findViewById(R.id.common_info_account);
        commonInfoSex = findViewById(R.id.common_info_sex);
        commonInfoAge = findViewById(R.id.common_info_age);
        commonInfoBtn = findViewById(R.id.common_info_btn);
        commonInfoBtnText = findViewById(R.id.common_info_btn_text);
        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        userInfo = nimUserInfos.get(0);
                        commonInfoNickname.setText(userInfo.getName());
                        commonInfoSign.setText(userInfo.getSignature());
                        commonInfoAccount.setText(userInfo.getAccount());
                        GenderEnum genderEnum = userInfo.getGenderEnum();
                        String sex = getSex(genderEnum);
                        commonInfoSex.setText(sex);
                        if (TextUtils.isEmpty(userInfo.getBirthday())) {
                            commonInfoAge.setText("0");
                        } else {
                            commonInfoAge.setText(String.valueOf(DateUtils.getAge(userInfo.getBirthday())));
                        }
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });

        commonInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FriendInfoActivity.this, FriendDetailsActivity.class);
                intent1.putExtra("account", account);
                startActivity(intent1);
                finish();
            }
        });

        commonInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendInfoActivity.this, SendMessageActivity.class);
                intent.putExtra("account", account);
                startActivity(intent);
                finish();
            }
        });
        commonInfoBtnText.setText("发消息");
    }
}
