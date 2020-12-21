package com.kashisol.fonediagnosis;

import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class speaker extends AppCompatActivity {

    Button btnplay, btnyes, btnno;
    GridLayout sc;
    MediaPlayer mp;
    ImageView img;
    TextView tv, tv2;
    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Speaker");
        setContentView(R.layout.activity_speaker);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        btnplay = findViewById(R.id.button);
        btnyes = findViewById(R.id.button3);
        btnno = findViewById(R.id.button4);
        sc = findViewById(R.id.gd1sp);
        img = findViewById(R.id.resimg);
        tv = findViewById(R.id.restext);
        tv2 = findViewById(R.id.textres);

        mp = new MediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();

    }

    public void playaudio(View view) {

        String filename = "android.resource://" + this.getPackageName() + "/raw/music";

        try {
            mp.setDataSource(this, Uri.parse(filename));//Write your location here
            mp.prepare();
            mp.start();
            mp.setLooping(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnplay.setVisibility(View.INVISIBLE);
        sc.setVisibility(View.VISIBLE);


    }

    public void spyes(View view) {

        mp.stop();
        sc.setVisibility(View.INVISIBLE);
        tv2.setText("Test Complete");
        img.setImageResource(R.drawable.fpsuccess);
        tv.setText("Speakers working fine");

        if (helper.searchName("Speaker", database) == 0) {
            helper.insertData("Speaker", "Working", "Pass", database);
        } else {
            helper.updateData(helper.searchName("Speaker", database), "Working", "Pass", database);
        }

    }

    public void spno(View view) {

        mp.stop();
        sc.setVisibility(View.INVISIBLE);
        tv2.setText("Test Failed");
        img.setImageResource(R.drawable.fpfail);
        tv.setText("Speakers not working");
        if (helper.searchName("Speaker", database) == 0) {
            helper.insertData("Speaker", "NA", "Fail", database);
        } else {
            helper.updateData(helper.searchName("Speaker", database), "NA", "Fail", database);
        }

    }


}