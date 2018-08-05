package com.example.vanluc.note.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vanluc.note.R;

public class Music extends Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("abcd","Tôi trong music");
        mediaPlayer = MediaPlayer.create(this, R.raw.abc);
        mediaPlayer.start();
        return START_NOT_STICKY;

    }
}
