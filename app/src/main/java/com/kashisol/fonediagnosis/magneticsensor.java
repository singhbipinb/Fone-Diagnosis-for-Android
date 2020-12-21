package com.kashisol.fonediagnosis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class magneticsensor extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    ScrollView sc;
    CardView cv;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
    MySQLHelper helper;
    SQLiteDatabase database;
    private Sensor magnetometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Magnetometer");
        setContentView(R.layout.activity_magneticsensor);
        tv1 = findViewById(R.id.tvmagnet1);
        tv2 = findViewById(R.id.tvmagnet2);
        tv3 = findViewById(R.id.tvmagnet3);
        tv4 = findViewById(R.id.tvmagnet4);
        tv5 = findViewById(R.id.tvmagnet5);
        tv6 = findViewById(R.id.tvmagnet6);
        tv7 = findViewById(R.id.tvmagnet7);
        tv8 = findViewById(R.id.tvmagnet8);
        tv9 = findViewById(R.id.tvmagnet9);
        tv10 = findViewById(R.id.tvmagnet10);
        sc = findViewById(R.id.magsc);
        cv = findViewById(R.id.cardnascmag);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {

            magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

            cv.setVisibility(View.INVISIBLE);
            sc.setVisibility(View.VISIBLE);


        } else {
            sc.setVisibility(View.INVISIBLE);
            cv.setVisibility(View.VISIBLE);
            if (helper.searchName("Magnetometer", database) == 0) {
                helper.insertData("Magnetometer", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Magnetometer", database), "NA", "Fail", database);
            }
        }


    }

    protected void onResume() {
        super.onResume();
        manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        float xvalue = event.values[0];
        float yvalue = event.values[1];
        float zvalue = event.values[2];

        tv1.setText(event.sensor.getName());
        tv2.setText(String.valueOf(event.sensor.getMaximumRange() + " \u00B5T"));
        tv6.setText(String.valueOf(event.sensor.getResolution() + " \u00B5T"));
        tv7.setText(String.valueOf(event.sensor.getPower() + " mA"));

        tv3.setText(xvalue + " \u00B5T");
        tv4.setText(yvalue + " \u00B5T");
        tv5.setText(zvalue + " \u00B5T");

        if (helper.searchName("Magnetometer", database) == 0) {
            helper.insertData("Magnetometer", "" + xvalue + ", " + yvalue + ", " + zvalue, "Pass", database);
        } else {
            helper.updateData(helper.searchName("Magnetometer", database), "" + xvalue + ", " + yvalue + ", " + zvalue, "Pass", database);
        }

        tv8.setText(String.valueOf(event.sensor.getMinDelay() + " microsecond"));
        tv9.setText(String.valueOf(event.sensor.getVendor()));
        tv10.setText(String.valueOf(event.sensor.getVersion()));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}