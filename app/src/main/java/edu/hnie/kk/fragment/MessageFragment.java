package edu.hnie.kk.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.R;
import edu.hnie.kk.SearchFriendsOrTeamActivity;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private ListView messageListView;
    private List<IMMessage> dataList;

    public MessageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        //初始化title信息
        TextView commonTitle = view.findViewById(R.id.common_title);
        TextView commonAdd = view.findViewById(R.id.common_add);
        CircleImageView commonAvatar = view.findViewById(R.id.common_avatar);
        commonTitle.setText("消息");
        commonAdd.setBackgroundResource(R.drawable.icon_add);
        commonAvatar.setImageResource(R.drawable.common_avatar);
        commonAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout myDrawer = getActivity().findViewById(R.id.my_drawer);
                myDrawer.openDrawer(GravityCompat.START);
            }
        });
        commonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });


        //初始化内容信息
        messageListView = view.findViewById(R.id.message_list_view);
        dataList = new ArrayList<>();
//        dataList.add(new Message("你好我是呆瓜", new User("呆瓜", R.drawable.common_avatar)));
//        dataList.add(new Message("你好我是Rose", new User("Rose", R.drawable.common_avatar)));
//        dataList.add(new Message("你好我是Jack", new User("Jack", R.drawable.common_avatar)));
//        dataList.add(new Message("你好我是Tom", new User("Tom", R.drawable.common_avatar)));
//        dataList.add(new Message("你好我是KK", new User("KK", R.drawable.common_avatar)));
//        MessageAdapter messageAdapter = new MessageAdapter(dataList, getActivity());
//        messageListView.setAdapter(messageAdapter);

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Message message = dataList.get(position);
//                Intent intent = new Intent(getActivity(), SendMessageActivity.class);
//                intent.putExtra("friends_name", message.getSendUser().getNickname());
//                startActivity(intent);
            }
        });
        return view;
    }

    private void initPopWindow(View v) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.message_popup_item, null, false);
        LinearLayout messageCreateGroupChat = view.findViewById(R.id.message_create_group_chat);
        LinearLayout messageAddPeopleOrGroupChat = view.findViewById(R.id.message_add_people_or_group_chat);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
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

        popupWindow.showAsDropDown(v, -380, 38);

        //当popupWindow弹出时使活动背景变色
        final Window window = getActivity().getWindow();
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

        //给组件添加监听事件
        messageCreateGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "To创建群聊", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        messageAddPeopleOrGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchFriendsOrTeamActivity.class);
                intent.putExtra("message_fragment",true);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }
}
