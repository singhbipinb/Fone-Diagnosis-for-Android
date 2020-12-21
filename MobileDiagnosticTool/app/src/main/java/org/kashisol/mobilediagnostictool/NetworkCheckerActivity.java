package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class NetworkCheckerActivity extends AppCompatActivity {

    private Button check_internet_speed_button;
    private TextView aeroplane_mode_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_checker);

        // initialize widgets
        check_internet_speed_button = findViewById(R.id.check_internet_speed_button);
        aeroplane_mode_field = findViewById(R.id.aeroplane_mode_field);

        check_internet_speed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://fast.com"));
                startActivity(intent);
            }
        });

        Timer timer = new Timer();;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Run here to update values
                        isAirplaneModeOn();
                        getSignalStatus();
                    }
                });
            }
        },0, 500);
    }

    private void isAirplaneModeOn() {
        boolean aeroplaneMode = Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        aeroplane_mode_field.setText("AEROPLANE MODE : " + aeroplaneMode);
    }

    private void getSignalStatus() {
        TelephonyManager mTelephonyManager;
        MyPhoneStateListener mPhoneStatelistener;
        mPhoneStatelistener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int signalStrengthInt1 = signalStrength.getGsmSignalStrength();
            int signalStrengthInt2 = (2 * signalStrengthInt1) - 113;
            System.out.println("()()()() SIGNAL_STR 1 : " + signalStrengthInt1);
            System.out.println("()()()() SIGNAL_STR 2 : " + signalStrengthInt2);
        }
    }

}