package com.vinaymaneti.apps.simpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinay on 29/10/16.
 */

public class Tweet {

    //    [{
//        "created_at": "Sat Oct 29 01:52:06 +0000 2016",
//                "id": 792182189840629761,
//                "id_str": "792182189840629761",
//                "text": "Hold up, this tiny Beyoncé superfan has the fiercest Halloween costume https:\/\/t.co\/99xm6mOB3H",
//                "truncated": false,
//                "entities": {
//            "hashtags": [],
//            "symbols": [],
//            "user_mentions": [],
//            "urls": [{
//                "url": "https:\/\/t.co\/99xm6mOB3H",
//                        "expanded_url": "http:\/\/on.mash.to\/2fp3Bba",
//                        "display_url": "on.mash.to\/2fp3Bba",
//                        "indices": [71, 94]
//            }]
//        },
//        "source": "<a href=\"http:\/\/www.socialflow.com\" rel=\"nofollow\">SocialFlow<\/a>",
//                "in_reply_to_status_id": null,
//                "in_reply_to_status_id_str": null,
//                "in_reply_to_user_id": null,
//                "in_reply_to_user_id_str": null,
//                "in_reply_to_screen_name": null,
//                "user": {
//            "id": 972651,
//                    "id_str": "972651",
//                    "name": "Mashable",
//                    "screen_name": "mashable",
//                    "location": "",
//                    "description": "News, resources, inspiration and fun for the connected generation. Tweets by @mashable staff.",
//                    "url": "http:\/\/t.co\/1Gm8aVACKn",
//                    "entities": {
//                "url": {
//                    "urls": [{
//                        "url": "http:\/\/t.co\/1Gm8aVACKn",
//                                "expanded_url": "http:\/\/mashable.com",
//                                "display_url": "mashable.com",
//                                "indices": [0, 22]
//                    }]
//                },
//                "description": {
//                    "urls": []
//                }
//            },
//            "protected": false,
//                    "followers_count": 7840547,
//                    "friends_count": 2834,
//                    "listed_count": 129742,
//                    "created_at": "Mon Mar 12 01:28:01 +0000 2007",
//                    "favourites_count": 615,
//                    "utc_offset": -14400,
//                    "time_zone": "Eastern Time (US & Canada)",
//                    "geo_enabled": false,
//                    "verified": true,
//                    "statuses_count": 212821,
//                    "lang": "en",
//                    "contributors_enabled": false,
//                    "is_translator": false,
//                    "is_translation_enabled": false,
//                    "profile_background_color": "00AEEF",
//                    "profile_background_image_url": "http:\/\/pbs.twimg.com\/profile_background_images\/705312036\/bf7ca2a3f077d7e57b12a5ea4f1db268.png",
//                    "profile_background_image_url_https": "https:\/\/pbs.twimg.com\/profile_background_images\/705312036\/bf7ca2a3f077d7e57b12a5ea4f1db268.png",
//                    "profile_background_tile": false,
//                    "profile_image_url": "http:\/\/pbs.twimg.com\/profile_images\/760998040949841922\/XT79xzRT_normal.jpg",
//                    "profile_image_url_https": "https:\/\/pbs.twimg.com\/profile_images\/760998040949841922\/XT79xzRT_normal.jpg",
//                    "profile_banner_url": "https:\/\/pbs.twimg.com\/profile_banners\/972651\/1401484849",
//                    "profile_link_color": "00AEEF",
//                    "profile_sidebar_border_color": "FFFFFF",
//                    "profile_sidebar_fill_color": "88DBF4",
//                    "profile_text_color": "3B3B3B",
//                    "profile_use_background_image": false,
//                    "has_extended_profile": false,
//                    "default_profile": false,
//                    "default_profile_image": false,
//                    "following": true,
//                    "follow_request_sent": false,
//                    "notifications": false,
//                    "translator_type": "none"
//        },
//        "geo": null,
//                "coordinates": null,
//                "place": null,
//                "contributors": null,
//                "is_quote_status": false,
//                "retweet_count": 3,
//                "favorite_count": 7,
//                "favorited": false,
//                "retweeted": false,
//                "possibly_sensitive": false,
//                "possibly_sensitive_appealable": false,
//                "lang": "en"
//    },
//    {
//     ....
//    },
//    {
//        .....
//    }]


    //list out the attributes
    private long uid;// unique id from database
    private User user;
    private String createdAt;
    private String body;
    private String retweetCount;
    private String favoriteCount;
    private boolean favorited; //if we favourite a tweet, we need to enable
    private boolean retweeted; // if we retweeted a tweet we need to enable show different image

    //Deserialize the json
    // Tweet.fromJson("{...}")  ==> <Tweet>
    private static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.uid = jsonObject.getLong("id");
            tweet.body = jsonObject.getString("text");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getString("retweet_count");
            tweet.favoriteCount = jsonObject.getString("favorite_count");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    //Tweet.fromJsonArray( [{..}, {...}, {...}])  ===> o/p List<Tweet>
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) {
        List<Tweet> tweets = new ArrayList<>();
        //Iterate the json array and create tweets
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJsonObject = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJson(tweetJsonObject);
                if (tweet != null)
                    tweets.add(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
                continue; // here why we use the continue is -- if single tweet is null still it continue with other tweets to iterate over
            }
        }
        //return the finished lists
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }
}
