package com.lge.volumelocker;

import android.util.Log;

public class LOG {
    public static void d(String TAG, String message) {
        Log.d(Constants.TAG + " - " + TAG, message);
    }
}
