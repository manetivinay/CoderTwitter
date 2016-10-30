package com.vinaymaneti.apps.simpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.vinaymaneti.apps.simpletweets.utils.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int REQUEST_CODE = 1;
    private TweetArrayAdapter mTweetArrayAdapter;
    private List<Tweet> mTweetList;
    private TwitterClient mClient;
    private int count = 25;
    private EndlessRecyclerViewScrollListener scrollListener;

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

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.progressBarLoadMore)
    ProgressBar progressBarLoadMore;

    private interface Listener {
        void onResult(JSONArray jsonArray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        mClient = TwitterApplication.getRestClient();
        //create the List (data source)
        mTweetList = new ArrayList<>();
        if (checkConnection()) {
            populateTimeLine(new Listener() {
                @Override
                public void onResult(JSONArray jsonArray) {
                    responseJsonArrayToList(jsonArray);
                }
            });
            swipeToFresh();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
        }
        setUpUiView();
    }

    private void swipeToFresh() {
        //setup refresh listener which  triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //here we need to refresh list
                // we need to make sure to call swipeContainer.setRefresh(false)
                //once the network request has completed successfully
                populateTimeLine(new Listener() {
                    @Override
                    public void onResult(JSONArray jsonArray) {
                        if (jsonArray != null) {
                            mTweetList.clear();
                            if (jsonArray.toString() != null) {
                                List<Tweet> tweetSize = Tweet.fromJsonArray(jsonArray);
                                if (tweetSize.size() > 0) {
                                    mTweetList.addAll(tweetSize);
                                } else {
                                    emptyView.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            }
                            mTweetArrayAdapter = new TweetArrayAdapter(TimelineActivity.this, mTweetList);
                            recyclerView.setAdapter(mTweetArrayAdapter);
                            // now here we need to call setRefreshing(false) to signal refresh to finished
                            swipeContainer.setRefreshing(false);
                        }
                    }
                });
            }
        });

        configureSwipeToFreshUI();
    }

    private void configureSwipeToFreshUI() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void setUpUiView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        getSupportActionBar().setTitle("Coder Twitter");
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        // construct  the adapter from data source
        mTweetArrayAdapter = new TweetArrayAdapter(this, mTweetList);
        //connect adapter to recycler view
        recyclerView.setAdapter(mTweetArrayAdapter);
//        mTweetArrayAdapter.setLoadMoreTweetsListener(new TweetArrayAdapter.LoadMoreTweets() {
//            @Override
//            public void onLoadMore(boolean hasMore) {
//                if (hasMore) {
//                    searchMore();
//                }
//            }
//        });

        //add divider to each item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        //==============================
        //endless Scroll listener
        scrollListener = new EndlessRecyclerViewScrollListener(new LinearLayoutManager(this)) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mClient.getHomeTimeline(nextCount(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("Infinite loading", response.toString());
                        // You have create tweetSize each time onSuccess callback
                        mTweetList.addAll(Tweet.fromJsonArray(response));
//                        mTweetArrayAdapter.addMoreTweets(mTweetList);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("Infinite loading", errorResponse.toString());
                    }
                });
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        //set the layout type for the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(this);
    }

    private void searchMore() {
        progressBarLoadMore.setVisibility(View.VISIBLE);
        mClient.getHomeTimeline(nextCount(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Infinite loading", response.toString());
                // You have create tweetSize each time onSuccess callback
                mTweetList.addAll(Tweet.fromJsonArray(response));
                //mTweetArrayAdapter.addMoreTweets(mTweetList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("Infinite loading", errorResponse.toString());
            }
        });
    }

    private void populateTimeLine(final Listener listener) {
        mRelativeLayout.setVisibility(View.VISIBLE);
        mClient.getHomeTimeline(count, new JsonHttpResponseHandler() {
            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Success response:-", response.toString());
                mRelativeLayout.setVisibility(View.GONE);
                if (response != null & listener != null)
                    listener.onResult(response);
                // once after we get data, what we need to follow
                //1.JSON HERE
                //2.DE-SERIALISE JSON
                //3.CREATE MODEL AND THEM TO ADAPTER
                //4. LOAD THE MODEL INTO LIST VIEW (Now here we do this)
                // Adapter job is to take the data that we are eventually create  and populate it into the listview
                responseJsonArrayToList(response);
                mTweetArrayAdapter.notifyDataSetChanged();
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Error response:-", errorResponse.toString());
            }
        });
    }

    private void responseJsonArrayToList(JSONArray response) {
        if (response.toString() != null) {
            List<Tweet> tweetSize = Tweet.fromJsonArray(response);
            if (tweetSize.size() > 0) {
                mTweetList.addAll(tweetSize);
            } else {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
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
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                boolean postedUpdated = data.getBooleanExtra("status", false);
                Log.d("Bundle value", postedUpdated + "");
                mTweetList.clear();
                mRelativeLayout.setVisibility(View.VISIBLE);
                populateTimeLine(new Listener() {
                    @Override
                    public void onResult(JSONArray jsonArray) {
                        responseJsonArrayToList(jsonArray);
                    }
                });
                recyclerView.scrollToPosition(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public int nextCount() {
        return count += 25;
    }
}
