package com.example.vanluc.note.alarm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import com.example.vanluc.note.activity.AddNoteActivity;


public class AlarmRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("abc", "Tôi ở trong Alarm");
        String id = intent.getStringExtra(AddNoteActivity.keyString);
        Intent myIntent1 = new Intent(context,Music.class);
        context.startService(myIntent1);
        Intent myIntetn2 = new Intent(context,SchedulingService.class);
        myIntetn2.putExtra(AddNoteActivity.keyString,id);
        context.startService(myIntetn2);




    }
}
