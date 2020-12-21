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

public class rotationsensor extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    ScrollView sc;
    CardView cv;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
    MySQLHelper helper;
    SQLiteDatabase database;
    private Sensor rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rotation");
        setContentView(R.layout.activity_rotationsensor);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        tv1 = findViewById(R.id.tvrota1);
        tv2 = findViewById(R.id.tvrota2);
        tv3 = findViewById(R.id.tvrota3);
        tv4 = findViewById(R.id.tvrota4);
        tv5 = findViewById(R.id.tvrota5);
        tv6 = findViewById(R.id.tvrota6);
        tv7 = findViewById(R.id.tvrota7);
        tv8 = findViewById(R.id.tvrota8);
        tv9 = findViewById(R.id.tvrota9);
        tv10 = findViewById(R.id.tvrota10);
        sc = findViewById(R.id.rotsc);
        cv = findViewById(R.id.cardnascrot);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {

            rotation = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            manager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);


            cv.setVisibility(View.INVISIBLE);
            sc.setVisibility(View.VISIBLE);


        } else {
            sc.setVisibility(View.INVISIBLE);
            cv.setVisibility(View.VISIBLE);
            if (helper.searchName("Rotation Vector", database) == 0) {
                helper.insertData("Rotation Vector", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Rotation Vector", database), "NA", "Fail", database);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        manager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xvalue = event.values[0];
        float yvalue = event.values[0];
        float zvalue = event.values[0];

        tv1.setText(event.sensor.getName());
        tv2.setText(String.valueOf(event.sensor.getMaximumRange() + " \u00b0"));
        tv6.setText(String.valueOf(event.sensor.getResolution() + " \u00b0"));
        tv7.setText(String.valueOf(event.sensor.getPower() + " mA"));

        tv3.setText("" + xvalue);
        tv4.setText("" + yvalue);
        tv5.setText("" + zvalue);
        if (helper.searchName("Rotation Vector", database) == 0) {
            helper.insertData("Rotation Vector", "" + xvalue + ", " + yvalue + ", " + zvalue, "Pass", database);
        } else {
            helper.updateData(helper.searchName("Rotation Vector", database), "" + xvalue + ", " + yvalue + ", " + zvalue, "Pass", database);
        }
        tv8.setText(String.valueOf(event.sensor.getMinDelay() + " microsecond"));
        tv9.setText(String.valueOf(event.sensor.getVendor()));
        tv10.setText(String.valueOf(event.sensor.getVersion()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}