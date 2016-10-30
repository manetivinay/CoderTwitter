package com.vinaymaneti.apps.simpletweets.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.vinaymaneti.apps.simpletweets.R;
import com.vinaymaneti.apps.simpletweets.models.Tweet;
import com.vinaymaneti.apps.simpletweets.utils.ParseRelativeDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinay on 29/10/16.
 */

//taking the tweet object and turning them into views to display them in the list
public class TweetArrayAdapter extends RecyclerView.Adapter<TweetArrayAdapter.TweetViewHolder> {


    private final List<Tweet> mTweetList;
    private Context mContext;

    public TweetArrayAdapter(Context context, List<Tweet> tweetList) {
        this.mContext = context;
        this.mTweetList = tweetList;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //Inflate the custom layout
        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        // return the new holder instance
        TweetViewHolder tweetViewHolder = new TweetViewHolder(tweetView);
        return tweetViewHolder;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        //get the data model based on the position
        Tweet tweet = mTweetList.get(position);
//        holder.profileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.profileImage);
        holder.userNameTv.setText(tweet.getUser().getName());
        holder.bodyTv.setText(tweet.getBody());
        String timeAgo = ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt());
        holder.createdAtTv.setText(timeAgo);
        holder.retweetCount.setText(tweet.getRetweetCount());
        holder.likeCount.setText(tweet.getFavoriteCount());
        if (tweet.isFavorited())
            holder.likeIv.setImageResource(R.drawable.ic_like_enabled);
        if (tweet.isRetweeted())
            holder.reTweetIv.setImageResource(R.drawable.ic_retweet_enabled);
        holder.userNameTwitter.setText("@" + tweet.getUser().getScreenName());
    }


    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    //Easy access to context object in the recycler view
    public Context getContext() {
        return mContext;
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profileImage)
        RoundedImageView profileImage;

        @BindView(R.id.userNameTv)
        AppCompatTextView userNameTv;

        @BindView(R.id.userNameTwitter)
        AppCompatTextView userNameTwitter;

        @BindView(R.id.bodyTv)
        AppCompatTextView bodyTv;

        @BindView(R.id.view)
        View view;

        @BindView(R.id.replyIv)
        AppCompatImageView replyIv;

        @BindView(R.id.reTweetIv)
        AppCompatImageView reTweetIv;

        @BindView(R.id.retweetCount)
        AppCompatTextView retweetCount;

        @BindView(R.id.likeIv)
        AppCompatImageView likeIv;

        @BindView(R.id.likeCount)
        AppCompatTextView likeCount;

        @BindView(R.id.mailIv)
        AppCompatImageView mailIv;

        @BindView(R.id.createdAtTv)
        AppCompatTextView createdAtTv;


        public TweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
