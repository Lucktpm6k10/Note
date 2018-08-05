package com.example.vanluc.note.alarm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.vanluc.note.R;
import com.example.vanluc.note.activity.MainActivity;
import com.example.vanluc.note.ulti.Constant;

public class SchedulingService extends IntentService {

    public SchedulingService() {
        super(SchedulingService.class.getSimpleName());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("abcxyz","Đang ở trong notifcation");
    }


}
