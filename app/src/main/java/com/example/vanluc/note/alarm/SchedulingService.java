package com.example.vanluc.note.alarm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.vanluc.note.R;
import com.example.vanluc.note.activity.AddNoteActivity;
import com.example.vanluc.note.activity.EditNoteActivity;
import com.example.vanluc.note.activity.MainActivity;

public class SchedulingService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String id = intent.getStringExtra(AddNoteActivity.keyString);
        String tittle = null;
        String cottent = null;
        Cursor cursor = MainActivity.databaseNote.getData("SELECT * FROM NOTE WHERE ID = "+Integer.parseInt(id));
        while (cursor.moveToNext())
        {
            tittle = cursor.getString(1);
            cottent = cursor.getString(2);
        }


        PendingIntent pi = PendingIntent.getActivity(this,
                0, new Intent(this, EditNoteActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.noteicon)
                .setContentTitle(tittle)
                .setContentText(cottent)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.parseInt(id), notification);

        return super.onStartCommand(intent, flags, startId);
    }
}
