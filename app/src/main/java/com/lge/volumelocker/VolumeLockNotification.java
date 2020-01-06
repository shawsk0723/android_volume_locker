package com.lge.volumelocker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;

public class VolumeLockNotification {
    private static final int INTENT_REQUEST_CODE = 0;
    private static String channelId = VolumeLockNotification.class.getSimpleName();
    private static String channelName = VolumeLockNotification.class.getCanonicalName();

    private Context context;

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
        PendingIntent pendingIntentVolumeUp = PendingIntent.getBroadcast(
                context,
                INTENT_REQUEST_CODE,
                new Intent(Constants.ACTION_VOLUME_UP),
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentVolumeDown = PendingIntent.getBroadcast(
                context,
                INTENT_REQUEST_CODE,
                new Intent((Constants.ACTION_VOLUME_DOWN)),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Action actionVolumeUp = new Notification.Action.Builder(
                Icon.createWithResource(context, R.drawable.ic_volume_up), "up", pendingIntentVolumeUp).build();
        Notification.Action actionVolumeDown = new Notification.Action.Builder(
                Icon.createWithResource(context, R.drawable.ic_volume_down), "down", pendingIntentVolumeDown).build();

        return new Notification.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("media key block service")
                .addAction(actionVolumeUp)
                .addAction(actionVolumeDown)
                .setAutoCancel(true)
                .build();
    }
}