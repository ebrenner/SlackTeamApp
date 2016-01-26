package com.example.ericbrenner.slackteamapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ericbrenner on 1/25/16.
 */
public class Utilities {

    public static String formatColorString(String string) {
        return "#" + string;
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null)&&(activeNetworkInfo.isConnected())){
            return true;
        }else{
            return false;
        }
    }
}
