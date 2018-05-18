package edu.hnie.kk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.List;

import edu.hnie.kk.adapter.TeamMemberListAdapter;

public class TeamDetailsActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;
    private String teamId;
    private RelativeLayout exitTeamLayout;
    private RelativeLayout dismissTeamLayout;

    private TextView teamName;
    private TextView teamCount;
    private ListView teamMemberListView;
    private RelativeLayout addMemberLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        //初始化title
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);

        teamId = getIntent().getStringExtra("teamId");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamDetailsActivity.this, TeamSendMessageActivity.class);
                intent.putExtra("teamId", teamId);
                startActivity(intent);
                finish();
            }
        });
        commonBackTitle.setText("群聊资料");

        exitTeamLayout = findViewById(R.id.exit_team_layout);
        dismissTeamLayout = findViewById(R.id.dismiss_team_layout);
        teamName = findViewById(R.id.team_name);
        teamCount = findViewById(R.id.team_count);
        teamMemberListView = findViewById(R.id.team_member_list_view);
        addMemberLayout = findViewById(R.id.add_member_layout);

        NIMClient.getService(TeamService.class).queryTeam(teamId).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                teamName.setText(team.getName());
                teamCount.setText("共" + team.getMemberCount() + "人");
                String creator = team.getCreator();
                String account = getAccount();
                if (account.equals(creator)) {
                    exitTeamLayout.setVisibility(View.GONE);
                    dismissTeamLayout.setVisibility(View.VISIBLE);
                    teamName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TeamDetailsActivity.this,EditTeamNameActivity.class);
                            intent.putExtra("teamId",teamId);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    exitTeamLayout.setVisibility(View.VISIBLE);
                    dismissTeamLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });


        //获取群组成员
        NIMClient.getService(TeamService.class).queryMemberList(teamId).setCallback(new RequestCallbackWrapper<List<TeamMember>>() {
            @Override
            public void onResult(int code, final List<TeamMember> members, Throwable exception) {
                if (members.size() > 0) {
                    TeamMemberListAdapter adapter = new TeamMemberListAdapter(members, TeamDetailsActivity.this,teamId);
                    teamMemberListView.setAdapter(adapter);
                }
            }
        });

        addMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@todo 跳转到添加成员界面
//                Intent intent
            }
        });

        exitTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIMClient.getService(TeamService.class).quitTeam(teamId).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 退群成功
                        Toast.makeText(TeamDetailsActivity.this, "退群成功", Toast.LENGTH_SHORT).show();
                        actionStart(TeamDetailsActivity.this, MainActivity.class);
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 退群失败
                        Toast.makeText(TeamDetailsActivity.this, "退群失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(TeamDetailsActivity.this, "退群错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dismissTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIMClient.getService(TeamService.class).dismissTeam(teamId).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 解散群成功
                        Toast.makeText(TeamDetailsActivity.this, "解散群成功", Toast.LENGTH_SHORT).show();
                        actionStart(TeamDetailsActivity.this, MainActivity.class);
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 解散群失败
                        Toast.makeText(TeamDetailsActivity.this, "解散群失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(TeamDetailsActivity.this, "解散群错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
