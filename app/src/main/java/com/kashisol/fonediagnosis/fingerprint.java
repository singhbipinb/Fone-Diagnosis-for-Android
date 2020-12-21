package com.kashisol.fonediagnosis;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class fingerprint extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    ImageView im1, im2, im3;
    Button bt;
    TextView tv;
    boolean success = false;
    MySQLHelper helper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Fingerprint");
        setContentView(R.layout.activity_fingerprint);
        bt = findViewById(R.id.launchAuthentication);
        im1 = findViewById(R.id.fpdefim);
        im2 = findViewById(R.id.fpimagesuc);
        im3 = findViewById(R.id.fpimagefail);
        tv = findViewById(R.id.fptext);

        Executor newExecutor = Executors.newSingleThreadExecutor();

        helper = new MySQLHelper(this);
        database = helper.getWritableDatabase();

        FragmentActivity activity = this;


        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {


            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                if (helper.searchName("Fingerprint", database) == 0) {
                    helper.insertData("Fingerprint", "NA", "Fail", database);
                } else {
                    helper.updateData(helper.searchName("Fingerprint", database), "NA", "Fail", database);
                }

                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                } else {
                    Log.d(TAG, "An unrecoverable error occurred");
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Log.d(TAG, "()()()Fingerprint recognised successfully");
                success = true;

                if (helper.searchName("Fingerprint", database) == 0) {
                    helper.insertData("Fingerprint", "Authenticated", "Pass", database);
                } else {
                    helper.updateData(helper.searchName("Fingerprint", database), "Authenticated", "Pass", database);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        im1.setVisibility(View.INVISIBLE);
                        bt.setVisibility(View.INVISIBLE);
                        im2.setImageResource(R.drawable.fpsuccess);
                        im2.setVisibility(View.VISIBLE);
                        im3.setVisibility(View.INVISIBLE);
                        tv.setText("Authentication Successful");
                    }
                });


            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                Log.d(TAG, "()()()Fingerprint not recognised");

                if (helper.searchName("Fingerprint", database) == 0) {
                    helper.insertData("Fingerprint", "Wrong Input", "Fail", database);
                } else {
                    helper.updateData(helper.searchName("Fingerprint", database), "Wrong Input", "Fail", database);
                }

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {

                                      im1.setVisibility(View.INVISIBLE);
                                      bt.setVisibility(View.INVISIBLE);
                                      im3.setVisibility(View.VISIBLE);
                                      im2.setVisibility(View.INVISIBLE);
                                      im2.setImageResource(R.drawable.fpfail);
                                      tv.setText("Authentication Failed");

                                  }
                              }

                );

            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()

                .setTitle("Fingerprint Authentication")
                .setNegativeButtonText("Cancel")
                .build();

        findViewById(R.id.launchAuthentication).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBiometricPrompt.authenticate(promptInfo);

            }
        });

    }


}