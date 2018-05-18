package edu.hnie.kk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.R;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private List<IMMessage> dataList;
    private Context context;

    public MessageAdapter(List<IMMessage> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.message_item,parent,false);
        CircleImageView sendUserIcon = convertView.findViewById(R.id.send_user_icon);
        TextView sendUsername = convertView.findViewById(R.id.send_user_name);
        TextView sendMessage = convertView.findViewById(R.id.send_user_message);
        TextView sendTime = convertView.findViewById(R.id.send_time);
        IMMessage message = dataList.get(position);
//        sendUserIcon.setImageResource(message.getSendUser().getIcon());
//        sendUsername.setText(message.getSendUser().getNickname());
//        sendMessage.setText(message.getSendMessage());
//        sendTime.setText(DateUtils.dateToString(new Date()));
        return convertView;
    }
}
