package com.kashisol.fonediagnosis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class test extends AppCompatActivity {
    ImageView imageView;
    TextView tv;
    MySQLHelper helper;
    SQLiteDatabase database;

    LinearLayout l1, l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setTitle("Report Generation");
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();

        l1 = findViewById(R.id.generatepart);
        l2 = findViewById(R.id.sucessgenerate);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            return;
        }

    }


    public void runtest(View view) throws IOException, DocumentException {


        Cursor cursor = database.rawQuery("select * from Test", new String[]{});

        if (cursor != null && cursor.getCount() > 0) {
            String dir = Environment.getExternalStorageDirectory() + File.separator + "MAD";
            File folder = new File(dir);
            folder.mkdirs();


            File file = new File(folder, "/mobilediagnosisresult.pdf");
            file.createNewFile();
            System.out.println("()()()() PATH: " + file.getAbsolutePath());

            Document document = new Document();  // create the document
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Paragraph p3 = new Paragraph();  // to enter value you have to create paragraph  and add value in it then paragraph is added into document
            p3.add("Mobile Diagnosis Report\n\n");
            document.add(p3);

            PdfPTable table = new PdfPTable(4);

            table.addCell("S No.");
            table.addCell("Name");
            table.addCell("Value");
            table.addCell("Result");

            int i = 1;
            while (cursor.moveToNext()) {
                table.addCell(String.valueOf(i));
                table.addCell(cursor.getString(1));
                table.addCell(cursor.getString(2));
                table.addCell(cursor.getString(3));
                i++;
            }
            document.add(table);
            document.addCreationDate();
            document.close();

            database.execSQL("delete from Test");
            l1.setVisibility(View.INVISIBLE);
            l2.setVisibility(View.VISIBLE);

        } else {

            Toast.makeText(this, "No tests performed", Toast.LENGTH_LONG).show();
        }
    }

    public void openfile(View view) {


        try {

            String dir = Environment.getExternalStorageDirectory() + File.separator + "MAD";
            File file = new File(dir, "/mobilediagnosisresult.pdf");

            Uri uri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "File not found", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(this, home.class);
        startActivity(it);
    }
}