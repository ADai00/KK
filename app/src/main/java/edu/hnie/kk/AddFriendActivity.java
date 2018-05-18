package edu.hnie.kk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends BaseActivity {
    private TextView addFriendCancel;
    private TextView addFriendSend;
    private CircleImageView addFriendAvatar;
    private TextView addFriendNickname;
    private TextView addFriendSex;
    private TextView addFriendAge;
    private MaterialEditText addFriendValidateInfo;

    private NimUserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        final String account = getIntent().getStringExtra("account");

        //初始化title
        addFriendCancel = findViewById(R.id.add_friend_cancel);
        addFriendSend = findViewById(R.id.add_friend_send);
        addFriendCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(AddFriendActivity.this,AddFriendInfoActivity.class);
                finish();
            }
        });
        addFriendSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final VerifyType verifyType = VerifyType.DIRECT_ADD; // 直接添加
//                String msg = addFriendValidateInfo.getText().toString();
                NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType))
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddFriendActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });
                Intent intent = new Intent(AddFriendActivity.this, AddFriendInfoActivity.class);
                intent.putExtra("account",account);
                startActivity(intent);
            }
        });


        addFriendAvatar = findViewById(R.id.add_friend_avatar);
        addFriendNickname = findViewById(R.id.add_friend_nickname);
        addFriendSex = findViewById(R.id.add_friend_sex);
        addFriendAge = findViewById(R.id.add_friend_age);
        addFriendValidateInfo = findViewById(R.id.add_friend_validate_info);


        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        userInfo = nimUserInfos.get(0);
                        addFriendNickname.setText(userInfo.getName());
                        GenderEnum genderEnum = userInfo.getGenderEnum();
                        String sex = getSex(genderEnum);
                        addFriendSex.setText(sex);
                        if (TextUtils.isEmpty(userInfo.getBirthday())) {
                            addFriendAge.setText("0");
                        } else {
                            addFriendAge.setText(String.valueOf(DateUtils.getAge(userInfo.getBirthday())));
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
