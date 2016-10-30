package com.vinaymaneti.apps.simpletweets.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vinaymaneti.apps.simpletweets.TwitterApplication;

/**
 * Created by vinay on 30/10/16.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener mConnectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    // this is with the help of broadcast receivers
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (mConnectivityReceiverListener != null)
            mConnectivityReceiverListener.OnNetworkConnectionChanged(isConnected);
    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) TwitterApplication
                .getTwitterApplication()
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void OnNetworkConnectionChanged(boolean isConnected);
    }
}
