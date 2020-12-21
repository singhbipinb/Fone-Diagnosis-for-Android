package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Sensors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        setTitle("Sensors");
    }

    public void testaccel(View view) {
        Intent it1 = new Intent(this, accelerometer.class);
        it1.putExtra("passimage", R.drawable.accelerometer_icon_15);
        it1.putExtra("passtext", "Accelerometer");
        startActivity(it1);
    }

    public void testgyro(View view) {
        Intent it1 = new Intent(this, gyroscopesensor.class);
        it1.putExtra("passimage", R.drawable.gyroscope);
        it1.putExtra("passtext", "Gyroscope");
        startActivity(it1);
    }

    public void testfp(View view) {
        Intent it1 = new Intent(this, fingerprint.class);
        it1.putExtra("passimage", R.drawable.fingerprint);
        it1.putExtra("passtext", "FingerPrint Scanner");
        startActivity(it1);
    }

    public void testproximity(View view) {
        Intent it1 = new Intent(this, proximitysensor.class);
        it1.putExtra("passimage", R.drawable.icon_proximity_grey_outlined);
        it1.putExtra("passtext", "Proximity Sensor");
        startActivity(it1);
    }


    public void testlight(View view) {
        Intent it1 = new Intent(this, lightsensor.class);
        it1.putExtra("passimage", R.drawable.light);
        it1.putExtra("passtext", "Light Sensor");
        startActivity(it1);
    }

    public void testcompass(View view) {
        Intent it1 = new Intent(this, magneticsensor.class);
        it1.putExtra("passimage", R.drawable.compass);
        it1.putExtra("passtext", "Compass");
        startActivity(it1);
    }

    public void testrota(View view) {
        Intent it1 = new Intent(this, rotationsensor.class);
        startActivity(it1);
    }

    public void testgrav(View view) {
        Intent it1 = new Intent(this, gravity.class);
        startActivity(it1);
    }

    public void testorien(View view) {
        Intent it1 = new Intent(this, orientation.class);
        startActivity(it1);
    }

    public void teststepdetect(View view) {
        Intent it1 = new Intent(this, stepdetectsensor.class);
        startActivity(it1);
    }

    public void teststepcount(View view) {
        Intent it1 = new Intent(this, stepcountsensor.class);
        startActivity(it1);
    }
}