package edu.hnie.kk.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import edu.hnie.kk.R;
import edu.hnie.kk.TeamSendMessageActivity;
import edu.hnie.kk.adapter.TeamListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment {
    private List<Team> dataList = new ArrayList<>();
    private TeamListAdapter adapter;

    public TeamFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        final ListView teamListView = view.findViewById(R.id.team_list_view);
        //初始化加入的群
        NIMClient.getService(TeamService.class).queryTeamList().setCallback(new RequestCallback<List<Team>>() {
            @Override
            public void onSuccess(List<Team> teams) {
                Toast.makeText(getActivity(), "成功", Toast.LENGTH_SHORT).show();
                // 获取成功，teams为加入的所有群组
                dataList.addAll(teams);
                adapter = new TeamListAdapter(dataList, getActivity());
                teamListView.setAdapter(adapter);
            }

            @Override
            public void onFailed(int i) {
                // 获取失败，具体错误码见i参数
                Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                // 获取异常
                Toast.makeText(getActivity(), "异常", Toast.LENGTH_SHORT).show();
            }
        });
        teamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team team = dataList.get(position);
                Intent intent = new Intent(getActivity(), TeamSendMessageActivity.class);
                intent.putExtra("teamId", team.getId());
                startActivity(intent);
            }
        });
        return view;
    }

}
