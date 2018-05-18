package edu.hnie.kk;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ForgetPasswordStepOneActivity extends BaseActivity {
    private RelativeLayout forgetPasswordStepOneConfirmLayout;
    private TextView commonBack;
    private TextView commonBackTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_step_one);

        forgetPasswordStepOneConfirmLayout = findViewById(R.id.forget_password_step_one_confirm_layout);
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBackTitle.setText("短信验证");
        forgetPasswordStepOneConfirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(ForgetPasswordStepOneActivity.this,ForgetPasswordStepTwoActivity.class);
                finish();
            }
        });
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(ForgetPasswordStepOneActivity.this,LoginActivity.class);
                finish();
            }
        });
    }
}
