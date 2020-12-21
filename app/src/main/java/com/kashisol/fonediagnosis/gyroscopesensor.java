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

public class gyroscopesensor extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    ScrollView sc;
    CardView cv;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
    MySQLHelper helper;
    SQLiteDatabase database;
    private Sensor gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Gyroscope");
        setContentView(R.layout.activity_gyroscopesensor);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        tv1 = findViewById(R.id.tvgyro1);
        tv2 = findViewById(R.id.tvgyro2);
        tv3 = findViewById(R.id.tvgyro3);
        tv4 = findViewById(R.id.tvgyro4);
        tv5 = findViewById(R.id.tvgyro5);
        tv6 = findViewById(R.id.tvgyro6);
        tv7 = findViewById(R.id.tvgyro7);
        tv8 = findViewById(R.id.tvgyro8);
        tv9 = findViewById(R.id.tvgyro9);
        tv10 = findViewById(R.id.tvgyro10);
        sc = findViewById(R.id.gyrsc);
        cv = findViewById(R.id.cardnascgyr);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {

            gyroscope = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            manager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

            cv.setVisibility(View.INVISIBLE);
            sc.setVisibility(View.VISIBLE);


        } else {
            sc.setVisibility(View.INVISIBLE);
            cv.setVisibility(View.VISIBLE);
            if (helper.searchName("Gyroscope", database) == 0) {
                helper.insertData("Gyroscope", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Gyroscope", database), "NA", "Fail", database);
            }
        }


    }

    protected void onResume() {
        super.onResume();
        manager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
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
        tv2.setText(String.valueOf(event.sensor.getMaximumRange() + " rad/s"));
        tv6.setText(String.valueOf(event.sensor.getResolution() + " rad/s"));
        tv7.setText(String.valueOf(event.sensor.getPower() + " mA"));

        tv3.setText(xvalue + " rad/s");
        tv4.setText(yvalue + " rad/s");
        tv5.setText(zvalue + " rad/s");

        if (helper.searchName("Gyroscope", database) == 0) {
            helper.insertData("Gyroscope", "" + xvalue + ", " + yvalue + ", " + zvalue, "Pass", database);
        } else {
            helper.updateData(helper.searchName("Gyroscope", database), "" + xvalue + ", " + yvalue + ", " + zvalue, "Pass", database);
        }

        tv8.setText(String.valueOf(event.sensor.getMinDelay() + " microsecond"));
        tv9.setText(String.valueOf(event.sensor.getVendor()));
        tv10.setText(String.valueOf(event.sensor.getVersion()));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}