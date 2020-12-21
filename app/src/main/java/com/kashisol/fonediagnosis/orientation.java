package com.kashisol.fonediagnosis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class orientation extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    TextView tv3, tv4, tv5;
    MySQLHelper helper;
    SQLiteDatabase database;
    float[] accel;
    float[] magnet;
    private Sensor accelerometer, magnetometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Orientation");
        setContentView(R.layout.activity_orientation);

        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        tv3 = findViewById(R.id.tvorien3);
        tv4 = findViewById(R.id.tvorien4);
        tv5 = findViewById(R.id.tvorien5);


        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    protected void onResume() {
        super.onResume();
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accel = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnet = event.values;
        }

        if (accel != null && magnet != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, accel, magnet)) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                tv3.setText(String.valueOf(orientation[0]));
                tv4.setText(String.valueOf(orientation[1]));
                tv5.setText(String.valueOf(orientation[2]));

                if (helper.searchName("Orientation", database) == 0) {
                    helper.insertData("Orientation", "" + orientation[0] + ", " + orientation[1] + ", " + orientation[2], "Pass", database);
                } else {
                    helper.updateData(helper.searchName("Orientation", database), "" + orientation[0] + ", " + orientation[1] + ", " + orientation[2], "Pass", database);
                }


            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}