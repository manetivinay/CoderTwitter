package com.vinaymaneti.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.vinaymaneti.apps.simpletweets.R;
import com.vinaymaneti.apps.simpletweets.TwitterApplication;
import com.vinaymaneti.apps.simpletweets.TwitterClient;
import com.vinaymaneti.apps.simpletweets.utils.ConnectivityReceiver;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int MAX_CHARACTER = 140;
    private TwitterClient mClient;

    @BindView(R.id.cancel_btn)
    AppCompatImageView cancelBtn;

    @BindView(R.id.bodyEt)
    AppCompatEditText bodyEt;

    @BindView(R.id.btn_tweet)
    AppCompatButton btn_tweet;

    @BindView(R.id.textCharacterCount)
    AppCompatTextView textCharacterCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);
        mClient = TwitterApplication.getRestClient();
        bodyEt.addTextChangedListener(mTextEditorWatcher);
        bodyEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
        cancelBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ConnectivityReceiver.isConnected())
            btn_tweet.setEnabled(false);
        else
            btn_tweet.setEnabled(true);

        btn_tweet.setOnClickListener(this);

    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            textCharacterCount.setText(String.valueOf(MAX_CHARACTER - s.length()));
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cancel_btn:
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                finish();
                break;
            case R.id.btn_tweet:

                mClient.updateTweet(bodyEt.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("Successful post:-", response.toString());
                        Intent intent = getIntent();
                        intent.putExtra("status", true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("error post:-", errorResponse.toString());
                    }
                });
                break;

        }
    }
}
