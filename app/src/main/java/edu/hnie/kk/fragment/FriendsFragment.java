package edu.hnie.kk.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import edu.hnie.kk.FriendInfoActivity;
import edu.hnie.kk.R;
import edu.hnie.kk.adapter.FriendsListAdapter;

import java.util.List;

public class FriendsFragment extends Fragment {
    private List<NimUserInfo> dataList;

    public FriendsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ListView friendsListView = view.findViewById(R.id.friends_list_view);
        FriendsListAdapter adapter = init();
        friendsListView.setAdapter(adapter);
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NimUserInfo userInfo = dataList.get(position);
                Intent intent = new Intent(getActivity(),FriendInfoActivity.class);
                intent.putExtra("account",userInfo.getAccount());
                startActivity(intent);
            }
        });
        return view;
    }

    private FriendsListAdapter init() {
        List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts(); // 获取所有好友帐号
        dataList = NIMClient.getService(UserService.class).getUserInfoList(accounts); // 获取所有好友用户资料
        FriendsListAdapter adapter = new FriendsListAdapter(dataList,getActivity());
        return adapter;
    }


}
