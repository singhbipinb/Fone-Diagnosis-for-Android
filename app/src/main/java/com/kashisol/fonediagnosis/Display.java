package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Display extends AppCompatActivity {

    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setTitle("Display");
    }

    public void testmultitouch(View view) {
        Intent it1 = new Intent(this, multitouch.class);
        it1.putExtra("passimage", R.drawable.multitouch);
        it1.putExtra("passtext", "Multi Touch");
        startActivity(it1);
    }

    public void testotuchsen(View view) {
        Intent it1 = new Intent(this, test.class);
        it1.putExtra("passimage", R.drawable.touch);
        it1.putExtra("passtext", "Touch Response");
        startActivity(it1);
    }
}