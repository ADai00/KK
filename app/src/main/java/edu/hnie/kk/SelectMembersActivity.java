package edu.hnie.kk;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import edu.hnie.kk.adapter.SelectMembersAdapter;

public class SelectMembersActivity extends BaseActivity {
    private TextView selectMembersCancel;
    private TextView selectMembersTitle;

    private RelativeLayout createLayout;
    private ListView selectMembersListView;
    private List<NimUserInfo> dataList;
    private SelectMembersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_members);

        //初始化title
        selectMembersCancel = findViewById(R.id.select_members_cancel);
        selectMembersTitle = findViewById(R.id.select_members_title);
        selectMembersCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(SelectMembersActivity.this, MainActivity.class);
                finish();
            }
        });
        selectMembersTitle.setText("创建群聊");

        //初始化内容
        createLayout = findViewById(R.id.create_layout);
        selectMembersListView = findViewById(R.id.select_members_list_view);
        initFriends();
        selectMembersListView.setAdapter(adapter);

        createLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> accounts = getAccounts();
                // 群组类型
                TeamTypeEnum type = TeamTypeEnum.Normal;
                String name = "普通群群名字2";
                // 创建时可以预设群组的一些相关属性，如果是普通群，仅群名有效。
                // fields 中，key 为数据字段，value 对对应的值，该值类型必须和 field 中定义的 fieldType 一致
                HashMap<TeamFieldEnum, Serializable> fields = new HashMap<>();
                fields.put(TeamFieldEnum.Name, name);
                NIMClient.getService(TeamService.class).createTeam(fields, type, "", accounts)
                        .setCallback(new RequestCallback<CreateTeamResult>() {
                            @Override
                            public void onSuccess(CreateTeamResult createTeamResult) {
                                actionStart(SelectMembersActivity.this, MainActivity.class);
                                finish();
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
    }

    private void initFriends() {
        List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts(); // 获取所有好友帐号
        dataList = NIMClient.getService(UserService.class).getUserInfoList(accounts); // 获取所有好友用户资料
        adapter = new SelectMembersAdapter(dataList, SelectMembersActivity.this);
    }


}
