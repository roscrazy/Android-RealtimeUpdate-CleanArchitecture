package com.mike.utility;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public class OSUtil {
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
