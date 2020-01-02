package com.lge.volumelocker;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceStateChecker {

    private Context context;

    public ServiceStateChecker(Context context) {
        this.context = context;
    }

    public Boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        }

        return false;
    }

}
