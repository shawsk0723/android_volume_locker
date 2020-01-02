package com.lge.volumelocker;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.media.VolumeProviderCompat;

public class VolumeLock {
    private static final String TAG = VolumeLock.class.getSimpleName();

    private Context context;
    private MediaSessionCompat mediaSession;

    public VolumeLock(Context context) {
        this.context = context;
    }

    public void lock() {
        mediaSession = new MediaSessionCompat(context, "PlayerService");
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0) //you simulate a player which plays something.
                .build());

        //this will only work on Lollipop and up, see https://code.google.com/p/android/issues/detail?id=224134
        VolumeProviderCompat myVolumeProvider =
                new VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE, /*max volume*/100, /*initial volume level*/50) {
                    @Override
                    public void onAdjustVolume(int direction) {
                        Log.d(TAG, "direction = " + direction);
                        if(direction != 0)
                            Toast.makeText(context, "media key event = " + direction, Toast.LENGTH_SHORT).show();
                      /*
                    -1 -- volume down
                    1 -- volume up
                    0 -- volume button released
                     */
                    }
                };

        mediaSession.setPlaybackToRemote(myVolumeProvider);
        mediaSession.setActive(true);
    }

    public void unlock() {
        mediaSession.setActive(false);
        mediaSession.release();
    }
}
