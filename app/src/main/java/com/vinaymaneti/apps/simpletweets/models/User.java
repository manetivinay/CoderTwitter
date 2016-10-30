package com.vinaymaneti.apps.simpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vinay on 29/10/16.
 */

//"user": {
//        "id": 972651,
//        "id_str": "972651",
//        "name": "Mashable",
//        "screen_name": "mashable",
//        "location": "",
//        "description": "News, resources, inspiration and fun for the connected generation. Tweets by @mashable staff.",
//        "url": "http:\/\/t.co\/1Gm8aVACKn",
//        "entities": {
//        "url": {
//        "urls": [{
//        "url": "http:\/\/t.co\/1Gm8aVACKn",
//        "expanded_url": "http:\/\/mashable.com",
//        "display_url": "mashable.com",
//        "indices": [0, 22]
//        }]
//        },
//        "description": {
//        "urls": []
//        }
//        },
//        "protected": false,
//        "followers_count": 7840547,
//        "friends_count": 2834,
//        "listed_count": 129742,
//        "created_at": "Mon Mar 12 01:28:01 +0000 2007",
//        "favourites_count": 615,
//        "utc_offset": -14400,
//        "time_zone": "Eastern Time (US & Canada)",
//        "geo_enabled": false,
//        "verified": true,
//        "statuses_count": 212821,
//        "lang": "en",
//        "contributors_enabled": false,
//        "is_translator": false,
//        "is_translation_enabled": false,
//        "profile_background_color": "00AEEF",
//        "profile_background_image_url": "http:\/\/pbs.twimg.com\/profile_background_images\/705312036\/bf7ca2a3f077d7e57b12a5ea4f1db268.png",
//        "profile_background_image_url_https": "https:\/\/pbs.twimg.com\/profile_background_images\/705312036\/bf7ca2a3f077d7e57b12a5ea4f1db268.png",
//        "profile_background_tile": false,
//        "profile_image_url": "http:\/\/pbs.twimg.com\/profile_images\/760998040949841922\/XT79xzRT_normal.jpg",
//        "profile_image_url_https": "https:\/\/pbs.twimg.com\/profile_images\/760998040949841922\/XT79xzRT_normal.jpg",
//        "profile_banner_url": "https:\/\/pbs.twimg.com\/profile_banners\/972651\/1401484849",
//        "profile_link_color": "00AEEF",
//        "profile_sidebar_border_color": "FFFFFF",
//        "profile_sidebar_fill_color": "88DBF4",
//        "profile_text_color": "3B3B3B",
//        "profile_use_background_image": false,
//        "has_extended_profile": false,
//        "default_profile": false,
//        "default_profile_image": false,
//        "following": true,
//        "follow_request_sent": false,
//        "notifications": false,
//        "translator_type": "none"
//        }

public class User {
    //list the attribues
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;


    //deserialize the user josn ==> User
    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        // here we will extract and fill the values
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return User Object
        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
