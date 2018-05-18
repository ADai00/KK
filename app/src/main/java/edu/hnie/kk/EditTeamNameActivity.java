package edu.hnie.kk;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.rengwuxian.materialedittext.MaterialEditText;

public class EditTeamNameActivity extends BaseActivity {

    private TextView commonBackMoreBack;
    private TextView commonBackMoreTitle;
    private TextView commonBackMoreMore;
    private MaterialEditText teamNameEditText;

    private String teamId;
    private String name;

    public EditTeamNameActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team_name);

        teamId = getIntent().getStringExtra("teamId");

        commonBackMoreBack = findViewById(R.id.common_back_more_back);
        commonBackMoreTitle = findViewById(R.id.common_back_more_title);
        commonBackMoreMore = findViewById(R.id.common_back_more_more);
        commonBackMoreBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTeamNameActivity.this, TeamDetailsActivity.class);
                intent.putExtra("teamId", teamId);
                startActivity(intent);
                finish();
            }
        });
        commonBackMoreTitle.setText("编辑群聊名称");
        commonBackMoreMore.setText("完成");

        teamNameEditText = findViewById(R.id.team_name_edit_text);

        NIMClient.getService(TeamService.class).queryTeam(teamId).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                String teamName = team.getName();
                teamNameEditText.setText(teamName);
                teamNameEditText.setSelection(teamName.length());
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });

        teamNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Resources resources = getResources();
                if (s.length() > 0) {
                    name = s.toString();
                    commonBackMoreMore.setTextColor(resources.getColor(R.color.white));
                    commonBackMoreMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NIMClient.getService(TeamService.class).updateName(teamId, name);
                            Intent intent = new Intent(EditTeamNameActivity.this, TeamDetailsActivity.class);
                            intent.putExtra("teamId", teamId);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    commonBackMoreMore.setTextColor(resources.getColor(R.color.gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
