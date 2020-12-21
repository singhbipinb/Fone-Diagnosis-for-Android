package com.kashisol.fonediagnosis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class wifistatus extends AppCompatActivity {

    WifiManager manager;
    WifiInfo info;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    String wifistate, wifistrength;
    int level;
    MySQLHelper helper;
    SQLiteDatabase database;

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Wi-Fi");
        setContentView(R.layout.activity_wifistatus);

        tv1 = findViewById(R.id.tvwifi1);
        tv2 = findViewById(R.id.tvwifi2);
        tv3 = findViewById(R.id.tvwifi3);
        tv4 = findViewById(R.id.tvwifi4);
        tv5 = findViewById(R.id.tvwifi5);
        tv6 = findViewById(R.id.tvwifi6);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();

        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        info = manager.getConnectionInfo();

        int getwifistate = manager.getWifiState();
        boolean fghz = manager.is5GHzBandSupported();
//        boolean wifienabled = manager.isWifiEnabled();

        String ssid = info.getSSID();

        String mac = getMacAddr();

        int rssi = info.getRssi();

        int linkspeed = info.getLinkSpeed();

        int wifiip = info.getIpAddress();

        level = WifiManager.calculateSignalLevel(info.getRssi(), 5);


        switch (getwifistate) {
            case 0:
                wifistate = "Disabling";
                break;
            case 1:
                wifistate = "Disabled";
                break;
            case 2:
                wifistate = "Enabling";
                break;
            case 3:
                wifistate = "Enabled";
                break;
            case 4:
                wifistate = "Unknown";
                break;

        }


        switch (level) {

            case 1:
                wifistrength = "Poor";
                break;
            case 2:
                wifistrength = "Average";
                break;
            case 3:
                wifistrength = "Good";
                break;
            case 4:
                wifistrength = "Very Good";
                break;
            case 5:
                wifistrength = "Excellent";
                break;

        }


        tv1.setText(wifistate);
        tv2.setText(ssid);
        tv3.setText(mac);
        tv4.setText(linkspeed + " Mbps");
        tv5.setText(wifistrength);
        if (fghz) {
            tv6.setText("Yes");
        } else {
            tv6.setText("No");
        }

        if (helper.searchName("Wi-Fi", database) == 0) {
            helper.insertData("Wi-Fi", "Working", "Pass", database);
        } else {
            helper.updateData(helper.searchName("Wi-Fi", database), "Working", "Pass", database);
        }

    }
}