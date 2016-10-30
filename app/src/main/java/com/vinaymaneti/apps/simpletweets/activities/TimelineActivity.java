package com.vinaymaneti.apps.simpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.vinaymaneti.apps.simpletweets.R;
import com.vinaymaneti.apps.simpletweets.TwitterApplication;
import com.vinaymaneti.apps.simpletweets.TwitterClient;
import com.vinaymaneti.apps.simpletweets.adapter.TweetArrayAdapter;
import com.vinaymaneti.apps.simpletweets.models.Tweet;
import com.vinaymaneti.apps.simpletweets.utils.ConnectivityReceiver;
import com.vinaymaneti.apps.simpletweets.utils.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private TweetArrayAdapter mTweetArrayAdapter;
    private List<Tweet> mTweetList;
    private TwitterClient mClient;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.emptyView)
    AppCompatTextView emptyView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        mClient = TwitterApplication.getRestClient();
        //create the List (data source)
        mTweetList = new ArrayList<>();
        if (checkConnection())
            populateTimeLine();
        else {
            Toast.makeText(this, "No iNternet", Toast.LENGTH_LONG).show();
        }
        setUpUiView();
    }


    private void setUpUiView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Coder Twitter");
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        // construct  the adapter from data source
        mTweetArrayAdapter = new TweetArrayAdapter(this, mTweetList);
        //connect adapter to recycler view
        recyclerView.setAdapter(mTweetArrayAdapter);
        //add divider to each item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        //set the layout type for the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(this);
    }

    private void populateTimeLine() {

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


    @Override
    protected void onResume() {
        super.onResume();
        //register connection status listener
        TwitterApplication.getTwitterApplication().setConnectivityListener(this);

    }

    @Override
    public void onClick(View v) {
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    //Method to manually check connection status
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnackBarInternet(isConnected);
        return isConnected;
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void OnNetworkConnectionChanged(boolean isConnected) {
        showSnackBarInternet(isConnected);
    }

    private void showSnackBarInternet(boolean isConnected) {
        String message = null;
        int color = 0;
        if (!isConnected) {
            message = "Sorry, not connected to internet!!";
            color = Color.RED;
        } else {
            message = "Welcome back to Coder Twitter App!!";
            color = Color.WHITE;
        }
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        AppCompatTextView textView = (AppCompatTextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}
