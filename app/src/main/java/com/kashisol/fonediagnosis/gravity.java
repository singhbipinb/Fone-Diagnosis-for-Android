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

public class gravity extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    ScrollView sc;
    CardView cv;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
    MySQLHelper helper;
    SQLiteDatabase database;
    private Sensor gravity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Gravity Sensor");
        setContentView(R.layout.activity_gravity);

        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();

        tv1 = findViewById(R.id.tvgra1);
        tv2 = findViewById(R.id.tvgra2);
        tv3 = findViewById(R.id.tvgra3);
        tv4 = findViewById(R.id.tvgra4);
        tv5 = findViewById(R.id.tvgra5);
        tv6 = findViewById(R.id.tvgra6);
        tv7 = findViewById(R.id.tvgra7);
        tv8 = findViewById(R.id.tvgra8);
        tv9 = findViewById(R.id.tvgra9);
        tv10 = findViewById(R.id.tvgra10);
        sc = findViewById(R.id.progra);
        cv = findViewById(R.id.cardnascgra);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (manager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {

            gravity = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            manager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);

            cv.setVisibility(View.INVISIBLE);
            sc.setVisibility(View.VISIBLE);


        } else {
            sc.setVisibility(View.INVISIBLE);
            cv.setVisibility(View.VISIBLE);
            if (helper.searchName("Gravity Sensor", database) == 0) {
                helper.insertData("Gravity Sensor", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Gravity Sensor", database), "NA", "Fail", database);
            }
        }

    }

    protected void onResume() {
        super.onResume();
        manager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
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
        tv2.setText(String.valueOf(event.sensor.getMaximumRange() + " m/s\u00B2"));
        tv6.setText(String.valueOf(event.sensor.getResolution() + " m/s\u00B2"));
        tv7.setText(String.valueOf(event.sensor.getPower() + " mA"));

        tv3.setText(xvalue + " m/s\u00B2");
        tv4.setText(yvalue + " m/s\u00B2");
        tv5.setText(zvalue + " m/s\u00B2");

        if (helper.searchName("Gravity Sensor", database) == 0) {
            helper.insertData("Gravity Sensor", "X:" + xvalue + " Y:" + yvalue + "Z: " + zvalue, "Pass", database);
        } else {
            helper.updateData(helper.searchName("Gravity Sensor", database), "X:" + xvalue + " Y:" + yvalue + "Z: " + zvalue, "Pass", database);
        }

        tv8.setText(String.valueOf(event.sensor.getMinDelay() + " microsecond"));
        tv9.setText(String.valueOf(event.sensor.getVendor()));
        tv10.setText(String.valueOf(event.sensor.getVersion()));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}