package com.lge.volumelocker;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

public class VolumeLockService extends Service implements ShakeDetector.Listener {
    private static final String TAG = VolumeLockService.class.getSimpleName();

    private ServiceCommandProcessor serviceCommandProcessor;

    private VolumeLockActionReceiver volumeLockActionReceiver;

    private VolumeControl volumeControl;
    public VolumeLock volumeLock;
    private VolumeLockNotification volumeLockNotification;

    private ShakeDetector shakeDetector;

    @Override
    public void onCreate() {
        LOG.d(TAG, "onCreate()");

        volumeControl = new VolumeControl(getApplicationContext());

        volumeLock = new VolumeLock(getApplicationContext());

        volumeLockNotification = new VolumeLockNotification(getApplicationContext());
        volumeLockNotification.createChannel();

        shakeDetector = new ShakeDetector(this);
        shakeDetector.start((SensorManager)getSystemService(SENSOR_SERVICE));

        volumeLockActionReceiver = new VolumeLockActionReceiver(getApplicationContext());
        volumeLockActionReceiver.setVolumeLockActionListener(volumeLockActionListener);
        volumeLockActionReceiver.start();

        serviceCommandProcessor = new ServiceCommandProcessor(this);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LOG.d(TAG,"onDestroy()");
        shakeDetector.stop();
        volumeLockActionReceiver.stop();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null)
                serviceCommandProcessor.processCommand(action);
        }

        return super.onStartCommand(intent, flags, startId);
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

    private IVolumeLockActionListener volumeLockActionListener = new IVolumeLockActionListener() {
        @Override
        public void volumeUp() {
            volumeLock.unlock();
            volumeControl.volumeUp();
            volumeLock.lock();
        }

        @Override
        public void volumeDown() {
            volumeLock.unlock();
            volumeControl.volumeDown();
            volumeLock.lock();
        }
    };

}