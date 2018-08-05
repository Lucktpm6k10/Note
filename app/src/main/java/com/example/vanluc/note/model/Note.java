package com.example.vanluc.note.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Note {
    private int id;
    private String tittle;
    private String conttent;
    private ArrayList<String> noteImage;
    private String nowTime;
    private String nowDate;
    private String clockTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getConttent() {
        return conttent;
    }

    public void setConttent(String conttent) {
        this.conttent = conttent;
    }

    public ArrayList<String> getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(ArrayList<String> noteImage) {
        this.noteImage = noteImage;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getClockTime() {
        return clockTime;
    }

    public void setClockTime(String clockTime) {
        this.clockTime = clockTime;
    }

    public String getClockDate() {
        return clockDate;
    }

    public void setClockDate(String clockDate) {
        this.clockDate = clockDate;
    }

    public Integer getItemBackground() {
        return itemBackground;
    }

    public void setItemBackground(Integer itemBackground) {
        this.itemBackground = itemBackground;
    }

    public Note(int id, String tittle, String conttent, ArrayList<String> noteImage, String nowTime, String nowDate, String clockTime, String clockDate, Integer itemBackground) {

        this.id = id;
        this.tittle = tittle;
        this.conttent = conttent;
        this.noteImage = noteImage;
        this.nowTime = nowTime;
        this.nowDate = nowDate;
        this.clockTime = clockTime;
        this.clockDate = clockDate;
        this.itemBackground = itemBackground;
    }

    private String clockDate;
    private Integer itemBackground;


}
