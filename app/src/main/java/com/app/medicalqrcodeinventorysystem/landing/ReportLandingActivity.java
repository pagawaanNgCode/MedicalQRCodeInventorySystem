package com.app.medicalqrcodeinventorysystem.landing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.equipments.EquipmentListActivity;
import com.app.medicalqrcodeinventorysystem.scanner.QRCodeScannerActivity;

public class ReportLandingActivity extends AppCompatActivity {

    private TextView reportLandingTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_landing);

        reportLandingTxtView = findViewById(R.id.reportLandingTxtView);

        final String reportLandingMessage = "Thank you for reporting Equipment ID "+ getIntent().getStringExtra("equipment_id") +" as defective. " +
                "Our staff will inspect the equipment and resolve the issue as soon as possible.";
        reportLandingTxtView.setText(reportLandingMessage);
    }


    public void backToScanner(View view) {
        Intent borrowLandingActivity = new Intent(getApplicationContext(), QRCodeScannerActivity.class);
        startActivity(borrowLandingActivity);
    }

    public void backToMenu(View view) {
        Intent borrowLandingActivity = new Intent(getApplicationContext(), EquipmentListActivity.class);
        startActivity(borrowLandingActivity);
    }
}