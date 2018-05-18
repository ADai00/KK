package edu.hnie.kk;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

import edu.hnie.kk.utils.DateUtils;

public class AddFriendInfoActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;
    private TextView commonInfoNickname;
    private TextView commonInfoSign;
    private LinearLayout commonInfoLayout;
    private TextView commonInfoAccount;
    private TextView commonInfoSex;
    private TextView commonInfoAge;
    private RelativeLayout commonInfoBtn;
    private TextView commonInfoBtnText;

    private NimUserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_info);

        //初始化title
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(AddFriendInfoActivity.this, SearchFriendsOrTeamActivity.class);
                finish();
            }
        });
        commonBackTitle.setText("个人资料");


        commonInfoNickname = findViewById(R.id.common_info_nickname);
        commonInfoSign = findViewById(R.id.common_info_sign);
        commonInfoLayout = findViewById(R.id.common_info_layout);
        commonInfoAccount = findViewById(R.id.common_info_account);
        commonInfoSex = findViewById(R.id.common_info_sex);
        commonInfoAge = findViewById(R.id.common_info_age);
        commonInfoBtn = findViewById(R.id.common_info_btn);
        commonInfoBtnText = findViewById(R.id.common_info_btn_text);


        commonInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@TODO 去资料详情页面
            }
        });

        final String account = getIntent().getStringExtra("account");
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

        commonInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddFriendInfoActivity.this, AddFriendActivity.class);
//                intent.putExtra("account",account);
//                startActivity(intent);
//                finish();
                final VerifyType verifyType = VerifyType.DIRECT_ADD; // 直接添加
                NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType))
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddFriendInfoActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });
            }
        });
        commonInfoBtnText.setText("加好友");


    }
}
