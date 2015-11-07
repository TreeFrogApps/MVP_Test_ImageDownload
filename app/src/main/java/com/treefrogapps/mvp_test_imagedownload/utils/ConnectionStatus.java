package com.treefrogapps.mvp_test_imagedownload.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Class to check connection status
 */
public class ConnectionStatus {

    public static boolean isConnected(Context context) {

        // system service connectivity manager
        ConnectivityManager checkNetworkStatus = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // network info will get all the status
        NetworkInfo networkInfo = checkNetworkStatus.getActiveNetworkInfo();

        // check that the state is 'connected' (either wifi or phone network - only 1 connection type
        // can exist at the same time
        return networkInfo != null && networkInfo.isConnected();

    }
}
