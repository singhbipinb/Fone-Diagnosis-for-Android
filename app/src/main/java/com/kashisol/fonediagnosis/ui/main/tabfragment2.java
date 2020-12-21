package com.kashisol.fonediagnosis.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kashisol.fonediagnosis.MySQLHelper;
import com.kashisol.fonediagnosis.R;

import java.lang.reflect.Method;

import static com.kashisol.fonediagnosis.ui.main.tabfragment1.PERMISSION_READ_STATE;

public class tabfragment2 extends Fragment {


    //    static final int PERMISSION_READ_STATE = 200;
    TextView tv1, tv3, tv4, tv5, tv6, tv7, tv8, tv9;
    String strphonetype = "";
    String netison = "";
    String nettype = "", netvoicetype = "", servicestate = "";
    CardView er;
    ScrollView sc;
    int phonetype;

    MySQLHelper helper;
    SQLiteDatabase database;
    //    int oldstrength=0;
//    //    SignalStrength signalStrength;
//    TelephonyManager telephonyManager;
//    mPhoneStateListener psListener;
//    ServiceState servstate= new ServiceState();
    int count = 0;

    private static String getOutput(Context context, String methodName, int slotId) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        String reflectionMethod = null;
        String output = null;
        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            for (Method method : telephonyClass.getMethods()) {
                String name = method.getName();
                if (name.contains(methodName)) {
                    Class<?>[] params = method.getParameterTypes();
                    if (params.length == 1 && params[0].getName().equals("int")) {
                        reflectionMethod = name;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (reflectionMethod != null) {
            try {
                output = getOpByReflection(telephony, reflectionMethod, slotId, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    private static String getOpByReflection(TelephonyManager telephony, String predictedMethodName, int slotID, boolean isPrivate) {

        //Log.i("Reflection", "Method: " + predictedMethodName+" "+slotID);
        String result = null;

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID;
            if (slotID != -1) {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(predictedMethodName, parameter);
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
                }
            } else {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(predictedMethodName);
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName);
                }
            }

            Object ob_phone;
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            if (getSimID != null) {
                if (slotID != -1) {
                    ob_phone = getSimID.invoke(telephony, obParameter);
                } else {
                    ob_phone = getSimID.invoke(telephony);
                }

                if (ob_phone != null) {
                    result = ob_phone.toString();

                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        //Log.i("Reflection", "Result: " + result);
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_tabfragment2, container, false);
        tv1 = v.findViewById(R.id.tvsim20);
        tv3 = v.findViewById(R.id.tvsim21);
        tv4 = v.findViewById(R.id.tvsim22);
        tv5 = v.findViewById(R.id.tvsim23);
        tv7 = v.findViewById(R.id.tvsim25);
        tv8 = v.findViewById(R.id.tvsim26);
        tv9 = v.findViewById(R.id.tvsim27);
        er = v.findViewById(R.id.notavailable);
        sc = v.findViewById(R.id.notshow);
//        psListener = new mPhoneStateListener();
//        telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        helper = new MySQLHelper(getContext());
        database = helper.getWritableDatabase();

        if (getOutput(getContext(), "getNetworkOperatorName", 2).equals("")) {

            er.setVisibility(View.VISIBLE);
            sc.setVisibility(View.INVISIBLE);

            if (helper.searchName("Sim 2", database) == 0) {
                helper.insertData("Sim 2", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Sim 2", database), "NA", "Fail", database);
            }

        } else {
            er.setVisibility(View.INVISIBLE);
            sc.setVisibility(View.VISIBLE);
            mytelco1();

            if (helper.searchName("Sim 2", database) == 0) {
                helper.insertData("Sim 2", "Present", "Pass", database);
            } else {
                helper.updateData(helper.searchName("Sim 2", database), "Present", "Pass", database);
            }

        }


        return v;
    }

    public void checkpermission() {
        int permissioncheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);
        if (permissioncheck == PackageManager.PERMISSION_GRANTED) {
            mytelco1();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_PHONE_STATE
            }, PERMISSION_READ_STATE);
        }

    }

    private void mytelco1() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

            return;
        }


        boolean isRoaming = Boolean.parseBoolean(getOutput(getContext(), "getVoiceNetworkType", 2));

        String simserial = getOutput(getContext(), "getSimSerialNumber", 2);

        String carrier = getOutput(getContext(), "getNetworkOperatorName", 2);


        switch (Integer.parseInt(getOutput(getContext(), "getDataNetworkType", 2))) {
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

        int getvoicenettype = Integer.parseInt(getOutput(getContext(), "getVoiceNetworkType", 2));
        switch (getvoicenettype) {
            case (TelephonyManager.NETWORK_TYPE_HSDPA):
                netvoicetype = "3G";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPAP):
            case (TelephonyManager.NETWORK_TYPE_LTE):
                netvoicetype = "4G VoLTE";
                break;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                netvoicetype = "GPRS";
                break;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                netvoicetype = "2G";
                break;
        }


        tv1.setText(carrier);
        tv3.setText("Disconnected");
        tv4.setText(simserial);
        tv5.setText(getOutput(getContext(), "getNetworkCountryIso", 1).toUpperCase());
        tv7.setText(netvoicetype);
        tv8.setText(nettype);

        if (isRoaming) {
            tv9.setText("Roaming");
        } else {
            tv9.setText("Not Roaming");
        }

    }

}


