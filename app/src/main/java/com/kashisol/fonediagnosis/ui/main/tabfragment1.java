package com.kashisol.fonediagnosis.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.kashisol.fonediagnosis.MySQLHelper;
import com.kashisol.fonediagnosis.R;


public class tabfragment1 extends Fragment {

    static final int PERMISSION_READ_STATE = 200;
    public static TextView tv6;
    public TextView tv1, tv3, tv4, tv5, tv7, tv8, tv9;
    String strphonetype = "";
    String netison = "";
    String nettype = "", netvoicetype = "", servicestate = "";
    int oldstrength = 0;

    MySQLHelper helper;
    SQLiteDatabase database;

    TelephonyManager telephonyManager;
    MyPhoneStateListener psListener;
    ServiceState servstate = new ServiceState();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_tabfragment1, container, false);
        tv1 = v.findViewById(R.id.tvsim10);
        tv3 = v.findViewById(R.id.tvsim12);
        tv4 = v.findViewById(R.id.tvsim19);
        tv5 = v.findViewById(R.id.tvsim14);
        tv6 = v.findViewById(R.id.tvsim15);
        tv7 = v.findViewById(R.id.tvsim16);
        tv8 = v.findViewById(R.id.tvsim17);
        tv9 = v.findViewById(R.id.tvsim18);
        psListener = new MyPhoneStateListener();
        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        helper = new MySQLHelper(getContext());
        database = helper.getWritableDatabase();
        mytelco();

        return v;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READ_STATE:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mytelco();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
        }
    }


    public void mytelco() {
        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        int phonetype = manager.getPhoneType();

        switch (phonetype) {

            case (TelephonyManager.PHONE_TYPE_CDMA):
                strphonetype = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                strphonetype = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                strphonetype = "None";
                break;

        }

        boolean isRoaming = manager.isNetworkRoaming();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

            return;
        }
        String imei = manager.getDeviceId(1);

        String simserial = manager.getSimSerialNumber();
        String networkcoutryiso = manager.getNetworkCountryIso();
        String simcountryiso = manager.getSimCountryIso();
//        int nettype = manager.getNetworkType();
        String carrier = (String) manager.getNetworkOperatorName();


        int netstatus = manager.getDataState();
        switch (netstatus) {
            case (TelephonyManager.DATA_CONNECTED):
                netison = "Connected";
                break;
            case (TelephonyManager.DATA_CONNECTING):
                netison = "Connecting";
                break;
            case (TelephonyManager.DATA_DISCONNECTED):
                netison = "Disconnected";
                break;
        }


        int getnettype = manager.getDataNetworkType();
        switch (getnettype) {
            case (TelephonyManager.NETWORK_TYPE_HSDPA):
                nettype = "3G";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPAP):
                nettype = "4G";
                break;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                nettype = "GPRS";
                break;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                nettype = "2G";
                break;
            case (TelephonyManager.NETWORK_TYPE_LTE):
                nettype = "4G";
                break;


        }

//
//        oldstrength = signalStrength.getGsmSignalStrength();
//        oldstrength = (2 * oldstrength) - 113; //dbm

        int getvoicenettype = manager.getVoiceNetworkType();
        switch (getvoicenettype) {
            case (TelephonyManager.NETWORK_TYPE_HSDPA):
                netvoicetype = "3G";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPAP):
            case (TelephonyManager.NETWORK_TYPE_LTE):
                netvoicetype = "4G";
                break;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                netvoicetype = "GPRS";
                break;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                netvoicetype = "2G";
                break;
        }


        tv1.setText(carrier);
        tv3.setText(netison);
        tv4.setText(simserial);
        tv5.setText(networkcoutryiso.toUpperCase());
//        tv6.setText("" + "dBm");
        SignalStrength signalStrength;
//        psListener.onSignalStrengthsChanged( signalStrength);
        tv7.setText(netvoicetype);
        tv8.setText(nettype);

        if (isRoaming) {
            tv9.setText("Roaming");
        } else {
            tv9.setText("Not Roaming");
        }

        if (helper.searchName("Sim 1", database) == 0) {
            helper.insertData("Sim 1", carrier, "Pass", database);
        } else {
            helper.updateData(helper.searchName("Sim 1", database), carrier, "Pass", database);
        }


    }

}

class MyPhoneStateListener extends PhoneStateListener {
    int signalStrengthInt2;

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        int signalStrengthInt1 = signalStrength.getGsmSignalStrength();
        signalStrengthInt2 = (2 * signalStrengthInt1) - 113;

        tabfragment1.tv6.setText("-" + signalStrengthInt2 + " dBm");
    }

}
