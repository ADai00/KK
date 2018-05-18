package edu.hnie.kk;

import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        String account = getAccount();
        if (account != null) {
            actionStart(StartActivity.this, MainActivity.class);
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    actionStart(StartActivity.this, LoginActivity.class);
                    finish();
                }
            }, 2000);
        }


    }


}

