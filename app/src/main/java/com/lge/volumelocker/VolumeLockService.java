package com.lge.volumelocker;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

public class VolumeLockService extends Service implements ShakeDetector.Listener {
    private static final String TAG = VolumeLockService.class.getSimpleName();

    private static final int notificationId = 7718;

    private Handler handler;
    private Runnable runnable;

    private VolumeLock volumeLock;
    private VolumeLockNotification volumeLockNotification;

    private ShakeDetector shakeDetector;

    @Override
    public void onCreate() {
        LOG.d(TAG, "onCreate()");

        volumeLock = new VolumeLock(getApplicationContext());

        volumeLockNotification = new VolumeLockNotification(getApplicationContext());
        volumeLockNotification.createChannel();

        shakeDetector = new ShakeDetector(this);
        shakeDetector.start((SensorManager)getSystemService(SENSOR_SERVICE));

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LOG.d(TAG,"onDestroy()");
        shakeDetector.stop();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null)
                processAction(action);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void processAction(final String action) {
        LOG.d(TAG,"processAction(), action = " + action);
        if (action.equalsIgnoreCase(Constants.ACTION_START_SERVICE)) {
            //if(!isForegroundServiceRunning()) {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    LOG.d(TAG,"start foregroud service ~");
                    startForeground(notificationId, new VolumeLockNotification(getApplicationContext()).build());
                    volumeLock.lock();
                }
            };
            handler.post(runnable);
            //}
        } else if (action.equalsIgnoreCase(Constants.ACTION_STOP_SERVICE)) {
            //if(isForegroundServiceRunning()) {
            LOG.d(TAG,"stop foregroud service ~");
            volumeLock.unlock();
            stopForeground(true);
            stopSelf();
            //}
        } else {
            LOG.d(TAG,"unknown action received ~");
            throw new RuntimeException("Unknown action: " + action);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void hearShake() {
        Toast.makeText(this, "restart volume lock ~", Toast.LENGTH_SHORT).show();
        volumeLock.unlock();
        volumeLock.lock();
    }
}
