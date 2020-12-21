package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class hardware extends AppCompatActivity {

    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hardware");
        setContentView(R.layout.activity_hardware);
    }

    public void testbattery(View view) {
        Intent it1 = new Intent(this, battery.class);
        it1.putExtra("passimage", R.drawable.battery);
        it1.putExtra("passtext", "Battery");
        startActivity(it1);


    }

    public void testcharging(View view) {
        Intent it1 = new Intent(this, charging.class);
        it1.putExtra("passimage", R.drawable.charging);
        it1.putExtra("passtext", "Charging");
        startActivity(it1);
    }

    public void testvib(View view) {
        Intent it1 = new Intent(this, vibration.class);
        it1.putExtra("passimage", R.drawable.vibration);
        it1.putExtra("passtext", "Vibration");
        startActivity(it1);
    }

    public void testUSB(View view) {
        Intent it1 = new Intent(this, test.class);
        it1.putExtra("passimage", R.drawable.usb);
        it1.putExtra("passtext", "USB Port");
        startActivity(it1);
    }


}