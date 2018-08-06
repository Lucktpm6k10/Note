package com.example.vanluc.note.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vanluc.note.R;
import com.example.vanluc.note.adapter.NoteAdapter;
import com.example.vanluc.note.database.DatabaseManager;
import com.example.vanluc.note.model.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static DatabaseManager databaseNote;
    RecyclerView rvNote;
    NoteAdapter noteAdapter;
    public static ArrayList<Note> noteList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editActionBar();

        initView();

        initRecyclerView();

        getData();
    }

    private void initView() {
        createDatabase();
        createTableOnDatabase();
        rvNote = findViewById(R.id.rv_Note);
    }

    //Tạo icon và tittle của actionbar
    private void editActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Note");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_note_icon);    //Icon muốn hiện thị
        actionBar.setDisplayUseLogoEnabled(true);
    }

    //Add icon thêm vào actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Bắt sự kiện cho icon Add
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_Icon_Menu:
                startActivityAddItem();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Funcition start Activity Add Item
    private void startActivityAddItem() {
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        startActivity(intent);
    }

    //Tạo cơ sở dữ liệu
    private void createDatabase() {
        databaseNote = new DatabaseManager(this, "Note.sqlite", null, 1);

    }

    //Tạo bảng trong cơ sở dữ liệu
    private void createTableOnDatabase() {
        databaseNote.queryData("CREATE TABLE IF NOT EXISTS NOTE" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITTLE NVARCHAR(200) NOT NULL," +
                "CONTENT NAVARCHAR(1000) NOT NULL," +
                "NOWTIME TIME NOT NULL," +
                "NOWDATE DATE NOT NULL," +
                "CLOCKTIME TIME," +
                "CLOCKDATE DATE," +
                "BACKGROUND INTEGER)");
        databaseNote.queryData("CREATE TABLE IF NOT EXISTS IMAGE" +
                "(IDIMAGE INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NOTEIMAGE NVARCHAR(20000)," +
                "IDNOTE INTEGER," +
                "FOREIGN KEY(IDNOTE) REFERENCES NOTE(ID))");

    }
    

    //Lưu dữ liệu vào noteList
    private void getData() {
        Cursor testData = databaseNote.getData("Select * from Note");
        noteList.clear();
        while (testData.moveToNext()) {
            int id = testData.getInt(0);
            String tittle = testData.getString(1);
            String conttent = testData.getString(2);
            String nowTime = testData.getString(3);
            String nowDate = testData.getString(4);
            String clockTime = testData.getString(5);
            String clockDate = testData.getString(6);
            int noteBackground = testData.getInt(7);
            Cursor getStringImage = databaseNote.getData("Select NOTEIMAGE FROM IMAGE WHERE IDNOTE = " + id);
            ArrayList<String> arrayList = new ArrayList<>();
            while (getStringImage.moveToNext()) {
                String s = getStringImage.getString(0);
                arrayList.add(s);
            }
            noteList.add(new Note(id, tittle, conttent, arrayList, nowTime, nowDate, clockTime, clockDate, noteBackground));
        }

        noteAdapter.notifyDataSetChanged();
    }

    //Khởi tạo recyclerview
    private void initRecyclerView() {
        rvNote.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvNote.setLayoutManager(layoutManager);
        noteAdapter = new NoteAdapter(noteList, getApplicationContext());
        rvNote.setAdapter(noteAdapter);
    }



}
