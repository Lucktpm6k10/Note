package com.example.vanluc.note.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vanluc.note.R;
import com.example.vanluc.note.adapter.ImageAdapter;
import com.example.vanluc.note.adapter.NoteAdapter;
import com.example.vanluc.note.define.DefaultValues;
import com.example.vanluc.note.model.ImageNote;
import com.example.vanluc.note.ulti.ChangeData;
import com.example.vanluc.note.ulti.NextToDate;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareStoryContent;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;


public class EditNoteActivity extends AppCompatActivity {
    public static String EXTRA_REMINDER_ID = "abc";
    TextView tv_EditNowDate, tv_EditNowTime, tv_EditArlam;
    EditText et_EditTittle, et_EditConttent;
    LinearLayout ln_EditNote;
    Spinner spn_EditDate, spn_EditTime;
    ImageButton ib_EditCloseClock;
    BottomNavigationView bottomBar;
    //Gắn các giá trị vào biến để sử dụng câu lệnh sql
    ArrayList<ImageNote> listImage = new ArrayList<>();
    int colorBackground, id;
    String timeClock;
    String dateClock;
    String timeNow, dateNow;
    String tittle, conttent;
    int position;
    //Adpater list ảnh
    ImageAdapter imageEditAdapter;
    RecyclerView rv_EditImageNote;

    //Dialog
    Dialog dig_EditCamera, dig_EditColor, dig_EditDatePicker, dig_EditTimePicker, dig_Delete;

