package com.kashisol.fonediagnosis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class audiojack extends AppCompatActivity {

    TextView s;
    MySQLHelper helper;
    SQLiteDatabase database;
    private MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Audio Jack");
        setContentView(R.layout.activity_audiojack);
        s = (TextView) findViewById(R.id.restextjack);
        myReceiver = new MusicIntentReceiver();

        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();


    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }

    @Override

    public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        s.setText("Headset is unplugged");
                        break;
                    case 1:
                        s.setText("Headset is plugged");
                        break;
                    default:
                        s.setText("Plugin the headset");
                }

                if (helper.searchName("Audio Jack", database) == 0) {
                    helper.insertData("Audio Jack", "NA", "Pass", database);
                } else {
                    helper.updateData(helper.searchName("Audio Jack", database), "NA", "Pass", database);
                }

            }
        }
    }

}