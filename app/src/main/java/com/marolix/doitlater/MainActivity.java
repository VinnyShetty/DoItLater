package com.marolix.doitlater;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    int REQUEST_CODE_DOC=101;
    DatePickerDialog.OnDateSetListener from_dateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Phone_number=findViewById(R.id.Phone_number);
        getContacts=findViewById(R.id.getContacts);
        message=findViewById(R.id.message);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        radiogroup=findViewById(R.id.radiogroup);
        send=findViewById(R.id.send);
       getContacts.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,Contacts.class);
               startActivity(intent);
           }
       });
       Phone_number.setText(getIntent().getStringExtra("number"));
       String msg=message.getText().toString().trim();
       calendar = Calendar.getInstance();
       date.setOnClickListener(this);
       time.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               Calendar mcurrentTime = Calendar.getInstance();
             final  int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
              final int minute = mcurrentTime.get(Calendar.MINUTE);
               TimePickerDialog mTimePicker;
               mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selecteddate=date.getText().toString().trim();
//                        Log.e("date",selecteddate);
//                        Log.e("current date", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                        if(selecteddate.equals( new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date())))
                        {
                            if( hour<=selectedHour  )
                            {

                                if(minute<selectedMinute)
                                time.setText(selectedHour + ":" + selectedMinute);
                            }
                            else {
                                Toast.makeText(MainActivity.this,"select after the current time",Toast.LENGTH_LONG).show();

                            }
                        }
                       else {
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
        int selectedId = radiogroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
        String sim = selectedRadioButton.getText().toString();
    }
    private void updateFromDateDisplay() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        date.setText(sdf.format(calendar.getTime()));

        /*from_date.setText(new StringBuilder()
                .append(from_day).append("-").append(from_month + 1).append("-").append(from_year));*/
    }
    @Override
    public void onClick(View v) {
        DatePickerDialog dpDialog =  new DatePickerDialog(this, from_dateListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = dpDialog.getDatePicker();

        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());
        //datePicker.setMaxDate(calendar.getTimeInMillis());
        dpDialog.show();
    }

}

