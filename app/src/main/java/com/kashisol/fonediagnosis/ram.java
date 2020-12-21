package com.kashisol.fonediagnosis;

import android.app.ActivityManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Timer;
import java.util.TimerTask;

public class ram extends AppCompatActivity {

    TextView tvtot, tvavail, tvused;
    ArcProgress pb;
    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("RAM");
        setContentView(R.layout.activity_ram);
        tvtot = findViewById(R.id.tvtotram);
        tvavail = findViewById(R.id.tvavailram);
        tvused = findViewById(R.id.tvusedram);
        pb = findViewById(R.id.pbar1);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(mi);
                final double availram = mi.availMem / 0x100000L; //0x100000 = 1048576 Bytes
                final double totalram = mi.totalMem / 0x100000L;
                final double usedram = totalram - availram;


                int percentused = (int) ((mi.totalMem - mi.availMem) * 100.0 / mi.totalMem);
                pb.setProgress(percentused);
                pb.setBottomText("Memory");
                pb.setFinishedStrokeColor(Color.parseColor("#000000"));
                pb.setUnfinishedStrokeColor(Color.parseColor("#ffffff"));
                pb.setStrokeWidth(40);
                pb.setBottomTextSize(100);
                pb.refreshDrawableState();

                pb.setTextColor(Color.parseColor("#000000"));
                if (helper.searchName("RAM", database) == 0) {
                    helper.insertData("RAM", totalram + " MB", "Pass", database);
                } else {
                    helper.updateData(helper.searchName("RAM", database), totalram + " MB", "Pass", database);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvtot.setText(String.valueOf(totalram) + " MB");
                        tvused.setText(String.valueOf(usedram) + " MB");
                        tvavail.setText(String.valueOf(availram) + " MB");
                    }
                });

            }
        }, 0, 1000);

    }


}