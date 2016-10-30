package com.vinaymaneti.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.vinaymaneti.apps.simpletweets.R;
import com.vinaymaneti.apps.simpletweets.utils.AlertNetwork;
import com.vinaymaneti.apps.simpletweets.utils.ConnectivityReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.activity_splash_activity)
    FrameLayout mActivity_splash_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ConnectivityReceiver.isConnected())
                    startActivity(new Intent(SplashActivity.this, TimelineActivity.class));
                else
                    AlertNetwork.networkAlertDialog(SplashActivity.this);
            }
        }, 1000);
    }
}

