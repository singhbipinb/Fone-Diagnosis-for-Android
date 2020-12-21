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

public class lightsensor extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    ScrollView sc;
    CardView cv;
    TextView tv1, tv2, tv3, tv6, tv7, tv8, tv9, tv10;
    MySQLHelper helper;
    SQLiteDatabase database;
    private Sensor light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Light Sensor");
        setContentView(R.layout.activity_lightsensor);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        tv1 = findViewById(R.id.tvlight1);
        tv2 = findViewById(R.id.tvlight2);
        tv3 = findViewById(R.id.tvlight3);
        tv6 = findViewById(R.id.tvlight6);
        tv7 = findViewById(R.id.tvlight7);
        tv8 = findViewById(R.id.tvlight8);
        tv9 = findViewById(R.id.tvlight9);
        tv10 = findViewById(R.id.tvlight10);
        sc = findViewById(R.id.lisc);
        cv = findViewById(R.id.cardnascli);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (manager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {

            light = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
            manager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
            cv.setVisibility(View.INVISIBLE);
            sc.setVisibility(View.VISIBLE);


        } else {
            sc.setVisibility(View.INVISIBLE);
            cv.setVisibility(View.VISIBLE);
            if (helper.searchName("Light Sensor", database) == 0) {
                helper.insertData("Light Sensor", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Light Sensor", database), "NA", "Fail", database);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        manager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float xvalue = event.values[0];

        tv1.setText(event.sensor.getName());
        tv2.setText(String.valueOf(event.sensor.getMaximumRange() + " lux"));
        tv6.setText(String.valueOf(event.sensor.getResolution() + " lux"));
        tv7.setText(String.valueOf(event.sensor.getPower() + " mA"));

        tv3.setText(xvalue + " lux");

        if (helper.searchName("Light Sensor", database) == 0) {
            helper.insertData("Light Sensor", xvalue + " lux", "Pass", database);
        } else {
            helper.updateData(helper.searchName("Light Sensor", database), xvalue + " lux", "Pass", database);
        }


        tv8.setText(String.valueOf(event.sensor.getMinDelay() + " microsecond"));
        tv9.setText(String.valueOf(event.sensor.getVendor()));
        tv10.setText(String.valueOf(event.sensor.getVersion()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}