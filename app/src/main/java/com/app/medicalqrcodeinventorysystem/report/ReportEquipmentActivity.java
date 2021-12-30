package com.app.medicalqrcodeinventorysystem.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.borrowreturn.BorrowEquipmentActivity;
import com.app.medicalqrcodeinventorysystem.borrowreturn.BorrowLandingActivity;
import com.app.medicalqrcodeinventorysystem.borrowreturn.ReturnLandingActivity;
import com.app.medicalqrcodeinventorysystem.environment.Environment;
import com.app.medicalqrcodeinventorysystem.landing.ReportLandingActivity;
import com.app.medicalqrcodeinventorysystem.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportEquipmentActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private TextView statusTxtView;

    private TextView equipmentTypeTxtView;

    private TextView modelNumberTxtView;

    private TextView defaultDesignationTxtView;

    private EditText defectDescriptionMultiLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_equipment);
        sessionManager = new SessionManager(getApplicationContext());
        modelNumberTxtView = findViewById(R.id.modelNumberReportTxtView);
        statusTxtView = findViewById(R.id.statusReportTxtView);
        equipmentTypeTxtView = findViewById(R.id.equipmentTypeReportTxtView);
        defaultDesignationTxtView = findViewById(R.id.defaultDesignationReportTxtView);
        defectDescriptionMultiLine = findViewById(R.id.defectDescriptionMultiLine);

        modelNumberTxtView.setText( getIntent().getStringExtra("model_number") );
        equipmentTypeTxtView.setText( getIntent().getStringExtra("equipment_type") );
        statusTxtView.setText( "Available" );
        defaultDesignationTxtView.setText( getIntent().getStringExtra("designation_name") );

        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        }
    }

    public void report(View view) {
        ReportEquipmentTask reportEquipmentTask = new ReportEquipmentTask(getApplicationContext(), getIntent().getStringExtra("equipment_id"),
                defectDescriptionMultiLine.getText().toString());
        Log.i("report equipmentId", getIntent().getStringExtra("equipment_id"));
        Log.i("report desc", defectDescriptionMultiLine.getText().toString());
        Log.i("report userId", sessionManager.getUserId());
        reportEquipmentTask.execute();
    }

    public class ReportEquipmentTask extends AsyncTask<String, Void, Void> {

        private Context context;

        private final String equipmentID;

        private String defectDescription;

        public ReportEquipmentTask(Context context, final String equipmentID, String defectDescription) {
            this.context = context;
            this.equipmentID = equipmentID;
            this.defectDescription = defectDescription;
        } //·

        @Override
        protected Void doInBackground(String... strings) {

            String getUrl = Environment.ROOT_PATH + "/api/v1/report-equipment";
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getUrl,
                    response -> {
                        Log.i("ReportEquipmentTask", response);

                        try {
                            JSONObject reportEquipmentObject = new JSONObject(response);

                            if( reportEquipmentObject.getBoolean("success") ) {
                                Intent reportLandingActivity = new Intent(context, ReportLandingActivity.class);
                                reportLandingActivity.putExtra("equipment_id", equipmentID);
                                startActivity(reportLandingActivity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, Throwable::printStackTrace){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("equipment_id", equipmentID);
                    params.put("user_id", sessionManager.getUserId());
                    params.put("defect_description", defectDescription);
                    return params;
                }
            };

            requestQueue.add(stringRequest);

            return null;
        }
    }
}