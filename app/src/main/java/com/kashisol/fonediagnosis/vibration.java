package com.kashisol.fonediagnosis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class vibration extends AppCompatActivity {

    Button btnvib, btnvibcheck;
    GridLayout sc;
    ImageView img;
    TextView tv, tv2;
    EditText et;
    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Vibration");
        setContentView(R.layout.activity_vibration);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        btnvib = findViewById(R.id.buttonvib);
        btnvibcheck = findViewById(R.id.buttonvibcheck);
        sc = findViewById(R.id.gd1vib);
        img = findViewById(R.id.resimgvib);
        tv = findViewById(R.id.restextvib);
        tv2 = findViewById(R.id.textresvib);
        et = findViewById(R.id.valuevib);
    }

    public void vibclick(View view) {

        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnvib.setVisibility(View.INVISIBLE);
        sc.setVisibility(View.VISIBLE);

        long pattern[] = {0, 500, 200, 500, 200, 500};
        vb.vibrate(pattern, -1);

        Toast.makeText(this, "Vibrating", Toast.LENGTH_LONG).show();

    }

    public void anssub(View view) {

        if (et.getText().toString().matches("")) {
            Toast.makeText(this, "Please input first", Toast.LENGTH_LONG).show();
        } else {
            int vib = Integer.parseInt(String.valueOf(et.getText()));
            if (vib == 3) {
                sc.setVisibility(View.INVISIBLE);
                tv2.setText("Test Complete");
                img.setImageResource(R.drawable.fpsuccess);
                tv.setText("Vibrator Working Fine");

                if (helper.searchName("Vibration", database) == 0) {
                    helper.insertData("Vibration", "Working", "Pass", database);
                } else {
                    helper.updateData(helper.searchName("Vibration", database), "Working", "Pass", database);
                }

            } else {
                sc.setVisibility(View.INVISIBLE);
                tv2.setText("Test Failed");
                img.setImageResource(R.drawable.fpfail);
                tv.setText("Vibrator Not Working");
                if (helper.searchName("Vibration", database) == 0) {
                    helper.insertData("Vibration", "NA", "Fail", database);
                } else {
                    helper.updateData(helper.searchName("Vibration", database), "NA", "Fail", database);
                }
            }
        }
    }


}
