package edu.hnie.kk;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBackTitle.setText("注册");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               actionStart(RegisterActivity.this,LoginActivity.class);
               finish();
            }
        });
    }
}
