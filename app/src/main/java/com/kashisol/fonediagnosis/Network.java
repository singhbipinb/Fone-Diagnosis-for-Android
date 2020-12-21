package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Network extends AppCompatActivity {

    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        setTitle("Connectivity");
    }

    public void testnet(View view) {
        Intent it1 = new Intent(this, mobilenetwork.class);
        it1.putExtra("passimage", R.drawable.net);
        it1.putExtra("passtext", "Mobile network");
        startActivity(it1);
    }

    public void testwifi(View view) {
        Intent it1 = new Intent(this, wifistatus.class);
        it1.putExtra("passimage", R.drawable.wifi);
        it1.putExtra("passtext", "Wi-Fi");
        startActivity(it1);
    }

    public void testblue(View view) {
        Intent it1 = new Intent(this, bluetooth.class);
        it1.putExtra("passimage", R.drawable.bluetooth);
        it1.putExtra("passtext", "Bluetooth");
        startActivity(it1);
    }

    public void testgps(View view) {
        Intent it1 = new Intent(this, location.class);
        it1.putExtra("passimage", R.drawable.gps1);
        it1.putExtra("passtext", "GPS");
        startActivity(it1);
    }

}