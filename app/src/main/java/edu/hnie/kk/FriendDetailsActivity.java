package edu.hnie.kk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

import edu.hnie.kk.utils.DateUtils;

public class FriendDetailsActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;
    private String account;
    private NimUserInfo userInfo;
    private TextView friendNickname;
    private TextView friendSex;
    private TextView friendAge;
    private TextView friendBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);

        //初始化title
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);

        account = getIntent().getStringExtra("account");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendDetailsActivity.this, FriendInfoActivity.class);
                intent.putExtra("account", account);
                startActivity(intent);
                finish();
            }
        });
        commonBackTitle.setText("详细资料");


        friendNickname = findViewById(R.id.friend_nickname);
        friendSex = findViewById(R.id.friend_sex);
        friendAge = findViewById(R.id.friend_age);
        friendBirthday = findViewById(R.id.friend_birthday);

        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        userInfo = nimUserInfos.get(0);
                        friendNickname.setText(userInfo.getName());
                        GenderEnum genderEnum = userInfo.getGenderEnum();
                        String sex = getSex(genderEnum);
                        friendSex.setText(sex);
                        String birthday = userInfo.getBirthday();
                        if (TextUtils.isEmpty(birthday)) {
                            friendAge.setText("0");
                            friendBirthday.setText("");
                        } else {
                            friendAge.setText(String.valueOf(DateUtils.getAge(birthday)));
                            friendBirthday.setText(birthday);
                        }


                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }
}
