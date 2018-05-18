package edu.hnie.kk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import edu.hnie.kk.R;

import java.util.ArrayList;
import java.util.List;

public class SelectMembersAdapter extends BaseAdapter {

    private List<NimUserInfo> dataList;
    private List<String> accounts = new ArrayList<>();
    private Context context;

    public SelectMembersAdapter(List<NimUserInfo> dataList, Context context) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.select_member_item, parent, false);
        LinearLayout selectLayout = convertView.findViewById(R.id.select_layout);
        final CheckBox selectButton = convertView.findViewById(R.id.select_button);
        TextView friendsItemNickname = convertView.findViewById(R.id.friends_item_name);
        final NimUserInfo userInfo = dataList.get(position);
        friendsItemNickname.setText(userInfo.getName());
        selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = selectButton.isChecked();
                if (!checked) {
                    selectButton.setChecked(true);
                    accounts.add(userInfo.getAccount());
                    saveMembers();
                } else {
                    selectButton.setChecked(false);
                    accounts.remove(userInfo.getAccount());
                    saveMembers();
                }
            }
        });
        return convertView;
    }

    //
    private void saveMembers() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        editor.putInt("accountSize", accounts.size());
        for (int i = 0; i < accounts.size(); i++) {
            editor.putString("account_" + i, accounts.get(i));
        }
        editor.apply();
    }
}
