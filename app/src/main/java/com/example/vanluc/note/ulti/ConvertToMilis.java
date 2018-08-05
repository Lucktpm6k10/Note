package com.example.vanluc.note.ulti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertToMilis {
    public static long covertMilis(String date, String time)
    {
        String convertDate = date+" "+time;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        Date d = null;
        try {
            d = format.parse(convertDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long results = d.getTime();
        return results;
    }
}
