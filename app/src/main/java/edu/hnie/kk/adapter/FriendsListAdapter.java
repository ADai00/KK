package edu.hnie.kk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.R;

import java.util.List;

public class FriendsListAdapter extends BaseAdapter {

    private List<NimUserInfo> dataList;
    private Context context;

    public FriendsListAdapter(List<NimUserInfo> dataList, Context context) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.friends_item,parent,false);
        CircleImageView friendsItemIcon = convertView.findViewById(R.id.friends_item_icon);
        TextView friendsItemNickname = convertView.findViewById(R.id.friends_item_name);
        TextView friendsItemSign = convertView.findViewById(R.id.friends_item_sign);
        NimUserInfo userInfo = dataList.get(position);
        friendsItemNickname.setText(userInfo.getName());
        friendsItemSign.setText(userInfo.getSignature());
        return convertView;
    }
}
