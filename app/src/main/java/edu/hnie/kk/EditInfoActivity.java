package edu.hnie.kk;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditInfoActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;

    private LinearLayout editInfoSexLayout;
    private LinearLayout editInfoBirthdayLayout;
//    private LinearLayout editInfoHometownLayout;

    private MaterialEditText editInfoNickname;
    private TextView editInfoSex;
    private TextView editInfoBirthday;
//    private TextView editInfoHometown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        NimUserInfo currentUser = getCurrentUser();
        //初始化title
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<UserInfoFieldEnum, Object> fields = new HashMap<>(3);
                fields.put(UserInfoFieldEnum.Name, editInfoNickname.getText().toString());
                String gender = editInfoSex.getText().toString();
                if ("男".equals(gender)) {
                    fields.put(UserInfoFieldEnum.GENDER, 1);
                } else {
                    fields.put(UserInfoFieldEnum.GENDER, 2);
                }
                fields.put(UserInfoFieldEnum.BIRTHDAY, editInfoBirthday.getText().toString());
//                fields.put(UserInfoFieldEnum.EXTEND, editInfoHometown.getText());
                editInfo(fields);
                actionStart(EditInfoActivity.this, UserInfoActivity.class);
                finish();
            }
        });
        commonBackTitle.setText("编辑资料");

        //初始化content
        editInfoSexLayout = findViewById(R.id.edit_info_sex_layout);
        editInfoBirthdayLayout = findViewById(R.id.edit_info_birthday_layout);
//        editInfoHometownLayout = findViewById(R.id.edit_info_hometown_layout);
        editInfoNickname = findViewById(R.id.edit_info_nickname);
        editInfoSex = findViewById(R.id.edit_info_sex);
        editInfoBirthday = findViewById(R.id.edit_info_birthday);
//        editInfoHometown = findViewById(R.id.edit_info_hometown);

        editInfoNickname.setText(currentUser.getName());
        editInfoSex.setText(currentUser.getGenderEnum() == GenderEnum.MALE ? "男" : "女");
        editInfoBirthday.setText(currentUser.getBirthday());
//        editInfoHometown.setText(currentUser.getExtension());

        //添加监听事件
        editInfoSexLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSexPopupWindow(v);
            }
        });

        editInfoBirthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(EditInfoActivity.this, SelectBirthdayActivity.class);
                finish();
            }
        });

        /*editInfoHometownLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(EditInfoActivity.this, SelectHometownActivity.class);
                finish();
            }
        });*/


    }

    private void initSexPopupWindow(View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.edit_info_sex_picker_view, null, false);
        WheelView wheelView = view.findViewById(R.id.sex_wheel_view);

        //初始化popupWindow
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editInfoSex.setTextColor(getResources().getColor(R.color.black));
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));    //要为popWindow设置一个背景才有效

//        popupWindow.showAsDropDown(v,0, Gravity.BOTTOM);
        popupWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        //初始化WheelView
        wheelView.setCyclic(false);

        final List<String> mOptionsItems = new ArrayList<>();
        mOptionsItems.add("男");
        mOptionsItems.add("女");

        wheelView.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        int index = mOptionsItems.indexOf(editInfoSex.getText().toString());
        wheelView.setCurrentItem(index);
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                editInfoSex.setTextColor(getResources().getColor(R.color.titleblue));
                editInfoSex.setText(mOptionsItems.get(index));
            }
        });
    }


}
