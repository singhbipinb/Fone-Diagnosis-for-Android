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

public class stepcountsensor extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    TextView tv1, tv2, tv3, tv6, tv7, tv8, tv9, tv10;
    ScrollView sc;
    CardView cv;
    MySQLHelper helper;
    SQLiteDatabase database;
    private Sensor stepcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Step Counter");
        setContentView(R.layout.activity_stepcountsensor);
        tv1 = findViewById(R.id.tvstepcount1);
        tv2 = findViewById(R.id.tvstepcount2);
        tv3 = findViewById(R.id.tvstepcount3);
        tv6 = findViewById(R.id.tvstepcount6);
        tv7 = findViewById(R.id.tvstepcount7);
        tv8 = findViewById(R.id.tvstepcount8);
        tv9 = findViewById(R.id.tvstepcount9);
        tv10 = findViewById(R.id.tvstepcount10);
        sc = findViewById(R.id.scsc);
        cv = findViewById(R.id.cardnasc);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {

            stepcount = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            manager.registerListener(this, stepcount, SensorManager.SENSOR_DELAY_NORMAL);
            cv.setVisibility(View.INVISIBLE);
            sc.setVisibility(View.VISIBLE);


        } else {
            sc.setVisibility(View.INVISIBLE);
            cv.setVisibility(View.VISIBLE);
            if (helper.searchName("Step Counter", database) == 0) {
                helper.insertData("Step Counter", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Step Counter", database), "NA", "Fail", database);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        manager.registerListener(this, stepcount, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float xvalue = event.values[0];
        tv1.setText(event.sensor.getName());
        tv2.setText(String.valueOf(event.sensor.getMaximumRange()));
        tv6.setText(String.valueOf(event.sensor.getResolution()));
        tv7.setText(String.valueOf(event.sensor.getPower() + " mA"));
        tv3.setText(String.valueOf(xvalue));
        tv8.setText(String.valueOf(event.sensor.getMinDelay() + " microsecond"));
        tv9.setText(String.valueOf(event.sensor.getVendor()));
        tv10.setText(String.valueOf(event.sensor.getVersion()));

        if (helper.searchName("Step Counter", database) == 0) {
            helper.insertData("Step Counter", "Present", "Pass", database);
        } else {
            helper.updateData(helper.searchName("Step Counter", database), "Present", "Pass", database);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}