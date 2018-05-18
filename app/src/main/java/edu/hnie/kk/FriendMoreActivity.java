package edu.hnie.kk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

public class FriendMoreActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;
    private String account;
    private NimUserInfo userInfo;
    private TextView friendNickname;
    private RelativeLayout friendDetailLayout;
    private RelativeLayout deleteFriendLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_more);

        //初始化title
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);

        account = getIntent().getStringExtra("account");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendMoreActivity.this, FriendInfoActivity.class);
                intent.putExtra("account", account);
                startActivity(intent);
                finish();
            }
        });
        commonBackTitle.setText("更多");

        friendNickname = findViewById(R.id.friend_nickname);
        friendDetailLayout = findViewById(R.id.friend_detail_layout);
        deleteFriendLayout = findViewById(R.id.delete_friend_layout);

        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        userInfo = nimUserInfos.get(0);
                        friendNickname.setText(userInfo.getName());
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });

        friendDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendMoreActivity.this, FriendDetailsActivity.class);
                intent.putExtra("account", account);
                startActivity(intent);
                finish();
            }
        });

        deleteFriendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIMClient.getService(FriendService.class).deleteFriend(account)
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                Toast.makeText(FriendMoreActivity.this, "删除好友成功", Toast.LENGTH_SHORT).show();
                                actionStart(FriendMoreActivity.this, MainActivity.class);
                                finish();
                            }

                            @Override
                            public void onFailed(int code) {
                                Toast.makeText(FriendMoreActivity.this, "删除好友失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onException(Throwable exception) {
                                Toast.makeText(FriendMoreActivity.this, "删除好友错误", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
