package com.kashisol.fonediagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Fone Master");
    }

    public void funcamera(View view) {

        Intent cam = new Intent(this, Camera.class);
        startActivity(cam);
    }

    public void funsensor(View view) {

        Intent sen = new Intent(this, Sensors.class);
        startActivity(sen);

    }

    public void fundisplay(View view) {
        Intent dis = new Intent(this, Display.class);
        startActivity(dis);
    }

    public void funaudio(View view) {
        Intent aud = new Intent(this, Audio.class);
        startActivity(aud);
    }

    public void funnetwork(View view) {
        Intent net = new Intent(this, Network.class);
        startActivity(net);
    }

    public void funmemory(View view) {
        Intent mem = new Intent(this, Memory.class);
        startActivity(mem);
    }

    public void funhardware(View view) {
        Intent it = new Intent(this, hardware.class);
        startActivity(it);
    }

    public void funinfo(View view) {
        Intent it = new Intent(this, phoneinfo.class);
        startActivity(it);

    }

    public void genreport(View view) {
        Intent it = new Intent(this, test.class);
        startActivity(it);
    }

}