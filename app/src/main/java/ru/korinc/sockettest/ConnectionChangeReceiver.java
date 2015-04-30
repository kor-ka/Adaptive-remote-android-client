package ru.korinc.sockettest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import ru.korinc.sockettest.util.IPAddressValidator;

public class ConnectionChangeReceiver extends BroadcastReceiver {
    public ConnectionChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "wifi", Toast.LENGTH_SHORT).show();
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals (intent.getAction())) {
            NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (ConnectivityManager.TYPE_WIFI == netInfo.getType() && netInfo.isConnected()) {

                SharedPreferences shp = context.getSharedPreferences("default", Context.MODE_MULTI_PROCESS);
                SharedPreferences.Editor ed = shp.edit();

                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo ();
                String ssid = info.getBSSID ();
                if(shp.contains("WIFIIP:"+ssid) && IPAddressValidator.validate(shp.getString("ip", ""))){
                    ed.putString("ip", shp.getString("WIFIIP:"+ssid, ""));
                    ed.putString("port", shp.getString("WIFIPORT:"+ssid, ""));
                    ed.commit();
                }
            }
        }
    }

    public static String getKey(){
        return "BSwlLiZKIyIjCQBRDhkIIFQSXgokPSkpYSMjIi86GCc/KC4vJgkDJi09KmEPFgdeXm8HRg4RPRwI";
    }
}
