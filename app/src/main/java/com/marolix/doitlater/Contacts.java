package com.marolix.doitlater;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Contacts extends AppCompatActivity {

ArrayList<Map<String,String>>  al=new ArrayList<>();
RecyclerView lv;
CustomAdapter customAdapter;
    private String[] mColumnProjection = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};


    private String mOrderBy = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        lv=findViewById(R.id.recycle);

        if (isPermissionGranted()) {
//            ContentResolver contentResolver = getContentResolver();
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, mOrderBy);
            while (phones.moveToNext())
            {
                Map<String,String> hmap=new HashMap<>();
                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                hmap.put("name",name);
                hmap.put("number",phoneNumber);
                al.add(hmap);
            }
            phones.close();
//            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
//                    mColumnProjection,
//                    null,
//                    null,
//                    mOrderBy);
//
//            if (cursor != null && cursor.getCount() > 0) {
//
//
//                while (cursor.moveToNext()) {
//                    Map<String,String> hmap=new HashMap<>();
//                    hmap.put("name",cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
//                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
//                    {
//                        hmap.put("number",cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
//                    }
//                    al.add(hmap);
//                }
                Log.e("NAME1", al.toString());
                lv.setLayoutManager(new LinearLayoutManager(this));
                customAdapter=new CustomAdapter(this,al);
                lv.setAdapter(customAdapter);

//                aa = new ArrayAdapter<String>(  this,
//                        android.R.layout.simple_list_item_1,al);
//                lv.setAdapter(aa);
                //textViewQueryResult.setText(stringBuilderQueryResult.toString());
            } else {
               //textViewQueryResult.setText("No Contacts in device");
            }


//        }
//        else {
//            Toast.makeText(Contacts.this, "permission denied", Toast.LENGTH_SHORT).show();
//        }
//        userDictionary();
    }
    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED )&&(checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED )) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,Manifest.permission.SEND_SMS}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "below 23");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 2: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }     // other 'case' lines to check for other
        // permissions this app might request



    }
}
