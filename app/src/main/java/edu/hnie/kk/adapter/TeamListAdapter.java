package edu.hnie.kk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.netease.nimlib.sdk.team.model.Team;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.R;
import edu.hnie.kk.domain.GroupChat;
import edu.hnie.kk.domain.GroupChatGroup;

import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends BaseAdapter {

    private List<Team> dataList;
    private Context context;

    public TeamListAdapter(List<Team> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.team_item,parent,false);
        CircleImageView teamIcon = convertView.findViewById(R.id.team_item_icon);
        TextView teamName = convertView.findViewById(R.id.team_item_name);
        Team team = dataList.get(position);
        teamName.setText(team.getName());
        return convertView;
    }
}
