package com.kashisol.fonediagnosis;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static java.lang.System.getProperty;

public class phoneinfo extends AppCompatActivity {

    TextView tvimei, tvdn, tvdm, tvav, tvaspl, tvmd, tvbv, tvkv, tvmf;
    MySQLHelper helper;
    SQLiteDatabase database;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Phone Info");
        setContentView(R.layout.activity_phoneinfo);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        tvimei = findViewById(R.id.tvimei1);
        tvdn = findViewById(R.id.tvdn);
        tvdm = findViewById(R.id.tvdm);
        tvav = findViewById(R.id.tvav);
        tvaspl = findViewById(R.id.tvaspv);
        tvmd = findViewById(R.id.tvmd);
        tvbv = findViewById(R.id.tvbv);
        tvkv = findViewById(R.id.tvkv);
        tvmf = findViewById(R.id.tvmf);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(phoneinfo.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            return;
        }
        String imei;
        if (Build.VERSION.SDK_INT > 9) {

            imei = telephonyManager.getImei(0);

            if (telephonyManager.getPhoneCount() == 2) {
                imei = imei + ", " + telephonyManager.getImei(1);
            }

        } else {
            imei = telephonyManager.getDeviceId(0);
            if (telephonyManager.getPhoneCount() == 2) {
                imei = imei + ", " + telephonyManager.getDeviceId(1);
            }

        }
        tvimei.setText(imei);

        tvdn.setText(Build.PRODUCT);
        tvmf.setText(Build.MANUFACTURER);
        tvdm.setText(Build.MODEL);
        if (Build.VERSION.SDK_INT < 7.1) {
            tvmd.setText(Build.SERIAL);
        } else {
            tvmd.setText(Build.getSerial());
        }
        tvav.setText(Build.VERSION.RELEASE + " " + Build.VERSION.INCREMENTAL);
        tvaspl.setText(Build.VERSION.SECURITY_PATCH);
        tvbv.setText(Build.getRadioVersion());
        tvkv.setText(getProperty("os.version") + " " + getProperty("os.name") + " " + getProperty("os.arch"));

        if (helper.searchName("Audio Jack", database) == 0) {
            helper.insertData("Audio Jack", "NA", "Pass", database);
        } else {
            helper.updateData(helper.searchName("Audio Jack", database), "NA", "Pass", database);
        }

    }
}