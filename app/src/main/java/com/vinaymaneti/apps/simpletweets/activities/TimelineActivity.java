package com.vinaymaneti.apps.simpletweets.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.vinaymaneti.apps.simpletweets.R;
import com.vinaymaneti.apps.simpletweets.TwitterApplication;
import com.vinaymaneti.apps.simpletweets.TwitterClient;
import com.vinaymaneti.apps.simpletweets.adapter.TweetArrayAdapter;
import com.vinaymaneti.apps.simpletweets.models.Tweet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private TweetArrayAdapter mTweetArrayAdapter;
    private List<Tweet> mTweetList;
    private TwitterClient mClient;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.emptyView)
    AppCompatTextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        mClient = TwitterApplication.getRestClient();
        populateTimeLine();
        setUpUiView();
    }


    private void setUpUiView() {
        // construct  the adapter from data source
        mTweetArrayAdapter = new TweetArrayAdapter(this, mTweetList);
        //connect adapter to recycler view
        recyclerView.setAdapter(mTweetArrayAdapter);
        //set the layout type for the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void populateTimeLine() {
        //create the List (data source)
        mTweetList = new ArrayList<>();
        mRelativeLayout.setVisibility(View.VISIBLE);
        mClient.getHomeTimeline(new JsonHttpResponseHandler() {
            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Success response:-", response.toString());
                mRelativeLayout.setVisibility(View.GONE);
                // once after we get data, what we need to follow
                //1.JSON HERE
                //2.DE-SERIALISE JSON
                //3.CREATE MODEL AND THEM TO ADAPTER
                //4. LOAD THE MODEL INTO LIST VIEW (Now here we do this)
                // Adapter job is to take the data that we are eventually create  and populate it into the listview
                if (response.toString() != null) {
                    List<Tweet> tweetSize = Tweet.fromJsonArray(response);
                    if (tweetSize.size() > 0) {
                        mTweetList.addAll(Tweet.fromJsonArray(response));
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }
                mTweetArrayAdapter.notifyDataSetChanged();
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Error response:-", errorResponse.toString());
            }
        });
    }
}
