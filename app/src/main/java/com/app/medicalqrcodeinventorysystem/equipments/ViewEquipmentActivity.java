package com.app.medicalqrcodeinventorysystem.equipments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.app.medicalqrcodeinventorysystem.R;

public class ViewEquipmentActivity extends AppCompatActivity {

    private TextView viewEquipmentTxtView;

    private TextView statusTxtView;

    private TextView equipmentTypeTxtView;

    private TextView modelNumberTxtView;

    private TextView currentDesignationTxtView;

    private TextView purposeTxtView;

    private TextView dateBorrowedTxtView;

    private TextView borrowedByTxtView;

    private TextView purposeLabel;

    private TextView dateBorrowedLabel;

    private TextView borrowedByTxtLabel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_equipment);

        viewEquipmentTxtView = findViewById(R.id.viewEquipmentTxtView);
        statusTxtView = findViewById(R.id.statusViewTxtView);
        equipmentTypeTxtView = findViewById(R.id.equipmentTypeViewTxtView);
        modelNumberTxtView = findViewById(R.id.modelNumberViewTxtView);
        currentDesignationTxtView = findViewById(R.id.currentDesignationViewTxtView);
        purposeTxtView = findViewById(R.id.purposeViewTxtView);
        dateBorrowedTxtView = findViewById(R.id.dateBorrowedViewTxtView);
        borrowedByTxtView = findViewById(R.id.borrowedByViewTxtView);

        purposeLabel = findViewById(R.id.purposeLabel);
        dateBorrowedLabel = findViewById(R.id.dateBorrowedLabel);
        borrowedByTxtLabel = findViewById(R.id.borrowedByLabel);

        final String status = getIntent().getStringExtra("status");
        final String viewEquipment = "Equipment ID# ";
        viewEquipmentTxtView.setText( viewEquipment.concat( getIntent().getStringExtra("equipment_id") ) );
        modelNumberTxtView.setText( getIntent().getStringExtra("model_number") );
        equipmentTypeTxtView.setText( getIntent().getStringExtra("equipment_type") );
        statusTxtView.setText( status );
        currentDesignationTxtView.setText( getIntent().getStringExtra("current_designation") );

        Log.i("ViewEquipmentActivity", "HERE, status: " + status);

        if( "In-Use".equalsIgnoreCase( status ) ) {
            purposeLabel.setText("Purpose");
            purposeTxtView.setText( getIntent().getStringExtra("purpose") );

            dateBorrowedLabel.setText("Date Borrowed: ");
            dateBorrowedTxtView.setText( getIntent().getStringExtra("date_borrowed") );

            borrowedByTxtLabel.setText("Borrowed By: ");
            borrowedByTxtView.setText( getIntent().getStringExtra("borrowed_by") );
        }

        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}