package edu.hnie.kk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import edu.hnie.kk.fragment.AddressBookFragment;
import edu.hnie.kk.fragment.MessageFragment;
import edu.hnie.kk.item.MyTabItem;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainActivity extends BaseActivity {
    private MessageFragment messageFragment;
    private AddressBookFragment addressBookFragment;
    private FragmentManager fragmentManager;
    private PageNavigationView myTab;
    private NavigationView navView;
    private BaseTabItem messageItem;
    private BaseTabItem addressBookItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        messageItem = newItem(R.drawable.icon_message_gray, R.drawable.icon_message_blue, "消息");
        addressBookItem = newItem(R.drawable.icon_addressbook_gray, R.drawable.icon_addressbook_blue, "联系人");

//        // 用户资料变更观察者
//        Observer<List<NimUserInfo>> userInfoUpdateObserver = new Observer<List<NimUserInfo>>() {
//            @Override
//            public void onEvent(List<NimUserInfo> nimUserInfos) {
//
//            }
//        };
//        // 注册/注销观察者
//        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver, true);

        //注册添加好友的监听事件
        Observer<SystemMessage> systemMessageObserver = new Observer<SystemMessage>() {
            @Override
            public void onEvent(SystemMessage systemMessage) {
                if (systemMessage.getType() == SystemMessageType.AddFriend) {
                    AddFriendNotify attachData = (AddFriendNotify) systemMessage.getAttachObject();
                    if (attachData != null) {
                        // 针对不同的事件做处理
                        if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_DIRECT) {
                            // 对方直接添加你为好友
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_AGREE_ADD_FRIEND) {
                            // 对方通过了你的好友验证请求
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_REJECT_ADD_FRIEND) {
                            // 对方拒绝了你的好友验证请求
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
                            // 对方请求添加好友，一般场景会让用户选择同意或拒绝对方的好友请求。
                            // 通过message.getContent()获取好友验证请求的附言
                        }
                    }
                }
            }
        };
        NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(systemMessageObserver, true);


        defaultShowFragment();
        myTab = findViewById(R.id.my_tab);
        navView = findViewById(R.id.nav_view);

        View headerView = navView.getHeaderView(0);
        RelativeLayout navHeaderLayout = headerView.findViewById(R.id.nav_header_layout);
        LinearLayout navHeaderSignLayout = headerView.findViewById(R.id.nav_header_sign_layout);
        TextView navHeaderSign = headerView.findViewById(R.id.nav_header_sign);
        TextView navHeaderNickname = headerView.findViewById(R.id.nav_header_nickname);
        NimUserInfo currentUser = getCurrentUser();
        navHeaderNickname.setText(currentUser.getName());
        if (!TextUtils.isEmpty(currentUser.getSignature())) {
            navHeaderSign.setText(currentUser.getSignature());
        } else {
            navHeaderSign.setText("编辑个性签名");
        }

        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(MainActivity.this, UserInfoActivity.class);
            }
        });
        navHeaderSignLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(MainActivity.this, EditSignActivity.class);
            }
        });
        MenuItem item = navView.getMenu().findItem(R.id.logout);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logout();
                return true;
            }
        });



        //注意这里调用了custom()方法
        NavigationController navigationController = myTab.custom()
                .addItem(messageItem)
                .addItem(addressBookItem)
                .build();

        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                hideAllFragment(fragmentTransaction);
                if (index == 0) {
                    fragmentTransaction.show(messageFragment);
                } else if (index == 1) {
                    if (addressBookFragment == null) {
                        addressBookFragment = new AddressBookFragment();
                        fragmentTransaction.add(R.id.show_fragment_layout, addressBookFragment);
                    } else {
                        fragmentTransaction.show(addressBookFragment);
                    }
                }
                fragmentTransaction.commit();
            }

            @Override
            public void onRepeat(int index) {

            }
        });
    }

    /**
     * 注销
     */
    private void logout() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.clear().commit();
        NIMClient.getService(AuthService.class).logout();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        MyTabItem myTabItem = new MyTabItem(this);
        myTabItem.initialize(drawable, checkedDrawable, text);
        myTabItem.setTextDefaultColor(Color.GRAY);
        myTabItem.setTextCheckedColor(getResources().getColor(R.color.titleblue));
        return myTabItem;
    }

    //隐藏所有fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (messageFragment != null) {
            fragmentTransaction.hide(messageFragment);
        }
        if (addressBookFragment != null) {
            fragmentTransaction.hide(addressBookFragment);
        }
    }

    //默认显示的fragment
    private void defaultShowFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        messageFragment = new MessageFragment();
        fragmentTransaction.add(R.id.show_fragment_layout, messageFragment);
        fragmentTransaction.commit();
    }
}
