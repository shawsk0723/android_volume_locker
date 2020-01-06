package com.lge.volumelocker;

import android.content.Context;
import android.media.AudioManager;

public class VolumeControl {
    private Context context;

    public VolumeControl(Context context) {
        this.context = context;
    }

    public void volumeUp() {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
    }

    public void volumeDown() {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
    }
}
