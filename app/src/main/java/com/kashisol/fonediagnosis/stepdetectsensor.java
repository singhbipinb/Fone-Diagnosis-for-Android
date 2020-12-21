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

public class stepdetectsensor extends AppCompatActivity implements SensorEventListener {

    SensorManager manager;
    TextView tv1, tv2, tv6, tv7, tv8, tv9, tv10;
    ScrollView sc;
    CardView cv;
    MySQLHelper helper;
    SQLiteDatabase database;
    private Sensor stepdetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Step Detector");
        setContentView(R.layout.activity_stepdetectsensor);
        tv1 = findViewById(R.id.tvstepdetect1);
        tv2 = findViewById(R.id.tvstepdetect2);
        tv6 = findViewById(R.id.tvstepdetect6);
        tv7 = findViewById(R.id.tvstepdetect7);
        tv8 = findViewById(R.id.tvstepdetect8);
        tv9 = findViewById(R.id.tvstepdetect9);
        tv10 = findViewById(R.id.tvstepdetect10);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        sc = findViewById(R.id.scvsd);
        cv = findViewById(R.id.cardnasd);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {

            stepdetect = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            manager.registerListener(this, stepdetect, SensorManager.SENSOR_DELAY_NORMAL);
            cv.setVisibility(View.INVISIBLE);
            sc.setVisibility(View.VISIBLE);


        } else {
            sc.setVisibility(View.INVISIBLE);
            cv.setVisibility(View.VISIBLE);
            if (helper.searchName("Step Detector", database) == 0) {
                helper.insertData("Step Detector", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Step Detector", database), "NA", "Fail", database);
            }
        }


    }

    protected void onResume() {
        super.onResume();
        manager.registerListener(this, stepdetect, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;

        tv1.setText(event.sensor.getName());
        tv2.setText(String.valueOf(event.sensor.getMaximumRange()));
        tv6.setText(String.valueOf(event.sensor.getResolution()));
        tv7.setText(String.valueOf(event.sensor.getPower() + " mA"));

        if (helper.searchName("Step Detector", database) == 0) {
            helper.insertData("Step Detector", "Present", "Pass", database);
        } else {
            helper.updateData(helper.searchName("Step Detector", database), "Present", "Pass", database);
        }

//        Toast.makeText(this,event.sensor.getName()+"Test TOast",Toast.LENGTH_LONG).show();

        tv8.setText(String.valueOf(event.sensor.getMinDelay() + " microsecond"));
        tv9.setText(String.valueOf(event.sensor.getVendor()));
        tv10.setText(String.valueOf(event.sensor.getVersion()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}