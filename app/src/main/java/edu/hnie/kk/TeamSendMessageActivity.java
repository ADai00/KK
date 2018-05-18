package edu.hnie.kk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.rengwuxian.materialedittext.MaterialEditText;

import edu.hnie.kk.adapter.MsgAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TeamSendMessageActivity extends BaseActivity {
    private TextView commonSendMessageTitle;
    private ImageView commonSendMessageBack;
    private ImageView commonSendMessageGroup;

    private ImageView additionImg;
    private LinearLayout albumLayout;
    private LinearLayout cameraLayout;
    private LinearLayout fileLayout;
    private LinearLayout addImgLayout;
    private MaterialEditText textMessage;
    private RelativeLayout sendBtn;
    private RecyclerView messageRecycleView;
    private List<IMMessage> messageList = new ArrayList<>();
    private MsgAdapter msgAdapter;
    private boolean flag = false;//用来判断是否点击加号图片和emoji图片

    private String teamId;
    private Uri imageUri;
    private static final int CAMERA = 1;//相机的请求码
    private static final int ALBUM = 2;//相册的请求码
    private static final int FILE = 3;//相册的请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_send_message);
        final Intent intent = getIntent();
        teamId = intent.getStringExtra("teamId");

        NIMClient.getService(TeamService.class).queryTeam(teamId).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                commonSendMessageTitle.setText(team.getName());
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
        commonSendMessageTitle = findViewById(R.id.common_send_message_title);
        commonSendMessageBack = findViewById(R.id.common_send_message_back);
        commonSendMessageGroup = findViewById(R.id.common_send_message_more);


        commonSendMessageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(TeamSendMessageActivity.this, MainActivity.class);
                finish();
            }
        });
        commonSendMessageGroup.setImageResource(R.drawable.icon_group);
        commonSendMessageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TeamSendMessageActivity.this, TeamDetailsActivity.class);
                intent1.putExtra("teamId", teamId);
                startActivity(intent1);
                finish();
            }
        });

        //初始化content
        additionImg = findViewById(R.id.addition_img);
        albumLayout = findViewById(R.id.album_layout);
        cameraLayout = findViewById(R.id.camera_layout);
        fileLayout = findViewById(R.id.file_layout);
        addImgLayout = findViewById(R.id.add_img_layout);
        textMessage = findViewById(R.id.text_message);
        sendBtn = findViewById(R.id.send_btn);
        messageRecycleView = findViewById(R.id.message_recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messageRecycleView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(messageList, TeamSendMessageActivity.this);
        messageRecycleView.setAdapter(msgAdapter);


        receiveMessage();//开启接收消息的监听
        addImgLayout.setVisibility(View.GONE);
        additionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    additionImg.setImageResource(R.drawable.icon_delete);
                    addImgLayout.setVisibility(View.VISIBLE);
                    flag = true;
                } else {
                    additionImg.setImageResource(R.drawable.icon_addition);
                    addImgLayout.setVisibility(View.GONE);
                    flag = false;
                }
            }
        });

        albumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有权限
                if (ContextCompat.checkSelfPermission(TeamSendMessageActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TeamSendMessageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALBUM);
                } else {
                    openAlbum(ALBUM);
                }
            }
        });

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = photoShoot(CAMERA);

            }
        });

        fileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(TeamSendMessageActivity.this)
                        .withRequestCode(FILE)
                        .withTitle("文件选择")
                        .withStartPath("/storage/emulated/0")//指定初始显示路径
                        .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                        .withFileSize(10 * 1024 * 1024)//指定文件大小为2M
                        .withIconStyle(Constant.ICON_STYLE_BLUE)
                        .withFileFilter(new String[]{".txt", ".doc", ".docx"})
                        .start();
            }
        });

        textMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    sendBtn.setBackgroundColor(getResources().getColor(R.color.gainsboro));
                } else {
                    sendBtn.setBackgroundColor(getResources().getColor(R.color.titleblue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textMessage.getText().toString().length() != 0) {
                    sendTextMessage(textMessage.getText().toString());
                    textMessage.setText("");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭接收监听
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALBUM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(ALBUM);
                } else {
                    Toast.makeText(this, "你没有赋予访问权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA:
                if (resultCode == RESULT_OK) {
                    String imagePath = "/storage/emulated/0" + imageUri.getPath().substring(7);
                    sendImageMessage(imagePath);
                }
                break;
            case ALBUM:
                if (resultCode == RESULT_OK) {
                    String imagePath = sdkVersion(data);
                    if (imagePath != null) {
                        sendImageMessage(imagePath);
                    }
                }
                break;
            case FILE:
                if (resultCode == RESULT_OK) {
                    //如果是文件选择模式，需要获取选择的所有文件的路径集合
                    //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                    List<String> list = data.getStringArrayListExtra("paths");
                    //如果是文件夹选择模式，需要获取选择的文件夹路径
//                    String path = data.getStringExtra("path");
                    if (list != null && list.size() > 0) {
                        for (String filePath : list) {
                            sendFileMessage(filePath);
                        }
                    }

                }
            default:
                break;
        }
    }

    /**
     * 发送文本消息
     *
     * @param textMessage
     */
    private void sendTextMessage(String textMessage) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.Team;
        // 创建一个文本消息
        IMMessage message = MessageBuilder.createTextMessage(teamId, sessionType, textMessage);

        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(message, false);

        //显示发送的文本消息
        showMessage(message);
    }

    /**
     * 显示消息
     *
     * @param message
     */
    private void showMessage(IMMessage message) {
        messageList.add(message);
        msgAdapter.notifyItemInserted(messageList.size() - 1);
        messageRecycleView.scrollToPosition(messageList.size() - 1);
    }


    /**
     * 监听消息的Observer
     */
    Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> messages) {
                    // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                    if (messages != null && messages.size() > 0) {
                        for (IMMessage message : messages) {
                            if (message.getMsgType() == MsgTypeEnum.image) {
                                showMessage(message);
                            } else if (message.getMsgType() == MsgTypeEnum.text) {
                                showMessage(message);
                            } else if (message.getMsgType() == MsgTypeEnum.file) {
                                showMessage(message);
                            }
                        }
                    }
                }
            };


    /**
     * 开启接收消息的监听
     */
    private void receiveMessage() {
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }


    /**
     * 发送图片消息
     */
    private void sendImageMessage(String imagePath) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.Team;
        // 示例图片，需要开发者在相应目录下有图片
        File file = new File(imagePath);
        // 创建一个图片消息
        IMMessage message = MessageBuilder.createImageMessage(teamId, sessionType, file, null);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(message, false);
        //显示发送的图片消息
        showMessage(message);
    }


    /**
     * 发送文件消息
     *
     * @param filePath 文件路径
     */
    private void sendFileMessage(String filePath) {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.Team;
        // 示例文件，需要开发者在相应目录下有文件
        File file = new File(filePath);
        String name = file.getName();
        // 创建文件消息
        IMMessage message = MessageBuilder.createFileMessage(teamId, sessionType, file, name);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(message, false);
        //显示发送的文件消息
        showMessage(message);
    }
}
