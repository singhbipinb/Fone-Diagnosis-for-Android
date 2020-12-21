package com.kashisol.fonediagnosis;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class multitouch extends AppCompatActivity implements MultiTouchCanvas.MultiTouchStatusListener {

    TextView tv;
    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Multi touch");
        setContentView(R.layout.activity_multitouch);
        tv = findViewById(R.id.touchnum);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        ((MultiTouchCanvas) findViewById(R.id.multiTouchView)).setStatusListener(this);

    }

    @Override
    public void onStatus(List<Point> pointerLocations, int numPoints) {
        String str = String.format(getResources().getString(R.string.num_touches), Integer.toString(numPoints));
        tv.setText(str);

        if (helper.searchName("Multitouch", database) == 0) {
            helper.insertData("Multitouch", "Touch Detected: " + numPoints, "Pass", database);
        } else {
            helper.updateData(helper.searchName("Multitouch", database), "Touch Detected: " + numPoints, "Pass", database);
        }

    }

}
