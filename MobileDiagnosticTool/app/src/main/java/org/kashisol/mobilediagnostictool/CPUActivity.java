package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.kashisol.mobilediagnostictool.util.NewLocationCapture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CPUActivity extends AppCompatActivity {

    private TextView info_text_view_cpu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_p_u);


        // TODO to be included in the future versions
        try {
            String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder processBuilder = new ProcessBuilder(DATA);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            byte[] byteArry = new byte[1024];
            String output = "";
            while (inputStream.read(byteArry) != -1) {
                output = output + new String(byteArry);
            }
            inputStream.close();

            Log.d("()()()()", output);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        NewLocationCapture.mandatoryForGettingLocation(getApplicationContext(), CPUActivity.this);

    }

}