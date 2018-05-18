package edu.hnie.kk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.rengwuxian.materialedittext.MaterialEditText;
import edu.hnie.kk.adapter.SearchTeamAdapter;
import edu.hnie.kk.adapter.SearchPeopleAdapter;
import edu.hnie.kk.domain.GroupChat;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendsOrTeamActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;
    private MaterialEditText searchEdit;
    private LinearLayout searchLayout;
    private TextView searchPeopleNumber;
    private TextView searchGroupChatNumber;
    private ListView searchListView;
    private List<NimUserInfo> peopleDataList = new ArrayList<>();
    private List<GroupChat> groupChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends_or_team);

        //初始化title信息
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra("message_fragment", false);
        if (!flag) {
            commonBack.setText("联系人");
        } else {
            commonBack.setText("返回");
        }
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(SearchFriendsOrTeamActivity.this, MainActivity.class);
                finish();
            }
        });
        commonBackTitle.setText("添加");

        //初始化组件
        searchEdit = findViewById(R.id.search_edit);
        searchLayout = findViewById(R.id.search_layout);
        searchPeopleNumber = findViewById(R.id.search_people_number);
        searchGroupChatNumber = findViewById(R.id.search_group_chat_number);
        searchListView = findViewById(R.id.search_list_view);
        searchLayout.setVisibility(View.GONE);


        /**
         * 先判断输入的值是否为数字，位数字且位数大于等于5位，通过KK号进行精确查找，位数小于5位通过昵称进行精确查找，精确查找没找到，在通过模糊查询进行查找
         * 如果为字符串通过昵称进行精确查找，精确查找没找到，在通过模糊查询进行查找
         *
         */
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    searchLayout.setVisibility(View.GONE);
                    searchListView.setVisibility(View.GONE);
                } else {
                    searchLayout.setVisibility(View.VISIBLE);
                    searchListView.setVisibility(View.GONE);
                    searchPeopleNumber.setText(s.toString());
                    searchGroupChatNumber.setText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchPeopleNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLayout.setVisibility(View.GONE);
                showSearchPeopleInfo();

            }
        });

        searchGroupChatNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLayout.setVisibility(View.GONE);
                initGroupChatList();
                SearchTeamAdapter searchGroupChatAdapter = new SearchTeamAdapter(groupChatList, SearchFriendsOrTeamActivity.this);
                searchListView.setAdapter(searchGroupChatAdapter);
                searchListView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initGroupChatList() {
        groupChatList = new ArrayList<>();
        GroupChat groupChat = new GroupChat();
        groupChat.setIcon(R.drawable.common_avatar);
        groupChat.setName("Java技术交流群");
        groupChat.setDescription("这是交流Java技术的群,欢迎大家加入，一起讨论技术和遇到的问题，一起进步，一起成长");
        groupChatList.add(groupChat);

        GroupChat groupChat1 = new GroupChat();
        groupChat1.setIcon(R.drawable.common_avatar);
        groupChat1.setName("C++技术交流群");
        groupChat1.setDescription("这是交流C++技术的群,欢迎大家加入，一起讨论技术和遇到的问题，一起进步，一起成长");
        groupChatList.add(groupChat1);

        GroupChat groupChat2 = new GroupChat();
        groupChat2.setIcon(R.drawable.common_avatar);
        groupChat2.setName("Android技术交流群");
        groupChat2.setDescription("这是交流Android技术的群,欢迎大家加入，一起讨论技术和遇到的问题，一起进步，一起成长");
        groupChatList.add(groupChat2);
    }


    private void showSearchPeopleInfo() {
        List<String> accounts = initAccounts();
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        peopleDataList.clear();
                        String searchNum = searchPeopleNumber.getText().toString();
                        if (searchNum.length() == 11) {
                            searchByAccount(nimUserInfos, searchNum);
                        } else {
                            searchByName(nimUserInfos, searchNum);
                        }
                        SearchPeopleAdapter searchPeopleAdapter = new SearchPeopleAdapter(peopleDataList, SearchFriendsOrTeamActivity.this);
                        searchListView.setAdapter(searchPeopleAdapter);
                        searchListView.setVisibility(View.VISIBLE);
                        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String account = peopleDataList.get(position).getAccount();
                                Intent intent = new Intent(SearchFriendsOrTeamActivity.this, AddFriendInfoActivity.class);
                                intent.putExtra("account",account);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }

    private List<String> initAccounts() {
        List<String> accounts = new ArrayList<>();
        accounts.add("18174697739");
        accounts.add("18174697729");
        accounts.add("18174697719");
        accounts.add("18163977739");
        accounts.add("18163977805");
        accounts.add("17608401017");
        return accounts;
    }

    private void searchByAccount(List<NimUserInfo> nimUserInfos, String searchNum) {
        for (NimUserInfo userInfo : nimUserInfos) {
            String account = userInfo.getAccount();
            if (account.equals(searchNum)) {
                peopleDataList.add(userInfo);
            }
        }
    }

    private void searchByName(List<NimUserInfo> nimUserInfos, String searchNum) {
        for (NimUserInfo userInfo : nimUserInfos) {
            String name = userInfo.getName();
            if (name.contains(searchNum)) {
                peopleDataList.add(userInfo);
            }
        }
    }

}
