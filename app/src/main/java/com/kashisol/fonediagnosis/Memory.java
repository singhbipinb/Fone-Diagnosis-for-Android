package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Memory extends AppCompatActivity {

    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        setTitle("Internals");
    }

    public void testram(View view) {
        Intent it1 = new Intent(this, ram.class);
        it1.putExtra("passimage", R.drawable.ram);
        it1.putExtra("passtext", "RAM");
        startActivity(it1);
    }

    public void teststorage(View view) {
        Intent it1 = new Intent(this, storage.class);
        it1.putExtra("passimage", R.drawable.storage);
        it1.putExtra("passtext", "Storage");
        startActivity(it1);
    }


    public void testcpu(View view) {
        Intent it1 = new Intent(this, cpu.class);
        it1.putExtra("passimage", R.drawable.cpu);
        it1.putExtra("passtext", "CPU");
        startActivity(it1);
    }


}