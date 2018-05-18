package edu.hnie.kk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class EditSignActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;

    private MaterialEditText editSign;
    private RelativeLayout publishSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sign);
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                boolean flag = intent.getBooleanExtra("my_info", false);
                if (!flag) {
                    actionStart(EditSignActivity.this, MainActivity.class);
                    finish();
                } else {
                    actionStart(EditSignActivity.this, UserInfoActivity.class);
                    finish();
                }
            }
        });
        commonBackTitle.setText("个性签名");

        editSign = findViewById(R.id.edit_sign);
        String signature = getCurrentUser().getSignature();
        if (signature != null) {
            editSign.setText(signature);
        }
        editSign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    editSign.setHint("编辑个性签名");
                    editSign.setHintTextColor(getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        publishSign = findViewById(R.id.publish_sign);
        publishSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sign = editSign.getText().toString();
                if (sign != null && sign.length() > 0) {
                    Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
                    fields.put(UserInfoFieldEnum.SIGNATURE, sign);
                    editInfo(fields);
                }

            }
        });


    }
}
