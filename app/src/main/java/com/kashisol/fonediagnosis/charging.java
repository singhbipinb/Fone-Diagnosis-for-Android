package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;


public class charging extends AppCompatActivity {

//    https://source.android.com/devices/tech/power/device

    TextView tv1, tv2;
    TextView tv8, tv9, tv10, tv11;
    ImageView stateper, staticstate;
    MySQLHelper helper;
    SQLiteDatabase database;

    //    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Charging");
        setContentView(R.layout.activity_charging);

        tv1 = findViewById(R.id.tvstatus);
        tv2 = findViewById(R.id.tvps);

        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();

        tv8 = findViewById(R.id.textView8);
        tv9 = findViewById(R.id.textView9);
        tv10 = findViewById(R.id.textView10);
        tv11 = findViewById(R.id.textView11);
        stateper = findViewById(R.id.pbarcharge);
        staticstate = findViewById(R.id.pbarcharge2);
        stateper.setVisibility(View.INVISIBLE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = registerReceiver(null, ifilter);

                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                final float batteryPct = level * 100 / (float) scale;


                // Are we charging / charged?
                final int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean chStat = status == BatteryManager.BATTERY_STATUS_CHARGING;
                boolean dcStat = status == BatteryManager.BATTERY_STATUS_DISCHARGING;
                boolean fullStat = status == BatteryManager.BATTERY_STATUS_FULL;
                boolean nchStat = status == BatteryManager.BATTERY_STATUS_NOT_CHARGING;
                boolean unStat = status == BatteryManager.BATTERY_STATUS_UNKNOWN;

//                switch (status){
//
//                    case 1:tv1.setText("Unknown");
//                        break;
//                    case 2:tv1.setText("Charging");
//                        break;
//                    case 3:tv1.setText("Discharging");
//                    case 4:tv1.setText("Not Charging");
//                        break;
//                    case 5:tv1.setText("Full");
//                        break;
//                }

//        int stasa=batteryStatus.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
//        iv.setImageResource(stasa);
//        Toast t = Toast.makeText(this,String.valueOf(stasa),Toast.LENGTH_LONG);
//        t.show();

// How are we charging?
                final int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                boolean wlCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;


//                switch (chargePlug){
//                    case 0:tv2.setText("Battery");
//                        break;
//                    case 1:tv2.setText("AC Adapter");
//                        break;
//                    case 2:tv2.setText("USB Port");
//                        break;
//                    case 3:tv2.setText("Wireless");
//
//                }


                BatteryManager btm = (BatteryManager) getSystemService(BATTERY_SERVICE);
                final long cu = btm.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);

                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {

                    if (helper.searchName("Charging", database) == 0) {
                        helper.insertData("Charging", "Yes", "Pass", database);
                    } else {
                        helper.updateData(helper.searchName("Charging", database), "Yes", "Pass", database);
                    }
                } else {
                    if (helper.searchName("Charging", database) == 0) {
                        helper.insertData("Charging", "No", "Fail", database);
                    } else {
                        helper.updateData(helper.searchName("Charging", database), "No", "Fail", database);
                    }
                }


//                tv8.setText("Current : " + String.valueOf(cu) + " mA");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        switch (status) {

                            case 1:
                                tv1.setText("Unknown");
                                break;
                            case 2:
                                tv1.setText("Charging");
                                break;
                            case 3:
                                tv1.setText("Discharging");
                            case 4:
                                tv1.setText("Not Charging");
                                break;
                            case 5:
                                tv1.setText("Full");
                                break;
                        }

                        switch (chargePlug) {
                            case 0:
                                tv2.setText("Battery");
                                break;
                            case 1:
                                tv2.setText("AC Adapter");
                                break;
                            case 2:
                                tv2.setText("USB Port");
                                break;
                            case 3:
                                tv2.setText("Wireless");

                        }

                        AnimationDrawable frameAnimation = new AnimationDrawable();

                        if (chargePlug == 1 || chargePlug == 2 || chargePlug == 3) {

                            if (staticstate.getDrawable() != null) {
                                staticstate.setImageDrawable(null);
                            }
                            if (staticstate.getDrawable() == null) {
                                stateper.setVisibility(View.VISIBLE);
                            }
//                    stateper.setVisibility(View.VISIBLE);
                            Log.e("ANime", "" + frameAnimation.isRunning());
                            stateper.setBackgroundResource(R.drawable.spin_animation);
                            frameAnimation = (AnimationDrawable) stateper.getBackground();
                            frameAnimation.start();
                        }
//                if (chargePlug==-1 ||chargePlug==0)
                        else {

                            if (frameAnimation.isRunning()) {
                                frameAnimation.stop();
                                stateper.setImageDrawable(null);
                                stateper.setVisibility(View.INVISIBLE);
                            }
                            Log.e("ANime", "" + frameAnimation.isRunning());

                            frameAnimation.setVisible(false, false);
//
                            frameAnimation.stop();
                            Log.e("ANime", "" + frameAnimation.isRunning());
                            if (batteryPct <= 20) {
                                staticstate.setImageResource(R.drawable.batterystatempty);
                                frameAnimation.stop();
                                stateper.setImageDrawable(null);
                            } else if (batteryPct <= 40 && batteryPct > 20) {
                                staticstate.setImageResource(R.drawable.batterystate40);
                                frameAnimation.stop();
                                stateper.setImageDrawable(null);
                            } else if (batteryPct <= 60 && batteryPct > 40) {
                                staticstate.setImageResource(R.drawable.batterystate60);
                                frameAnimation.stop();
                                stateper.setImageDrawable(null);
                            } else if (batteryPct <= 80 && batteryPct > 60) {
                                staticstate.setImageResource(R.drawable.batterystate80);
                                frameAnimation.stop();
                                stateper.setImageDrawable(null);
                                Log.e("ANime", "" + frameAnimation.isRunning());
                            } else if (batteryPct <= 100 && batteryPct > 80) {
                                staticstate.setImageResource(R.drawable.batterystatefull);
                                frameAnimation.stop();
                                stateper.setImageDrawable(null);
                            }

                        }
//                tv8.setText("Current : " + String.valueOf(cu) + " mA");

                    }
                });
            }
        }, 0, 1000);
//        iv = findViewById(R.id.ivchar);


    }

}