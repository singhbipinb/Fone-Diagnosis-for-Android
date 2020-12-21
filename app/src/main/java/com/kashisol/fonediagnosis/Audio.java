package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Audio extends AppCompatActivity {

    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        setTitle("Audio");

    }

    public void testjack(View view) {
        Intent it1 = new Intent(this, audiojack.class);
        it1.putExtra("passimage", R.drawable.jack);
        it1.putExtra("passtext", "Audio Jack");
        startActivity(it1);
    }

    public void testmic(View view) {
        Intent it1 = new Intent(this, microphone.class);
        it1.putExtra("passimage", R.drawable.mic);
        it1.putExtra("passtext", "Microphone");
        startActivity(it1);
    }

    public void testspeaker(View view) {
        Intent it1 = new Intent(this, speaker.class);
        it1.putExtra("passimage", R.drawable.speaker);
        it1.putExtra("passtext", "Speakers");
        startActivity(it1);
    }
}