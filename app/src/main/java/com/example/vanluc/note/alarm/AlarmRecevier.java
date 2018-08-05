package com.example.vanluc.note.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("abc","Tôi ở trong Alarm");
        Intent intent1 = new Intent(context,Music.class);
        context.startService(intent1);
        Intent intent2 = new Intent(context,SchedulingService.class);
        context.startService(intent2);
    }
}
