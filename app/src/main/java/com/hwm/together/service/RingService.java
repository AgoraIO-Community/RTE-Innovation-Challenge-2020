package com.hwm.together.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hwm.together.R;

public class RingService extends Service {
    private static final String TAG = "RingService";
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaPlayer = MediaPlayer.create(this, R.raw.default_ring);
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);
        Log.d(TAG, "onStartCommand: " + android.os.Process.myPid());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
