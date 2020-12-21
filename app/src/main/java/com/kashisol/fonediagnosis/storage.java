package com.kashisol.fonediagnosis;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.io.File;

public class storage extends AppCompatActivity {

    static long totint = 0, avint = 0, totext = 0, avext = 0;
    TextView tvtotint, tvusedint, tvavailint, tvtotext, tvusedext, tvavailext, tvexp1, tvexp2;
    CardView ext1, ext2, ext3, excv1, int1, int2, int3, cvnotfound;
    Boolean invi1 = true, invi2 = true;
    MySQLHelper helper;
    SQLiteDatabase database;

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        avint = availableBlocks;
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        totint = totalBlocks;
        return formatSize(totalBlocks * blockSize);
    }

    public static String getUsedInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long usedint = totint - avint;
        return formatSize(usedint * blockSize);

    }

    public static String getUsedExternalMemorySize() {
        long usedext = totext - avext;
        System.out.println(formatSize(usedext));
        return formatSize(usedext);
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;

//                if(size>=1024){
//                    suffix = " GB";
//                    size /= 1024 ;
//                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();

    }

    public boolean externalMemoryAvailable() {


        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        System.out.println("" + isSDPresent);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        System.out.println("" + isSDSupportedDevice);

        if (isSDPresent && isSDSupportedDevice) {
            System.out.println("SD Present ");
            return true;
        } else {
            System.out.println("SD absent");
            return false;
        }

    }

    public String getAvailableExternalMemorySize() {

        File path = this.getExternalFilesDir(null);
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        avext = availableBlocks * blockSize;
        System.out.println(formatSize(availableBlocks * blockSize));
        return formatSize(availableBlocks * blockSize);

    }

    public String getTotalExternalMemorySize() {

        File path = this.getExternalFilesDir(null);
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        totext = totalBlocks * blockSize;
        System.out.println(formatSize(totalBlocks * blockSize));
        return formatSize(totalBlocks * blockSize);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Storage");
        setContentView(R.layout.activity_storage);

        tvtotint = findViewById(R.id.tvtotint);
        tvusedint = findViewById(R.id.tvusedint);
        tvavailint = findViewById(R.id.tvavailint);
        tvtotext = findViewById(R.id.tvtotext);
        tvusedext = findViewById(R.id.tvusedext);
        tvavailext = findViewById(R.id.tvavailext);
        tvexp1 = findViewById(R.id.stotvex1);
        tvexp2 = findViewById(R.id.stotvex2);
        cvnotfound = findViewById(R.id.tvsdnotfound);
        ext1 = findViewById(R.id.strcv1);
        ext2 = findViewById(R.id.strcv2);
        ext3 = findViewById(R.id.strcv3);
        int1 = findViewById(R.id.strcv4);
        int2 = findViewById(R.id.strcv5);
        int3 = findViewById(R.id.strcv6);
        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();
        excv1 = findViewById(R.id.expandablecv1);


        tvavailint.setText(getAvailableInternalMemorySize());
        tvtotint.setText(getTotalInternalMemorySize());
        tvusedint.setText(getUsedInternalMemorySize());


        if (externalMemoryAvailable() == true) {
            tvtotext.setText(getTotalExternalMemorySize());
            tvusedext.setText(getUsedExternalMemorySize());
            tvavailext.setText(getAvailableExternalMemorySize());

        } else {
            ext1.setVisibility(View.INVISIBLE);
            ext2.setVisibility(View.INVISIBLE);
            ext3.setVisibility(View.INVISIBLE);
        }

    }


    public void strexpand1(View view) {

        if (invi1 == true) {
            int1.setVisibility(View.VISIBLE);
            int2.setVisibility(View.VISIBLE);
            int3.setVisibility(View.VISIBLE);
//           tvexp1.setCompoundDrawables(null,null,getDrawable(R.drawable.uparrow),null);
            tvexp1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.uparrow), null);
            invi1 = false;

            if (helper.searchName("Internal Storage", database) == 0) {
                helper.insertData("Internal Storage", getTotalInternalMemorySize(), "Pass", database);
            } else {
                helper.updateData(helper.searchName("Internal Storage", database), getTotalInternalMemorySize(), "Pass", database);
            }

        } else if (invi1 == false) {
            int1.setVisibility(View.INVISIBLE);
            int2.setVisibility(View.INVISIBLE);
            int3.setVisibility(View.INVISIBLE);
            Drawable img = ContextCompat.getDrawable(this, R.drawable.downarrow);
            tvexp1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, img, null);
            invi1 = true;
            if (helper.searchName("Internal Storage", database) == 0) {
                helper.insertData("Internal Storage", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Internal Storage", database), "NA", "Fail", database);
            }
        }

    }

    public void strexpand2(View view) {

        if (externalMemoryAvailable()) {

            if (invi2 == true) {
                cvnotfound.setVisibility(View.INVISIBLE);
                ext1.setVisibility(View.VISIBLE);
                ext2.setVisibility(View.VISIBLE);
                ext3.setVisibility(View.VISIBLE);
                tvexp2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.uparrow), null);
                invi2 = false;
                if (helper.searchName("External Storage", database) == 0) {
                    helper.insertData("External Storage", getTotalExternalMemorySize(), "Pass", database);
                } else {
                    helper.updateData(helper.searchName("External Storage", database), getTotalExternalMemorySize(), "Pass", database);
                }

            } else if (invi2 == false) {
                cvnotfound.setVisibility(View.INVISIBLE);
                ext1.setVisibility(View.INVISIBLE);
                ext2.setVisibility(View.INVISIBLE);
                ext3.setVisibility(View.INVISIBLE);
                Drawable img = ContextCompat.getDrawable(this, R.drawable.downarrow);
                tvexp2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, img, null);
                invi2 = true;
                if (helper.searchName("External Storage", database) == 0) {
                    helper.insertData("External Storage", "NA", "Pass", database);
                } else {
                    helper.updateData(helper.searchName("External Storage", database), "NA", "Pass", database);
                }
            }
        } else {

            if (invi2 == true) {
                cvnotfound.setVisibility(View.VISIBLE);
                tvexp2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.uparrow), null);
                invi2 = false;
            } else if (invi2 == false) {
                cvnotfound.setVisibility(View.INVISIBLE);
                Drawable img = ContextCompat.getDrawable(this, R.drawable.downarrow);
                tvexp2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, img, null);
                invi2 = true;

            }
        }

    }
}