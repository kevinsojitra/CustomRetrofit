package com.developer.webservice.retrofit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.developer.webservice.retrofit.NetworkUtil;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private static OnConnectionChangeListener listener;

    @Override
    public void onReceive(final Context context, final Intent intent) {


        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
           if (listener!=null){
               listener.onConnectionChange(NetworkUtil.isOnline(context));
           }
       }
    }

    public static void setOnConnectionChangeListener(OnConnectionChangeListener listener){
        NetworkChangeReceiver.listener=listener;
    }

    public interface OnConnectionChangeListener{
        void onConnectionChange(boolean b);
    }


}