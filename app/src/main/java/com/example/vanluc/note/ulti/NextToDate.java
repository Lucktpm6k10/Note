package com.example.vanluc.note.ulti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NextToDate {
    public static String changeDate(String nowDate,int i)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, i);  // number of days to add
            return sdf.format(c.getTime());
    }
}
