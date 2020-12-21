package com.kashisol.fonediagnosis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class flashlight extends AppCompatActivity {

    ImageView img;
    MySQLHelper helper;
    SQLiteDatabase database;
    private CameraManager mCameraManager;
    private String mCameraId;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Flash Light");
        setContentView(R.layout.activity_flashlight);

        img = findViewById(R.id.flashimage);

        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();

        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            if (helper.searchName("Flashlight", database) == 0) {
                helper.insertData("Flashlight", "NA", "Fail", database);
            } else {
                helper.updateData(helper.searchName("Flashlight", database), "NA", "Fail", database);
            }

            showNoFlashError();
        }


        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        toggleButton = findViewById(R.id.toggleButton);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchFlashLight(isChecked);
                if (isChecked) {
                    img.setImageResource(R.drawable.flashlighton);
                } else {
                    img.setImageResource(R.drawable.flashlightoff);
                }

                if (helper.searchName("Flashlight", database) == 0) {
                    helper.insertData("Flashlight", "Present", "Pass", database);
                } else {
                    helper.updateData(helper.searchName("Flashlight", database), "Present", "Pass", database);
                }
            }
        });
    }

    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    public void switchFlashLight(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
