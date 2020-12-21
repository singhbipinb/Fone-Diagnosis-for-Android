package org.kashisol.mobilediagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.timqi.sectorprogressview.SectorProgressView;

public class StorageActivity extends AppCompatActivity {

    SectorProgressView storage_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        setTitle("Storage Diagnosis");

        storage_progress = findViewById(R.id.storage_progress);
        storage_progress.setPercent(76);

    }
}