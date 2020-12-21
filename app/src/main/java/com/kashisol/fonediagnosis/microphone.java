package com.kashisol.fonediagnosis;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class microphone extends AppCompatActivity {

    ImageView img;
    TextView tv, tv2;
    MySQLHelper helper;
    SQLiteDatabase database;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Microphone");
        setContentView(R.layout.activity_microphone);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        img = findViewById(R.id.resimgmic);
        tv = findViewById(R.id.restextmic);
        tv2 = findViewById(R.id.textresmic);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen();
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }

                    tts.speak("What's your name?", TextToSpeech.QUEUE_FLUSH, null, null);


                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }

    private void listen() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(microphone.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);

                if (inSpeech.contains("my name is")) {

                    findViewById(R.id.llbut).setVisibility(View.INVISIBLE);
                    tv2.setText("Test Complete");
                    img.setImageResource(R.drawable.fpsuccess);
                    tv.setText("Microphone Working Fine");

                    if (helper.searchName("Microphone", database) == 0) {
                        helper.insertData("Microphone", "Working", "Pass", database);
                    } else {
                        helper.updateData(helper.searchName("Microphone", database), "Working", "Pass", database);
                    }

                } else {
                    findViewById(R.id.llbut).setVisibility(View.INVISIBLE);
                    tv2.setText("Test Failed");
                    img.setImageResource(R.drawable.fpfail);
                    tv.setText("Microphone not Working");
                    if (helper.searchName("Microphone", database) == 0) {
                        helper.insertData("Microphone", "NA", "Fail", database);
                    } else {
                        helper.updateData(helper.searchName("Microphone", database), "NA", "Fail", database);
                    }
                }
            }
        }
    }
}