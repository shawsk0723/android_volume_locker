package com.lge.volumelocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class VolumeLockActionReceiver extends BroadcastReceiver {
    private static final String TAG = VolumeLockActionReceiver.class.getSimpleName();

    private Context context;
    private IVolumeLockActionListener volumeLockActionListener;

    public VolumeLockActionReceiver(Context context) {
        this.context = context;
    }

    public void setVolumeLockActionListener(IVolumeLockActionListener volumeLockActionListener) {
        this.volumeLockActionListener = volumeLockActionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Constants.ACTION_VOLUME_UP)){
            LOG.d(TAG, "recevied action : "+ Constants.ACTION_VOLUME_UP);
            volumeLockActionListener.volumeUp();
        } else if(intent.getAction().equals(Constants.ACTION_VOLUME_DOWN)) {
            LOG.d(TAG, "recevied action : " + Constants.ACTION_VOLUME_DOWN);
            volumeLockActionListener.volumeDown();
         }
    }

    public void start(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_VOLUME_UP);
        intentFilter.addAction(Constants.ACTION_VOLUME_DOWN);

        context.registerReceiver(this, intentFilter);
    }

    public void stop() {
        context.unregisterReceiver(this);
    }
}