    //ArrList và adapter của spinner
    public static ArrayList<String> listChooseEditDate = new ArrayList<>();
    public static ArrayList<String> listChooseEditTime = new ArrayList<>();
    public static ArrayAdapter<String> timeEditAdapter;
    public static ArrayAdapter<String> dateEditAdapter;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editActionBar();
        initView();
        initListener();
        setInformationForView();

    }

    //Khai báo cho view
    private void initView() {
        tv_EditArlam = findViewById(R.id.tv_EditArlarm);
        ln_EditNote = findViewById(R.id.ln_EditNote);
        tv_EditNowDate = findViewById(R.id.tv_EditNowDate);
        tv_EditNowTime = findViewById(R.id.tv_EditNowTime);
        et_EditConttent = findViewById(R.id.et_EditConttent);
        et_EditTittle = findViewById(R.id.et_EditTittle);
        spn_EditDate = findViewById(R.id.spn_EditDateClock);
        spn_EditTime = findViewById(R.id.spn_EditTimeClock);
        ib_EditCloseClock = findViewById(R.id.ib_EditCloseClock);
        rv_EditImageNote = findViewById(R.id.rv_EditImageNote);
        bottomBar = findViewById(R.id.menuBottom);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

    }


    //Gắn text cho view
    private void setInformationForView() {
        Intent i = getIntent();
        Bundle b = i.getBundleExtra(NoteAdapter.checkID);
        position = b.getInt(NoteAdapter.idBundle);
        id = MainActivity.noteList.get(position).getId();
        //Gán text cho các view
        dateNow = MainActivity.noteList.get(position).getNowDate();
        timeNow = MainActivity.noteList.get(position).getNowTime();
        tittle = MainActivity.noteList.get(position).getTittle();
        conttent = MainActivity.noteList.get(position).getConttent();
        colorBackground = MainActivity.noteList.get(position).getItemBackground();
        et_EditConttent.setText(conttent);
        et_EditTittle.setText(tittle);
        tv_EditNowDate.setText(dateNow);
        tv_EditNowTime.setText(timeNow);
        timeClock = MainActivity.noteList.get(position).getClockTime();
        dateClock = MainActivity.noteList.get(position).getClockDate();
        //Set màu cho background
        if (colorBackground == DefaultValues.itemBackground2) {
            //set màu itemView bằng màu itemBackground2
            ln_EditNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem2));
        } else if (colorBackground == DefaultValues.itemBackground3) {
            //set màu itemView bằng màu itemBackground2
            ln_EditNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem3));
        } else if (colorBackground == DefaultValues.itemBackground4) {
            //set màu itemView bằng màu itemBackground2
            ln_EditNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem4));
        } else {
            //set màu itemView bằng màu itemBackground1
            ln_EditNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem1));
        }
        //Ẩn hiện spinner
        if (timeClock.equals("null") == false || dateClock.equals("null") == false) {
            spn_EditTime.setVisibility(View.VISIBLE);
            spn_EditDate.setVisibility(View.VISIBLE);
            ib_EditCloseClock.setVisibility(View.VISIBLE);
            setSpiner();
        }
        //Gán list ảnh
        for (int j = 0; j < MainActivity.noteList.get(position).getNoteImage().size(); j++) {
            listImage.add(new ImageNote(MainActivity.noteList.get(position).getNoteImage().get(j)));
        }
        setRecylerviewEditImage();


    }

    //Tạo icon và tittle của actionbar
    private void editActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  Note");
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Thêm nút back
        actionBar.setLogo(R.mipmap.ic_note_icon);    //Icon muốn hiện thị
        actionBar.setDisplayUseLogoEnabled(true);
    }

    //Tạo icon top menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_edit_item, menu);
        return true;
    }


    //Bắt sự kiện cho item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDelete:
                deleteNote();
                break;
            case R.id.menuSave:
                saveNote();
                break;
            case R.id.menuNew:
                newNote();
                break;
            case R.id.menuCameraEdit:
                menuCameraClick();
                break;
            case R.id.menuEditBackground:
                choseColorBackground();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //Chọn lại màu cho background
    private void choseColorBackground() {
        showDIalogColor();
    }

    //Show dialog color
    private void showDIalogColor() {
        dig_EditColor = new Dialog(this);
        dig_EditColor.setContentView(R.layout.dialog_color);
        dig_EditColor.show();
        Button btn_Background1, btn_Background2, btn_Background3, btn_Background4;
        btn_Background1 = dig_EditColor.findViewById(R.id.btn_Background1);
        btn_Background2 = dig_EditColor.findViewById(R.id.btn_Background2);
        btn_Background3 = dig_EditColor.findViewById(R.id.btn_Background3);
        btn_Background4 = dig_EditColor.findViewById(R.id.btn_Background4);
        btn_Background1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBackground(DefaultValues.itemBackground1);
                dig_EditColor.dismiss();
            }
        });
        btn_Background2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBackground(DefaultValues.itemBackground2);
                dig_EditColor.dismiss();
            }
        });
        btn_Background3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBackground(DefaultValues.itemBackground3);
                dig_EditColor.dismiss();
            }
        });
        btn_Background4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBackground(DefaultValues.itemBackground4);
                dig_EditColor.dismiss();
            }
        });
    }

    //Set background của item
    private void setColorBackground(int itemBackground1) {
        if (itemBackground1 == DefaultValues.itemBackground1) {
            ln_EditNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem1));
            colorBackground = itemBackground1;
        } else if (itemBackground1 == DefaultValues.itemBackground2) {
            ln_EditNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem2));
            colorBackground = itemBackground1;
        } else if (itemBackground1 == DefaultValues.itemBackground3) {
            ln_EditNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem3));
            colorBackground = itemBackground1;
        } else if (itemBackground1 == DefaultValues.itemBackground4) {
            ln_EditNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem4));
            colorBackground = itemBackground1;
        }
    }


    //Hiện thị camera
    private void menuCameraClick() {
        showDialogPhoto();
    }

    //SHow dialog chọn ảnh
    private void showDialogPhoto() {
        dig_EditCamera = new Dialog(this);
        dig_EditCamera.setContentView(R.layout.dialog_camera);
        dig_EditCamera.show();
        LinearLayout ln_Take = dig_EditCamera.findViewById(R.id.ln_TakePhoto);
        LinearLayout ln_Choose = dig_EditCamera.findViewById(R.id.ln_ChoosePhoto);
        ln_Take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                dig_EditCamera.dismiss();
            }
        });
        ln_Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
                dig_EditCamera.dismiss();
            }
        });
    }

    //Chọn ảnh
    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, DefaultValues.choose_Photo);
    }

    //Chụp ảnh
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, DefaultValues.take_Photo);
    }

    //Hàm trả kết quả của diaglog chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == DefaultValues.choose_Photo) {
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    String stringImage = ChangeData.BitMapToString(bitmap);
                    listImage.add(new ImageNote(stringImage));
                    imageEditAdapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == DefaultValues.take_Photo) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                String stringImage = ChangeData.BitMapToString(bitmap);
                listImage.add(new ImageNote(stringImage));
                imageEditAdapter.notifyDataSetChanged();

            }
        }
    }

    //Lùi một note
    private void previousNote() {
        if (position == 0) {
            position = MainActivity.noteList.size() - 1;
        } else {
            position = position - 1;
        }
        Intent intent = new Intent(EditNoteActivity.this, EditNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(NoteAdapter.idBundle, position);
        intent.putExtra(NoteAdapter.checkID, bundle);
        startActivity(intent);
    }

    //Tiến một note
    private void nextNote() {
        if (position == MainActivity.noteList.size() - 1) {
            position = 0;
        } else {
            position = position + 1;
        }
        Intent intent = new Intent(EditNoteActivity.this, EditNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(NoteAdapter.idBundle, position);
        intent.putExtra(NoteAdapter.checkID, bundle);
        startActivity(intent);
    }

    //Mở activity thêm note mới
    private void newNote() {
        Intent intent = new Intent(EditNoteActivity.this, AddNoteActivity.class);
        startActivity(intent);
    }

    //Lưu lại note sau khi đã edit
    private void saveNote() {
        tittle = et_EditTittle.getText().toString();
        conttent = et_EditConttent.getText().toString();
        if (tittle.isEmpty() == true) {
            Toast.makeText(this, getResources().getString(R.string.loiTittleEmty), Toast.LENGTH_SHORT).show();
        }
        if (tittle.isEmpty() == true) {
            Toast.makeText(this, getResources().getString(R.string.loiConttentEmty), Toast.LENGTH_SHORT).show();
        } else {
            MainActivity.databaseNote.queryData("UPDATE NOTE SET TITTLE = '" + tittle + "', " +
                    "CONTENT = '" + conttent + "'," +
                    "NOWTIME = '" + timeNow + "', " +
                    "NOWDATE = '" + dateNow + "', " +
                    "CLOCKTIME = '" + timeClock + "', " +
                    "CLOCKDATE = '" + dateClock + "', " +
                    "BACKGROUND = " + colorBackground +
                    " WHERE ID = " + id);
            MainActivity.databaseNote.queryData("DELETE FROM IMAGE WHERE IDNOTE = " + id);
            for (int i = 0; i < listImage.size(); i++) {
                MainActivity.databaseNote.queryData("insert into Image values ( null , '" +
                        listImage.get(i).getBitmapImageNote() + "', " + id + ")");
            }
            
            Toast.makeText(this, getResources().getString(R.string.update), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    //Hàm delete note
    private void deleteNote() {
        showDeleteDialog();
    }

    //Show dialog delete
    private void showDeleteDialog() {
        dig_Delete = new Dialog(this);
        dig_Delete.setContentView(R.layout.dig_delete_note);
        dig_Delete.show();
        //Anh xa cac buttonn
        Button btn_Ok = dig_Delete.findViewById(R.id.btn_Ok);
        Button btn_Cancel = dig_Delete.findViewById(R.id.btn_Cancel);
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.databaseNote.queryData("DELETE FROM NOTE WHERE ID = " + id);
                MainActivity.databaseNote.queryData("DELETE FROM IMAGE WHERE IDNOTE = " + id);
                Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
                dig_Delete.dismiss();
                Toast.makeText(EditNoteActivity.this, getResources().getString(R.string.delete), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dig_Delete.dismiss();
            }
        });
    }

    //Xử lý ẩn hiện spinner
    private void initListener() {
        tv_EditArlam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinerClock();
            }
        });
        ib_EditCloseClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSpinerClock();
            }
        });
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuNext:
                        nextNote();
                        break;
                    case R.id.menuPrevious:
                        previousNote();
                        break;
                    case R.id.menuShare:
                        shareOnFacebook();
                        break;
                }
                return true;
            }
        });
    }

    private void shareOnFacebook() {
        String uri = "facebook://facebook.com/inbox";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }
    //Đóng spinner
    private void closeSpinerClock() {
        spn_EditTime.setVisibility(View.INVISIBLE);
        spn_EditDate.setVisibility(View.INVISIBLE);
        ib_EditCloseClock.setVisibility(View.INVISIBLE);
        dateClock = null;
        timeClock = null;
    }

    //Hiện  spinner
    private void showSpinerClock() {
        spn_EditDate.setVisibility(View.VISIBLE);
        spn_EditTime.setVisibility(View.VISIBLE);
        ib_EditCloseClock.setVisibility(View.VISIBLE);
    }

    // Xử lý spinner
    private void setSpiner() {
        //Spn time
        dateEditAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                listChooseEditDate);
        listChooseEditDate.add(dateClock);
        listChooseEditDate.add(getResources().getString(R.string.spinerDate1));
        listChooseEditDate.add(getResources().getString(R.string.spinerDate2));
        listChooseEditDate.add(getResources().getString(R.string.spinerDate3));
        listChooseEditDate.add(getResources().getString(R.string.spinerDate4));
        spn_EditDate.setAdapter(dateEditAdapter);

        //Xử lý spinner time
        timeEditAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                listChooseEditTime);
        listChooseEditTime.add(timeClock);
        listChooseEditTime.add(getResources().getString(R.string.spinerTime1));
        listChooseEditTime.add(getResources().getString(R.string.spinerTime2));
        listChooseEditTime.add(getResources().getString(R.string.spinerTime3));
        listChooseEditTime.add(getResources().getString(R.string.spinerTime4));
        spn_EditTime.setAdapter(timeEditAdapter);

        //Bắt sự kiện cho spinner date
        spn_EditDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spn_EditDate.getItemAtPosition(position).toString().equals(listChooseEditDate.get(1)) &&
                        spn_EditDate.getVisibility() == View.VISIBLE) {
                    dateClock = NextToDate.changeDate(dateNow, 2);
                } else if (spn_EditDate.getItemAtPosition(position).toString().equals(listChooseEditDate.get(2)) &&
                        spn_EditDate.getVisibility() == View.VISIBLE) {
                    dateClock = NextToDate.changeDate(dateNow, 3);
                } else if (spn_EditDate.getItemAtPosition(position).toString().equals(listChooseEditDate.get(3)) &&
                        spn_EditDate.getVisibility() == View.VISIBLE) {
                    dateClock = NextToDate.changeDate(dateNow, 4);
                } else if (spn_EditDate.getItemAtPosition(position).toString().equals(listChooseEditDate.get(4)) &&
                        spn_EditDate.getVisibility() == View.VISIBLE) {
                    showDateDialog();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Bắt sự kiện cho spinner time
        spn_EditTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spn_EditTime.getItemAtPosition(position).toString().equals(listChooseEditTime.get(1)) &&
                        spn_EditTime.getVisibility() == View.VISIBLE) {
                    timeClock = getResources().getString(R.string.spinerTime1);
                } else if (spn_EditTime.getItemAtPosition(position).toString().equals(listChooseEditTime.get(2)) &&
                        spn_EditTime.getVisibility() == View.VISIBLE) {
                    timeClock = getResources().getString(R.string.spinerTime2);
                } else if (spn_EditTime.getItemAtPosition(position).toString().equals(listChooseEditTime.get(3)) &&
                        spn_EditTime.getVisibility() == View.VISIBLE) {
                    timeClock = getResources().getString(R.string.spinerTime3);
                } else if (spn_EditTime.getItemAtPosition(position).toString().equals(listChooseEditTime.get(4)) &&
                        spn_EditTime.getVisibility() == View.VISIBLE) {
                    showTimeDialog();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Show diaglog Datepicker
    private void showDateDialog() {
        dig_EditDatePicker = new Dialog(this);
        dig_EditDatePicker.setContentView(R.layout.dialog_choose_date);
        dig_EditDatePicker.show();
        DatePicker dp_DatePickClock = dig_EditDatePicker.findViewById(R.id.dp_DatePickClock);
        Calendar calendar = Calendar.getInstance();
        // Lấy ra năm - tháng - ngày hiện tại
        int year = calendar.get(calendar.YEAR);
        final int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        dp_DatePickClock.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateClock = dayOfMonth + "/" + monthOfYear + "/" + year;
                dig_EditDatePicker.dismiss();
                listChooseEditDate.set(0, dateClock);
                dateEditAdapter.notifyDataSetChanged();
                spn_EditDate.setSelection(0);
                dateClock = listChooseEditDate.get(0);

            }
        });
    }


    //Show diaglog Timepicker
    private void showTimeDialog() {
        dig_EditTimePicker = new Dialog(this);
        dig_EditTimePicker.setContentView(R.layout.dialog_choose_time);
        dig_EditTimePicker.show();
        TimePicker dp_TimePickClock = dig_EditTimePicker.findViewById(R.id.dp_TimePickClock);
        dp_TimePickClock.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeClock = hourOfDay + ":" + minute;
                dig_EditTimePicker.dismiss();
                listChooseEditTime.set(0, timeClock);
                timeEditAdapter.notifyDataSetChanged();
                spn_EditTime.setSelection(0);
                timeClock = listChooseEditTime.get(0);
            }
        });
    }


    //Set ImageRecyvlerView
    private void setRecylerviewEditImage() {
        rv_EditImageNote.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_EditImageNote.setLayoutManager(layoutManager);
        imageEditAdapter = new ImageAdapter(listImage, getApplicationContext());
        rv_EditImageNote.setAdapter(imageEditAdapter);

    }


}

