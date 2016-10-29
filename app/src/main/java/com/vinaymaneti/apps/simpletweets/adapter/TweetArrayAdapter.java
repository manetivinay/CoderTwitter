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

import java.util.Calendar;
import java.util.Date;
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
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.profileImage);
        holder.userNameTv.setText(tweet.getUser().getName());
        holder.bodyTv.setText(tweet.getBody());
        String timeAgo = ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt());
        holder.createdAtTv.setText(timeAgo);

    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date, Context ctx) {

        if (date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (dim == 0) {
            timeAgo = ctx.getResources().getString(R.string.date_util_term_less) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim == 1) {
            return "1 " + ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = dim + " " + ctx.getResources().getString(R.string.date_util_unit_minutes);
        } else if (dim >= 45 && dim <= 89) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + ctx.getResources().getString(R.string.date_util_term_an) + " " + ctx.getResources().getString(R.string.date_util_unit_hour);
        } else if (dim >= 90 && dim <= 1439) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 60)) + " " + ctx.getResources().getString(R.string.date_util_unit_hours);
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 " + ctx.getResources().getString(R.string.date_util_unit_day);
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = (Math.round(dim / 1440)) + " " + ctx.getResources().getString(R.string.date_util_unit_days);
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_month);
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = (Math.round(dim / 43200)) + " " + ctx.getResources().getString(R.string.date_util_unit_months);
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_over) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_almost) + " 2 " + ctx.getResources().getString(R.string.date_util_unit_years);
        } else {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 525600)) + " " + ctx.getResources().getString(R.string.date_util_unit_years);
        }

        return timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix);
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
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
//            profileImage = (AppCompatImageView) itemView.findViewById(R.id.profileImage);
//            userNameTv = (AppCompatTextView) itemView.findViewById(R.id.userNameTv);
//            userNameTwitter = (AppCompatTextView) itemView.findViewById(R.id.userNameTwitter);
//            bodyTv = (AppCompatTextView) itemView.findViewById(R.id.bodyTv);
//            view = itemView.findViewById(R.id.view);
//            replyIv = (AppCompatImageView) itemView.findViewById(R.id.replyIv);
//            reTweetIv = (AppCompatImageView) itemView.findViewById(R.id.reTweetIv);
//            retweetCount = (AppCompatTextView) itemView.findViewById(R.id.retweetCount);
//            likeIv = (AppCompatImageView) itemView.findViewById(R.id.likeIv);
//            likeCount = (AppCompatTextView) itemView.findViewById(R.id.likeCount);
//            mailIv = (AppCompatImageView) itemView.findViewById(R.id.mailIv);
        }
    }
}
