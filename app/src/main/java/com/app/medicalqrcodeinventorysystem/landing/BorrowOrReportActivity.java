package com.app.medicalqrcodeinventorysystem.landing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.borrowreturn.BorrowEquipmentActivity;
import com.app.medicalqrcodeinventorysystem.dto.EquipmentDTO;
import com.app.medicalqrcodeinventorysystem.report.ReportEquipmentActivity;
import com.app.medicalqrcodeinventorysystem.util.ActivityDispatcherUtil;

public class BorrowOrReportActivity extends AppCompatActivity {

    private Button borrowBtn;

    private Button reportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_or_report);

        EquipmentDTO equipmentDTO = new EquipmentDTO( getIntent().getStringExtra("equipment_id"), getIntent().getStringExtra("model_number") ,
                getIntent().getStringExtra("equipment_type"), getIntent().getStringExtra("designation_name") );

        borrowBtn = findViewById(R.id.borrowBtn);
        reportBtn = findViewById(R.id.reportBtn);

        borrowBtn.setOnClickListener(v -> goToActivity(v, equipmentDTO, BorrowEquipmentActivity.class));
        reportBtn.setOnClickListener(v -> goToActivity(v, equipmentDTO, ReportEquipmentActivity.class));
    }

    private void goToActivity(View view, EquipmentDTO equipmentDTO, Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.putExtra("equipment_id", equipmentDTO.getEquipmentId());
        intent.putExtra("model_number", equipmentDTO.getModelNumber());
        intent.putExtra("equipment_type", equipmentDTO.getEquipmentType());
        intent.putExtra("designation_name", equipmentDTO.getDesignationName());
        startActivity( intent );
    }




}