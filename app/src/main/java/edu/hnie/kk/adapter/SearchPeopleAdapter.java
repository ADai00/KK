package edu.hnie.kk.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.R;
import edu.hnie.kk.utils.DateUtils;

import java.util.List;

public class SearchPeopleAdapter extends BaseAdapter {
    private List<NimUserInfo> dataList;
    private Context context;

    public SearchPeopleAdapter(List<NimUserInfo> dataList, Context context) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.search_people_item, parent, false);
//        CircleImageView searchPeopleIcon = convertView.findViewById(R.id.search_people_icon);
        TextView searchPeopleNickname = convertView.findViewById(R.id.search_people_nickname);
        TextView searchPeopleAccount = convertView.findViewById(R.id.search_people_account);
        LinearLayout searchPeopleSexAgeLayout = convertView.findViewById(R.id.search_people_sex_age_layout);
        TextView searchPeopleSex = convertView.findViewById(R.id.search_people_sex);
        TextView searchPeopleAge = convertView.findViewById(R.id.search_people_age);
//        TextView searchPeopleAddress = convertView.findViewById(R.id.search_people_address);
        NimUserInfo user = dataList.get(position);
//        searchPeopleIcon.setImageResource(user.getAvatar());
        searchPeopleNickname.setText(user.getName());
        searchPeopleAccount.setText(user.getAccount());
        GenderEnum sex = user.getGenderEnum();
        if (sex == GenderEnum.MALE) {
            searchPeopleSex.setText("男");
            searchPeopleSexAgeLayout.setBackgroundColor(convertView.getResources().getColor(R.color.titleblue));
        } else if (sex == GenderEnum.FEMALE) {
            searchPeopleSex.setText("女");
            searchPeopleSexAgeLayout.setBackgroundColor(convertView.getResources().getColor(R.color.deeppink));
        } else if (sex == GenderEnum.UNKNOWN) {
            searchPeopleSex.setText("未知");
            searchPeopleSexAgeLayout.setBackgroundColor(convertView.getResources().getColor(R.color.gray));
        }
        if (TextUtils.isEmpty(user.getBirthday())) {
            searchPeopleAge.setText("0");
        } else {
            searchPeopleAge.setText(String.valueOf(DateUtils.getAge(user.getBirthday())));
        }
//        searchPeopleAddress.setText(user.getAddress());
        return convertView;
    }


}
