package com.lge.volumelocker;

import android.os.Handler;


public class ServiceCommandProcessor {
    private static final String TAG = ServiceCommandProcessor.class.getSimpleName();

    private VolumeLockService volumeLockService;
    private Handler handler;
    private VolumeLock volumeLock;

    public ServiceCommandProcessor(VolumeLockService volumeLockService) {
        this.volumeLockService = volumeLockService;
        handler = new Handler();
        volumeLock = volumeLockService.volumeLock;
    }

    public void processCommand(String command) {
        LOG.d(TAG,"processCommand(), command = " + command);
        if (command.equalsIgnoreCase(Constants.ACTION_START_SERVICE)) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    LOG.d(TAG,"start foregroud service ~");
                    volumeLockService.startForeground(
                            Constants.notificationId,
                            new VolumeLockNotification(volumeLockService.getApplicationContext())
                            .build());
                    volumeLock.lock();
                }
            };
            handler.post(runnable);
        } else if (command.equalsIgnoreCase(Constants.ACTION_STOP_SERVICE)) {
            LOG.d(TAG,"stop foregroud service ~");
            volumeLock.unlock();
            volumeLockService.stopForeground(true);
            volumeLockService.stopSelf();
        } else {
            LOG.d(TAG,"unknown action received ~");
            throw new RuntimeException("Unknown action: " + command);
        }
    }
}
