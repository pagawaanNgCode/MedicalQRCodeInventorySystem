package com.app.medicalqrcodeinventorysystem.borrowreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.equipments.EquipmentListActivity;
import com.app.medicalqrcodeinventorysystem.landing.LandingActivity;
import com.app.medicalqrcodeinventorysystem.scanner.QRCodeScannerActivity;

public class BorrowLandingActivity extends AppCompatActivity {

    private TextView borrowLandingTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_landing);
        borrowLandingTxtView = findViewById(R.id.borrowLandingTxtView);

        final String borrowLandingMessage = "Equipment ID " + getIntent().getStringExtra("equipment_id") + " borrowed successfully.";
        borrowLandingTxtView.setText(borrowLandingMessage);
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