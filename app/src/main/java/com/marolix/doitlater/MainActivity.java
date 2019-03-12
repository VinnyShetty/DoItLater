package com.marolix.doitlater;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
EditText Phone_number,message;
TextView date,time;
RadioGroup radiogroup;
ImageView getContacts;
Button send;
private Calendar calendar;
DatePickerDialog.OnDateSetListener from_dateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String datetime = dateformat.format(c.getTime());
        SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm");
        String timetime = timeformat.format(c.getTime());
        Phone_number = findViewById(R.id.Phone_number);
        getContacts = findViewById(R.id.getContacts);
        message = findViewById(R.id.message);
        date = findViewById(R.id.date);
        date.setText(datetime);
        time = findViewById(R.id.time);
        time.setText(timetime);
        radiogroup = findViewById(R.id.radiogroup);
        send = findViewById(R.id.send);
        getContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Contacts.class);
                startActivity(intent);
            }
        });
        Phone_number.setText(getIntent().getStringExtra("number"));
        calendar = Calendar.getInstance();
        date.setOnClickListener(this);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selecteddate = date.getText().toString().trim();
                        Log.e("date", selecteddate);
                        Log.e("current date", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                        if (selecteddate.equals(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()))) {
                            if (hour <= selectedHour) {
                                if (minute < selectedMinute)
                                    time.setText(selectedHour + ":" + selectedMinute);
                            } else {
                                Toast.makeText(MainActivity.this, "select after the current time", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            time.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        from_dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
              updateFromDateDisplay();
            }

        };

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Phone_no = Phone_number.getText().toString().trim();
                final String msg = message.getText().toString().trim();
                int selectedId = radiogroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                String sim = selectedRadioButton.getText().toString();
                String date1 = date.getText().toString().trim();
                String time1 = time.getText().toString().trim();
                if (!Phone_no.equals("") && !msg.equals("") && !sim.equals("")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    String dateString = date1 + " " + time1;
                    Log.e("dateString", dateString);
                    Date dateee = new Date();
                    try {
                        dateee = sdf.parse(dateString);
                        Calendar calendar1 = Calendar.getInstance();
                        //Setting the Calendar date and time to the given date and time
                        calendar1.setTime(dateee);
                        Log.e("calenderf", String.valueOf(calendar1.getTimeInMillis()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Given Time in milliseconds : " + dateee.getTime());

                    Log.e("calender", String.valueOf(calendar.getTimeInMillis()));
                    Date c = Calendar.getInstance().getTime();
                    Log.e("current", String.valueOf(c));
                    Log.e("System time", String.valueOf(System.currentTimeMillis()));
                    Log.e("pendingIntent", String.valueOf(calendar.getTimeInMillis() - System.currentTimeMillis()));

                    int num = (int) System.currentTimeMillis();
                    Intent intent = new Intent(getApplication(), MyReceiver.class);
                    intent.putExtra("phoneNo", Phone_no);
                    intent.putExtra("msg", msg);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.add(Calendar.MILLISECOND, (int) calendar.getTimeInMillis());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            getApplicationContext(), num, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, dateee.getTime(), pendingIntent);

                    Toast.makeText(getApplication(), "Alarm set in " + (dateee.getTime() / 1000) / 60 + " millseconds",
                            Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(getApplication(),"enter all the fields",Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    private void updateFromDateDisplay() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        date.setText(sdf.format(calendar.getTime()));
    }


    @Override
    public void onClick(View v) {
        DatePickerDialog dpDialog =  new DatePickerDialog(this, from_dateListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = dpDialog.getDatePicker();

        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());
        dpDialog.show();
    }



}

