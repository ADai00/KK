package edu.hnie.kk;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPasswordStepTwoActivity extends BaseActivity {
    private RelativeLayout forgetPasswordStepTwoConfirmLayout;
    private TextView commonBack;
    private TextView commonBackTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_step_two);

        forgetPasswordStepTwoConfirmLayout = findViewById(R.id.forget_password_step_two_confirm_layout);
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBackTitle.setText("设置KK密码");
        forgetPasswordStepTwoConfirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*actionStart(ForgetPasswordStepTwoActivity.this,ForgetPasswordStepTwoActivity.class);
                finish();*/
                Toast.makeText(ForgetPasswordStepTwoActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
            }
        });
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(ForgetPasswordStepTwoActivity.this,LoginActivity.class);
                finish();
            }
        });
    }
}
