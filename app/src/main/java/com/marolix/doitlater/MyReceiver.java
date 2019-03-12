package com.marolix.doitlater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    SmsManager manager = SmsManager.getDefault();
    String number=intent.getStringExtra("phoneNo");
    String msg=intent.getStringExtra("msg");
    manager.sendTextMessage(number,null,msg,null,null);
    }
}
