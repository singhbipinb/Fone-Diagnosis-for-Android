package com.kashisol.fonediagnosis;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class battery extends AppCompatActivity {

    //https://developer.android.com/reference/android/os/BatteryManager.html#BATTERY_PLUGGED_AC
//https://developer.android.com/training/monitoring-device-state/battery-monitoring
    TextView tvper, tvhealth, tvcap, tvtemp, tvtech, tvvolt;
    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Battery");
        setContentView(R.layout.activity_battery);

        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        tvper = findViewById(R.id.tvbattery);
        tvhealth = findViewById(R.id.tvhealth);
        tvcap = findViewById(R.id.tvcap);
        tvtemp = findViewById(R.id.tvtemp);
        tvtech = findViewById(R.id.tvtech);
        tvvolt = findViewById(R.id.tvvolt);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = registerReceiver(null, ifilter);

                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                final float batteryPct = level * 100 / (float) scale;
//                tvper.setText(String.valueOf(batteryPct)+" %");

                final int bhealth = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);

                boolean unknownhealth = bhealth == BatteryManager.BATTERY_HEALTH_UNKNOWN;
                boolean goodhealth = bhealth == BatteryManager.BATTERY_HEALTH_GOOD;
                boolean overheathealth = bhealth == BatteryManager.BATTERY_HEALTH_OVERHEAT;
                boolean deadhealth = bhealth == BatteryManager.BATTERY_HEALTH_DEAD;
                boolean ovhealth = bhealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE;
                boolean unfhealth = bhealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE;
                boolean coldhealth = bhealth == BatteryManager.BATTERY_HEALTH_COLD;

//                switch (bhealth){
//                    case 1:tvhealth.setText("Unknown");
//                        break;
//                    case 2:tvhealth.setText("Good");
//                        break;
//                    case 3:tvhealth.setText("Overheat");
//                        break;
//                    case 4:tvhealth.setText("Dead");
//                        break;
//                    case 5:tvhealth.setText("Over Voltage");
//                        break;
//                    case 6:tvhealth.setText("Unspecified Failure");
//                        break;
//                    case 7:tvhealth.setText("Cold");
//                        break;
//
//                }


                final double batterycapacity = getBatteryCapacity(getApplicationContext());

//                tvcap.setText(String.valueOf(batterycapacity) +" mAh");

                int temp = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

                final float batterytemp = temp / 10;
//                tvtemp.setText(String.valueOf(batterytemp)+" \u2103");

                final String tech = batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);


//                tvtech.setText(String.valueOf(tech));


                final int volt = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

//                tvvolt.setText(String.valueOf(volt)+" mV");

                if (helper.searchName("Battery", database) == 0) {
                    helper.insertData("Battery", "Health: " + bhealth, "Pass", database);
                } else {
                    helper.updateData(helper.searchName("Battery", database), "Health: " + bhealth, "Pass", database);
                }

                if (bhealth != BatteryManager.BATTERY_HEALTH_GOOD) {
                    if (helper.searchName("Battery", database) == 0) {
                        helper.insertData("Battery", "Health: " + bhealth, "Pass", database);
                    } else {
                        helper.updateData(helper.searchName("Battery", database), "Health: " + bhealth, "Pass", database);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tvper.setText(String.valueOf(batteryPct) + " %");

                        switch (bhealth) {
                            case 1:
                                tvhealth.setText("Unknown");
                                break;
                            case 2:
                                tvhealth.setText("Good");
                                break;
                            case 3:
                                tvhealth.setText("Overheat");
                                break;
                            case 4:
                                tvhealth.setText("Dead");
                                break;
                            case 5:
                                tvhealth.setText("Over Voltage");
                                break;
                            case 6:
                                tvhealth.setText("Unspecified Failure");
                                break;
                            case 7:
                                tvhealth.setText("Cold");
                                break;

                        }

                        tvcap.setText(String.valueOf(batterycapacity) + " mAh");
                        tvtemp.setText(String.valueOf(batterytemp) + " \u2103");
                        tvtech.setText(String.valueOf(tech));
                        tvvolt.setText(String.valueOf(volt) + " mV");

                    }
                });

            }
        }, 0, 1000);

    }

    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;

    }
}