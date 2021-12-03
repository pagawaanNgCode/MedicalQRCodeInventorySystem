package com.app.medicalqrcodeinventorysystem.landing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.scanner.QRCodeScannerActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    public void scan(View view) {
        Intent scanActivity = new Intent(this, QRCodeScannerActivity.class);
        startActivity(scanActivity);
    }

    public void checkEquipments(View view) {

    }
}