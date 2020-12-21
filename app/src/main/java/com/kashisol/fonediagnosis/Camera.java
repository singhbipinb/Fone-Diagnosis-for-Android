package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Camera extends AppCompatActivity {

    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setTitle("Camera");
    }

    public void testrearcam(View view) {

        Intent it1 = new Intent(this, rearcam.class);
        it1.putExtra("passimage", R.drawable.rearcam);
        it1.putExtra("passtext", "Rear Camera");
        startActivity(it1);

    }

    public void testfrontcam(View view) {
        Intent it2 = new Intent(this, frontcam.class);
        it2.putExtra("passimage", R.drawable.frontcam);
        it2.putExtra("passtext", "Front Camera");
        startActivity(it2);
    }

    public void testflashlight(View view) {
        Intent it2 = new Intent(this, flashlight.class);

        startActivity(it2);
    }
}