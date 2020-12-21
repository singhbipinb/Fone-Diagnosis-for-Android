package com.kashisol.fonediagnosis;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class bluetooth extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4;
    ListView lv1;
    MySQLHelper helper;
    SQLiteDatabase database;
    private ArrayAdapter aAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Bluetooth");
        setContentView(R.layout.activity_bluetooth);
        tv1 = findViewById(R.id.tvblue1);
        tv2 = findViewById(R.id.tvblue2);
        tv3 = findViewById(R.id.tvblue3);
        lv1 = findViewById(R.id.listblue);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();


        BluetoothAdapter badapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        if (badapter == null) {

            if (helper.searchName("Bluetooth", database) == 0) {
                helper.insertData("Bluetooth", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Bluetooth", database), "NA", "Fail", database);
            }

            showNoBluetoothError();

        } else {

            if (!badapter.isEnabled()) {
                Toast.makeText(this, "Bluetooth is disabled, enabling it", Toast.LENGTH_LONG).show();
                badapter.enable();
            }
            tv1.setText("Enabled");

            Set<BluetoothDevice> pairedDevices = badapter.getBondedDevices();
            ArrayList list = new ArrayList();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    String devicename = device.getName();
                    String macAddress = device.getAddress();
                    list.add("Name: " + devicename + "\nMAC Address: " + macAddress);
                }
            }
            aAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
            lv1.setAdapter(aAdapter);


            tv2.setText("" + badapter.getName());
            tv3.setText("" + badapter.getAddress());

            if (helper.searchName("Bluetooth", database) == 0) {
                helper.insertData("Bluetooth", "Available", "Pass", database);
            } else {
                helper.updateData(helper.searchName("Bluetooth", database), "Available", "Pass", database);
            }

        }


    }

    public void showNoBluetoothError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Bluetooth is not supported on this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }
}