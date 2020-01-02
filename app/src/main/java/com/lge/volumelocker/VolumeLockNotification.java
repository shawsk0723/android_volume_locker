package com.lge.volumelocker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class VolumeLockNotification {
    private Context context;

    private static String channelId = VolumeLockNotification.class.getSimpleName();
    private static String channelName = VolumeLockNotification.class.getCanonicalName();

    public VolumeLockNotification(Context context) {
        this.context = context;

    }

    public void createChannel() {
        NotificationManager notifManager
                = (NotificationManager) context.getSystemService  (Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notifManager.createNotificationChannel(mChannel);
        }
    }

    public Notification build() {
        return new Notification.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("media key block service")
                .setContentText("running ~")
                .build();
    }
}
