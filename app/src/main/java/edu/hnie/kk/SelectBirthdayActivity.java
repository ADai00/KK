package edu.hnie.kk;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import edu.hnie.kk.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SelectBirthdayActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;

    private TimePickerView pvTime;
    private LinearLayout ageLayout;
    private TextView ageTextView;
    private LinearLayout birthdayLayout;
    private TextView birthdayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_birthday);
        NimUserInfo currentUser = getCurrentUser();
        //初始化title
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(SelectBirthdayActivity.this, EditInfoActivity.class);
                finish();
            }
        });
        commonBackTitle.setText("选择出生日期");

        //初始化TimePickerView
        initTimePicker();

        //初始化内容
        ageLayout = findViewById(R.id.age_layout);
        ageTextView = findViewById(R.id.age_text_view);
        birthdayLayout = findViewById(R.id.birthday_layout);
        birthdayTextView = findViewById(R.id.birthday_text_view);
        ageTextView.setText(String.valueOf(DateUtils.getAge(currentUser.getBirthday())));
        birthdayTextView.setText(currentUser.getBirthday());
        birthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show(v);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
            }
        });

    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(1900, 0, 1);
        endDate.set(2018, 11, 31);
        String birthday = getCurrentUser().getBirthday();
        if (!TextUtils.isEmpty(birthday)) {
            Date date = DateUtils.stringToDate(birthday);
            selectedDate.setTime(date);
        }


        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String birthday = DateUtils.dateToString(date,"yyyy-MM-dd");
                birthdayTextView.setText(birthday);
                Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
                fields.put(UserInfoFieldEnum.BIRTHDAY, birthday);
                editInfo(fields);
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        ageTextView.setText(String.valueOf(DateUtils.getAge(date)));
                    }
                })
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .isDialog(true)//是否显示为对话框样式
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }

}
