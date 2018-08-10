package com.example.vanluc.note.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.vanluc.note.alarm.AlarmRecevier;
import com.example.vanluc.note.define.DefaultValues;
import com.example.vanluc.note.model.ImageNote;
import com.example.vanluc.note.ulti.ChangeData;
import com.example.vanluc.note.ulti.ConvertToMilis;
import com.example.vanluc.note.ulti.NextToDate;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    public static String keyString = "KeyString";
    TextView tvNowDate, tvNowTime, tvArlam;
    EditText etTittle, etConttent;
    LinearLayout lnAddNote;
    Spinner spnDate, spnTime;
    ImageButton ibCloseClock;
    RecyclerView rvImageNote;
    ImageAdapter imageAdapter;
    Integer idNew;

    //Dialog
    Dialog dig_Camera, dig_Color, dig_DatePicker, dig_TimePicker;
    //ArrayList và adapter của spinner
    public static ArrayList<String> listChooseDate = new ArrayList<>();
    public static ArrayList<String> listChooseTime = new ArrayList<>();
    public static ArrayAdapter<String> timeAdapter;
    public static ArrayAdapter<String> dateAdapter;

    //Gắn các giá trị vào biến để sử dụng câu lệnh sql
    ArrayList<ImageNote> listImage = new ArrayList<>();
    int colorBackground;
    String timeClock = null;
    String dateClock = null;
    String timeNow, dateNow;
    String tittle, conttent;

    //Khai báo báo thức
    AlarmManager alarmManager;
    PendingIntent musicIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editActionBar();

        initView();

        setRecylerviewImage();

        setNowTimeNowDate();

        initListener();


    }

    //Ánh xạ
    private void initView() {
        tvArlam = findViewById(R.id.tv_Arlarm);
        lnAddNote = findViewById(R.id.ln_AddNote);
        tvNowDate = findViewById(R.id.tv_NowDate);
        tvNowTime = findViewById(R.id.tv_NowTime);
        etConttent = findViewById(R.id.et_Conttent);
        etTittle = findViewById(R.id.et_Tittle);
        spnDate = findViewById(R.id.spn_DateClock);
        spnTime = findViewById(R.id.spn_TimeClock);
        ibCloseClock = findViewById(R.id.ib_CloseClock);
        rvImageNote = findViewById(R.id.rv_ImageNote);
        setSpiner();
    }


    //Bắt sự kiện
    private void initListener() {
        tvArlam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinerClock();
            }
        });
        ibCloseClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSpinerClock();
            }
        });
    }

    //Đóng spinner
    private void closeSpinerClock() {
        spnDate.setVisibility(View.INVISIBLE);
        spnTime.setVisibility(View.INVISIBLE);
        ibCloseClock.setVisibility(View.INVISIBLE);
        timeClock = null;
        dateClock = null;
    }

    //Hiện thị spiner
    private void showSpinerClock() {
        spnTime.setVisibility(View.VISIBLE);
        spnDate.setVisibility(View.VISIBLE);
        ibCloseClock.setVisibility(View.VISIBLE);
        dateClock = dateNow;
        timeClock = "9:00";
    }

    //Xử lý spiner
    private void setSpiner() {
        //Xử lý spinner date
        listChooseDate.clear();
        dateAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                listChooseDate);
        listChooseDate.add(getResources().getString(R.string.spinerDate1));
        listChooseDate.add(getResources().getString(R.string.spinerDate2));
        listChooseDate.add(getResources().getString(R.string.spinerDate3));
        listChooseDate.add(getResources().getString(R.string.spinerDate4));
        spnDate.setAdapter(dateAdapter);

        //Xử lý spinner time
        listChooseTime.clear();
        timeAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                listChooseTime);
        listChooseTime.add(getResources().getString(R.string.spinerTime1));
        listChooseTime.add(getResources().getString(R.string.spinerTime2));
        listChooseTime.add(getResources().getString(R.string.spinerTime3));
        listChooseTime.add(getResources().getString(R.string.spinerTime4));
        spnTime.setAdapter(timeAdapter);

        //Bắt sự kiện cho spinner date
        spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnDate.getItemAtPosition(position).toString().equals(listChooseDate.get(0)) &&
                        spnDate.getVisibility() == View.VISIBLE) {
                    dateClock = NextToDate.changeDate(dateNow, 1);
                } else if (spnDate.getItemAtPosition(position).toString().equals(listChooseDate.get(1)) &&
                        spnDate.getVisibility() == View.VISIBLE) {
                    dateClock = NextToDate.changeDate(dateNow, 2);
                } else if (spnDate.getItemAtPosition(position).toString().equals(listChooseDate.get(2)) &&
                        spnDate.getVisibility() == View.VISIBLE) {
                    dateClock = NextToDate.changeDate(dateNow, 3);
                } else if (spnDate.getItemAtPosition(position).toString().equals(listChooseDate.get(3)) &&
                        spnDate.getVisibility() == View.VISIBLE) {
                    showDateDialog();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Bắt sự kiện cho spinner time
        spnTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnTime.getItemAtPosition(position).toString().equals(listChooseTime.get(0)) &&
                        spnTime.getVisibility() == View.VISIBLE) {
                    timeClock = getResources().getString(R.string.spinerTime1);
                } else if (spnTime.getItemAtPosition(position).toString().equals(listChooseTime.get(1)) &&
                        spnTime.getVisibility() == View.VISIBLE) {
                    timeClock = getResources().getString(R.string.spinerTime2);
                } else if (spnTime.getItemAtPosition(position).toString().equals(listChooseTime.get(2)) &&
                        spnTime.getVisibility() == View.VISIBLE) {
                    timeClock = getResources().getString(R.string.spinerTime3);
                } else if (spnTime.getItemAtPosition(position).toString().equals(listChooseTime.get(3)) &&
                        spnTime.getVisibility() == View.VISIBLE) {
                    showTimeDialog();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Show diaglog Timepicker
    private void showTimeDialog() {
        dig_TimePicker = new Dialog(this);
        dig_TimePicker.setContentView(R.layout.dialog_choose_time);
        dig_TimePicker.show();
        TimePicker dp_TimePickClock = dig_TimePicker.findViewById(R.id.dp_TimePickClock);
        dp_TimePickClock.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeClock = hourOfDay + ":" + minute;
                dig_TimePicker.dismiss();
                listChooseTime.set(3, timeClock);
                timeAdapter.notifyDataSetChanged();
            }
        });
    }

    //Show diaglog Datepicker
    private void showDateDialog() {
        dig_DatePicker = new Dialog(this);
        dig_DatePicker.setContentView(R.layout.dialog_choose_date);
        dig_DatePicker.show();
        DatePicker dp_DatePickClock = dig_DatePicker.findViewById(R.id.dp_DatePickClock);
        Calendar calendar = Calendar.getInstance();
        // Lấy ra năm - tháng - ngày hiện tại
        int year = calendar.get(calendar.YEAR);
        final int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        dp_DatePickClock.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateClock = dayOfMonth + "/" + monthOfYear + "/" + year;
                dig_DatePicker.dismiss();
                listChooseDate.set(3, dateClock);
                dateAdapter.notifyDataSetChanged();

            }
        });
    }


    //Gán ngày tháng hiện tại vào textview
    private void setNowTimeNowDate() {
        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        SimpleDateFormat nowDate = new SimpleDateFormat(strDateFormat);
        tvNowDate.setText(nowDate.format(date));
        dateNow = tvNowDate.getText().toString();
        String strTimeFormat = "HH:mm";
        SimpleDateFormat nowTime = new SimpleDateFormat(strTimeFormat);
        tvNowTime.setText(nowTime.format(date));
        timeNow = tvNowTime.getText().toString();
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

    //Bắt sự kiện cho item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menuAdd:
                addNewNote();
                return true;
            case R.id.menuEdit:
                choseColorBackground();
                return true;
            case R.id.menuCamera:
                menuCameraClick();
                return true;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Tạo icon menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }


    //Hàm click icon menu chọn màu
    private void choseColorBackground() {
        showDIalogColor();
    }


    //Show diaglog chọn màu
    private void showDIalogColor() {
        dig_Color = new Dialog(this);
        dig_Color.setContentView(R.layout.dialog_color);
        dig_Color.show();
        Button btn_Background1, btn_Background2, btn_Background3, btn_Background4;
        btn_Background1 = dig_Color.findViewById(R.id.btn_Background1);
        btn_Background2 = dig_Color.findViewById(R.id.btn_Background2);
        btn_Background3 = dig_Color.findViewById(R.id.btn_Background3);
        btn_Background4 = dig_Color.findViewById(R.id.btn_Background4);
        btn_Background1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBackground(DefaultValues.itemBackground1);
                dig_Color.dismiss();
            }
        });
        btn_Background2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBackground(DefaultValues.itemBackground2);
                dig_Color.dismiss();
            }
        });
        btn_Background3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBackground(DefaultValues.itemBackground3);
                dig_Color.dismiss();
            }
        });
        btn_Background4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorBackground(DefaultValues.itemBackground4);
                dig_Color.dismiss();
            }
        });
    }


    //Set background của item
    private void setColorBackground(int itemBackground1) {
        if (itemBackground1 == DefaultValues.itemBackground1) {
            lnAddNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem1));
            colorBackground = itemBackground1;
        } else if (itemBackground1 == DefaultValues.itemBackground2) {
            lnAddNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem2));
            colorBackground = itemBackground1;
        } else if (itemBackground1 == DefaultValues.itemBackground3) {
            lnAddNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem3));
            colorBackground = itemBackground1;
        } else if (itemBackground1 == DefaultValues.itemBackground4) {
            lnAddNote.setBackgroundColor(getResources().getColor(R.color.backGroundItem4));
            colorBackground = itemBackground1;
        }
    }


    //Show diaglog chọn ảnh
    private void menuCameraClick() {
        showDialogPhoto();

    }

    //Show diaglog chọn ảnh
    private void showDialogPhoto() {
        dig_Camera = new Dialog(this);
        dig_Camera.setContentView(R.layout.dialog_camera);
        dig_Camera.show();
        LinearLayout ln_Take = dig_Camera.findViewById(R.id.ln_TakePhoto);
        LinearLayout ln_Choose = dig_Camera.findViewById(R.id.ln_ChoosePhoto);
        ln_Take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                dig_Camera.dismiss();
            }
        });
        ln_Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
                dig_Camera.dismiss();
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
                    imageAdapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == DefaultValues.take_Photo) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                String stringImage = ChangeData.BitMapToString(bitmap);
                listImage.add(new ImageNote(stringImage));
                imageAdapter.notifyDataSetChanged();

            }
        }
    }


    //Set ImageRecyvlerView
    private void setRecylerviewImage() {
        rvImageNote.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImageNote.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(listImage, getApplicationContext());
        rvImageNote.setAdapter(imageAdapter);

    }

    //Hàm thêm note mới vào sqlite
    private void addNewNote() {
        conttent = etConttent.getText().toString();
        tittle = etTittle.getText().toString();
        if (tittle.isEmpty()) {
            Toast.makeText(this, "" + getResources().getString(R.string.loiTittleEmty), Toast.LENGTH_SHORT).show();
        } else if (conttent.isEmpty() == true) {
            Toast.makeText(this, "" + getResources().getString(R.string.loiConttentEmty), Toast.LENGTH_SHORT).show();
        } else {
            //Thêm dữ liệu vào bảng note
            MainActivity.databaseNote.queryData("INSERT INTO NOTE VALUES " +
                    "(null,'" + tittle + "','" + conttent + "','" + timeNow + "','" + dateNow + "','" + timeClock +
                    "','" + dateClock + "'," + colorBackground + ")");

            //Trả về id của luồng dữ liệu mới nhất vừa thêm vào
            Cursor getID = MainActivity.databaseNote.getData("SELECT ID from NOTE order by ID DESC limit 1");
            if (getID != null && getID.moveToFirst()) {
                idNew = getID.getInt(0);
            }

            //Thêm dữ liệu vào bảng Image
            for (int i = 0; i < listImage.size(); i++) {
                MainActivity.databaseNote.queryData("insert into Image values ( null , '" +
                        listImage.get(i).getBitmapImageNote() + "', " + idNew + ")");
            }
            if (dateClock != null || timeClock != null) {
                long alarm = ConvertToMilis.covertMilis(dateClock, timeClock);
                alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlarmRecevier.class);
                intent.putExtra(keyString,idNew);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                //Alarm là  ngày và giờ  báo thức parse sang millisecond
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarm ,pendingIntent);
                Log.e("Time",""+alarm);

            }
            this.finish();

        }
    }

}
