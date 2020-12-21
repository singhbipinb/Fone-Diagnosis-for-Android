package com.kashisol.fonediagnosis;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class cpu extends AppCompatActivity {
    TextView textView;
    ProcessBuilder processBuilder;
    RecyclerView recview;
    RecyclerView.Adapter recadapter;
    RecyclerView.LayoutManager recman;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process;
    byte[] byteArry;
    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("CPU");
        setContentView(R.layout.activity_cpu);

        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
//        textView = (TextView)findViewById(R.id.textView15);

        byteArry = new byte[1024];

        try {
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            inputStream = process.getInputStream();

            while (inputStream.read(byteArry) != -1) {

                Holder = Holder + new String(byteArry);
            }

            inputStream.close();

        } catch (IOException ex) {

            ex.printStackTrace();
        }

//        textView.setText(Holder);
        Log.e("CPU Holder", Holder);

//        System.out.println(Holder);

        if (Holder.isEmpty()) {
            if (helper.searchName("CPU", database) == 0) {
                helper.insertData("CPU", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("CPU", database), "NA", "Fail", database);
            }
        } else {
            if (helper.searchName("CPU", database) == 0) {
                helper.insertData("CPU", "Working", "Pass", database);
            } else {
                helper.updateData(helper.searchName("CPU", database), "Working", "Pass", database);
            }
        }

//        ArrayList<String> srt = new ArrayList<String>();

        String[] stsd = Holder.split("\n");

        String[][] st2 = new String[stsd.length][2];
        for (int i = 0; i < stsd.length; i++) {

            if (stsd[i].isEmpty()) {

            } else {


                st2[i] = stsd[i].split(":");
            }
        }

//                Log.e("CPU Split1",stsd.toString());
        for (int i = 0; i < st2.length; i++) {


            System.out.println(st2[i].length);
        }

//        Log.e("CPU Split2",st2.toString());


        ArrayList<cpuinput> cpulist = new ArrayList<>();
        for (int i = 0; i < st2.length; i++) {
            String str = "";
            if (st2[i].length == 2) {

                cpulist.add(new cpuinput(st2[i][0], st2[i][1]));

            } else {
//        str=st2[i][1];
//        cpulist.add(new cpuinput(st2[i][0],st2[i][1]));
            }
        }

        recview = findViewById(R.id.recview);
        recview.setHasFixedSize(true);
        recman = new LinearLayoutManager(this);
        recadapter = new CPUAdapter(cpulist);

        recview.setLayoutManager(recman);
        recview.setAdapter(recadapter);


        System.out.println(Arrays.toString(cpulist.toArray()));


    }
}