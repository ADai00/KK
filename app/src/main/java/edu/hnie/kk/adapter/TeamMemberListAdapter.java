package edu.hnie.kk.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.R;
import edu.hnie.kk.TeamDetailsActivity;
import edu.hnie.kk.utils.DateUtils;

public class TeamMemberListAdapter extends BaseAdapter {

    private List<TeamMember> dataList;
    private Context context;
    private String teamId;

    public TeamMemberListAdapter(List<TeamMember> dataList, Context context,String teamId) {
        this.dataList = dataList;
        this.context = context;
        this.teamId = teamId;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null && dataList.size() > 0) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.team_member_item,parent,false);
        CircleImageView teamMemberIcon = convertView.findViewById(R.id.team_member_icon);
        final TextView teamMemberName = convertView.findViewById(R.id.team_member_name);
        ImageView removeTeamMember = convertView.findViewById(R.id.remove_team_member);
        final TeamMember teamMember = dataList.get(position);
        String account = teamMember.getAccount();
        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        NimUserInfo userInfo = nimUserInfos.get(0);
                        teamMemberName.setText(userInfo.getName());
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
        removeTeamMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("确认移除？")
                        .setPositiveButton("移除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NIMClient.getService(TeamService.class).removeMember(teamId,teamMember.getAccount());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
            }
        });
        return convertView;
    }
}
