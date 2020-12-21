package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Fone Master");

        helper = new MySQLHelper(this);
        database = helper.getReadableDatabase();


//        database.execSQL("delete from Test ");
    }


    public void funstart(View view) {

        Intent it = new Intent(this, home.class);
        startActivity(it);
    }
}