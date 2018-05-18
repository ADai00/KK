package edu.hnie.kk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.R;
import edu.hnie.kk.domain.GroupChat;

import java.util.List;

public class SearchTeamAdapter extends BaseAdapter {
    private List<GroupChat> dataList;
    private Context context;

    public SearchTeamAdapter(List<GroupChat> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.search_team_item, parent, false);
        CircleImageView searchGroupChatIcon = convertView.findViewById(R.id.search_group_chat_icon);
        TextView searchGroupChatName = convertView.findViewById(R.id.search_group_chat_name);
        TextView searchGroupChatDescription = convertView.findViewById(R.id.search_group_chat_description);
        GroupChat groupChat = dataList.get(position);
        searchGroupChatIcon.setImageResource(groupChat.getIcon());
        searchGroupChatName.setText(groupChat.getName());
        searchGroupChatDescription.setText(groupChat.getDescription());
        return convertView;
    }
}
