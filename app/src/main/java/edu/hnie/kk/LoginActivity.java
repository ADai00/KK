package edu.hnie.kk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends BaseActivity {
    private TextView forgetPassword;
    private TextView registerUser;
    private RelativeLayout loginLayout;
    private MaterialEditText loginNumber;
    private MaterialEditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgetPassword = findViewById(R.id.forget_password);
        registerUser = findViewById(R.id.register_user);
        loginLayout = findViewById(R.id.login_layout);
        loginNumber = findViewById(R.id.login_number);
        loginPassword = findViewById(R.id.login_password);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(LoginActivity.this, RegisterActivity.class);
                finish();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(LoginActivity.this, ForgetPasswordStepOneActivity.class);
                finish();
            }
        });

        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = loginNumber.getText().toString();
                String password = loginPassword.getText().toString();
                login(account, password);
            }
        });
    }

    /**
     * 登入
     */
    public void login(String account, String password) {
        LoginInfo info = new LoginInfo(account, password);
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                        editor.putString("account", loginInfo.getAccount());
                        editor.putString("password", loginInfo.getToken());
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailed(int i) {
                        Toast.makeText(LoginActivity.this, "登入失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }
}
