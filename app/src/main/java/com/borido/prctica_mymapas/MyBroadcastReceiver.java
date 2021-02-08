package com.borido.prctica_mymapas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isMyReceiver = MapsActivity.MY_ACTION_RECEIVER.equals(intent.getAction());
        if(isMyReceiver){
            Toast.makeText(context, intent.getStringExtra(MapsActivity.MY_ACTION_RECEIVER_EXTRA), Toast.LENGTH_LONG).show();
        } else{
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
            String log = sb.toString();
            Log.d("log", log);
            Toast.makeText(context, log, Toast.LENGTH_LONG).show();
        }
    }
}

